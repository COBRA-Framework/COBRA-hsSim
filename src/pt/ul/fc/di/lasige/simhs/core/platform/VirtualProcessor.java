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
package pt.ul.fc.di.lasige.simhs.core.platform;

/**
 * @author jcraveiro
 *
 */
public class VirtualProcessor implements IProcessor {
	
	private final IProcessor parent;
	
	public VirtualProcessor(IProcessor parent) {
		this.parent = parent;
	}

	@Override
	public IProcessor getParentProcessor() {
		return this.parent;
	}

	@Override
	public IProcessor getRootProcessor() {
		return this.parent.getRootProcessor();
	}

	@Override
	public double getSpeed() {
		return this.parent.getSpeed();
	}

	@Override
	public int compareTo(IProcessor arg0) {
		final int r1 = -Double.compare(this.getSpeed(), arg0.getSpeed());
		if (r1 != 0)
			return r1;
		return this.toString().compareTo(arg0.toString());
	}

	@Override
	public String toString() {
		return this.parent.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.parent.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IProcessor))
			return false;
		final IProcessor other = (IProcessor) obj;
		return this.hashCode() == other.hashCode();
	}

}
