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
package pt.ul.fc.di.lasige.simhs.addons.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.IScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.Job;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.AbsPeriodicSchedulable;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.BasicComponent;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IComponent;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.Workload;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;
import pt.ul.fc.di.lasige.simhs.core.platform.VirtualPlatform;


/**
 * Component bases on the UMPR model (Craveiro and Rufino, 2012); generalizes
 * periodic resource model (Shin and Lee, 2003) and MPR model (Shin et al.,
 * 2008).
 * 
 * @author jcraveiro
 * 
 */
public class UMPRComponent extends AbsPeriodicSchedulable implements IComponent, Observer {



	private IComponent decorator;

	private List<PeriodicInterfaceTask> interfaceTasks;
	private int PPTNumber = 0;
	public UMPRComponent clone() {
		UMPRComponent other = null;

		other = (UMPRComponent) super.clone();
		other.decorator = decorator.clone();
		return other;
	}


	public UMPRComponent(String id, IComponent parent, double capacity, int period, int numberOfProcs) {
		super(id, parent, capacity, period);
		this.decorator = new BasicComponent(new VirtualPlatform(numberOfProcs));
		this.interfaceTasks = new ArrayList<PeriodicInterfaceTask>();
	}

	@Override
	public void tick() {
		decorator.tick();
	}

	@Override
	public void tickle() {
		decorator.tickle();
		consumeBudget(1.0);
	}

	@Override
	public void tickle(double exec) {
		decorator.tickle(exec);
		consumeBudget(exec);
	}

	public void addChild(IAbsSchedulable at) {
		decorator.addChild(at);
		at.addObserver(this);
	}

	public void setScheduler(IScheduler s) {
		decorator.setScheduler(s);
		s.addObserver(this);
		s.setObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		setChanged();
		notifyObservers(arg1);

	}

	@Override
	public int compareTo(IAbsSchedulable arg0) {
		return toStringId().compareTo(arg0.toStringId());
	}

	@Override
	public List<Job> launchJob(int releaseTime) {
		List<Job> result = new ArrayList<Job>();
		Job job;
		
		for (PeriodicInterfaceTask ppt : interfaceTasks) {
			List<Job> subResult = ppt.launchJob(releaseTime);
			for (Job j : subResult) {
				job = new Job((IAbsSchedulable) this, this.getNextJobSequenceNo(), j
						.getReleaseTime(), j.getRemainingCapacity(), j
						.getDeadlineTime(), j.getRemainingCapacity());
				job.setPPT(ppt);
				job.setPPTSequenceNumber(j.getSequenceNo());
				result.add(job);
				addBudget(j.getRemainingCapacity());
				this.incrementNextJobSequenceNo();
			}
		}
		return result;
	}

	public void addInterfaceTask(PeriodicInterfaceTask ppt) {
		interfaceTasks.add(ppt);
		ppt.setCoreID(this.PPTNumber++);
	}

	@Override
	public double getBudget() {
		return decorator.getBudget();
	}

	@Override
	public void addBudget(double plus) {
		decorator.addBudget(plus);
	}

	@Override
	public void consumeBudget(double exec) {
		decorator.consumeBudget(exec);
	} 


	@Override
	public void bindProcessor(IProcessor proc, int numberOfProc) {
		//int numberOfProc = 0;
		//System.err.println("Bound "+proc.toString()+" to " + this.toStringId());
		decorator.bindProcessor(proc, numberOfProc);
	}


	@Override
	public void unbindProcessor(IProcessor proc, int numberOfProc) {
		//int numberOfProc = 0;
		//System.err.println("Unbound "+proc.toString()+" from " + this.toStringId());
		decorator.unbindProcessor(proc,numberOfProc);
	}


	@Override
	public IScheduler getScheduler() {

		return decorator.getScheduler();
	}


	@Override
	public Workload getWorkload() {
		return decorator.getWorkload();
	}


	@Override
	public List<PeriodicInterfaceTask> getInterfaceTasks() {
		return interfaceTasks;
	}




}
