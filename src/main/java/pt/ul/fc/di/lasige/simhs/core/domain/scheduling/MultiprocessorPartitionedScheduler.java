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

import java.util.*;

import pt.ul.fc.di.lasige.simhs.core.domain.events.JobPreemptedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobReleasedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.taxonomy.IPartitionedScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IComponent;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.PeriodicTask;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.Workload;
import pt.ul.fc.di.lasige.simhs.core.platform.IPlatform;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;


/**
 * @author jcraveiro
 * 
 */
public abstract class MultiprocessorPartitionedScheduler extends AbsScheduler implements IPartitionedScheduler {
	
	private static final int DEFAULT_NUMBER_OF_PROCESSORS = 2;
	private int latestTickle = -1;
	private Collection<? extends PartitionedUniprocessorScheduler> schedulers;
	protected MultiprocessorPartitionedScheduler() {
		this(DEFAULT_NUMBER_OF_PROCESSORS);
	}

	protected MultiprocessorPartitionedScheduler(int numberOfProcessors) {
		this.schedulers = getNewSchedulerCollection(numberOfProcessors);
		int coreID=0;
		for (PartitionedUniprocessorScheduler scheduler : this.schedulers)
		{
			scheduler.setCoreID(coreID++);
		}
	}

	public void setNumberOfProcessors(int numberOfProcessors) throws InstantiationException {
		this.schedulers = getNewSchedulerCollection(numberOfProcessors);
		int coreID=0;
		for (PartitionedUniprocessorScheduler scheduler : this.schedulers)
		{
			scheduler.setCoreID(coreID++);
		}
	}
	@Override
	public void setObserver(Observer c)
	{
		for (PartitionedUniprocessorScheduler scheduler : this.schedulers) {
			scheduler.addObserver(c);
		}
	}
	

	protected abstract Collection<? extends PartitionedUniprocessorScheduler> getNewSchedulerCollection(int n);

	@Override
	public void tick() {
		super.tick();
		for (AbsScheduler scheduler : this.schedulers)
			scheduler.tick();
		pruneCompletedJobs();
	}

	@Override
	public void tickle() {
		List<Integer> activeprocs  = this.getPlatform().getActiveProcs();
		if(latestTickle!=getInternalTime()) {
			/*if(getInternalTime()%1000==0) {
				System.out.println(this.toString() + " tickled" + " " + getInternalTime());
				for(int t:activeprocs)
					System.out.print(t+" ");
				System.out.println();
			}*/
			for (PartitionedUniprocessorScheduler scheduler : this.schedulers) {
				if (activeprocs.contains(scheduler.getCoreID())) {
					scheduler.tickle();
				}
			}
		}
		latestTickle = getInternalTime();
	}

	@Override
	protected boolean isCurrent(Job j) {
		for (PartitionedUniprocessorScheduler scheduler : this.schedulers)
			if (scheduler.isCurrent(j))
				return true;
		return false;
	}
	
	@Override
	protected void removeFromCurrent(Job j) {
		for (PartitionedUniprocessorScheduler scheduler : this.schedulers)
			if (scheduler.isCurrent(j))
				scheduler.removeFromCurrent(j);

	}

	@Override
	public void setPlatform(IPlatform platform)
	{
		super.setPlatform(platform);
		platform.maxProcessors(schedulers.size());
		for(PartitionedUniprocessorScheduler scheduler: schedulers) {
			scheduler.setPlatform(platform);
		}
	}

	protected void pruneCompletedJobs()
	{
		for(PartitionedUniprocessorScheduler sched: schedulers)
			sched.pruneCompletedJobs();
	}


	protected void launchJobs(){
		for (IAbsSchedulable at : getTaskSet()) {
			at.tick();
			List<Job> jobsToLaunch = at.launchJob(getInternalTime());
			for (Job j: jobsToLaunch) {
				j.addObserver(this);
				for(PartitionedUniprocessorScheduler sched: schedulers){
					if(j.getPPT()==null) {
						if (j.getParentTask().getProcessor() == sched.getCoreID()) {
							sched.addToReadyQueue(j);
							break;
						}
					}
					else {
						if (j.getPPT().getProcessor() == sched.getCoreID()) {
							sched.addToReadyQueue(j);
							break;
						}
					}
				}
				setChanged();
				notifyObservers(new JobReleasedEvent(getInternalTime(), j.clone()));
			}
		}
	}

	protected void removeFromReadyQueue(Job j){
		for(PartitionedUniprocessorScheduler sched: schedulers) {
			if (j.getPPT() == null) {
				if (j.getParentTask().getProcessor() == sched.getCoreID()) {
					sched.removeFromReadyQueue(j);
					break;
				}
			} else {
				if (j.getPPT().getProcessor() == sched.getCoreID()) {
					sched.removeFromReadyQueue(j);
					break;
				}
			}
		}
	}
	
	@Override
	protected void preemptJobInProc(IProcessor proc) {

		for(PartitionedUniprocessorScheduler sched:schedulers)
			sched.preemptJobInProc(proc);
		//System.out.println(proc.toString());
		//System.out.println("Number of CPUS("+this.getInternalTime()+": "+this.getPlatform().getNumberOfProcessors());

		/*final Job preempted = current.get(proc);
		if (preempted != null) {
			current.remove(proc);
			setChanged();
			notifyObservers(new JobPreemptedEvent(getInternalTime(), preempted, null, proc));

		}*/
		//throw new UnsupportedOperationException();
	}

}
