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
import java.util.Observer;

import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.Job;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;

public interface IAbsSchedulable extends Comparable<IAbsSchedulable>,Cloneable {

	public IComponent getParent();

	public void tick();

	public void tickle();

	public void tickle(double exec);

	public String toStringId();

	/**
	 * 
	 * @param releaseTime
	 * @return null if no job is to be launched at this time.
	 */
	public List<Job> launchJob(int releaseTime);

	public int hashCode();

	public boolean equals(Object obj);

	public void addObserver(Observer o);
	
	public double getCapacity();
	
	public double getUtilization();
	
	public IAbsSchedulable clone();

	public void bindProcessor(IProcessor proc);

	public void unbindProcessor(IProcessor proc);

}