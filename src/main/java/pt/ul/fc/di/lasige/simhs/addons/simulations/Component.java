package pt.ul.fc.di.lasige.simhs.addons.simulations;

import java.util.ArrayList;
import java.util.List;


public class Component {
	
	private String componentFilename;
	private String componentName;
	private String schedulingPolicy;
	private List<Task> taskset;
	
	private List<Component> childComponents;
	private Component parentComponent;
	private boolean isRoot;

	/*private double getTasksetUtil(){
		double tasksetUtil = 0;
		List<Task> workload = this.taskset;

		for(int i=0; i<workload.size(); i++){
			tasksetUtil += workload.get(i).getExe() / workload.get(i).getPeriod();
		}
		return tasksetUtil;
	}*/
	
	
	
	/////////////////////////////////
	public void addChildComponent(Component childComponent){
		this.childComponents.add(childComponent);
	}
	
	public void addTask(Task task){
		this.taskset.add(task);
	}
	
	//////////////////Construct function///////////////////////////////
	public Component(String componentName, String schedulingPolicy, String interfacePeriod, Component parentComponent, boolean isRoot){
		this.childComponents = new ArrayList<Component>();
		
		this.taskset = new ArrayList<Task>();
		this.componentName = componentName;
		this.schedulingPolicy = schedulingPolicy;
		//System.out.println("period="+interfacePeriod+"!");
		this.parentComponent = parentComponent;
		this.isRoot = isRoot;
	
	}

	/**
	 * Function toString
	 * Return the component's property, including the name of the parent component and child components.	
	 */
	public String toString(){
		String str = "==================================\r\n";
		str += "componentName:"+ this.componentName + " schedulingPolicy:" + this.schedulingPolicy +
				" isRoot: " + this.isRoot + "\r\n";
		if(this.parentComponent != null)
			str += "parentComponentName: " + this.parentComponent.getComponentName() + "\r\n";
		str += "----Child Components' Name----------------\r\n";
		for(int i=0; i<this.childComponents.size();i++){
			str += "Child " + i + " Name: " + this.childComponents.get(i).getComponentName() + "\r\n";
		}
		str += "------Taskset-------------\r\n";
		for(int i=0;i<this.taskset.size();i++){
			str += this.taskset.get(i).toString();
		}
		str += "------Interface-------------\r\n";
		str += "=============================\r\n";
		
		return str;
	}
	
	
	//////////Below are get set function for properties//////////////////////////
	public List<Task> getTaskset() {
		return taskset;
	}
	public void setTaskset(List<Task> taskset) {
		this.taskset = taskset;
	}
	public String getSchedulingPolicy() {
		return schedulingPolicy;
	}
	public void setSchedulingPolicy(String schedulingPolicy) {
		this.schedulingPolicy = schedulingPolicy;
	}
	
	public List<Component> getChildComponents() {
		return childComponents;
	}
	public void setChildComponents(List<Component> childComponents) {
		this.childComponents = childComponents;
	}

	public Component getParentComponent() {
		return parentComponent;
	}

	public void setParentComponent(Component parentComponent) {
		this.parentComponent = parentComponent;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	
	public String getComponentFilename() {
		return componentFilename;
	}

	public void setComponentFilename(String componentFilename) {
		this.componentFilename = componentFilename;
	}
}
