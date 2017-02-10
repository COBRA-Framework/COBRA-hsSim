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
import java.util.Observer;

import pt.ul.fc.di.lasige.simhs.core.domain.events.ClockTickEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.IScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.BasicComponent;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IComponent;
import pt.ul.fc.di.lasige.simhs.core.platform.IPlatform;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;

/**
 * @author jcraveiro
 *
 */
public class RTSystem extends Observable implements Observer {

	private Clock clock;
	
	private IComponent rootComponent;
	
	public RTSystem clone() {
		RTSystem other = null;
		try {
			other = (RTSystem) super.clone();
			other.clock = this.clock.clone();
			other.rootComponent = this.rootComponent.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
		return other;
	}

	public RTSystem(IPlatform platform){
		this.clock = new Clock();
		this.clock.addObserver(this);
		this.rootComponent = new BasicComponent(platform);
	}
	
	public void setScheduler(IScheduler s) {
		this.rootComponent.setScheduler(s);		
		s.addObserver(this);
		s.setObserver(this);
	}
	
	public void addChild(IAbsSchedulable at) {
		this.rootComponent.addChild(at);
		at.addObserver(this);
	}


	public Clock getClock(){
		return this.clock;
	}

	public void tick(){
		this.clock.tick();
				
	}

	public void tickle() {
		throw new UnsupportedOperationException();
	}

	public void tickle(double exec) {
		throw new UnsupportedOperationException();
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof ClockTickEvent) {
			this.rootComponent.tick();
			this.rootComponent.tickle();
		}
		
		setChanged();
		notifyObservers(arg1);
	}

	
	public void bindProcessor(IProcessor proc, int numberOfProcessor) {
		this.rootComponent.bindProcessor(proc,numberOfProcessor);
	}


	
	public IComponent getRootComponent() {
		return this.rootComponent;
	}

	public void unbindProcessor(IProcessor proc, int numberOfProcessor) {
		this.rootComponent.unbindProcessor(proc,numberOfProcessor);
	}

	
	

}
