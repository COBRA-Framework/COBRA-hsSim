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
package pt.ul.fc.di.lasige.simhs.core.domain.workload;

import java.util.List;
import java.util.Observable;

import pt.ul.fc.di.lasige.simhs.addons.models.PeriodicInterfaceTask;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.IScheduler;
import pt.ul.fc.di.lasige.simhs.core.platform.IPlatform;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;

/**
 * @author jcraveiro
 *
 */
public class BasicComponent extends AbsPeriodicSchedulable  implements IComponent {
	
	/**
	 * 
	 */
	private IScheduler scheduler;
	
	/**
	 * 
	 */
	private final Workload taskset;
	
	/**
	 * 
	 */
	private double budget;
	
	private final IPlatform platform;
	
	public BasicComponent clone() {
		BasicComponent other = null;
		// try {
		other = (BasicComponent) super.clone();
		// TODO deep
		// } catch (CloneNotSupportedException e) {
		// throw new InternalError();
		// }
		return other;
	}

	/**
	 * 
	 */
	public BasicComponent(IPlatform platform) {
		this.taskset = new Workload();
		this.platform = platform; 
	}

	@Override
	public void addChild(IAbsSchedulable at) {
		this.taskset.add(at);
		//this.scheduler.setTaskSet(this.taskset);
	}

	@Override
	public void setScheduler(IScheduler s) {
		this.scheduler=s;	
		s.setTaskSet(this.taskset);
		s.setPlatform(this.platform);
	}
	
	public IScheduler getScheduler() {
		return this.scheduler;
	}

	@Override
	public void tick() {
		this.scheduler.tick();
	}

	@Override
	public void tickle() {
		this.scheduler.tickle();	
	}

	@Override
	public void tickle(double exec) {
		this.scheduler.tickle();		
	}

	@Override
	public double getBudget() {
		return this.budget;
	}

	@Override
	public void addBudget(double plus) {
		this.budget += plus;
	}

	@Override
	public void consumeBudget(double budget) {
		this.budget -= budget;
	}

	@Override
	public void bindProcessor(IProcessor proc, int numberOfProc) {
		this.scheduler.bindProcessor(proc,numberOfProc);
	}

	@Override
	public void unbindProcessor(IProcessor proc, int numberOfProc) {
		this.scheduler.unbindProcessor(proc,numberOfProc);
	}

	@Override
	public int compareTo(IAbsSchedulable arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Workload getWorkload() {
		return taskset;
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers(arg);
	}

	@Override
	public List<PeriodicInterfaceTask> getInterfaceTasks() {
		// TODO Auto-generated method stub
		return null;
	}

}
