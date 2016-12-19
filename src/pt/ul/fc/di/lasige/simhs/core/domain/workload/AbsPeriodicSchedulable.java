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
package pt.ul.fc.di.lasige.simhs.core.domain.workload;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.Job;

/**
 * @author jcraveiro
 *
 */
public abstract class AbsPeriodicSchedulable extends Observable implements IAbsSchedulable,IPeriodic  {
	
	private String id;
	
	private IComponent parent;
	
	private double capacity;
	private int period;
	private int deadline;

	private int nextReleaseTime = 0;
	private int nextJobSequenceNo = 1;
	
	public AbsPeriodicSchedulable clone() {
		AbsPeriodicSchedulable other = null;
		try {
			other = (AbsPeriodicSchedulable) super.clone();
			//other.parent = parent.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
		return other;
		
	}
	
	AbsPeriodicSchedulable() {
		//TODO something?
	}
	
	protected AbsPeriodicSchedulable(String id, IComponent parent, double capacity, int period, int deadline) {
		this.id = id;
		this.parent = parent;
		this.capacity = capacity;
		this.period = period;
		this.deadline = deadline;
	}
	
	protected AbsPeriodicSchedulable(String id, IComponent parent, double capacity, int period) {
		this(id, parent, capacity, period, period);
	}

	public int getPeriod() {
		return this.period;
	}
	
	public double getCapacity() {
		return this.capacity;
	}
	
	public int getRelativeDeadline() {
		return this.deadline;
	}
	
	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.IAbsSchedulable#getParent()
	 */
	@Override
	public IComponent getParent() {
		return this.parent;
	}

	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.IAbsSchedulable#tick()
	 */
	@Override
	public abstract void tick();
	
	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.IAbsSchedulable#tickle()
	 */
	@Override
	public abstract void tickle();

	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.IAbsSchedulable#toStringId()
	 */
	@Override
	public String toStringId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.IAbsSchedulable#launchJob(int)
	 */
	@Override
	public List<Job> launchJob(int releaseTime) {
		final List<Job> result = new ArrayList<Job>();
		if (releaseTime >= this.nextReleaseTime) {
			final Job releasedJob = new Job(this, this.nextJobSequenceNo,
					releaseTime, this.capacity, releaseTime + this.deadline,this.capacity);
			++this.nextJobSequenceNo;
			this.nextReleaseTime += this.period;
			result.add(releasedJob);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.IAbsSchedulable#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (null != this.id) {
			result += this.id.hashCode();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.IAbsSchedulable#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbsPeriodicSchedulable))
			return false;
		if (!getClass().equals(obj.getClass()))
			return false;
		final AbsPeriodicSchedulable other = (AbsPeriodicSchedulable) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}
	
	public abstract int compareTo(IAbsSchedulable arg0);
	
	public double getUtilization() {
		return this.capacity / this.period;
	}
	
	protected int getNextReleaseTime() {
		return this.nextReleaseTime;
	}

	protected int getNextJobSequenceNo() {
		return this.nextJobSequenceNo;
	}

	protected void incrementNextJobSequenceNo() {
		this.nextJobSequenceNo++;
	}
	
	
}
