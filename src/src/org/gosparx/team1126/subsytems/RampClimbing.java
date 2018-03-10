package src.org.gosparx.team1126.subsytems;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import src.org.gosparx.team1126.robot.IO;
import src.org.gosparx.team1126.util.DebuggerResult;

public class RampClimbing extends GenericSubsytem
{
	private Solenoid soulEqualsVoid;
	State newPork;
	WPI_TalonSRX wheel1;
	WPI_TalonSRX wheel2;
	DigitalInput limitSwitchThatTellsUsWhenToStopAndWhenToGoAndIsRedAndShinyAndClicky;
	DigitalInput limitSwitchThatTellsUsWhenToStopAndWhenToGoAndIsRedAndShinyAndClicky2;
	LeftOrRight deSide;
	
	
	public RampClimbing() {
		super("Ramp Climbing");
	}

	public enum State {
		STANDBY,
		GOINOUT,
		GOININ;
	} 
	
	public enum LeftOrRight {
		Left,
		Right,
		Both;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		soulEqualsVoid = new Solenoid(IO.CLIMBINGARMS);
		newPork = State.STANDBY;
		wheel1 = new WPI_TalonSRX(0);
		wheel2 = new WPI_TalonSRX(0);
		limitSwitchThatTellsUsWhenToStopAndWhenToGoAndIsRedAndShinyAndClicky = new DigitalInput(0);
		deSide = LeftOrRight.Both; 
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		switch(newPork) {
			case STANDBY:
				break;
			case GOINOUT:
				soulEqualsVoid.set(false);
				break;
			case GOININ:
				if(deSide==LeftOrRight.Both) {
					if(!limitSwitchThatTellsUsWhenToStopAndWhenToGoAndIsRedAndShinyAndClicky.get()) {
						wheel1.set(.4);
					}
					else {
						wheel1.set(0);
					}
					if(!limitSwitchThatTellsUsWhenToStopAndWhenToGoAndIsRedAndShinyAndClicky2.get()) {
						wheel2.set(.4);
					}
					else {
						wheel2.set(0);
					}
				}
				else if(deSide==LeftOrRight.Left) {
					if(!limitSwitchThatTellsUsWhenToStopAndWhenToGoAndIsRedAndShinyAndClicky.get()) {
						wheel1.set(.4);
					}
					else {
						wheel1.set(0);
					}
				}
				else {
					if(!limitSwitchThatTellsUsWhenToStopAndWhenToGoAndIsRedAndShinyAndClicky2.get()) {
						wheel2.set(.4);
					}
					else {
						wheel2.set(0);
					}
				}
		}
	}

	public void state (State newState) {
		newPork = newState;
	}
	
	@Override
	public void forceStandby() {
		// TODO Auto-generated method stub
		newPork = State.STANDBY;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long sleepTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DebuggerResult[] debug() {
		newPork = State.GOINOUT;
		boolean isLeftWorking  = !limitSwitchThatTellsUsWhenToStopAndWhenToGoAndIsRedAndShinyAndClicky.get();
		boolean isRightWorking = !limitSwitchThatTellsUsWhenToStopAndWhenToGoAndIsRedAndShinyAndClicky2.get();
		DebuggerResult[] result = new DebuggerResult[2];
		if( isLeftWorking && isRightWorking) {
			result[0] = new DebuggerResult("Limit switch",true,"Limit switch works");
		}
		else {
			result[0] = new DebuggerResult("Limit switch",false,"Limit switch does not work");
			return result;
		}
		
		execute();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newPork = State.GOININ;
		while(true) {
			execute();
			if(newPork == State.STANDBY) {
				break;
			}
		}
		result[1] = new DebuggerResult("Going in",true, "Going in works");
		return result;
	}
}
