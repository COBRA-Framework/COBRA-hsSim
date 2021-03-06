package pt.ul.fc.di.lasige.simhs.addons.simulations;

/**
 * Overhead aware task.
 * It's a POJO
 * 
 * JDK version used: <JDK1.7>
 * @author Meng Xu
 * Create Date: 4/4/2013 
 *
 */
public class Task {
	private double period;
	private double exe;
	private double deadline;
	private int core;
	private String name;
	
	
	public Task(String name, String period, String deadline, String exe, String core){
		
		this.name = name;
		this.period = Double.parseDouble(period);
		this.deadline = Double.parseDouble(deadline);
		this.exe = Double.parseDouble(exe);
		this.core = Integer.parseInt(core);
		if(this.exe > this.deadline){
			System.err.println("\r\nATTENTION: task's execution > deadline!\r\n");
		}
	}
	
	
	public Task() {
		
	}


	/*
	 * private double period;
	private double exe;
	private double deadline;
	private double delta_rel;	// release overhead
	private double delta_sch;	// schedule overhead
	private double delta_crpmd;	// cache related preemption/migration overhead
	private double delta_cxs;	// context swtich overhead
	private String name;(non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String str = "--task--";
		str += "name:" + this.name + ", period:" + this.period + ", exe:" + this.exe + 
				", deadline:" + this.deadline+", core:"+this.core;
		return str;
	}
	
	public double getWorkload(double t){
		double CI_t = Math.min(exe, Math.max(0, t - Math.floor((t+(period - deadline))/period)*period));
		return Math.floor((t+(period-deadline))/period)*exe + CI_t;
		
	}
	//////////Get Set function////////////////

	public double getPeriod() {
		return period;
	}
	public void setPeriod(double period) {
		this.period = period;
	}
	public double getExe() {
		return exe;
	}
	public void setExe(double exe) {
		this.exe = exe;
	}
	public double getDeadline() {
		return deadline;
	}
	public void setDeadline(double deadline) {
		this.deadline = deadline;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getCI(double t){
		return Math.min(exe, Math.max(0, t - Math.floor((t+(period - deadline))/period)*period));
	}
	public int getCore() {		return core;	}
	public void setCore(int core) {		this.core = core;	}





}
