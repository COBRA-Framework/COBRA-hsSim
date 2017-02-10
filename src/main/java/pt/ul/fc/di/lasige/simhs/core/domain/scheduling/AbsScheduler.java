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

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import pt.ul.fc.di.lasige.simhs.core.domain.events.JobCompletedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IComponent;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.Workload;
import pt.ul.fc.di.lasige.simhs.core.platform.IPlatform;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;
import pt.ul.fc.di.lasige.simhs.core.simulation.ILogger;

/**
 * @author jcraveiro
 *
 * @version $Revision: 1.0 $
 */
public abstract class AbsScheduler extends Observable implements IScheduler,Observer {

	private final Set<Job> deadlineMissesAlreadyNotified;

	private Workload taskset;
	private IPlatform platform;

	private final Set<Observer> observers;

	private int internalTime = -1;
	
	/**
	 * Method getInternalTime.
	 * @return int
	 */
	protected int getInternalTime() {
		return this.internalTime;
	}
	
	/**
	 * Method getPolicy.
	 * @return SchedulingPolicy
	 */
	protected abstract SchedulingPolicy getPolicy();

	protected AbsScheduler() {
		super();
		this.observers = new HashSet<Observer>();
		this.deadlineMissesAlreadyNotified = new TreeSet<Job>(this.getPolicy());
	}

	/**
	 * Method addLogger.
	 * @param l ILogger<?>
	 *
	 */
	public void addLogger(ILogger<?> l){
		this.addObserver(l);
	}

	/**
	 * Method getTaskSet.
	 * @return Workload
	 */
	protected Workload getTaskSet(){
		return this.taskset;
	}

	/**
	 * Method tick.
	 * @see pt.ul.fc.di.lasige.simhs.core.domain.scheduling.IScheduler#tick()
	 */
	public void tick() {
		this.internalTime++;
		launchJobs();
	}

	/**
	 * Method tickle.
	 * @see pt.ul.fc.di.lasige.simhs.core.domain.scheduling.IScheduler#tickle()
	 */
	public abstract void tickle();

	/**
	 * Method update.
	 * @param o Observable
	 * @param arg Object
	 * @see java.util.Observer#update(Observable, Object)
	 */
	@Override
	public void update(Observable o, Object arg) {

		if(arg instanceof JobCompletedEvent){

			final Job completedJob = ((JobCompletedEvent) arg).getJob();

			setChanged();		
			notifyObservers(new JobCompletedEvent(this.internalTime,completedJob));

			removeFromReadyQueue(completedJob);
			this.deadlineMissesAlreadyNotified.remove(completedJob);
		}

	}
	
	/**
	 * Method removeFromReadyQueue.
	 * @param j Job
	 */
	protected abstract void removeFromReadyQueue(Job j);

	/**
	 * Method isCurrent.
	 * @param j Job
	 * @return boolean
	 */
	protected abstract boolean isCurrent(Job j);
	
	/**
	 * Method removeFromCurrent.
	 * @param j Job
	 */
	protected abstract void removeFromCurrent(Job j);
	
	protected abstract void launchJobs();


	/**
	 * Method setTaskSet.
	 * @param t Workload
	 * @see pt.ul.fc.di.lasige.simhs.core.domain.scheduling.IScheduler#setTaskSet(Workload)
	 */
	public void setTaskSet(Workload t) {
		this.taskset = t;
	}
	

	public void setPlatform(IPlatform platform) {
		this.platform = platform;
	}

	/* (non-Javadoc)
	 * @see java.util.Observable#deleteObserver(java.util.Observer)
	 */
	@Override
	public synchronized void deleteObserver(Observer o) {
		super.deleteObserver(o);
		this.observers.remove(o);
		
	}

	/**
	 * @return the platform
	 */
	protected IPlatform getPlatform() {
		return this.platform;
	}
	
	@Override
	public void bindProcessor(IProcessor proc, int numberOfProc) {
		this.platform.bindProcessor(proc,numberOfProc);
	}

	@Override
	public void unbindProcessor(IProcessor proc, int numberOfProc) {
		preemptJobInProc(proc);
		this.platform.unbindProcessor(proc,numberOfProc);
	}
	
	protected abstract void preemptJobInProc(IProcessor proc);
	
	protected boolean isJobDeadlineAlreadyNotified(Job job) {
		return this.deadlineMissesAlreadyNotified.contains(job);
	}
	
	protected void setJobDeadlineAlreadyNotified(Job job) {
		this.deadlineMissesAlreadyNotified.add(job);
	}

	public void setObserver(Observer c1){};
	
}

