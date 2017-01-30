/**
 * 
 */
package pt.ul.fc.di.lasige.simhs.core.domain.scheduling.schedulers;

import java.util.Observable;
import java.util.Observer;

import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.AbsScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.IScheduler;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.Job;
import pt.ul.fc.di.lasige.simhs.core.domain.scheduling.SchedulingPolicy;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.IAbsSchedulable;
import pt.ul.fc.di.lasige.simhs.core.domain.workload.Workload;
import pt.ul.fc.di.lasige.simhs.core.platform.IPlatform;
import pt.ul.fc.di.lasige.simhs.core.platform.IProcessor;
import pt.ul.fc.di.lasige.simhs.core.simulation.ILogger;

/**
 * @author jcraveiro
 *
 */
public class TSPScheduler extends AbsScheduler {
	
	private static class SchedulingWindow {
		private int offset;
		private int duration;
		private IAbsSchedulable task;
		private IProcessor processor;
	}
	
	
	/**
	 * Idea: implement as a partitioned FCFS scheduler.
	 */
	
	private int majorTimeFrame;

	@Override
	protected SchedulingPolicy getPolicy() {
		return new SchedulingPolicy() {
			
			@Override
			public int compare(Job arg0, Job arg1) {
				int c1 = new Integer(arg0.getReleaseTime()).compareTo(arg1.getReleaseTime());
				if (c1 != 0)
					return c1;
				return arg0.toString().compareTo(arg1.toString());
			}
		};
	}

	@Override
	public void tickle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeFromReadyQueue(Job j) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isCurrent(Job j) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void removeFromCurrent(Job j) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void launchJobs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void preemptJobInProc(IProcessor proc) {
		// TODO Auto-generated method stub
		
	}

}
