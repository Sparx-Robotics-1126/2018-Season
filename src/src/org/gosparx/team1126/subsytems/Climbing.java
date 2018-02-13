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
	public void enableClimbing(boolean enabled) {
		climbingSoul.set (enabled);
	}
}
