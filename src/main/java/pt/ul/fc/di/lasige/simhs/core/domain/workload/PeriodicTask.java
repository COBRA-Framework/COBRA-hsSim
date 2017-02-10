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

import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;

/**
 * @author jcraveiro
 *
 */
public class PeriodicTask extends AbsPeriodicSchedulable implements IPeriodic,ITask {
	

	public PeriodicTask(String id, IComponent parent, double capacity, int period, int deadline, int processor) {
		super(id, parent, capacity, period, deadline,processor);
	}

	public PeriodicTask(String id, IComponent parent, double capacity, int period,
						int deadline) {
		super(id, parent, capacity, period, deadline);
	}

	public PeriodicTask(String id, IComponent parent, double capacity, int period) {
		super(id, parent, capacity, period);
	}


	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.ITask#tick()
	 */
	@Override
	public void tick() {
		// do nothing
		return;
	}

	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.ITask#tickle()
	 */
	@Override
	public void tickle() {
		// do nothing
		return;
	}

	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.ITask#tickle()
	 */
	@Override
	public void tickle(double exec) {
		// do nothing
		return;
	}


	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.workload.ITask#compareTo(pt.ul.fc.di.lasige.simhs.domain.workload.IAbsSchedulable)
	 */
	@Override
	public int compareTo(IAbsSchedulable arg0) {
		return toStringId().compareTo(arg0.toStringId());
	}


	@Override
	public void bindProcessor(IProcessor proc, int numberOfProc) {
		// do nothing
		return;
	}


	@Override
	public void unbindProcessor(IProcessor proc, int numberOfPro) {
		// do nothing
		return;
	}


	

}
