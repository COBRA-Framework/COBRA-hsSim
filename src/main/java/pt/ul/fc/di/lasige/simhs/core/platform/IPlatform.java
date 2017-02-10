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

import java.util.List;

public interface IPlatform extends Cloneable, Iterable<IProcessor> {

	public IPlatform clone();
	
	public int getNumberOfProcessors();
	
	public double getTotalCapacity();
	
	public void bindProcessor(IProcessor proc, int numberOfProc); //this is relative to the component, for virtual components this needs to be the number or id of the interfaceTask
	
	public void unbindProcessor(IProcessor proc, int numberOfProc); //same is above

	public void maxProcessors (int n);

	public List<Integer> getActiveProcs ();

}
