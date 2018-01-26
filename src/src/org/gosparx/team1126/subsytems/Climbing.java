/**
 * 
 */
package src.org.gosparx.team1126.subsytems;

import src.org.gosparx.team1126.util.DebuggerResult;

/**
 * @author Justin1205
 *
 */
public class Climbing extends GenericSubsytem
{

	public Climbing() {
		super("Climbing");
	}

	@Override
	public void execute() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public DebuggerResult[] debug() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	public enum State
	{
	STANDBY,
	ATTACHING,
	CLIMBING,
	HELPINGHANDS;
		
		@Override
		public String toString(){
			switch(this)
			{
			case STANDBY:
				return "Climbing standby";
				
			case ATTACHING:
				return "Climber attaching";
				
			case CLIMBING:
				return "Climbing";
				
			case HELPINGHANDS:
				return "Deploying helping hands";
			}
			return null;
		}
	}
	@Override
	public void forceStandby() {
		
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public long sleepTime() {
		return 20;
	}
}
