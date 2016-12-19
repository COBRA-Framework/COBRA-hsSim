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
package pt.ul.fc.di.lasige.simhs.core.domain.scheduling.taxonomy;

import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.IScheduler;

/**
 * This interface belongs to a set of interfaces serving only the purpose of
 * fitting schedulers into a priority- and migration-based taxonomy.
 * Implementing this interface does not add to the methods that the class must
 * implement, but rather allows using instanceof to verify properties of the
 * scheduler (e.g., <code>scheduler instanceof IFTPScheduler</code> allows
 * checking if a <code>scheduler</code> is a FTP (fixed task priority, RM-like)
 * scheduler, if such knowledge is needed for schedulability analysis purposes.
 * 
 * @author jcraveiro
 * 
 */
public interface IFJPScheduler extends IScheduler {

}
