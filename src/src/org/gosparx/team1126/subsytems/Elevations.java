package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.gosparx.team1126.robot.IO;
import src.org.gosparx.team1126.sensors.EncoderData;
import src.org.gosparx.team1126.util.DebuggerResult;

public class Elevations extends GenericSubsytem {

	public Elevations() {
		super("Elevations");
	}

 
	private double height; //Height of elevator
	private int top;
	private int middle;
	private int floor; 
	private WPI_TalonSRX motor1; 
	private WPI_TalonSRX motor2;
	private DigitalInput limitSwitch; //Limit switch at the bottom of winch
	private Encoder rawEnc;
	private EncoderData encoder; 
	
	private boolean isMoving = false;
	
	private boolean finishedInit = false; 
	
	enum State { //Execute decides what to do based on state
		INIT, 
		STANDBY, 
		MOVEMIDDLE,
		MOVEUP, //Go to the top
		MOVEDOWN; //Go to the bottom
	}

	
	State state;
	
	@Override
	public void init() {
		top = 95; 
		middle = 39;
		floor = 3;
		state = State.STANDBY;
		height = 0; //height is not actually 0 yet, it will be at end of init
		motor1 = new WPI_TalonSRX(IO.ELEVATIONSRIGHT); 
		motor2 = new WPI_TalonSRX(IO.ELEVATIONSLEFT);
		motor1.setNeutralMode(NeutralMode.Brake);
		motor2.setNeutralMode(NeutralMode.Brake);
		limitSwitch = new DigitalInput(IO.MAGNETICSENSOR);
		rawEnc = new Encoder(IO.ELEVATIONSENCODER1, IO.ELEVATIONSENCODER2);
		encoder = new EncoderData(rawEnc, 0.0310354993); 
	}
	
	public void putThingsOnDashboard() {
		SmartDashboard.putData("Right motor", motor1);
		SmartDashboard.putData("Left motor", motor2);
		SmartDashboard.putData("Elevator Encoder", rawEnc);
		SmartDashboard.putData("Elevator Limit Switch", limitSwitch);
	}

	@Override
	public void execute() {
		encoder.calculateSpeed();
		height = -encoder.getDistance();
		System.out.println("Encoder value "+height+" Limit "+limitSwitch.get());
		switch(state)
		{
		
			case INIT:
				setMotor(-.2);
				if(limitSwitch.get())
				{
					System.out.println("Home found");
					motor1.stopMotor();
					motor2.stopMotor();
					stopAll();
					encoder.reset();
					state = State.STANDBY;
				}
				break;
			case STANDBY: //while in standby, does nothing
				return; 
			case MOVEUP: //while in moveUp, elevator goes up to the top
				if(top<height )
				{
					state = State.STANDBY;
					stopAll();
					break;
				} 
				else 
				{	
					setMotor(1);
				}
				break;
			case MOVEMIDDLE: //while in moveMiddle goes to the middle
				if(middle<height+1 
				&& middle>height-1)
				{
					state = State.STANDBY;
					stopAll();
					break;
				} 
				else if(height>middle) //If below go down
				{
					setMotor(-.5);
					System.out.println("going down");
				}
				else //If above goes up
				{
					setMotor(1);
					System.out.println("going up");
				}
				break;
			case MOVEDOWN: //while in moveDown, elevator goes down
				if(floor>height)
				{
					state = State.STANDBY;
					stopAll();
					break;
				}
				else {
					setMotor(-.5);
				}
				break;
		}
	}

	@Override
	public DebuggerResult[] debug() {
		DebuggerResult[] result = new DebuggerResult[2]; 
		setMotor(-.2);
		while(limitSwitch.get()) //If this does not work then you have to force stop
		{
			stopAll(); 
			result[0] = new DebuggerResult("Limit switch and motors work",true,"Elevations limit switch hit");
			encoder.reset();
		}
		encoder.calculateSpeed();
		setMotor(.3);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		encoder.calculateSpeed();
		if(encoder.getDistance()>1) 
		{
			result[1] = new DebuggerResult("Encoder works",true,"Motors moved and encoder value greater than 0");
		}
		else
		{
			result[1] = new DebuggerResult("Encoder not working",false,"Encoder did not change when motors were set");
		}
		forceStandby();
		return result;
	}
	
	//Methods called to control elevator movement
	public boolean goSwitch() //Goes top
	{
		if(state!=State.INIT) //To make sure init is not messed up by inputs
		{
			isMoving = true;
			state = State.MOVEMIDDLE;
			return true;
		}else {return false;}
	}
	
	public void startInit() {
		if(!finishedInit) {
			state = State.INIT;
			finishedInit = true;
		}
	}
	
	public boolean goScale() //Exe goes middle, state = moveMiddle
	{
		if(state!=State.INIT) //To make sure init is not messed up by inputs
		{
			isMoving = true;
			state = State.MOVEUP; 
			return true;
		}else {return false;}
	}
	
	public boolean goFloor() //Exe goes bottom, state = moveDown
	{
		if(state!=State.INIT) //To make sure init is not messed up by inputs
		{
			isMoving = true;
			state = State.MOVEDOWN;
			return true;
		}else {return false;}
	}
	
	
	public boolean stopAll() //Stops all motors and state to standby 
	{
		if(state!=State.INIT) //To make sure init is not messed up by inputs
		{
			state = State.STANDBY;
			motor1.set(-.1);
			motor2.set(-.1);
			setBrake(false);
			isMoving = false;
			System.out.println("stoped");
			return true;
		}else {return false;}
	}

	@Override
	public void forceStandby() { //Use sparingly, might break init
		motor1.stopMotor();
		motor2.stopMotor();
		setBrake(false);
	}
	
	private void setMotor(double speed)
	{
		//System.out.println("Set motors with "+speed);
		if(!isMoving){
			setBrake(true);
			isMoving = true;
		}		
		setRawMotor(speed);
	}
	
	private void setRawMotor(double speed) {
		motor1.set(-speed);
		motor2.set(-speed);
	}
	
	private void setBrake(boolean power) {
		if(power){
			setRawMotor(0.4);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	@Override
	public boolean isDone() {
		if(isMoving){
			return false;
		}
		return true;
	}

	@Override
	public long sleepTime() {
		return 20;
	}
}