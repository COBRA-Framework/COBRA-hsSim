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

import java.util.Observable;

import pt.ul.fc.di.lasige.simhs.core.domain.events.ClockTickEvent;

/**
 * @author jcraveiro
 *
 */
public class Clock extends Observable implements Cloneable {

	private int time;
	
	public Clock clone() {
		Clock other = null;
		try {
			other = (Clock) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
		return other;
	}
	
	public Clock(){
		this.time = -1;
	}
	
	public void tick(){
		this.time++;
		setChanged();
		notifyObservers(new ClockTickEvent(this.getTime()));
	}
	
	public int getTime(){
		return this.time;
	}
	
	public String toString() {
		return Integer.toString(this.time);
	}
}
