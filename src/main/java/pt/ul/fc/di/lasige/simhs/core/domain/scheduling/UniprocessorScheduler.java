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

import java.util.*;

import pt.ul.fc.di.lasige.simhs.core.domain.events.JobCompletedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobDeadlineMissEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobPreemptedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobReleasedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;

/**
 * @author jcraveiro
 *
 */
public abstract class UniprocessorScheduler extends AbsScheduler {

	private final JobQueue readyQueue;
	private Job current;
	private IProcessor jobProc;
	protected UniprocessorScheduler() {
		super();
		this.readyQueue = new JobQueue(this.getPolicy());
	}

	@Override
	protected abstract SchedulingPolicy getPolicy();

	public void tick() {
		super.tick();
		pruneCompletedJobs();
		checkDeadlineMiss();
		readyQueue.refresh();
	}

	protected boolean isCurrent(Job j) {
		return this.current.equals(j);
	}
	
	protected void removeFromCurrent(Job j) {
		//argument j can be ignored in uniprocessor
		this.current = null;
	}
	
	public void tickle() {

		//TODO take platform into consideration
		
		final Job heir = getHeirJob();

		if(heir != null){
			removeFromReadyQueue(heir);
			heir.tickle();
			heir.getParentTask().tickle();
			if (heir.getRemainingCapacity() > 0)
				addToReadyQueue(heir);

		}
	}
	private void pruneCompletedJobs() {

		final Collection<Job> completedJobsToRemove = new ArrayList<Job>();
		for (Job j : readyQueue) {

			if (j.isCompleted()) {
				setChanged();
				notifyObservers(new JobCompletedEvent(getInternalTime(), j,
						jobProc));

				completedJobsToRemove.add(j); //no concurrent modif
			}

		}
		for (Job j : completedJobsToRemove) {
			removeFromReadyQueue(j);
			removeFromCurrent(j);
		}

	}



	protected Job getHeirJob() {

		Job heir = null;

		if(this.readyQueue.size() != 0) {
			heir = this.readyQueue.first();
		}

		if (heir != this.current && (this.current == null || this.current.getRemainingCapacity() > 0)) { // $codepro.audit.disable useEquals
			setChanged();
			notifyObservers(new JobPreemptedEvent(getInternalTime(), this.current, heir));
		}

		this.current = heir;
		return this.current;

	}
	


	protected void launchJobs(){
		
		final Collection<Job> toRemove = new ArrayList<Job>();

		for (IAbsSchedulable at : getTaskSet()) {
			at.tick();
			List<Job> jobsToLaunch = at.launchJob(getInternalTime());
			for (Job j : jobsToLaunch) {

				j.addObserver(this);
				addToReadyQueue(j);

				setChanged();				
				notifyObservers(new JobReleasedEvent (getInternalTime(), j.clone()));
			}
		}


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
			if(current.equals(j))
				removeFromCurrent(j);
		}
	}
	
	@Override
	protected void preemptJobInProc(IProcessor proc) {

		final Job preempted = current;
		if (preempted != null && proc.equals(jobProc)) {
			removeFromCurrent(preempted);
			setChanged();
			notifyObservers(new JobPreemptedEvent(getInternalTime(), preempted, null, proc));
		}
	}
	

}
