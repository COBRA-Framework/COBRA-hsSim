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
package pt.ul.fc.di.lasige.simhs.addons.loggers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Observable;

import javax.naming.event.EventContext;

import pt.ul.fc.di.lasige.simhs.core.domain.AbsEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.RTSystem;
import pt.ul.fc.di.lasige.simhs.core.domain.events.ClockTickEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobCompletedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobDeadlineMissEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobPreemptedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobReleasedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IComponent;
import pt.ul.fc.di.lasige.simhs.core.simulation.ILogger;

/**
 * @author jcraveiro
 *
 */
public class UnitLogger implements ILogger<Boolean> {

	/**
	 * 
	 */
	
	private List<BufferedWriter> outputFilesVMs;
	private List<BufferedWriter> outputFilesTasks;
	private int eventCounter;
	private int eventCounterVMs;
	private int mul = 1000;
	
	public UnitLogger(RTSystem system) {
		system.addObserver(this);
		outputFilesTasks = new ArrayList<BufferedWriter>();
		outputFilesVMs = new ArrayList<BufferedWriter>();
		File dir = new File("outputTasks");
		dir.mkdir();
		File file = new File("outputTasks/output0");
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			outputFilesTasks.add(writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File dirVM = new File("outputVMs");
		dirVM.mkdir();
		File fileVM = new File("outputVMs/output0");
		BufferedWriter writerVM;
		try {
			writerVM = new BufferedWriter(new FileWriter(fileVM));
			outputFilesVMs.add(writerVM);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eventCounter = 0;
		eventCounterVMs = 0;
		
	}
	public void addOutputFile(boolean TaskOrVM)//true is task, false is VM
	{
		File file;
		BufferedWriter writer;
		if(TaskOrVM)
		{
			file = new File("outputTasks/output"+outputFilesTasks.size());
			
			try {
				writer = new BufferedWriter(new FileWriter(file));
				outputFilesTasks.add(writer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			file = new File("outputVMs/output"+outputFilesVMs.size());
			
			try {
				writer = new BufferedWriter(new FileWriter(file));
				outputFilesVMs.add(writer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void printLine(String line, int pcpu,boolean TaskOrVM)
	{
		try {
			if(TaskOrVM)
			{
				while(outputFilesTasks.size()<=pcpu)
				{
					addOutputFile(true);
				}
				outputFilesTasks.get(pcpu).write(line+"\n");
			}
			else
			{
				while(outputFilesVMs.size()<=pcpu)
					addOutputFile(false);
				outputFilesVMs.get(pcpu).write(line+"\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Boolean visit(ClockTickEvent e) {
		// do nothing
		return false;
	}

	@Override
	public Boolean visit(JobReleasedEvent e) {
		if(!e.getJob().toString().contains("VM"))
		{
			String [] a = e.getJob().toString().split("_");
			long time = e.getTime();
			this.printLine("Event ID: "+eventCounterVMs++,0,true);
			this.printLine("Job: "+a[1]+"."+(Integer.parseInt(a[2])+1),0,true);
			this.printLine("Type: release",0,true);
			this.printLine("Time: "+time*mul,0,true);
			this.printLine("",0,true);
		}
		else
		//if(e.getJob().getParentTask() instanceof IComponent)
		{
			long time = e.getTime();
			this.printLine("Event ID: "+eventCounter++,0,false);
			this.printLine("Job: "+e.getJob().getPPTName()+"."+(e.getJob().getPPTSequenceNumber()+1),0,false);
			this.printLine("Type: release",0,false);
			this.printLine("Time: "+time*mul,0,false);
			this.printLine("",0,false);
		}
		return true;
	}

	public Boolean visit(JobDeadlineMissEvent e) {
		if(!e.getJob().toString().contains("VM"))
		{
			String [] a = e.getJob().toString().split("_");
			long time = e.getTime();
			this.printLine("Event ID: "+eventCounterVMs++,0,true);
			this.printLine("Job: "+a[1]+"."+(Integer.parseInt(a[2])+1),0,true);
			this.printLine("Type: completion",0,true);
			this.printLine("Time: "+time*mul,0,true);
			this.printLine("",0,true);			
		}
		else
		//if(e.getJob().getParentTask() instanceof IComponent)
		{
			long time = e.getTime();
			this.printLine("Event ID: "+eventCounter++,0,false);
			this.printLine("Job: "+e.getJob().getPPTName()+"."+(e.getJob().getPPTSequenceNumber()+1),0,false);
			this.printLine("Type: completion",0,false);
			this.printLine("Time: "+time*mul,0,false);
			this.printLine("",0,false);		
		}
		return true;
	}

	@Override
	public Boolean visit(JobCompletedEvent e) {
		if(!e.getJob().toString().contains("VM"))
		{

			String [] a = e.getJob().toString().split("_");
			int cpu = Integer.parseInt((e.getProcessor().toString().substring(e.getProcessor().toString().length()-1)));
			long time = e.getTime();
			this.printLine("Event ID: "+eventCounterVMs++,cpu,true);
			this.printLine("Job: "+a[1]+"."+(Integer.parseInt(a[2])+1),cpu,true);
			this.printLine("Type: completion",cpu,true);
			this.printLine("Time: "+time*mul,cpu,true);
			this.printLine("",cpu,true);
			//if(cpu==0)
				//System.out.println(e.getTime()+" "+a[1]+"."+(Integer.parseInt(a[2])+1));
		}
		else
		//if(e.getJob().getParentTask() instanceof IComponent)
		{
			int cpu = Integer.parseInt((e.getProcessor().toString().substring(e.getProcessor().toString().length()-1)));
			long time = e.getTime();
			this.printLine("Event ID: "+eventCounter++,cpu,false);
			this.printLine("Job: "+e.getJob().getPPTName()+"."+(e.getJob().getPPTSequenceNumber()+1),cpu,false);
			this.printLine("Type: completion",cpu,false);
			this.printLine("Time: "+time*mul,cpu,false);
			this.printLine("",cpu,false);
		}
		return true;
	}

	@Override
	public Boolean visit(JobPreemptedEvent e) {
		if ( e.getPreempted() != null)
		{
			if(!e.getPreempted().toString().contains("VM"))
			{
				int cpu = Integer.parseInt((e.getProcessor().toString().substring(e.getProcessor().toString().length()-1)));
				String [] a = e.getPreempted().toString().split("_");
				long time = e.getTime();
				this.printLine("Event ID: "+eventCounterVMs++,cpu,true);
				this.printLine("Job: "+a[1]+"."+(Integer.parseInt(a[2])+1),cpu,true);
				this.printLine("Type: switch_away",cpu,true);
				this.printLine("Time: "+time*mul,cpu,true);
				this.printLine("",cpu,true);
				
			}
			else
			{
				int cpu = Integer.parseInt((e.getProcessor().toString().substring(e.getProcessor().toString().length()-1)));
				long time = e.getTime();
				this.printLine("Event ID: "+eventCounter++,cpu,false);
				this.printLine("Job: "+e.getPreempted().getPPTName()+"."+(e.getPreempted().getPPTSequenceNumber()+1),cpu,false);
				this.printLine("Type: switch_away",cpu,false);
				this.printLine("Time: "+time*mul,cpu,false);
				this.printLine("",cpu,false);
			}
		}
		if (e.getPreemptedBy() != null)
		{
			if(!e.getPreemptedBy().toString().contains("VM"))
			{
				int cpu = Integer.parseInt((e.getProcessor().toString().substring(e.getProcessor().toString().length()-1)));
				String [] a = e.getPreemptedBy().toString().split("_");
				long time = e.getTime();
				this.printLine("Event ID: "+eventCounterVMs++,cpu,true);
				this.printLine("Job: "+a[1]+"."+(Integer.parseInt(a[2])+1),cpu,true);
				this.printLine("Type: switch_to",cpu,true);
				this.printLine("Time: "+time*mul,cpu,true);
				this.printLine("",cpu,true);
			}
			//if(e.getPreempted().getParentTask() instanceof IComponent)
			else
			{
				int cpu = Integer.parseInt((e.getProcessor().toString().substring(e.getProcessor().toString().length()-1)));
				long time = e.getTime();
				this.printLine("Event ID: "+eventCounter++,cpu,false);
				this.printLine("Job: "+e.getPreemptedBy().getPPTName()+"."+(e.getPreemptedBy().getPPTSequenceNumber()+1),cpu,false);
				this.printLine("Type: switch_to",cpu,false);
				this.printLine("Time: "+time*mul,cpu,false);
				this.printLine("",cpu,false);
			}
		}
		return true;
	}

	@Override
	public void update(Observable o, Object arg) {
		AbsEvent e = (AbsEvent)arg;
		e.accept(this);
	}

	@Override
	public void close() {
		try {
			for(BufferedWriter w:outputFilesTasks)
				w.close();
			for(BufferedWriter w:outputFilesVMs)
				w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
