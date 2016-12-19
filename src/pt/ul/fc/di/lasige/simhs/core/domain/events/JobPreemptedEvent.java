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
package pt.ul.fc.di.lasige.simhs.core.domain.events;

import pt.ul.fc.di.lasige.simhs.core.domain.AbsEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.EventVisitor;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.Job;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;

public class JobPreemptedEvent extends AbsEvent {

	private Job preempted;
	private Job preemptedBy;
	
	private IProcessor processor;

	public JobPreemptedEvent(int time, Job preempted, Job preemptedBy) {
		super(time);

		
		this.preempted = preempted;
		this.preemptedBy = preemptedBy;
	}
	
	public JobPreemptedEvent(int time, Job preempted, Job preemptedBy, IProcessor processor) {
		this(time,preempted,preemptedBy);
		this.processor = processor;
	}

	/**
	 * @return the preempted
	 */
	public Job getPreempted() {
		return this.preempted;
	}

	/**
	 * @return the preemptedBy
	 */
	public Job getPreemptedBy() {
		return this.preemptedBy;
	}

	public <E> E accept(EventVisitor<E> visitor) {
		return visitor.visit(this);
	}

	/**
	 * @return the processor
	 */
	public IProcessor getProcessor() {
		return this.processor;
	}

}
