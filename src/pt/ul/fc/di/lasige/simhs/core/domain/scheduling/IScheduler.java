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

import java.util.Observer;

import pt.ul.fc.di.lasige.simhs.core.domain.workload.Workload;
import pt.ul.fc.di.lasige.simhs.core.platform.IPlatform;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;
import pt.ul.fc.di.lasige.simhs.core.simulation.ILogger;

/**
 * Interface representing a scheduler. This interface defines the methods that
 * all scheduler must implement.
 * 
 * @author jcraveiro
 */
public interface IScheduler extends Observer {
	
	/**
	 * Adds a logger, so that it will be notified of events broadcast
	 * by this scheduler.
	 * @param l The logger to be added
	 */
	public void addLogger(ILogger<?> l);

	/**
	 * Advances 
	 */
	public void tick();

	/**
	 * 
	 */
	public void tickle();

	/**
	 * 
	 * @param t
	 */
	public void setTaskSet(Workload t);

	/**
	 * 
	 * @param p
	 */
	public void setPlatform(IPlatform p);
	
	/**
	 * 
	 * @param o
	 */
	public void addObserver(Observer o);
	
	/**
	 * Binds a processor to this scheduler's platform.
	 * @param proc The processor to bind
	 */
	public void bindProcessor(IProcessor proc);
	
	/**
	 * Unbinds a processor from this scheduler's platform.
	 * @param proc The processor to unbind
	 */
	public void unbindProcessor(IProcessor proc);

}

