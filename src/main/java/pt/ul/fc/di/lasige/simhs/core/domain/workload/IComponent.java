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

import pt.ul.fc.di.lasige.simhs.addons.models.PeriodicInterfaceTask;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.IScheduler;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;

/**
 * @author jcraveiro
 *
 */
public interface IComponent extends IAbsSchedulable,Cloneable,Observer {
	

	void addChild(IAbsSchedulable at);
	
	void setScheduler(IScheduler s);
	
	IScheduler getScheduler();
	
	void tick();
	
	void tickle();
	
	void tickle(double exec);
	
	double getBudget();
	
	void addBudget(double plus);
	
	void consumeBudget(double exec);

	void bindProcessor(IProcessor proc);

	void unbindProcessor(IProcessor proc);
	
	IComponent clone();
	
	Workload getWorkload();
	
	List<PeriodicInterfaceTask> getInterfaceTasks();

}
