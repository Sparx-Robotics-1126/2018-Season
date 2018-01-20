package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import src.org.gosparx.team1126.util.DebuggerResult;

public class Elevations extends GenericSubsytem {

	float height; //Height of elevator
	boolean init;
	int top;
	int middle;
	int floor;
	int deadBand = 1; 
	WPI_TalonSRX motor1; 
	WPI_TalonSRX motor2;
	DigitalInput limitSwitch; //Limit switch at the bottom of winch
	Encoder encoder; 
	
	enum State { //Execute decides what to do based on these states
		init,
		standBy, 
		moveMiddle,
		moveUp,
		moveDown;
	}
	
	enum Location {
		top,
		middle,
		bottom;
	}
	
	Location location;
	State state;
	
	@Override
	public void init() {
		state = State.init;
		height = 0; //height is not actually 0 yet, it will be at end of init
		motor1 = new WPI_TalonSRX(0); //TODO: get actual motor ID
		motor2 = new WPI_TalonSRX(0);
		limitSwitch = new DigitalInput(0); //TODO: get actual channel  
		encoder = new Encoder(0, 0); //TODO: find correct channels
		init = true;
	}

	@Override
	public void execute() {
		switch(state)
		{
			case init:
			{
				if(limitSwitch.get())
				{
					motor1.stopMotor();
					motor2.stopMotor();
					encoder.reset();
					init = false;
				}
				break;
			}
			case standBy: //while in standby, does nothing
			{
				break; 
			}
			case moveUp: //while in moveUp, elevator goes up
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
			case moveMiddle:
				if(middle+deadBand>height 
				&& middle-deadBand<height)
				{
					state = State.standBy;
					break;
				} 
				else if(height>middle)
				{
					motor1.set(10);
					motor2.set(10);
				}
				else
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
	public void goSwitch() //Goes top
	{
		if(!init)
		{
			state = State.moveUp;
		}
	}
	
	public void goScale() //Goes middle
	{
		if(!init)
		{
			state = State.moveMiddle;
		}
	}
	
	public void goFloor() //Goes bottom 
	{
		if(!init)
		{
			state = State.moveDown;
		}
	}
	
	
	public void stop()
	{
		if(!init)
		{
			state = State.standBy;
			motor1.stopMotor();
			motor2.stopMotor();
		}
	}
}