package src.org.gosparx.team1126.subsytems;

import src.org.gosparx.team1126.util.DebuggerResult;

public abstract class GenericSubsytem extends Thread{

	private String name;
	public GenericSubsytem(String name){
		this.name = name;
		setPriority(Thread.NORM_PRIORITY);
	}
	
	public abstract void init();
	
	public abstract void execute();
	
	public abstract void forceStandby();
	
	public abstract boolean isDone();
	
	public abstract long sleepTime();
	
	public abstract DebuggerResult[] debug();
	
	protected void log(String message){
		print(message);
	}
	
	protected void print(String message){
		System.out.println(name + ": " + message);
	}
	
	@Override
	public void run(){
		log("Initializing " + name + "...");
		//init();
		long timeWait = sleepTime();
		log("Starting " + name + "...");
		while(true){
			execute();
			try { Thread.sleep(timeWait); } catch (InterruptedException e) {}
		}
	}
}
