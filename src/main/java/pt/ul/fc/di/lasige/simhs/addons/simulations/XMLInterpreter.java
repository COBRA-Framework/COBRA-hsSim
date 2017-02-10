package pt.ul.fc.di.lasige.simhs.addons.simulations;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;


/**
 * Class XMLInterpreter4MPR2
 * This class parse the input xml file to the component tree and return the tree's root.
 * JDK version used: <JDK1.7>
 * @author Meng Xu
 * Create Date: 4/12/2013 
 *
 */

public class XMLInterpreter {
	
	private String inputFilename;
	private Component rootComponent;
	private Interface rootInterface;
	private Document doc;
	
	

	public XMLInterpreter(String inputFilename) {
		super();
		this.inputFilename = inputFilename;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(inputFilename));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public XMLInterpreter(File inputFile) {
		super();
		this.inputFilename = inputFile.getName();
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputFile);
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean parseFile(){
		try{
			
			Node rootNode = doc.getDocumentElement();
			if(rootNode.getNodeName().equalsIgnoreCase("system")){
				parseNode(rootNode, null);
			}else if(rootNode.getNodeName().equalsIgnoreCase("component")){
				parseNodeInterface(rootNode, null);
			}else{
				System.err.println("The root component is not <system>");
				return false;
			}		
		}  catch (FactoryConfigurationError e) {
			System.err.println("DocumentBuilderFactory Configuration error:"+e.getMessage());
			//e.printStackTrace();
			return false;
		} catch (Exception e) {
			if(e instanceof NumberFormatException) {
				System.err.println("CARTS XML tag value is not number:"+e.getMessage());
			}
			else
			{
				System.err.println("CARTS XML structure error:"+e.getMessage());
			}
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/*
	 * Function parseNode
	 * Parse each node recusively including the leaf node 
	 */
	private void parseNode(Node node, Component parentComponent){
		if(parentComponent == null){
			String schedulingPolicy = node.getAttributes().getNamedItem("os_scheduler").getNodeValue();	
			String period = "0";
			if(node.getAttributes().getNamedItem("period") == null){
				String period_min = node.getAttributes().getNamedItem("min_period").getNodeValue();
				String period_max = node.getAttributes().getNamedItem("max_period").getNodeValue();
				if(period_min.equals(period_max)){
					period = period_min;
				}else{
					System.err.println("NOT Support a range of period for a component! min_period must equal max_period");
					System.exit(1);
				}
			}else{
				period = node.getAttributes().getNamedItem("period").getNodeValue();
			}
			this.rootComponent = new Component("system", schedulingPolicy, period, null,true);
			this.rootComponent.setComponentFilename(this.inputFilename);
			
			//Now parse its child node recursively
			NodeList childNodes = node.getChildNodes();
			for(int i=0; i<childNodes.getLength(); i++){
				this.parseNode(childNodes.item(i), this.rootComponent);
			}
		}
		
		if(parentComponent != null && node.getNodeName().equals("component")){ //non-root component
			
			String name = node.getAttributes().getNamedItem("name").getNodeValue();
			String schedulingPolicy = node.getAttributes().getNamedItem("scheduler").getNodeValue();
			String period = "0";
			if(node.getAttributes().getNamedItem("period") == null){
				String period_min = node.getAttributes().getNamedItem("min_period").getNodeValue();
				String period_max = node.getAttributes().getNamedItem("max_period").getNodeValue();
				if(period_min.equals(period_max)){
					period = period_min;
				}else{
					System.err.println("NOT Support a range of period for a component! min_period must equal max_period");
					System.exit(1);
				}
			}else{
				period = node.getAttributes().getNamedItem("period").getNodeValue();
			}
			Component currentComponent = new Component(name,schedulingPolicy,period,parentComponent,false);
			currentComponent.setComponentFilename(this.inputFilename);
			
			parentComponent.addChildComponent(currentComponent);
			
			//now compute its child components recursively
			NodeList childNodes = node.getChildNodes();
			for(int i=0; i< childNodes.getLength(); i++){
				this.parseNode(childNodes.item(i), currentComponent);
			}
		}
		
		if(parentComponent != null && node.getNodeName().equals("task")){ //task
			String name = node.getAttributes().getNamedItem("name").getNodeValue();
			String period = node.getAttributes().getNamedItem("p").getNodeValue();
			String deadline = node.getAttributes().getNamedItem("d").getNodeValue();
			String exe = node.getAttributes().getNamedItem("e").getNodeValue();
			String core = node.getAttributes().getNamedItem("core").getNodeValue();
		/*	String delta_rel = "0", delta_sch = "0", delta_crpmd = "0", delta_cxs = "0";
			if(node.getAttributes().getNamedItem("delta_rel") != null)
				delta_rel= node.getAttributes().getNamedItem("delta_rel").getNodeValue();
			if(node.getAttributes().getNamedItem("delta_sch") != null) 
				delta_sch = node.getAttributes().getNamedItem("delta_sch").getNodeValue();
			if(node.getAttributes().getNamedItem("delta_crpmd") != null)
				delta_crpmd = node.getAttributes().getNamedItem("delta_crpmd").getNodeValue();
			if(node.getAttributes().getNamedItem("delta_cxs") != null)
				delta_cxs = node.getAttributes().getNamedItem("delta_cxs").getNodeValue();*/
			Task currentTask = new Task(name, period,deadline, exe,core);
			parentComponent.addTask(currentTask);
		}
	}
	private void parseNodeInterface(Node node, Interface rootInterface){
		if(rootInterface == null){
			this.rootInterface = new Interface("system",null,true);
			this.rootInterface.setComponentFilename(this.inputFilename);
			
			//Now parse its child node recursively
			NodeList childNodes = node.getChildNodes();	
			for(int i=0; i<childNodes.getLength(); i++){
				if(childNodes.item(i).getNodeName().equals("component")){
					this.parseNodeInterface(childNodes.item(i), this.rootInterface);			
				}
			}
		}
		
		if(rootInterface != null && node.getNodeName().equals("component")){ //task
			NodeList components = node.getChildNodes();
			NodeList VCPUs = null;
			Node VCPU = null;
			Interface childInterface;
			String name = node.getAttributes().getNamedItem("name").getNodeValue();
			int vcpunumber=0;
			for(int i=0; i< components.getLength(); i++){
				if(components.item(i).getNodeName().equals("processed_task")){
					VCPUs = components.item(i).getChildNodes();
					childInterface = new Interface(name, rootInterface, false);
					rootInterface.addChildComponent(childInterface);

					for(int j=0; j< VCPUs.getLength(); j++){
						if(VCPUs.item(j).getNodeName().equals("model")){
							VCPU = VCPUs.item(j);
							name = "VCPU"+vcpunumber++;
							String period = VCPU.getAttributes().getNamedItem("period").getNodeValue();
							String deadline = VCPU.getAttributes().getNamedItem("deadline").getNodeValue();
							String exe = VCPU.getAttributes().getNamedItem("execution_time").getNodeValue();
							String core = VCPU.getAttributes().getNamedItem("core").getNodeValue();
							Task currentTask = new Task(name, period,deadline, exe,core);
							childInterface.addTask(currentTask);
						}
					}
				}
			}
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public String getInputFilename() {
		return inputFilename;
	}






	public void setInputFilename(String inputFilename) {
		this.inputFilename = inputFilename;
	}






	public Component getRootComponent() {
		return rootComponent;
	}
	public Interface getRootInterface() {
		return rootInterface;
	}





	public void setRootComponent(Component rootComponent) {
		this.rootComponent = rootComponent;
	}






	public Document getDoc() {
		return doc;
	}


	public void setDoc(Document doc) {
		this.doc = doc;
	}

}
