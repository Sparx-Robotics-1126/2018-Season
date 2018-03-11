/**
 * 
 */
package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.gosparx.team1126.robot.IO;
import src.org.gosparx.team1126.util.DebuggerResult;
/**
 * @author Justin1205
 * @author No one else...
 */
public class Climbing extends GenericSubsytem
{
	private Solenoid climbingSoul;
	private WPI_TalonSRX latchServoLeft;
	private WPI_TalonSRX latchServoRight;
	private Solenoid climbingArms;
	private Solenoid climbingLatch;
	
	public Climbing() {
		super("Climbing");
	}

	@Override
	public void execute() {}

	@Override
	public void init() 
	{
		climbingSoul = new Solenoid(IO.PTO_ELE);
		climbingArms = new Solenoid(IO.CLIMBINGARMS);
		climbingLatch = new Solenoid(IO.CLIMBINGLATCH);
		latchServoRight = new WPI_TalonSRX(0);
		latchServoLeft = new WPI_TalonSRX(10);
		
		latchServoRight.set(ControlMode.Position,0);
		latchServoLeft.set(ControlMode.Position,0);
		//SmartDashboard.putData("ClimbPTO", climbingSoul);
	}

	@Override
	public DebuggerResult[] debug() 
	{
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
	
	public void climbingLatch(boolean bool) {
		climbingLatch.set(bool);
	}
	
	public void climbingArms(boolean bool) {
		climbingArms.set(bool);
	}
	
	public boolean getClimbingLatch() {
		return climbingLatch.get();
	}
	
	public boolean getClimbingArms() {
		return climbingArms.get();
	}
	
	public void latch() 
	{
		latchServoRight.set(ControlMode.Position,90);
		latchServoLeft.set(ControlMode.Position,90);
	}
}
