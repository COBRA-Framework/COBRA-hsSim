/*
 * Copyright (c) 2012, LaSIGE, FCUL, Lisbon, Portugal.
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached LICENSE file.
 * If you do not find this file, copies can be obtained by writing to:
 * LaSIGE, FCUL, Campo Grande, Ed. C6, Piso 3, 1749-016 LISBOA, Portugal
 * (c/o João Craveiro)
 * 
 * If you consider using this tool for your research, please be kind
 * as to cite the paper describing it:
 * 
 * J. Craveiro, R. Silveira and J. Rufino, "hsSim: an Extensible 
 * Interoperable Object-Oriented n-Level Hierarchical Scheduling 
 * Simulator," in WATERS 2012, Pisa, Italy, Jul. 2012.
 */
package pt.ul.fc.di.lasige.simhs.core.domain.scheduling;

import pt.ul.fc.di.lasige.simhs.core.domain.events.JobCompletedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobDeadlineMissEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobPreemptedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobReleasedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.AbsScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.Job;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.JobQueue;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.SchedulingPolicy;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;

import java.util.*;

/**
 * @author jcraveiro
 *
 */
public abstract class PartitionedUniprocessorScheduler extends AbsScheduler {

	private JobQueue readyQueue;
	private int coreID;
	private Job current;
	private IProcessor jobProc;

	protected PartitionedUniprocessorScheduler() {
		super();
		this.readyQueue = new JobQueue(this.getPolicy());
		current = null;
		coreID = -1;
	}

	@Override
	protected abstract SchedulingPolicy getPolicy();

	public void tick() {
		super.tick();
		Iterator it = this.getPlatform().iterator();
		/*if(getInternalTime()%1000==0) {
			System.out.println("Platform info:");
			while(it.hasNext())
				System.out.println("Processor binded to Component: " + it.next().toString()+ " " + getInternalTime());
			System.out.println();
		}*/
		pruneCompletedJobs();
		checkDeadlineMiss();
		readyQueue.refresh();

	}

	protected boolean isCurrent(Job j) {
		return this.current.equals(j);
	}
	
	protected void removeFromCurrent(Job j) {
		IProcessor toRemove;
		if (j.equals(current)) {
			toRemove = jobProc;
			current = null;


			if (j.getPPT() != null) {
				j.getParentTask().unbindProcessor(toRemove, j.getPPT().getCoreID());
			} else
				j.getParentTask().unbindProcessor(toRemove, -1);
		}
	}

	protected void setCoreID (int coreID)
	{
		this.coreID = coreID;

	}

	protected int getCoreID ()
	{
		return this.coreID;

	}
	
	public void tickle() {
		final Job heir = getHeirJob();
		final Map<IAbsSchedulable, Double> childrenToTickle = new TreeMap<IAbsSchedulable, Double>();

		if(heir != null){
			removeFromReadyQueue(heir);
			addToReadyQueue(heir);
			double amountTickled = heir.tickle(jobProc.getSpeed());

			Double toTickleChild = childrenToTickle.get(heir.getParentTask());
			if (toTickleChild == null)
				toTickleChild = 0.0;
			childrenToTickle.put(heir.getParentTask(),toTickleChild + amountTickled);
		}
		for (IAbsSchedulable at : childrenToTickle.keySet()) {
			at.tickle(childrenToTickle.get(at));
		}
	}

	protected Job getHeirJob() {

		Job heir = null;
		IProcessor proc = null;
		Iterator<IProcessor> it = this.getPlatform().iterator();
		int core = this.getPlatform().getActiveProcs().indexOf(coreID);
		int position = 0;

		if(this.readyQueue.size() != 0) {
			heir = this.readyQueue.first();
			while(it.hasNext()) {
				proc = it.next();
				if (position == core)
					jobProc = proc;
				position++;
			}
		}
		if (heir != this.current && (this.current == null || this.current.getRemainingCapacity() > 0)) { // $codepro.audit.disable useEquals
			setChanged();
			notifyObservers(new JobPreemptedEvent(getInternalTime(), this.current, heir, jobProc));
			if (this.current != null) {
				if(this.current.getPPT()!=null)
					this.current.getParentTask().unbindProcessor(jobProc,this.current.getPPT().getCoreID());
				else
					this.current.getParentTask().unbindProcessor(jobProc,-1);

			}
			if (heir != null) {
				if(heir.getPPT()!=null) {
					heir.getParentTask().unbindProcessor(jobProc,heir.getPPT().getCoreID());
					heir.getParentTask().bindProcessor(jobProc,heir.getPPT().getCoreID());
				}
				else{
					heir.getParentTask().unbindProcessor(jobProc,-1);
					heir.getParentTask().bindProcessor(jobProc,-1);
				}
			}
		}

		this.current = heir;
		return this.current;

	}
	protected void pruneCompletedJobs() {
		final Collection<Job> completedJobsToRemove = new ArrayList<Job>();
		for (Job j : readyQueue) {
			if (j.isCompleted()) {
				setChanged();
				notifyObservers(new JobCompletedEvent(getInternalTime(), j,jobProc));
				completedJobsToRemove.add(j); //no concurrent modif
			}
		}
		for (Job j : completedJobsToRemove) {
			removeFromReadyQueue(j);
			removeFromCurrent(j);
		}
	}

	protected void launchJobs(){

	}
	protected void addToReadyQueue(Job j){
		this.readyQueue.add(j);
	}
	
	
	protected void removeFromReadyQueue(Job j){
		this.readyQueue.remove(j);
	}

	protected void checkDeadlineMiss(){
		final Collection<Job> completedJobsToRemove = new ArrayList<Job>();
		for (Job j : this.readyQueue) {
			if(j.getRemainingCapacity() > 0 && j.getDeadlineTime() <= getInternalTime()){

				/*if (!isJobDeadlineAlreadyNotified(j)) {

					setChanged();
					notifyObservers(new JobDeadlineMissEvent(getInternalTime(), j));
					setJobDeadlineAlreadyNotified(j);
				}*/
				
				completedJobsToRemove.add(j); //no concurrent modif
				setChanged();
				notifyObservers(new JobDeadlineMissEvent(getInternalTime()+1, j));
				setJobDeadlineAlreadyNotified(j);
			
			}
		}
		for (Job j : completedJobsToRemove) {
			removeFromReadyQueue(j);
			if(current!=null && current.equals(j))
				removeFromCurrent(j);
		}
	}
	
	@Override
	protected void preemptJobInProc(IProcessor proc) {

		final Job preempted = current;
		if (preempted != null && jobProc.equals(proc)) {
			removeFromCurrent(preempted);
			setChanged();
			notifyObservers(new JobPreemptedEvent(getInternalTime(), preempted, null, proc));
		}

	}
	

}
