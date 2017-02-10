package pt.ul.fc.di.lasige.simhs.addons.simulations;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		long a=0,b=0;
		String error;
		if(args.length < 4)
		{
			error="----Missing arguments-----\n\n";
			error+="This java program must have 4 arguments as input:\n";
			error+="\t1) The input taskset for each VM\n";
			error+="\t2) The interfaces of each VM\n";
			error+="\t3) The number physical processors (speed 1)\n";
			error+="\t4) The simulation time in milliseconds\n\n";
			
			error+="For example: \"java -jar hssim.jar TaskSet_390.0_v0.xml interfaces.xml 4 30000\"";
			
			System.out.println(error);
			System.exit(1);
		}
		
		File f = new File(args[0]);
		if(!f.exists())
		{
			System.out.println("TaskSet file not found. Exiting Simulation");
			System.exit(1);
		}
		
		f = new File(args[1]);
		if(!f.exists())
		{
			System.out.println("Interfaces file not found. Exiting Simulation");
			System.exit(1);
		}
		
		try{
	        Integer.parseInt(args[2]);
	    }catch(NumberFormatException e){
	    	System.out.println("Argument 3 (physical processors) not in the right format");
			System.exit(1);
	    }
		
		try{
	        Long.parseLong(args[3]);
	    }catch(NumberFormatException e){
	    	System.out.println("Argument 4 (Simulation time) not in the right format");
			System.exit(1);
	    }
		
		try{
			a = System.currentTimeMillis();
			MPRSimulator MPRS = new MPRSimulator(args[0],args[1],Integer.parseInt(args[2]),Long.parseLong(args[3]));
			//UMPRSimulator MPRS = new UMPRSimulator(10000);
			System.out.println("\nSimulation started");
			MPRS.run();
			b = System.currentTimeMillis();
			System.out.println("Simulation finished");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("Simulation execution time = "+(b-a)+" ms");
	}
	public static void printError()
	{
		String error;
		
		error="----Missing/Wrong arguments-----\n\n";
		error+="This java program must have 4 arguments as input:\n";
		error+="\t1) The input taskset for each VM\n";
		error+="\t2) The interfaces of each VM\n";
		error+="\t3) The number physical processors (speed 1)\n";
		error+="\t4) The simulation time in milliseconds\n\n";
		
		error+="For example: \"java -jar hssim.jar TaskSet_390.0_v0.xml interfaces.xml 4 30000\"";
		
		System.out.println(error);
		System.exit(1);		
	}

}
