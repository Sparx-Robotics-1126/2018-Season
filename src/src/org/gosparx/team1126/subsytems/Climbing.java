/**
 * 
 */
package src.org.gosparx.team1126.subsytems;

import edu.wpi.first.wpilibj.Solenoid;
import src.org.gosparx.team1126.robot.IO;
import src.org.gosparx.team1126.util.DebuggerResult;
/**
 * @author Justin1205
 *
 */
public class Climbing extends GenericSubsytem
{
	Solenoid climbingSoul;
	
	public State climbingState;
	
	public Climbing() {
		super("Climbing");
	}

	@Override
	public void execute() 
	{
		// TODO Auto-generated method stub
		switch (climbingState)
		{
		case active:
			climbingSoul.set(true);
			break;
		case inactive:
			climbingSoul.set(false);
			break;
		
		default:
		    System.out.println("You done messed up ~Ghandi");
			break;
		
		}
	}

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		climbingSoul = new Solenoid(IO.ptoSwitch);
	}

	@Override
	public DebuggerResult[] debug() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	public enum State
	{
	active,
	inactive;
		
		@Override
		public String toString(){
			switch(this)
			{
			case active:
				return "Climbing active";
			case inactive:
				return "Climbing deactivated";
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
