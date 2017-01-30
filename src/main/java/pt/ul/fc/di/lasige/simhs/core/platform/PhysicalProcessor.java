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
public class PhysicalProcessor implements IProcessor {
	
	private String id;
	
	/**
	 * 
	 */
	private static final double DEFAULT_SPEED = 1.0; 

	private double speed;
	
	public PhysicalProcessor(String id) {
		this(id, DEFAULT_SPEED);
	}
	
	public PhysicalProcessor(String id, double speed) {
		this.id = id;
		this.speed = speed;
	}
	
	@Override
	public IProcessor getParentProcessor() {
		return null;
	}

	@Override
	public IProcessor getRootProcessor() {
		return this;
	}

	@Override
	public double getSpeed() {
		return this.speed;
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
		return this.id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (null != this.id) {
			result += this.id.hashCode();
		}
		final long temp;
		temp = Double.doubleToLongBits(this.speed);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
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
