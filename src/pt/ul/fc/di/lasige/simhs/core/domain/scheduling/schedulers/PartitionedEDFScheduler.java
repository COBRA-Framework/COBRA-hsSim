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
package pt.ul.fc.di.lasige.simhs.core.domain.scheduling.schedulers;

import java.util.ArrayList;
import java.util.Collection;

import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.MultiprocessorPartitionedScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.SchedulingPolicy;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.UniprocessorScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.policies.EDFSchedulingPolicy;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.taxonomy.IFTPScheduler;

/**
 * @author jcraveiro
 *
 */
public class PartitionedEDFScheduler extends MultiprocessorPartitionedScheduler implements IFTPScheduler {

	/**
	 * @param n
	 */
	public PartitionedEDFScheduler(int n) {
		super(n);
	}

	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.schedulers.MultiprocessorPartitionedScheduler#getNewSchedulerCollection(int)
	 */
	@Override
	protected Collection<? extends UniprocessorScheduler> getNewSchedulerCollection(
			int n) {
		Collection<EDFScheduler> result = new ArrayList<EDFScheduler>();
		for (int i=0; i<n; i++)
			result.add(new EDFScheduler());
		return result;
	}

	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.Scheduler#getPolicy()
	 */
	@Override
	protected SchedulingPolicy getPolicy() {
		return new EDFSchedulingPolicy();
	}

}
