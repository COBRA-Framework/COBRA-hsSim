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

public class ClockTickEvent extends AbsEvent {

	public ClockTickEvent(int time) {
		super(time);
	}

	/* (non-Javadoc)
	 * @see pt.ul.fc.di.lasige.simhs.domain.events.Event#accept(pt.ul.fc.di.lasige.simhs.domain.EventVisitor)
	 */
	@Override
	public <T> T accept(EventVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
