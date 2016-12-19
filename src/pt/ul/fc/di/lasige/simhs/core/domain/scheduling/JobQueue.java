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
package pt.ul.fc.di.lasige.simhs.core.domain.scheduling;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IComponent;

/**
 * 
 */

/**
 * @author jcraveiro
 *
 */
public class JobQueue implements Iterable<Job> {

	private SortedSet<Job> queue;
	
	private final SchedulingPolicy policy;
	
	public JobQueue(SchedulingPolicy policy) {
		this.policy = policy;
		this.queue = new TreeSet<Job>(policy);
	}

	public Job first() {
		return this.queue.first();
	}
	
	protected boolean add(Job j){
		boolean t = this.queue.add(j);
		
		return t;
	}
	protected boolean remove(Job j){
		return this.queue.remove(j);
	}

	public int size(){
		return this.queue.size();
	}

	public Iterator<Job> iterator(){
		return this.queue.iterator();
	}
	public boolean contains(Job j) {
		return this.queue.contains(j);	
	}
	
	public void refresh() {	
		setPolicy(this.policy);
	}
	public void setPolicy(SchedulingPolicy schedulingPolicy) {
		final SortedSet<Job> newQueue = new TreeSet<Job>(schedulingPolicy);
		newQueue.addAll(this.queue);
		this.queue = newQueue;
		/*System.out.println("BEGIN");
		for(Job j:queue)
			System.out.println(j.toString());
		System.out.println("EINDE");*/
	}
}
