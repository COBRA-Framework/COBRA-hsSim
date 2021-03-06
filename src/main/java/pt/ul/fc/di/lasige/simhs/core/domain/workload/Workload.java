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
package pt.ul.fc.di.lasige.simhs.core.domain.workload;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author jcraveiro
 *
 */
public class Workload  implements Iterable<IAbsSchedulable> {

	private Set<IAbsSchedulable> taskset;

	public Workload() {
		this.taskset = new TreeSet<IAbsSchedulable>();
	}


	public void add(IAbsSchedulable abs){
		this.taskset.add(abs);
	}

	public void addTaskSet(Set<IAbsSchedulable> abs){
		this.taskset=abs;
	}


	public Iterator<IAbsSchedulable> iterator() {
		return this.taskset.iterator();
	}

	public int size(){
		return this.taskset.size();
	}



}
