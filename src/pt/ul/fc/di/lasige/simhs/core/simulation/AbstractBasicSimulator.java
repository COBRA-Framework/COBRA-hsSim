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
package pt.ul.fc.di.lasige.simhs.core.simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Observable;
import java.util.Date;

import pt.ul.fc.di.lasige.simhs.core.domain.RTSystem;

/**
 * @author jcraveiro
 */
public abstract class AbstractBasicSimulator extends Observable {
	
	private RTSystem system;
	
	private Collection<ILogger<?>> loggers;

	/**
	 * @throws Exception 
	 * 
	 */
	protected AbstractBasicSimulator() throws Exception {
		
		
	}	
	protected void initialize() throws Exception
	{
		this.system = obtainSystem();
		this.loggers = obtainLoggers();
	}
	protected RTSystem getSystem() {
		return this.system;
	}
	
	protected abstract RTSystem obtainSystem() throws Exception;
	
	protected abstract Collection<ILogger<?>> obtainLoggers();
	
	protected abstract long getSimulationTime();
	
	public void run() {
		final long simulationTime = getSimulationTime();
		for (int t = 0; t <= simulationTime; t++) {
			this.system.tick();
			//System.out.println("Tick!");
		}
		for (ILogger<?> logger: this.loggers) {
			logger.close();
		}
	}

}
