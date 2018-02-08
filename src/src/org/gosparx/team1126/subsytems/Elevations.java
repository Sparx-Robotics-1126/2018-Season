package src.org.gosparx.team1126.subsytems;

import java.sql.Time;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import src.org.gosparx.team1126.sensors.EncoderData;
import src.org.gosparx.team1126.util.DebuggerResult;

public class Elevations extends GenericSubsytem {

	public Elevations() {
		super("Elevations");
	}


	double height; //Height of elevator
	int top;
	int middle;
	int floor; 
	WPI_TalonSRX motor1; 
	WPI_TalonSRX motor2;
	Solenoid breaker;
	DigitalInput limitSwitch; //Limit switch at the bottom of winch
	EncoderData encoder; 
	
	enum State { //Execute decides what to do based on state
		init, 
		standBy, 
		moveMiddle,
		moveUp, //Go to the top
		moveDown; //Go to the bottom
	}

	
	State state;
	
	@Override
	public void init() {
		top = 300; //TODO: Change these 
		middle = 50;
		floor = 0;
		state = State.init;
		height = 0; //height is not actually 0 yet, it will be at end of init
		motor1 = new WPI_TalonSRX(6); //TODO: get actual motor ID
		motor2 = new WPI_TalonSRX(9);
		breaker = new Solenoid(4);
		setSolenoid(true);
		limitSwitch = new DigitalInput(20); //TODO: get actual channel  
		encoder = new EncoderData(new Encoder(23, 22),0.0310354993); //TODO: find correct channels
	}

	@Override
	public void execute() {
		encoder.calculateSpeed();
		height = encoder.getDistance();
		System.out.println("Encoder value "+height+" Limit "+limitSwitch.get());
		switch(state)
		{
			case init:
			{
				setMotor(-.2);
				if(limitSwitch.get())
				{
					motor1.stopMotor();
					motor2.stopMotor();
					stopAll();
					encoder.reset();
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
				if(top<height )
				{
					state = State.standBy;
					stopAll();
					break;
				} 
				else 
				{
					setMotor(.2);
				}
				break;
			}
			case moveMiddle: //while in moveMiddle goes to the middle
				if(middle<height+10 
				&& middle>height-10)
				{
					state = State.standBy;
					stopAll();
					break;
				} 
				else if(height>middle) //If below go down
				{
					setMotor(-.2);
					System.out.println("going down");
				}
				else //If above goes up
				{
					setMotor(.2);
					System.out.println("going up");
				}
				break;
			case moveDown: //while in moveDown, elevator goes down
			{
				if(floor>height)
				{
					state = State.standBy;
					stopAll();
					break;
				}
				else {
					setMotor(-.2);
				}
				break;
			}
		}
		
	}

	@Override
	public DebuggerResult[] debug() {
		DebuggerResult[] result = new DebuggerResult[2]; 
		setMotor(-.1);
		while(!limitSwitch.get()) //If this does not work then you have to force stop
		{
			stopAll(); 
			result[0] = new DebuggerResult("Limit switch and motors work",true,"Elevations limit switch hit");;
			encoder.reset();
		}
		setMotor(.3);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		if(encoder.getDistance()>1) 
		{
			result[1] = new DebuggerResult("Encoder works",true,"Motors moved and encoder value greater than 0");
		}
		else
		{
			new DebuggerResult("Encoder not working",false,"Encoder did not change when motors were set");
		}
		return result;
	}
	
	//Methods called to control elevator movement
	public boolean goSwitch() //Goes top
	{
		if(state!=State.init) //To make sure init is not messed up by inputs
		{
			state = State.moveUp;
			return true;
		}else {return false;}
	}
	
	public boolean goScale() //Exe goes middle, state = moveMiddle
	{
		if(state!=State.init) //To make sure init is not messed up by inputs
		{
			state = State.moveMiddle; 
			return true;
		}else {return false;}
	}
	
	public boolean goFloor() //Exe goes bottom, state = moveDown
	{
		if(state!=State.init) //To make sure init is not messed up by inputs
		{
			state = State.moveDown;
			return true;
		}else {return false;}
	}
	
	
	public boolean stopAll() //Stops all motors and state to standby 
	{
		if(state!=State.init) //To make sure init is not messed up by inputs
		{
			state = State.standBy;
			motor1.stopMotor();
			motor2.stopMotor();
			setSolenoid(false);
			System.out.println("stoped");
			return true;
		}else {return false;}
	}

	@Override
	public void forceStandby() { //Use sparingly, might break init
		motor1.stopMotor();
		motor2.stopMotor();
		setSolenoid(false);
	}
	
	private void setMotor(double speed)
	{
		System.out.println("Set motors with "+speed);
		motor1.set(speed);
		motor2.set(-speed);
	}
	
	private void setSolenoid(boolean power) {
		breaker.set(power);
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