package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import src.org.gosparx.team1126.util.DebuggerResult;

public class Elevations extends GenericSubsytem {

	float height; //Height of elevator
	boolean homed; //Determines if input methods should be allowed
	int top;
	int middle;
	int floor;
	int deadBand = 1; 
	WPI_TalonSRX motor1; 
	WPI_TalonSRX motor2;
	DigitalInput limitSwitch; //Limit switch at the bottom of winch
	Encoder encoder; 

	enum State { //Execute decides what to do based on state
		init,
		standBy, 
		moveMiddle,
		moveUp,
		moveDown;
	}

	
	State state;
	
	@Override
	public void init() {
//		top = 100; //TODO: Change these 
		middle = 50;
		floor = 0;
		state = State.init;
		height = 0; //height is not actually 0 yet, it will be at end of init
		motor1 = new WPI_TalonSRX(0); //TODO: get actual motor ID
		motor2 = new WPI_TalonSRX(0);
		limitSwitch = new DigitalInput(2); //TODO: get actual channel  
		encoder = new Encoder(0, 1); //TODO: find correct channels
		homed = false;
	}

	@Override
	public void execute() {
		height = encoder.get();
		System.out.println("Encoder value "+height+"Limit "+limitSwitch.get());
		switch(state)
		{
			case init:
			{
				if(!limitSwitch.get() && !homed)
				{
					motor1.stopMotor();
					motor2.stopMotor();
					//encoder.reset();
					homed = true;
					state = State.standBy;
				}
				break;
			}
			case standBy: //while in standby, does nothing
			{
				break; 
			}
			case moveUp: //while in moveUp, elevator goes up to the top
			{
				if(top+deadBand>height 
				&& top-deadBand<height)
				{
					state = State.standBy;
					break;
				} 
				else 
				{
					motor1.set(10);
					motor2.set(10);
				}
				break;
			}
			case moveMiddle: //while in moveMiddle goes to the middle
				if(middle+deadBand>height 
				&& middle-deadBand<height)
				{
					state = State.standBy;
					break;
				} 
				else if(height>middle) //If below go up
				{
					motor1.set(10);
					motor2.set(10);
				}
				else //If above goes down
				{
					motor1.set(-10);
					motor2.set(-10);
				}
				break;
			case moveDown: //while in moveDown, elevator goes down
			{
				if(floor+deadBand>height 
				&& floor-deadBand<height)
				{
					state = State.standBy;
					break;
				}
				else {
					motor1.set(-10);
					motor2.set(-10);
				}
				break;
			}
		}
		
	}


	@Override
	public void logger() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DebuggerResult[] debug() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Methods called to control elevator movement
	public boolean goSwitch() //Goes top
	{
		if(!homed) //To make sure init is not messed up by inputs
		{
			state = State.moveUp;
			return true;
		}else {return false;}
	}
	
	public boolean goScale() //Exe goes middle, state = moveMiddle
	{
		if(!homed) //To make sure init is not messed up by inputs
		{
			state = State.moveMiddle; 
			return true;
		}else {return false;}
	}
	
	public boolean goFloor() //Exe goes bottom, state = moveDown
	{
		if(!homed) //To make sure init is not messed up by inputs
		{
			state = State.moveDown;
			return true;
		}else {return false;}
	}
	
	
	public boolean stop() //Stops all motors and state to standby 
	{
		if(!homed) //To make sure init is not messed up by inputs
		{
			state = State.standBy;
			motor1.stopMotor();
			motor2.stopMotor();
			return true;
		}else {return false;}
	}
}