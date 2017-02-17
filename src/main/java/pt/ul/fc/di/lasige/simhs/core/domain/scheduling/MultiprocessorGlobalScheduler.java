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
/**
 * 
 */
package pt.ul.fc.di.lasige.simhs.core.domain.scheduling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import pt.ul.fc.di.lasige.simhs.core.domain.events.JobCompletedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobDeadlineMissEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobPreemptedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobReleasedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.taxonomy.IGlobalScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;

/**
 * @author jcraveiro
 *
 */
public abstract class MultiprocessorGlobalScheduler extends AbsScheduler implements IGlobalScheduler {

	private final JobQueue readyQueue;
	private boolean noDeadlineMissesInQ;
	private Map<IProcessor,Job> current;
	private int latestTickle;

	protected MultiprocessorGlobalScheduler() {
		super();
		noDeadlineMissesInQ=true;
		readyQueue = new JobQueue(this.getPolicy());	
		current = new HashMap<IProcessor,Job>();
		latestTickle = -1;
	}

	@Override
	protected abstract SchedulingPolicy getPolicy();
	
	public void tick() {
		super.tick();
		pruneCompletedJobs();
		checkDeadlineMiss();
		readyQueue.refresh();
	}

	@Override
	public void tickle() {

		final Map<IProcessor, Job> heirs = getHeirJobs();
		final Map<IAbsSchedulable, Double> childrenToTickle = new TreeMap<IAbsSchedulable, Double>();

		//if(latestTickle!=getInternalTime()) {
		if(true){
			//if(getInternalTime()%1000==0)
				//System.out.println(this+"  "+getInternalTime()+"  "+heirs.size()+" "+latestTickle);
			for (IProcessor proc : heirs.keySet()) {
				Job heir = heirs.get(proc);
				if (heir != null) {
					removeFromReadyQueue(heir);
					addToReadyQueue(heir);

					double amountTickled = heir.tickle(proc.getSpeed());

					Double toTickleChild = childrenToTickle.get(heir.getParentTask());
					if (toTickleChild == null)
						toTickleChild = 0.0;

					childrenToTickle.put(heir.getParentTask(), toTickleChild + amountTickled);
					/*
					 * dynamic priority algorithms (e.g. LLF) force this, so that
					 * the job queue is appropriately reindexed
					 */
				}
			}

			for (IAbsSchedulable at : childrenToTickle.keySet()) {
				at.tickle(childrenToTickle.get(at));
			}
		}
		//if(getInternalTime()%1000==0)
		//System.out.println(latestTickle);
		latestTickle = getInternalTime();
		//if(getInternalTime()%1000==0)
		//System.out.println(latestTickle);
	}
	
	private void pruneCompletedJobs() {
		
		final Collection<Job> completedJobsToRemove = new ArrayList<Job>();
		for (Job j : readyQueue) {

			if (j.isCompleted()) {

				IProcessor jobProc = null;

				Set<IProcessor> procs = current.keySet();
				for (IProcessor proc : procs) {
					if (j.equals(current.get(proc))) {
						jobProc = proc;
						break;
					}
				}
				
				setChanged();
				notifyObservers(new JobCompletedEvent(getInternalTime(), j,
						jobProc));

				completedJobsToRemove.add(j); //no concurrent modif
			}
			
		}
		for (Job j : completedJobsToRemove) {
			removeFromReadyQueue(j);
			removeFromCurrent(j);
			j.getParentTask().setRunning(false);
		}
		
	}

	protected Map<IProcessor,Job> getHeirJobs() {
		final Map<IProcessor,Job> newCurrent = new HashMap<IProcessor,Job>();

		
			
		Iterator<IProcessor> platformIterator = getPlatform().iterator();

		for (Job j : readyQueue) {
			if (newCurrent.size() >= getPlatform().getNumberOfProcessors()) {
				break;
			}
			/*
			 * if (j.getRemainingCapacity() <= 0) continue;
			 */
			newCurrent.put(platformIterator.next(), j);
		}
		
		
		//If a job is set to migrate between identical processors, switch back
		
		for (IProcessor proc : current.keySet()) {
			Job j = current.get(proc);
			IProcessor newProc = null;
			for (IProcessor proc2 : newCurrent.keySet()) {
				Job j2 = newCurrent.get(proc2);
				if (j2 != null && j2.equals(j)) {
					newProc = proc2;
					break;
				}
			}
			if (newProc != null) {
				if (!(proc.equals(newProc))) {
					if (Math.abs(proc.getSpeed() - newProc.getSpeed()) <= 0.005) {
						//TODO switch!
						Job jobAtOldProcessor = newCurrent.get(proc);
						newCurrent.put(proc, j);
						if (jobAtOldProcessor != null)
							newCurrent.put(newProc, jobAtOldProcessor);
						else
							newCurrent.remove(newProc);
					}
				}
			}
		}
		
		platformIterator = null;

		final Set<IProcessor> procs = new TreeSet<IProcessor>();
		procs.addAll(newCurrent.keySet());
		procs.addAll(current.keySet());

		final List<JobPreemptedEvent> eventQueue = new ArrayList<JobPreemptedEvent>();

		for (IProcessor proc : procs) { 
			Job preempted = current.get(proc);
			Job preemptedBy = newCurrent.get(proc);

			if (preempted != preemptedBy && (preempted == null || preempted.getRemainingCapacity() > 0)) {

				JobPreemptedEvent ev = new JobPreemptedEvent(getInternalTime(), preempted, preemptedBy, proc);
				int indexToInsertBefore = 0;
				int eventQueueSize = eventQueue.size();
				for (int i=0; i < eventQueueSize; i++) {
					if (eventQueue.get(i).getPreemptedBy() == preempted) {
						indexToInsertBefore = i;
						break;
					}
					if (eventQueue.get(i).getPreempted() == preemptedBy) {
						indexToInsertBefore = i+1;
						break;
					}
				}
				eventQueue.add(indexToInsertBefore, ev);
				
				if (preempted != null) {
					if(preempted.getPPT()!=null)
						preempted.getParentTask().unbindProcessor(proc,preempted.getPPT().getCoreID());
					else
						preempted.getParentTask().unbindProcessor(proc,-1);
				}
				if (preemptedBy != null) {
					if(preemptedBy.getPPT()!=null) {
						preemptedBy.getParentTask().unbindProcessor(proc, preemptedBy.getPPT().getCoreID());
						preemptedBy.getParentTask().bindProcessor(proc, preemptedBy.getPPT().getCoreID());
					}
					else {
						preemptedBy.getParentTask().unbindProcessor(proc, -1);
						preemptedBy.getParentTask().bindProcessor(proc, -1);
					}
				}
			}
		}

		for (JobPreemptedEvent ev : eventQueue) {
			setChanged();
			notifyObservers(ev);
		}
		for(Job j:current.values()) {
			j.getParentTask().setRunning(false);
		}
		current = newCurrent;

		for(Job j:current.values())
			j.getParentTask().setRunning(true);

		return current;
	}

	@Override
	protected boolean isCurrent(Job j) {
		return current.values().contains(j);
	}

	@Override
	protected void removeFromCurrent(Job j) {

		IProcessor toRemove = null;

		final Set<IProcessor> procs = current.keySet();
		for (IProcessor proc : procs) {
			if (j.equals(current.get(proc))) {
				toRemove = proc;
				break;
			}
		}
		j.getParentTask().setRunning(false);
		current.remove(toRemove);
		if(j.getPPT()!=null)
			j.getParentTask().unbindProcessor(toRemove,j.getPPT().getCoreID());
		else
			j.getParentTask().unbindProcessor(toRemove,-1);

	}

	protected void addToReadyQueue(Job j){

		readyQueue.add(j);
	}

	@Override
	protected void removeFromReadyQueue(Job j) {
		readyQueue.remove(j);
			
	}

	@Override
	protected void launchJobs() {
		/*
		 * trigger tasks to launch their jobs (if it is time for that)
		 */
		for (IAbsSchedulable at : getTaskSet()) {
			at.tick();
			List<Job> jobsToLaunch = at.launchJob(getInternalTime());
			for (Job j: jobsToLaunch) {

				j.addObserver(this);
				addToReadyQueue(j);

				setChanged();				
				notifyObservers(new JobReleasedEvent (getInternalTime(), j.clone()));
			}
		}


	}

	protected void checkDeadlineMiss(){
		final Collection<Job> completedJobsToRemove = new ArrayList<Job>();
		if(noDeadlineMissesInQ)
		{
			for (Job j : readyQueue) {
				if(!j.isCompleted() && j.getDeadlineTime() <= getInternalTime()){
	
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
				removeFromCurrent(j);
				j.getParentTask().setRunning(false);
			}
		}
	}

	@Override
	protected void preemptJobInProc(IProcessor proc) {
		
		final Job preempted = current.get(proc);
		if (preempted != null) {
			current.remove(proc);
			setChanged();
			notifyObservers(new JobPreemptedEvent(getInternalTime(), preempted, null, proc));
			preempted.getParentTask().setRunning(false);
		}
	}
}
