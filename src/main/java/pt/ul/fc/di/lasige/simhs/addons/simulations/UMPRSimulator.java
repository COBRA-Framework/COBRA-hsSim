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

import pt.ul.fc.di.lasige.simhs.addons.loggers.GraspLogger;
import pt.ul.fc.di.lasige.simhs.addons.loggers.SuperBasicLogger;
import pt.ul.fc.di.lasige.simhs.addons.loggers.SimpleLogger;
import pt.ul.fc.di.lasige.simhs.addons.loggers.UnitLogger;
import pt.ul.fc.di.lasige.simhs.addons.models.PeriodicInterfaceTask;
import pt.ul.fc.di.lasige.simhs.addons.models.UMPRComponent;
import pt.ul.fc.di.lasige.simhs.core.domain.RTSystem;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.schedulers.GEDFScheduler;
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
	private static final long SIMULATION_TIME = 3000;


	public UMPRSimulator() throws Exception {
		super();
		initialize();
	}
	@Override
	protected RTSystem obtainSystem() throws InstantiationException, IllegalAccessException {
		
		/*
		 * This uses the UMPR to simulate MPR (which is a special case
		 * of the latter)
		 * 
		 * NOTE: Due to limitations in the Grasp tool itself, the GraspLogger
		 * does not deal well with processors of speed != 1.0
		 */
		IPlatform base = new PhysicalPlatform();
		base.bindProcessor(new PhysicalProcessor("PCPU0", 1.0));
		base.bindProcessor(new PhysicalProcessor("PCPU1", 1.0));
		base.bindProcessor(new PhysicalProcessor("PCPU2", 1.0));
		base.bindProcessor(new PhysicalProcessor("PCPU3", 1.0));
		base.bindProcessor(new PhysicalProcessor("PCPU4", 1.0));
		base.bindProcessor(new PhysicalProcessor("PCPU5", 1.0));
		base.bindProcessor(new PhysicalProcessor("PCPU6", 1.0));
		base.bindProcessor(new PhysicalProcessor("PCPU7", 1.0));
		base.bindProcessor(new PhysicalProcessor("PCPU8", 1.0));
		base.bindProcessor(new PhysicalProcessor("PCPU9", 1.0));
		
		
		RTSystem system = new RTSystem(base);
		
		system.setScheduler(new GEDFScheduler());
		
		UMPRComponent c1 = new UMPRComponent("VM1", system.getRootComponent(), 17, 20);
		c1.setScheduler(new GEDFScheduler());
		//add tasks to c1
		c1.addChild(new PeriodicTask("1", c1, 650, 695));
	
		//add interface tasks to c1
		c1.addInterfaceTask(new PeriodicInterfaceTask("VM1_VCPU0", c1, 17,20,20));
		system.addChild(c1);
		
		//Component C1
		/*UMPRComponent c1 = new UMPRComponent("VM1", system.getRootComponent(), 17, 20);
		c1.setScheduler(new GEDFScheduler());
		//add tasks to c1
		c1.addChild(new PeriodicTask("1", c1, 100, 695));
		c1.addChild(new PeriodicTask("5", c1, 226, 500));
		c1.addChild(new PeriodicTask("9", c1, 53, 365));
		//add interface tasks to c1
		c1.addInterfaceTask(new PeriodicInterfaceTask("VM1_VCPU0", c1, 17,20,20));
		system.addChild(c1);
		
		//Component C2
		UMPRComponent c2 = new UMPRComponent("VM2", system.getRootComponent(), 17,20);
		c2.setScheduler(new GEDFScheduler());
		//add tasks to c2
		c2.addChild(new PeriodicTask("2", c2, 346, 585));
		c2.addChild(new PeriodicTask("8", c2, 95, 445));
		c2.addInterfaceTask(new PeriodicInterfaceTask("VM2_VCPU0", c2, 17,20,20));
		//add interface tasks to c2
		system.addChild(c2);
		
		//Component C3
		UMPRComponent c3 = new UMPRComponent("VM3", system.getRootComponent(), 31,20);
		c3.setScheduler(new GEDFScheduler());
		//add tasks to c3
		c3.addChild(new PeriodicTask("3", c3, 105, 360));
		c3.addChild(new PeriodicTask("7", c3, 289, 615));
		c3.addChild(new PeriodicTask("10", c3, 253, 790));
		//add interface tasks to c3
		c3.addInterfaceTask(new PeriodicInterfaceTask("VM3_VCPU0", c3, 20,20,20));
		c3.addInterfaceTask(new PeriodicInterfaceTask("VM3_VCPU1", c3, 11,20,20));
		system.addChild(c3);
		
		//Component C3
		UMPRComponent c4 = new UMPRComponent("VM4", system.getRootComponent(), 20,20);
		c4.setScheduler(new GEDFScheduler());
		//add tasks to c3
		c4.addChild(new PeriodicTask("4", c4, 100, 605));
		c4.addChild(new PeriodicTask("6", c4, 357, 435));
		//add interface tasks to c3
		c4.addInterfaceTask(new PeriodicInterfaceTask("VM4_VCPU0", c4, 20,20,20));
		system.addChild(c4);*/
		
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
		return SIMULATION_TIME;
	}
}
