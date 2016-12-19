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
import java.io.Writer;
import java.util.Observable;

import pt.ul.fc.di.lasige.simhs.core.domain.AbsEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.RTSystem;
import pt.ul.fc.di.lasige.simhs.core.domain.events.ClockTickEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobCompletedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobDeadlineMissEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobPreemptedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobReleasedEvent;
import pt.ul.fc.di.lasige.simhs.core.simulation.ILogger;

/**
 * @author jcraveiro
 *
 */
public class SuperBasicLogger implements ILogger<Boolean> {

	/**
	 * 
	 */
	private BufferedWriter writer;
	
	public SuperBasicLogger(RTSystem system) {
		system.addObserver(this);
		File file = new File("log.txt");
		try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void printLine(String line)
	{
		try {
			writer.write(line+"\n");
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
		System.err.println(e.getTime() + " JobReleasedEvent "+ e.getJob().toString());
		this.printLine(e.getTime() + " JobReleasedEvent "+ e.getJob().toString());
		return true;
	}

	@Override
	public Boolean visit(JobDeadlineMissEvent e) {
		System.err.println(e.getTime() + " JobDeadlineMissEvent "+ e.getJob().toString());
		this.printLine(e.getTime() + " JobDeadlineMissEvent "+ e.getJob().toString());
		return true;
	}

	@Override
	public Boolean visit(JobCompletedEvent e) {
		System.err.println(e.getTime() + " JobCompletedEvent "+ e.getJob().toString() + " on proc " + e.getProcessor());
		this.printLine(e.getTime() + " JobCompletedEvent "+ e.getJob().toString() + " on proc " + e.getProcessor());
		return true;
	}

	@Override
	public Boolean visit(JobPreemptedEvent e) {
		if ( e.getPreempted() != null)
		{
			System.err.println(e.getTime() + " JobPreemptedEvent "+ e.getPreempted().toString() + " on proc " + e.getProcessor());
			this.printLine(e.getTime() + " JobPreemptedEvent "+ e.getPreempted().toString() + " on proc " + e.getProcessor());
		}
		if (e.getPreemptedBy() != null)
		{
			System.err.println(e.getTime() + " JobResumedEvent "+ e.getPreemptedBy().toString() + " on proc " + e.getProcessor());
			this.printLine(e.getTime() + " JobResumedEvent "+ e.getPreemptedBy().toString() + " on proc " + e.getProcessor());
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
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
