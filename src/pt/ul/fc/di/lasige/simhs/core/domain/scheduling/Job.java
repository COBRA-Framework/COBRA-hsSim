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

import java.util.Observable;

import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;

/**
 * This class provides a representation of a scheduling job, as seen in most
 * real-time scheduling theory. Regardless of the task model, a job is an
 * activation of a parent task, with a release time, an execution requirement,
 * and an absolute deadline. It is up to the clients of this class to use the
 * absolute deadline as they see fit for the individual case (hard real-time,
 * soft real-time, etc.), by (for instance) verifying (or not) deadline misses,
 * being lax about deadline fulfilment, or initializing it with
 * Integer.MAX_VALUE.
 * 
 * @author jcraveiro
 */
public class Job extends Observable implements Cloneable {
	
	/**
	 * The infinitesimal amount to be used for comparison between doubles.
	 */
	private static final double EPSILON = 0.00001;

	/**
	 * The schedulable entity of which this job is an instance.
	 */
	private IAbsSchedulable parentTask;

	/**
	 * The amount of execution units left to be executed.
	 */
	private double remainingCapacity;
	
	/**
	 * The amount of execution units.
	 */
	private double capacity;
	
	/**
	 * The instant at which this job was released.
	 */
	private final int releaseTime;
	
	/**
	 * The absolute deadline (time instant) of this job.
	 */
	private final int deadlineTime;

	/**
	 * The job's sequence number
	 */
	private final int sequenceNo;
	
	@Override
	public Job clone() {
		Job other = null;
		try {
			other = (Job) super.clone();
			other.parentTask = this.parentTask.clone();
		} catch (Exception exc) {
			throw new InternalError();
		}
		return other;
	}

	/**
	 * Creates a new instance representing a job.
	 * @param parentTask The schedulable entity of which this job is an instance. This may not necessarily be the schedulable entity
	 * which released the job, but it is the schedulable entity whose execution will advance when this job gets dispatched. 
	 * @param sequenceNo The sequence number of this job.
	 * @param releaseTime The instant at which this job was released. 
	 * @param capacity The initial amount of execution units left to be executed.
	 * @param deadlineTime The absolute deadline (time instant) of this job.
	 */
	public Job(IAbsSchedulable parentTask, int sequenceNo, int releaseTime, double remainingCapacity, int deadlineTime, double capacity) {
		this.parentTask = parentTask;
		
		this.sequenceNo = sequenceNo;
		
		this.releaseTime = releaseTime;
		
		this.remainingCapacity = remainingCapacity;

		this.deadlineTime = deadlineTime;
		
		this.capacity = capacity;
	}

	/**
	 * Returns the absolute deadline (time instant) of this job.
	 * @return the absolute deadline (time instant) of this job
	 */
	public int getDeadlineTime() {
		return this.deadlineTime;
	}

	/**
	 * Returns the instant at which this job was released. 
	 * @return the instant at which this job was released 
	 */
	public int getReleaseTime() {
		return this.releaseTime;
	}
	
	/**
	 * Returns the capacity of the Job 
	 * @return the capacity of the Job
	 */
	public double getCapacity() {
		return this.capacity;
	}

	/**
	 * Returns the amount of execution units left to be executed.
	 * @return the amount of execution units left to be executed
	 */
	public double getRemainingCapacity() {
		return this.remainingCapacity;
	}
	
	/**
	 * Returns <tt>true</tt> if this job has no execution units left to be performed.
	 * @return <tt>true</tt> if this job has no execution units left to be performed
	 */
	public boolean isCompleted() {
		return this.remainingCapacity <= EPSILON;
	}


	/**
	 * Returnts the schedulable entity of which this job is an instance. This may not necessarily be the schedulable entity
	 * which released the job, but it is the schedulable entity whose execution will advance when this job gets dispatched.
	 * @return the schedulable entity of which this job is an instance
	 */
	public IAbsSchedulable getParentTask() {
		return this.parentTask;
	}

	/**
	 * 
	 * @return
	 */
	public double getSlackTime() {
		return getDeadlineTime() - getRemainingCapacity();
	}

	/**
	 * Decrements the amount of execution units left to be executed by 1.
	 */
	public void tickle() {
		this.tickle(1.0);
	}
	
	/**
	 * Decrements the amount of execution units left to be executed by a specified real amount.
	 * @param exec the amount of execution units to be decremented
	 */
	public double tickle(double exec) {
		final double init = this.getRemainingCapacity();
		this.remainingCapacity -= exec;
		if (isCompleted())
			this.remainingCapacity = 0.0;
		return init - this.getRemainingCapacity();
	}

	@Override
	public String toString() {

		return "job_" + this.parentTask.toStringId() + "_" + this.sequenceNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (null != this.parentTask) {
			result += this.parentTask.hashCode();
		}

		result = prime * result + this.sequenceNo;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof Job)) {
			return false;
		} //supress warning
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		final Job other = (Job) obj;
		if (null == this.parentTask) {
			if (null != other.parentTask) {
				return false;
			}
		} else if (!this.parentTask.equals(other.parentTask)) {
			return false;
		}
		if (this.sequenceNo != other.sequenceNo) {
			return false;
		}
		//System.err.println("true!");
		return true;
	}

	/**
	 * Returns the job's sequence number.
	 * @return the job's sequence number
	 */
	public int getSequenceNo() {
		return this.sequenceNo;
	}
	
	

}
