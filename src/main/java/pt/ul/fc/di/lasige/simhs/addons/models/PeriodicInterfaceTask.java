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
package pt.ul.fc.di.lasige.simhs.addons.models;

import pt.ul.fc.di.lasige.simhs.core.domain.workload.IComponent;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.PeriodicTask;


/**
 * @author jcraveiro
 *
 */
public class PeriodicInterfaceTask extends PeriodicTask {

	private int coreID;
	/**
	 * @param id
	 * @param parent
	 * @param capacity
	 * @param period
	 * @param deadline
	 */
	public PeriodicInterfaceTask(String id, IComponent parent, double capacity, int period, int deadline, int processor) {
		super(id, parent, capacity, period, deadline, processor);
	}

	/**
	 * @param id
	 * @param parent
	 * @param capacity
	 * @param period
	 */
	public PeriodicInterfaceTask(String id, IComponent parent, int capacity,
			int period) {
		super(id, parent, capacity, period);
	}
	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.AbsPeriodicSchedulable#launchJob(int)
	 */
	/*@Override
	public List<Job> launchJob(int releaseTime) {
		List<Job> preResult = super.launchJob(releaseTime);
		List<Job> result = new ArrayList<Job>();
		for (Job j : preResult) {
			result.add(new Job((IAbsSchedulable) this.getParent(), j.getSequenceNo(), j.getReleaseTime(), j.getRemainingCapacity(), j.getDeadlineTime()));
		}
		return result;
	}*/
	
	public PeriodicInterfaceTask clone() {
		return (PeriodicInterfaceTask) super.clone();
	}

	public void setCoreID(int coreID)
	{
		this.coreID = coreID;
	}
	public int getCoreID()
	{
		return coreID;
	}

}
