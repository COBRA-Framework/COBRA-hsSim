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

import java.lang.reflect.Array;
import java.util.*;

public class VirtualPlatform implements IPlatform {
	
	private final Map<Integer,IProcessor> procs;
	private int maxProcessors;
	public VirtualPlatform(int numberOfProcs) {
		this.procs = new TreeMap<Integer,IProcessor>();

	}
	
	public VirtualPlatform clone() {
		VirtualPlatform other = null;
		try {
			other =  (VirtualPlatform) super.clone();
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
		return this.procs.values().iterator();
	}

	@Override
	public void bindProcessor(IProcessor proc, int numberOfProc) {
		this.procs.put(numberOfProc,new VirtualProcessor(proc));
		//System.out.println("PROC BINDEND "+proc.toString()+" "+numberOfProc);
	}

	@Override
	public void unbindProcessor(IProcessor proc, int numberOfProc) {
		List<Integer> toRemove = new ArrayList<>();
		for (Map.Entry<Integer,IProcessor> i : this.procs.entrySet()) {
			if (i.getValue().getParentProcessor().equals(proc)) {
				toRemove.add(i.getKey());
			}
		}

		for (Integer i : toRemove) {
			if(this.procs.containsKey(i))
				this.procs.remove(i);
		}
	}

	@Override
	public void maxProcessors(int n) {
		this.maxProcessors = n;
	}

	@Override
	public List<Integer> getActiveProcs() {
		List<Integer> procs = new ArrayList<>();
		for(int k=0;k<maxProcessors;k++)
		{
			if(this.procs.containsKey(k))
				procs.add(k);
		}
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
