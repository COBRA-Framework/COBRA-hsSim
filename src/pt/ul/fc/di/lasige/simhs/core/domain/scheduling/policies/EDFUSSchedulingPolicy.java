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
package pt.ul.fc.di.lasige.simhs.core.domain.scheduling.policies;

import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.Job;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.SchedulingPolicy;

/**
 * Implements the EDF-US[m/(2m-1)] policy (Srinivasan and Baruah, 2002)
 * 
 * @author jcraveiro
 * 
 */
public class EDFUSSchedulingPolicy implements SchedulingPolicy {

	/**
	 * 
	 */
	private double usBound;
	
	public EDFUSSchedulingPolicy() {
		this(1);
	}
	
	public EDFUSSchedulingPolicy(int m) {
		usBound = ((double) m) / (2 * m - 1);	
	}

	@Override
	public int compare(Job o1, Job o2) {

		if (o1.equals(o2))
			return 0;
		
		double u1 = o1.getParentTask().getUtilization();
		double u2 = o2.getParentTask().getUtilization();

		int firstResult = 0;
		boolean compareWithEDF = false;
		if (u1 > usBound) {
			if (u2 > usBound) {
				firstResult = 0;
			} else {
				firstResult = Integer.MIN_VALUE + 1;
			}
		} else {
			if (u2 > usBound) {
				firstResult = Integer.MAX_VALUE;
			} else {
				firstResult = 0;
				compareWithEDF = true;
			}
		}

		if (firstResult != 0)
			return firstResult;

		if (compareWithEDF) {
			return (new EDFSchedulingPolicy()).compare(o1, o2);
		}
		
		// break ties between heavy tasks
		
		int r2 = new Integer(o1.getReleaseTime()).compareTo(o2.getReleaseTime());

		if (r2 != 0) {
			return r2;
		}
		
		int result = o1.toString().compareTo(o2.toString());
		
		if (result == 0 && !o1.equals(o2))
			System.err.println("OopsEDFUS!");

		return result;
	}
}