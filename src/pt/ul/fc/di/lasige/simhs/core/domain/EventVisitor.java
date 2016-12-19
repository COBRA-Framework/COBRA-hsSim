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
package pt.ul.fc.di.lasige.simhs.core.domain;

import pt.ul.fc.di.lasige.simhs.core.domain.events.ClockTickEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobCompletedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobDeadlineMissEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobPreemptedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobReleasedEvent;

/**
 * Interface defining an event visitor. 
 * @author jcraveiro
 */
public interface EventVisitor<T> {
	
	public T visit(ClockTickEvent e);
	
	public T visit(JobReleasedEvent e);
	
	public T visit(JobDeadlineMissEvent e);
	
	public T visit(JobCompletedEvent e);
	
	public T visit(JobPreemptedEvent e);

}
