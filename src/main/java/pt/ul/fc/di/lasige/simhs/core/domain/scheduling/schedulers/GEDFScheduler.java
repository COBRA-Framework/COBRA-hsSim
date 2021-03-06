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

import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.MultiprocessorGlobalScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.SchedulingPolicy;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.policies.EDFSchedulingPolicy;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.taxonomy.IFJPScheduler;

/**
 * @author jcraveiro
 *
 */
public class GEDFScheduler extends MultiprocessorGlobalScheduler implements IFJPScheduler {

	/**
	 * 
	 */
	public GEDFScheduler() {
		super();
	}

	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.schedulers.MultiprocessorGlobalScheduler#getPolicy()
	 */
	@Override
	protected SchedulingPolicy getPolicy() {
		return new EDFSchedulingPolicy();
	}

}
