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

import java.util.Observable;

import pt.ul.fc.di.lasige.simhs.core.domain.AbsEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.RTSystem;
import pt.ul.fc.di.lasige.simhs.core.domain.events.ClockTickEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobCompletedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobDeadlineMissEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobPreemptedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobReleasedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.Job;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.ITask;
import pt.ul.fc.di.lasige.simhs.core.simulation.ILogger;

/**
 * A simple logger that prints to the screen
 * @author jcraveiro
 *
 */
public class SimpleLogger implements ILogger<String> {

	public SimpleLogger(RTSystem system) {
		
		system.addObserver(this);
		
	}
	
	@Override
	public void update(Observable o, Object arg) {

		System.out.println(((AbsEvent) arg).accept(this));
	}
	@Override
	public String visit(ClockTickEvent e) {		
		StringBuilder sb = new StringBuilder();
		sb.append("-------------------------Master time: "+ e.getTime() +" ---------------------------------" +
		"-----------------------------------");
		return sb.toString();
	}

	@Override
	public String visit(JobReleasedEvent e) {
		Job job = e.getJob();
		StringBuilder sb = new StringBuilder();
		sb.append("timeUnit ");
		sb.append(e.getTime()+" ");
		if(job.getParentTask() instanceof ITask){
			sb.append("job arrived ");
			sb.append(job.toString()+ " ");
			sb.append("ParentTask "+job.getParentTask().toStringId());
		}
		else {//partition
			sb.append("partition replenished ");
			sb.append(job.getParentTask().toStringId()+" ");
			

		}
		sb.append(System.getProperty("line.separator"));
		//System.out.print(sb);

		return sb.toString();
	}
	@Override
	public String visit(JobDeadlineMissEvent e) {
		Job job = e.getJob();
		StringBuilder sb = new StringBuilder();
		sb.append("timeUnit ");
		sb.append(e.getTime()+" ");
		if(job.getParentTask() instanceof ITask){
			sb.append("job deadline missed ");
			sb.append(job.toString()+ " ");
			sb.append("ParentTask "+job.getParentTask().toStringId());
		}
		else {//partition
			sb.append("partition deadline missed ");
			sb.append(job.getParentTask().toStringId()+" ");
			

		}
		sb.append(System.getProperty("line.separator"));
		//System.out.print(sb);

		return sb.toString();
	}
	
	@Override
	public String visit(JobCompletedEvent e) {
		Job job = e.getJob();
		StringBuilder sb = new StringBuilder();
		sb.append("timeUnit ");
		sb.append(e.getTime()+" ");
		if (e.getRemainingCapacity() == 0) {
			// Job terminated
			if(job.getParentTask() instanceof ITask){
				sb.append("job terminate ");
				sb.append(job.toString()+ " ");
				sb.append("ParentTask "+job.getParentTask().toStringId());
			}
			else {//partition
				sb.append("partition terminate ");
				sb.append(job.getParentTask().toStringId()+" ");
				

			}
		

		} else {

			if (e.getRemainingCapacity() >= 0) {
				// Job executed one unit of time
				if(job.getParentTask() instanceof ITask){
					sb.append("job execute ");
					sb.append(job.toString()+ " ");
					sb.append("ParentTask "+job.getParentTask().toStringId());
				}
				else {//partition
					sb.append("partition execute ");
					sb.append(job.getParentTask().toStringId()+" ");
					sb.append("remaining "+job.getRemainingCapacity());

				}
				
			}
		}
		sb.append(System.getProperty("line.separator"));
		//System.out.print(sb);

		return sb.toString();
	}
	@Override
	public void close() {
		// Non-applicable to this type of logger.
		
	}

	@Override
	public String visit(JobPreemptedEvent e) {
		// TODO Port code from Grasp logger (Rui should have done this!)
		return null;
	}

}
