package src.org.gosparx.team1126.subsytems;

import src.org.gosparx.team1126.util.DebuggerResult;

public abstract class GenericSubsytem {

	public abstract void execute();
	
	public abstract void init();
	
	public abstract void logger();
	
	public abstract DebuggerResult[] debug();
	
}
