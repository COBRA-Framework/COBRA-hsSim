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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class MPRSimulator extends AbstractBasicSimulator {

	/**
	 * @param args
	 */
	private List<Component> VMs;
	private List<Interface> interfaces;
	private int mul;
	private static long SIMULATION_TIME;
	private int pcpus;
	private String parentScheduler;

	public MPRSimulator(String input, String interfaces, int numberPCPUs, long simulationTime) throws Exception {
		super();
		this.pcpus = numberPCPUs;
		SIMULATION_TIME = simulationTime;
		parse(input, interfaces);
		initialize();
		
	}
	private void parse(String taskFile, String interfaceFile)
	{
		XMLInterpreter input = new XMLInterpreter(taskFile);
		XMLInterpreter output = new XMLInterpreter(interfaceFile);
		input.parseFile();
		output.parseFile();
		Component rootComponent = input.getRootComponent();
		parentScheduler= rootComponent.getSchedulingPolicy();
		
		Interface interfaceRoot = output.getRootInterface();
		VMs = rootComponent.getChildComponents();
		interfaces = interfaceRoot.getChildComponents();
	}
	@Override
	protected RTSystem obtainSystem() throws InstantiationException, IllegalAccessException {
		
		
		IPlatform base = new PhysicalPlatform();
		UMPRComponent c1;
		Component vm;
		String name;
		mul=1000;
		double total=0;
		int taskIndex=1;
        int vcpuIndex=1;

		for(int i = 0;i<pcpus;i++)
		{
			name = "PCPU"+i;
			base.bindProcessor(new PhysicalProcessor(name, 1.0));
		}
		
		System.out.println("Simulation information:");
		RTSystem system = new RTSystem(base);		
		selectAlgorithm(system, parentScheduler);
		System.out.println("\tScheduler system: "+parentScheduler);

		for(Interface inter:interfaces){
			for(Task task:inter.getTaskset()){
				total += task.getExe();
			}
			c1 = new UMPRComponent(inter.getInterfaceName(), system.getRootComponent(), total*mul, (int) inter.getTaskset().get(0).getPeriod()*mul);
			c1.setScheduler(new GEDFScheduler());
			selectAlgorithm(c1,VMs.get(interfaces.indexOf(inter)).getSchedulingPolicy(),inter.getTaskset().size());
			
			//add VCPUS to c1
			for(Task task:inter.getTaskset()){
				c1.addInterfaceTask(new PeriodicInterfaceTask(Integer.toString(vcpuIndex++), c1, task.getExe()*mul,(int)task.getPeriod()*mul,(int)task.getPeriod()*mul));
               // c1.addInterfaceTask(new PeriodicInterfaceTask(inter.getInterfaceName()+"_"+task.getName(), c1, task.getExe()*mul,(int)task.getPeriod()*mul,(int)task.getPeriod()*mul));
            }
			
			vm = VMs.get(interfaces.indexOf(inter));
			
			System.out.println("\tScheduler "+vm.getComponentName()+": "+vm.getSchedulingPolicy());
			System.out.println("\t\tLoad: "+c1.getUtilization());
			//add tasks to the VM
			for(Task task:vm.getTaskset())
                c1.addChild(new PeriodicTask(Integer.toString(taskIndex++), c1, task.getExe() * mul, (int) task.getPeriod() * mul));
			system.addChild(c1);
			total = 0;
		}
		
		System.out.println("\tTime to simulate: "+SIMULATION_TIME+" ms");
		
		return system;
	}
	
	private void selectAlgorithm(RTSystem system, String algorithm)
	{
		if(algorithm.equalsIgnoreCase("edf"))
			system.setScheduler(new EDFScheduler());
		else if(algorithm.equalsIgnoreCase("dm"))
			system.setScheduler(new DMScheduler());
		else if(algorithm.equalsIgnoreCase("gedf"))
			system.setScheduler(new GEDFScheduler());
		else if(algorithm.equalsIgnoreCase("gllf"))
			system.setScheduler(new GLLFScheduler());
		else if(algorithm.equalsIgnoreCase("grm"))
			system.setScheduler(new GRMScheduler());
		else if(algorithm.equalsIgnoreCase("llf"))
			system.setScheduler(new LLFScheduler());
		else if(algorithm.equalsIgnoreCase("mcnaughton"))
			system.setScheduler(new McNScheduler());
		else if(algorithm.equalsIgnoreCase("pedf"))
			system.setScheduler(new PartitionedEDFScheduler(this.pcpus));
		else if(algorithm.equalsIgnoreCase("prm"))
			system.setScheduler(new PartitionedRMScheduler(this.pcpus));
		else if(algorithm.equalsIgnoreCase("rm"))
			system.setScheduler(new RMScheduler());
		else if(algorithm.equalsIgnoreCase("tsp"))
			system.setScheduler(new TSPScheduler());
		else
		{
			System.out.println("Algorithm for System not found...");
			System.exit(1);
		}
	}
	
	private void selectAlgorithm(UMPRComponent component, String algorithm, int vcpus)
	{
		if(algorithm.equalsIgnoreCase("edf"))
			component.setScheduler(new EDFScheduler());
		else if(algorithm.equalsIgnoreCase("dm"))
			component.setScheduler(new DMScheduler());
		else if(algorithm.equalsIgnoreCase("gedf"))
			component.setScheduler(new GEDFScheduler());
		else if(algorithm.equalsIgnoreCase("gllf"))
			component.setScheduler(new GLLFScheduler());
		else if(algorithm.equalsIgnoreCase("grm"))
			component.setScheduler(new GRMScheduler());
		else if(algorithm.equalsIgnoreCase("llf"))
			component.setScheduler(new LLFScheduler());
		else if(algorithm.equalsIgnoreCase("mcnaughton"))
			component.setScheduler(new McNScheduler());
		else if(algorithm.equalsIgnoreCase("pedf"))
			component.setScheduler(new PartitionedEDFScheduler(vcpus));
		else if(algorithm.equalsIgnoreCase("prm"))
			component.setScheduler(new PartitionedRMScheduler(vcpus));
		else if(algorithm.equalsIgnoreCase("rm"))
			component.setScheduler(new RMScheduler());
		else if(algorithm.equalsIgnoreCase("tsp"))
			component.setScheduler(new TSPScheduler());
		else
		{
			System.out.println("Algorithm for Component not found...");
			System.exit(1);
		}
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
