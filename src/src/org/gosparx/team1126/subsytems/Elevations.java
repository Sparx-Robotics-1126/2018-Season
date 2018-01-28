package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import src.org.gosparx.team1126.sensors.EncoderData;
import src.org.gosparx.team1126.util.DebuggerResult;

public class Elevations extends GenericSubsytem {

	public Elevations() {
		super("Elevations");
	}


	double height; //Height of elevator
	boolean homed; //Determines if input methods should be allowed
	int top;
	int middle;
	int floor;
	int deadBand = 1; 
	WPI_TalonSRX motor1; 
	WPI_TalonSRX motor2;
	DigitalInput limitSwitch; //Limit switch at the bottom of winch
	EncoderData encoder; 

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
		motor1 = new WPI_TalonSRX(1); //TODO: get actual motor ID
		motor2 = new WPI_TalonSRX(2);
		for(int i = 0; i>10; i++) {new WPI_TalonSRX(i);}
		limitSwitch = new DigitalInput(2); //TODO: get actual channel  
		encoder = new EncoderData(new Encoder(0, 1),0.1); //TODO: find correct channels
		homed = false;
	}

	@Override
	public void execute() {
		encoder.calculateSpeed();
		height = encoder.getDistance();
		//System.out.println("Encoder value "+height+" Limit "+limitSwitch.get());
		switch(state)
		{
			case init:
			{
				setMotor(-.2);
				if(!limitSwitch.get() && !homed)
				{
					stopAll();
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
				if(top>height )
				{
					state = State.standBy;
					break;
				} 
				else 
				{
					setMotor(.8);
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
					setMotor(.8);
				}
				else //If above goes down
				{
					setMotor(-.8);
				}
				break;
			case moveDown: //while in moveDown, elevator goes down
			{
				if(floor+deadBand>height)
				{
					state = State.standBy;
					break;
				}
				else {
					setMotor(-.8);
				}
				break;
			}
		}
		
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
	
	
	public boolean stopAll() //Stops all motors and state to standby 
	{
		if(!homed) //To make sure init is not messed up by inputs
		{
			state = State.standBy;
			motor1.stopMotor();
			motor2.stopMotor();
			System.out.println("stoped");
			return true;
		}else {return false;}
	}

	@Override
	public void forceStandby() {
		motor1.stopMotor();
		motor2.stopMotor();
	}
	
	public void setMotor(double speed)
	{
		System.out.println("Set motors with "+speed);
		motor1.set(-speed);
		motor2.set(speed);
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