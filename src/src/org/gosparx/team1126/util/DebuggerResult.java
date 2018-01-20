package src.org.gosparx.team1126.util;

public class DebuggerResult {
	
	private String name;
	private boolean pass;
	private String message;
	
	public DebuggerResult(String name, boolean pass, String message) {
		
		this.name = name;
		this.pass = pass;
		this.message = message;
		
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getPass() {
		return pass;
	}
	
	public String getMessage() {
		return message;
	}

}
