package pt.ul.fc.di.lasige.simhs.addons.simulations;

import java.util.ArrayList;
import java.util.List;


public class Interface {
	
	private String componentFilename;
	private String interfaceName;
	private List<Task> VCPUset;
	
	private List<Interface> childComponents;
	private Interface parentComponent;
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
	public void addChildComponent(Interface childComponent){
		this.childComponents.add(childComponent);
	}
	
	public void addTask(Task task){
		this.VCPUset.add(task);
	}
	
	//////////////////Construct function///////////////////////////////
	public Interface(String componentName, Interface parentComponent, boolean isRoot){
		this.childComponents = new ArrayList<Interface>();
		
		this.VCPUset = new ArrayList<Task>();
		this.interfaceName = componentName;
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
		str += "InterfaceName:"+ this.interfaceName +
				" isRoot: " + this.isRoot + "\r\n";
	
		str += "------Taskset-------------\r\n";
		for(int i=0;i<this.VCPUset.size();i++){
			str += this.VCPUset.get(i).toString();
		}
		str += "------Interface-------------\r\n";
		str += "=============================\r\n";
		
		return str;
	}
	
	
	//////////Below are get set function for properties//////////////////////////
	public List<Task> getTaskset() {
		return VCPUset;
	}
	public void setTaskset(List<Task> taskset) {
		this.VCPUset = taskset;
	}
	public List<Interface> getChildComponents() {
		return childComponents;
	}
	public void setChildComponents(List<Interface> childComponents) {
		this.childComponents = childComponents;
	}

	public Interface getParentComponent() {
		return parentComponent;
	}

	public void setParentComponent(Interface parentComponent) {
		this.parentComponent = parentComponent;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String componentName) {
		this.interfaceName = componentName;
	}

	
	public String getComponentFilename() {
		return componentFilename;
	}

	public void setComponentFilename(String componentFilename) {
		this.componentFilename = componentFilename;
	}
}
