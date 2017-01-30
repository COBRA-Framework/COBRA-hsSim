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
package pt.ul.fc.di.lasige.simhs.core.platform;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class PhysicalPlatform implements IPlatform {
	
	private final SortedSet<IProcessor> procs;
	
	public PhysicalPlatform() {
		this.procs = new TreeSet<IProcessor>();
	}
	
	public PhysicalPlatform clone() {
		PhysicalPlatform other = null;
		try {
			other =  (PhysicalPlatform) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
		return other;
	}

	@Override
	public int getNumberOfProcessors() {
		return this.procs.size();
	}

	@Override
	public Iterator<IProcessor> iterator() {
		return this.procs.iterator();
	}

	@Override
	public void bindProcessor(IProcessor proc) {
		this.procs.add(proc);
	}

	@Override
	public void unbindProcessor(IProcessor proc) {
		this.procs.remove(proc);
	}
	
	@Override
	public double getTotalCapacity() {
		double result = 0.0;
		for (IProcessor p : this.procs)
			result += p.getSpeed();
		return result;
	}

}
