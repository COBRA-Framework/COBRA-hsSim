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
package pt.ul.fc.di.lasige.simhs.addons.loggers;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import pt.ul.fc.di.lasige.simhs.core.domain.AbsEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.RTSystem;
import pt.ul.fc.di.lasige.simhs.core.domain.events.ClockTickEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobCompletedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobDeadlineMissEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobPreemptedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.events.JobReleasedEvent;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.Job;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IComponent;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.ITask;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;
import pt.ul.fc.di.lasige.simhs.core.simulation.ILogger;

/**
 * 
 * @author jcraveiro
 */
public class GraspLogger implements ILogger<Boolean> {
	
	private static final String WHITESPACE = " ";
	
	private static final String PLOT = "plot ";
	
	private static final String CORE = "core";
	
	//private static final String RULE = "# -----------------------------------------------------------------------------";
	
	private static final String RULE = "\n";

	
	private PrintStream ps;
	
	private Map<IComponent,Set<Job>> currents;
	
	private StringBuilder sb;
	
	private static final String NEWLINE =  System.getProperty("line.separator");
	
	private static final String[] COLORS = {
		"#000000", //black
		"#0000FF",  //blue
		"#FF0000", //red
		"#FF00FF", //magenta 
		"#00FF00", //green 
		"#00FFFF", //cyan 
		"#FFFF00", //yellow 
		"#008080", //teal
		"#FF8C00" //dark orange
	};
	
	/**
	 * Collects the tasks with ids to prepend the Grasp logger format preamble
	 */
	private Set<IAbsSchedulable> taskIds;
	
	/**
	 * Collects the partitions with ids to prepend the Grasp logger format preamble
	 * (will use these as servers)
	 */
	private Set<IAbsSchedulable> partitionIds;
	
	private Set<IProcessor> processorIds;

	/**
	 * 
	 */
	public GraspLogger(RTSystem system, PrintStream ps) {
		system.addObserver(this);
		
		this.ps = ps;
		
		currents = new HashMap<IComponent, Set<Job>>();
		
		sb = new StringBuilder();
		
		taskIds = new TreeSet<IAbsSchedulable>();
		partitionIds = new TreeSet<IAbsSchedulable>();
		processorIds = new TreeSet<IProcessor>();
		
	}

	@Override
	public void update(Observable o, Object arg) {
		AbsEvent e = (AbsEvent)arg;
		e.accept(this);
	}

	@Override
	public Boolean visit(JobReleasedEvent e) {
		
		IAbsSchedulable at = e.getJob().getParentTask();
		
		if (at instanceof IComponent) {
			sb.append(PLOT);
			sb.append(e.getTime());
			sb.append(WHITESPACE);
			sb.append("serverReplenished ");
			sb.append(at.toStringId());
			partitionIds.add(at);
			sb.append(WHITESPACE);
			sb.append((int) ((IComponent) at).getBudget());
			//sb.append((int) e.getJob().getRemainingCapacity());
			sb.append(NEWLINE);
		} else if (at instanceof ITask) {
			sb.append(PLOT);
			sb.append(e.getTime());
			sb.append(WHITESPACE);
			sb.append("jobArrived ");
			sb.append(e.getJob().toString());
			sb.append(WHITESPACE);
			sb.append(at.toStringId());
			taskIds.add(at);
			sb.append(NEWLINE);
		}
		
		return true;
	}

	@Override
	public Boolean visit(JobPreemptedEvent e) {

		IAbsSchedulable atOff = null;
		
		if (e.getPreempted() != null) {
			atOff = e.getPreempted().getParentTask();
		}

		IAbsSchedulable atOn = null;
		
		if (e.getPreemptedBy() != null) {
			atOn = e.getPreemptedBy().getParentTask();
		}
		
		if (atOn == null || atOn instanceof IComponent) {
			
			Set<Job> childJobsPreempted = null;
			Set<Job> childJobsPreempting = null;

			if (atOff != null) {
				
				if (atOff instanceof IComponent) {
				
					sb.append(PLOT);
					sb.append(e.getTime());
					sb.append(WHITESPACE);
					sb.append("serverPreempted ");
					sb.append(atOff.toStringId());
					childJobsPreempted = currents.get(atOff);
				
					partitionIds.add(atOff);
				} else if (atOff instanceof ITask) {
					
					sb.append(PLOT);
					sb.append(e.getTime());
					sb.append(WHITESPACE);
					sb.append("jobPreempted ");
					sb.append(e.getPreempted().toString());
					sb.append(WHITESPACE);
					sb.append("-processor ");
					sb.append(CORE);
					sb.append(e.getProcessor().toString());
					
					processorIds.add(e.getProcessor());
					
				}

				sb.append(NEWLINE);
				
				
				
			}
			
			if (atOn != null) {

				sb.append(PLOT);
				sb.append(e.getTime());
				sb.append(WHITESPACE);
				sb.append("serverResumed ");
				sb.append(atOn.toStringId());

				partitionIds.add(atOn);

				sb.append(NEWLINE);


				childJobsPreempting = currents.get(atOn);

			}
			/*
			if (childJobsPreempted != null) {
				for (Job childJobPreempted : childJobsPreempted) {
					childPreemption(e.getTime(), childJobPreempted, null);
				}
			}

			if (childJobsPreempting != null) {
				for (Job childJobPreempting : childJobsPreempting) {
					childPreemption(e.getTime(), null, childJobPreempting);
				}
			}
			*/
		} else if (atOn instanceof ITask) {
			
			if (e.getPreempted() != null) {

				sb.append(PLOT);
				sb.append(e.getTime());
				sb.append(WHITESPACE);
				sb.append("jobPreempted ");
				sb.append(e.getPreempted().toString());
				sb.append(" -target ");
				sb.append(e.getPreemptedBy().toString());
				sb.append(" -processor ");
				sb.append(CORE);
				sb.append(e.getProcessor().toString());
				
				processorIds.add(e.getProcessor());


				sb.append(NEWLINE);
			}
			
			sb.append(PLOT);
			sb.append(e.getTime());
			sb.append(WHITESPACE);
			sb.append("jobResumed ");
			sb.append(e.getPreemptedBy().toString());
			sb.append(WHITESPACE);
			sb.append("-processor ");
			sb.append(CORE);
			sb.append(e.getProcessor().toString());
			
			processorIds.add(e.getProcessor());

			sb.append(NEWLINE);
			
		}

		IComponent parent = null;

		if (atOn != null) {
			parent = atOn.getParent();
		} else if (atOff != null) {
			parent = atOff.getParent();
		}
		
		if (parent != null) {
			//currents.remove(parent);
			if (atOn != null) {
				Set<Job> jobs = currents.get(parent);
				if (jobs == null) {
					jobs = new HashSet<Job>();
					currents.put(parent,jobs);
				}
				if (e.getPreempted() != null)
					jobs.remove(e.getPreempted());
				if (e.getPreemptedBy() != null)
					jobs.add(e.getPreemptedBy());
			}
		}
		
		return true;
	}
	
	/*
	private void childPreemption(int time, Job childJobPreemptedArg, Job childJobPreemptingArg) {
		IAbsSchedulable atOff = null;
		
		if (childJobPreemptedArg != null && childJobPreemptedArg.getRemainingCapacity() > 0) {
			atOff = childJobPreemptedArg.getParentTask();
		}

		IAbsSchedulable atOn = null;
		
		if (childJobPreemptingArg != null) {
			atOn = childJobPreemptingArg.getParentTask();
		}
		
		if (atOn == null || atOn instanceof IComponent) {
			
			Set<Job> childJobsPreempted = null;
			Set<Job> childJobsPreempting = null;

			if (atOff != null) {
				
				if (atOff instanceof IComponent) {
				
					sb.append(PLOT);
					sb.append(time);
					sb.append(WHITESPACE);
					sb.append("serverPreempted ");
					sb.append(atOff.toStringId());
					childJobsPreempted = currents.get(atOff);
				
					partitionIds.add(atOff);
					sb.append(" # childPreemption");
					
				} else if (atOff instanceof ITask) {
					
					sb.append(PLOT);
					sb.append(time);
					sb.append(WHITESPACE);
					sb.append("jobPreempted ");
					sb.append(childJobPreemptedArg.toString());
					
				}

				sb.append(NEWLINE);
				
				
				
			}
			
			if (atOn != null) {

				sb.append(PLOT);
				sb.append(time);
				sb.append(WHITESPACE);
				sb.append("serverResumed ");
				sb.append(atOn.toStringId());

				partitionIds.add(atOn);

				sb.append(NEWLINE);


				childJobsPreempting = currents.get(atOn);

			}
			
			if (childJobsPreempted != null) {
				
				for (Job childJobPreempted : childJobsPreempted)
					childPreemption(time, childJobPreempted, null);
				
				for (Job childJobPreempting : childJobsPreempting)
					childPreemption(time, null, childJobPreempting);
			}
			
		} else if (atOn instanceof ITask) {
			
			if (childJobPreemptedArg != null) {

				sb.append(PLOT);
				sb.append(time);
				sb.append(WHITESPACE);
				sb.append("jobPreempted ");
				sb.append(childJobPreemptedArg.toString());
				sb.append(" -target ");
				sb.append(childJobPreemptingArg.toString());
				


				sb.append(NEWLINE);
			}
			
			sb.append(PLOT);
			sb.append(time);
			sb.append(WHITESPACE);
			sb.append("jobResumed ");
			sb.append(childJobPreemptingArg.toString());
			


			sb.append(NEWLINE);
			
		}
	}
	*/

	@Override
	public Boolean visit(JobCompletedEvent e) {
		
		IAbsSchedulable at = e.getJob().getParentTask();
		
		if (at instanceof IComponent) {
			
			if (((IComponent) at).getBudget() == 0) {

				sb.append(PLOT);
				sb.append(e.getTime());
				sb.append(WHITESPACE);
				sb.append("serverDepleted ");
				sb.append(at.toStringId());
				partitionIds.add(at);

				sb.append(NEWLINE);
			}
			
			/*Set<Job> childJobsPreempted = currents.get(at);
			
			if (childJobsPreempted != null) 
				for (Job childJobPreempted : childJobsPreempted)
					childPreemption(e.getTime(), childJobPreempted, null);
					*/
			
			sb.append(PLOT);
			sb.append(e.getTime());
			sb.append(WHITESPACE);
			sb.append("serverPreempted ");
			sb.append(at.toStringId());
			partitionIds.add(at);
			
			//sb.append(" # because serverDepleted");
			
			sb.append(NEWLINE);
			
		} else if (at instanceof ITask) {
			sb.append(PLOT);
			sb.append(e.getTime());
			sb.append(WHITESPACE);
			sb.append("jobCompleted ");
			sb.append(e.getJob().toString());
			sb.append(WHITESPACE);
			sb.append("-processor ");
			sb.append(CORE);
			sb.append(e.getProcessor().toString());

			sb.append(NEWLINE);	
			
		}
		
		IComponent parent = at.getParent();
		currents.remove(parent);
				
		return true;
	}

	@Override
	public Boolean visit(JobDeadlineMissEvent e) {
		sb.append("# "); //comment
		
		sb.append(PLOT);
		sb.append(e.getTime());
		sb.append(WHITESPACE);
		sb.append("jobMissedDeadline ");
		sb.append(e.getJob().toString());
		
		sb.append(NEWLINE);
		return true;
	}

	@Override
	public Boolean visit(ClockTickEvent e) {
		sb.append(RULE);
		sb.append(NEWLINE);
		return true;
	}

	@Override
	public void close() {
		StringBuilder preamble = new StringBuilder();
		
		/*
		 * Processors
		 */
		for (IProcessor processorId : processorIds) {
			preamble.append("newProcessor " + CORE + processorId.toString() + " -name \"" + CORE + processorId.toString() + "\"");
			preamble.append(NEWLINE);
		}
		preamble.append(NEWLINE);
		
		/*
		 * Tasks
		 */
		int color = 0;
		String latestPrefix = "";
		for (IAbsSchedulable taskId : taskIds) {
			String name = taskId.toStringId();
			String prefix;
			if(!name.contains("VM"))
				prefix = "";
			else
				prefix =  name.substring(0, name.indexOf('_'));
			if (!prefix.equals(latestPrefix)) {
				color = (color+1) % COLORS.length;
				latestPrefix = prefix;
			}
			preamble.append("newTask " + name + " -name \""+name+"\" -color "+COLORS[color]);
			preamble.append(NEWLINE);
		}
		preamble.append(NEWLINE);
		
		/*
		 * Partitions (servers)
		 */
		for (IAbsSchedulable partitionId : partitionIds) {
			
			int bc = (int) Math.ceil(partitionId.getCapacity()+0.5);
			
			preamble.append("newServer " + partitionId.toStringId()
					+ " -name \"" + partitionId.toStringId() + "\" -capacity "
					+ bc + " -budget "
					+ bc);
			preamble.append(NEWLINE);
		}
		preamble.append(NEWLINE);
		
		/*
		 * Prepend the preamble to the collected log. 
		 */
		sb.insert(0, preamble);
		
		ps.append(sb);
		
		ps.close();
		
	}

}
