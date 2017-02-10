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

import java.util.*;

public class PhysicalPlatform implements IPlatform {

	private final Map<Integer,IProcessor> procs;
	private int maxNumberofProcs;

	public PhysicalPlatform(int maxNumberOfProcs) {
		this.procs = new TreeMap<Integer,IProcessor>();
		this.maxNumberofProcs = maxNumberOfProcs;
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
	public void maxProcessors(int n) {

	}

	@Override
	public int getNumberOfProcessors() {
		return this.procs.size();
	}

	@Override
	public Iterator<IProcessor> iterator() {
		return this.procs.values().iterator();
	}

	@Override
	public void bindProcessor(IProcessor proc, int numberProc) {
		this.procs.put(numberProc,proc);
	}

	@Override
	public void unbindProcessor(IProcessor proc, int numberProc) {
		List<Integer> toRemove = new ArrayList<>();
		for (Map.Entry<Integer,IProcessor> i : this.procs.entrySet()) {
			if (i.equals(proc)) {
				toRemove.add(i.getKey());
			}
		}

		for (Integer i : toRemove) {
			if(this.procs.containsKey(i))
				this.procs.remove(i);
		}
	}
	
	@Override
	public List<Integer> getActiveProcs() {
		List<Integer> procs = new ArrayList<>();
		for(Map.Entry<Integer,IProcessor> i:this.procs.entrySet())
			procs.add(i.getKey());
		return procs;
	}

	@Override
	public double getTotalCapacity() {
		double result = 0.0;
		for (Map.Entry<Integer,IProcessor> p : this.procs.entrySet())
			result += p.getValue().getSpeed();
		return result;
	}

}
