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
package pt.ul.fc.di.lasige.simhs.addons.simulations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import com.sun.javafx.logging.Logger;

import pt.ul.fc.di.lasige.simhs.addons.loggers.UnitLogger;
import pt.ul.fc.di.lasige.simhs.addons.models.PeriodicInterfaceTask;
import pt.ul.fc.di.lasige.simhs.addons.models.UMPRComponent;
import pt.ul.fc.di.lasige.simhs.core.domain.RTSystem;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.schedulers.*;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.PeriodicTask;
import pt.ul.fc.di.lasige.simhs.core.platform.IPlatform;
import pt.ul.fc.di.lasige.simhs.core.platform.PhysicalPlatform;
import pt.ul.fc.di.lasige.simhs.core.platform.PhysicalProcessor;
import pt.ul.fc.di.lasige.simhs.core.simulation.AbstractBasicSimulator;
import pt.ul.fc.di.lasige.simhs.core.simulation.ILogger;

/**
 * @author jcraveiro
 */
public class UMPRSimulator extends AbstractBasicSimulator {

	/**
	 * @param args
	 */
	private static long SIMULATION_TIME;
	private int mul;

	public UMPRSimulator(int simulationTime) throws Exception {
		super();
		SIMULATION_TIME = simulationTime;
		initialize();
	}
	@Override
	protected RTSystem obtainSystem() throws InstantiationException, IllegalAccessException {

		IPlatform base = new PhysicalPlatform(4);
		base.bindProcessor(new PhysicalProcessor("PCPU0", 1.0),0);
		base.bindProcessor(new PhysicalProcessor("PCPU1", 1.0),1);
		base.bindProcessor(new PhysicalProcessor("PCPU2", 1.0),2);
		base.bindProcessor(new PhysicalProcessor("PCPU3", 1.0),3);
		//base.bindProcessor(new PhysicalProcessor("PCPU2", 1.0));
		mul=1000;
		RTSystem system = new RTSystem(base);
		
		system.setScheduler(new PartitionedEDFScheduler(4));
		
		UMPRComponent c1 = new UMPRComponent("VM1", system.getRootComponent(), 100*mul, 100*mul,2);
		c1.setScheduler(new PartitionedEDFScheduler(2));
		//add tasks to c1
		c1.addChild(new PeriodicTask("1", c1, 50.476*mul, 156*mul,156*mul,0));
		c1.addChild(new PeriodicTask("2", c1, 300.391*mul, 807*mul,807*mul,0));
		c1.addChild(new PeriodicTask("3", c1, 10*mul, 200*mul,200*mul,1));

		//add interface tasks to c1
		c1.addInterfaceTask(new PeriodicInterfaceTask("1", c1, 20*mul,20*mul,20*mul,0));
		c1.addInterfaceTask(new PeriodicInterfaceTask("2", c1, 6*mul,20*mul,20*mul,1));
		system.addChild(c1);

		UMPRComponent c2 = new UMPRComponent("VM2", system.getRootComponent(), 100*mul, 100*mul,2);
		c2.setScheduler(new PartitionedEDFScheduler(2));
		//add tasks to c1
		c2.addChild(new PeriodicTask("4", c2, 50.476*mul, 156*mul,156*mul,0));
		c2.addChild(new PeriodicTask("5", c2, 300.391*mul, 807*mul,807*mul,0));
		c2.addChild(new PeriodicTask("6", c2, 10*mul, 200*mul,200*mul,1));

		//add interface tasks to c1
		c2.addInterfaceTask(new PeriodicInterfaceTask("3", c2, 20*mul,20*mul,20*mul,2));
		c2.addInterfaceTask(new PeriodicInterfaceTask("4", c2, 6*mul,20*mul,20*mul,1));
		system.addChild(c2);

		UMPRComponent c3 = new UMPRComponent("VM3", system.getRootComponent(), 100*mul, 100*mul,2);
		c3.setScheduler(new PartitionedEDFScheduler(2));
		//add tasks to c1
		c3.addChild(new PeriodicTask("7", c3, 50.476*mul, 156*mul,156*mul,0));
		c3.addChild(new PeriodicTask("8", c3, 300.391*mul, 807*mul,807*mul,0));
		c3.addChild(new PeriodicTask("9", c3, 10*mul, 200*mul,200*mul,1));

		//add interface tasks to c1
		c3.addInterfaceTask(new PeriodicInterfaceTask("5", c3, 20*mul,20*mul,20*mul,3));
		c3.addInterfaceTask(new PeriodicInterfaceTask("6", c3, 7*mul,20*mul,20*mul,1));
		system.addChild(c3);

		return system;
	}

	@Override
	protected Collection<ILogger<?>> obtainLoggers() {

		Collection<ILogger<?>> loggers = new ArrayList<ILogger<?>>();

		ILogger<?> gLog;

		// Log to screen
		//gLog = new SuperBasicLogger(getSystem());
		//loggers.add(gLog);
		
		gLog = new UnitLogger(getSystem());
		loggers.add(gLog);
		
		/*gLog = new SimpleLogger(getSystem());
		loggers.add(gLog);
		 //Log to .grasp file
		try {
			gLog = new GraspLogger(getSystem(), new PrintStream(new File(
					"HMPR1.grasp")));
			loggers.add(gLog);
		} catch (FileNotFoundException e) {
			gLog = null;
			e.printStackTrace();
		}*/

		return loggers;

	}
	
	@Override
	protected long getSimulationTime() {
		return SIMULATION_TIME*mul;
	}
}
