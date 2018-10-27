package src.org.gosparx.team1126.subsytems;

import src.org.gosparx.team1126.util.DebuggerResult;
import src.org.gosparx.team1126.util.Logger;
import src.org.gosparx.team1126.util.Logger.Tag;

public abstract class GenericSubsystem extends Thread{

	private static Logger logger;
	
	public GenericSubsystem(String name){
		setName(name);
		setPriority(Thread.NORM_PRIORITY);
	}
	
	public abstract void init();
	
	public abstract void execute();
	
	public abstract void forceStandby();
	
	public abstract boolean isDone();
	
	public abstract void toTele();
	
	public abstract long sleepTime();
	
	public abstract DebuggerResult[] debug();
	
	//protected void log(String message){
	//	print(message);
	//}
	
	protected void log(String message) {
		Logger.getInstance().log(this, message);
	}
	
	protected void log(String method, String message) {
		Logger.getInstance().log(this, method, message);
	}

	protected void error(String message) {
		Logger.getInstance().log(this, Tag.ERROR, message);
	}
	
	protected void error (String method, String message) {
		Logger.getInstance().log(this, method, Tag.ERROR, message);
	}
	
	protected void print(String message){
		System.out.println(getName() + ": " + message);
	}
	
	@Override
	public void run(){
		log("Initializing " + getName() + "...");
		//init();
		long timeWait = sleepTime();
		log("Starting " + getName() + "...");
		while(true){
			execute();
			try { Thread.sleep(timeWait); } catch (InterruptedException e) {}
		}
	}
}
