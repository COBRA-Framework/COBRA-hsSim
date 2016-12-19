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

import java.util.Collection;

import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.taxonomy.IPartitionedScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.Workload;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;


/**
 * @author jcraveiro
 * 
 */
public abstract class MultiprocessorPartitionedScheduler extends AbsScheduler implements IPartitionedScheduler {
	
	private static final int DEFAULT_NUMBER_OF_PROCESSORS = 2;

	private Collection<? extends UniprocessorScheduler> schedulers;
	
	protected MultiprocessorPartitionedScheduler() {
		this(DEFAULT_NUMBER_OF_PROCESSORS);
	}

	protected MultiprocessorPartitionedScheduler(int numberOfProcessors) {
		this.schedulers = getNewSchedulerCollection(numberOfProcessors);
	}

	public void setNumberOfProcessors(int numberOfProcessors) throws InstantiationException {
		//WARNING: behaviour when not called immediately after creation is unspecified
		this.schedulers = getNewSchedulerCollection(numberOfProcessors);
	}
	
	

	protected abstract Collection<? extends UniprocessorScheduler> getNewSchedulerCollection(int n);

	@Override
	public void tick() {
		super.tick();
		for (UniprocessorScheduler scheduler : this.schedulers)
			scheduler.tick();

	}

	@Override
	public void tickle() {
		for (UniprocessorScheduler scheduler : this.schedulers)
			scheduler.tickle();

	}

	@Override
	protected boolean isCurrent(Job j) {
		for (UniprocessorScheduler scheduler : this.schedulers)
			if (scheduler.isCurrent(j))
				return true;
		return false;
	}
	
	@Override
	protected void removeFromCurrent(Job j) {

		for (UniprocessorScheduler scheduler : this.schedulers)
			if (scheduler.isCurrent(j))
				scheduler.removeFromCurrent(j);

	}

	protected void launchJobs(){

		for (UniprocessorScheduler scheduler : this.schedulers)
			scheduler.launchJobs();

	}

	protected void removeFromReadyQueue(Job j){


		for (UniprocessorScheduler scheduler : this.schedulers)
			scheduler.removeFromReadyQueue(j);
	}
	
	@Override
	protected void preemptJobInProc(IProcessor proc) {
		//TODO
		throw new UnsupportedOperationException();
	}

}
