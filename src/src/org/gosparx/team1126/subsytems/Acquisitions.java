package src.org.gosparx.team1126.subsytems;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.gosparx.team1126.robot.IO;
import src.org.gosparx.team1126.sensors.AnalogSensor;
import src.org.gosparx.team1126.util.DebuggerResult;
import src.org.gosparx.team1126.util.Logger;
import src.org.gosparx.team1126.util.Logger.Tag;


/**
 * This class allows for the control of the acquisitions subsystem.
 * @author Aidan Cheeseman (cheesemanaidan@gmail.com)
 * @author Andrew Thompson (an
 * 
 * drewt015@gmail.com)
 */
public class Acquisitions extends GenericSubsystem{

	//Objects
	private WPI_TalonSRX leftIntake;
	
	private WPI_TalonSRX rightIntake;
	
	private Solenoid pincher;
	
	private Solenoid wrist;
	
	private AnalogSensor cubeSensor;
	
	
	//Constants
	private static final boolean PINCHED = false;
	
	private static final boolean RELEASED = !PINCHED;
	
	private static final boolean RAISED = false;
	
	private static final boolean LOWERED = !RAISED;
	
	private static final double MOTOR_ACQ = 1; //originally 0.9 
	
	private static final double MOTOR_LAUNCH = .75;
	
	private static final double MOTOR_STOP = 0.0;
	
	private static final double MOTOR_LOW = .5;
	
	private static final double CUBE_SENSOR_THRESHOLD = 2.4;
	

	
	
	//Variables
	private State AcqState;
	
	private double rightMotorPower;
	
	private double leftMotorPower;
	
	private double pinchTime;
		
	private double regScoreTime;
	
	private double sensorTime;

	private boolean pinchPosition;  //true = pinched 
	
	private boolean wristPosition;  //true = lowered
	
		
	
	public Acquisitions() {
		super("Acquisitions");
	}
	
	
	public enum State{
		STANDBY,
		RAISE,
		ACQUIRE,
		SCORE,
		HOME,
		GOT_CUBE,
		SPIT,
		SLOW_SPIT,
		WAIT_FOR_CUBE,
		WRIST_LOWER;
	}
	
	
	@Override
	public void init() {
		AcqState = State.STANDBY;
		rightMotorPower = MOTOR_STOP;
		leftMotorPower = MOTOR_STOP;
		pinchPosition = PINCHED;
		wristPosition = RAISED;
		cubeSensor = new AnalogSensor(IO.ACQ_CUBE_SENSOR, CUBE_SENSOR_THRESHOLD);
		leftIntake = new WPI_TalonSRX(IO.CAN_ACQ_LEFT_INTAKE);
		rightIntake = new WPI_TalonSRX(IO.CAN_ACQ_RIGHT_INTAKE);
		wrist = new Solenoid(IO.PNU_WRIST);
		pincher = new Solenoid(IO.PNU_PINCHER);
		wrist.set(wristPosition);
		pincher.set(pinchPosition);
		log("init", "Successfully INITIALIZED");
		SmartDashboard.putBoolean("Arms", pinchPosition);
	}
	
	/**
	 * The actual code of the robot. Constantly looping.
	 */
	@Override
	public void execute() {
		SmartDashboard.putBoolean("Arms", pinchPosition);
		switch(AcqState){
		
		case STANDBY:
			return;
		case ACQUIRE:
			lower();
			release();
			rollerAcq();
			setWaitForCube();
			break;
		case RAISE:
			pinch();
			rollerAcq();
			if (Timer.getFPGATimestamp() > pinchTime + .5){ //.5 //if this var is changed the var in setRaise has to be changed
				stopRollers();
				raise();
				setStandby();
			}
			break;
		case SCORE:
			release();
			if (Timer.getFPGATimestamp() > regScoreTime + .5) {
				raise();
				setStandby();
			}
			break;
		case SPIT:
			rollerScore();
			setStandby();
			break;
		case SLOW_SPIT:
			slowRollerScore();
			setStandby();
			break;
		case WRIST_LOWER:
			lower();
			setStandby();
			break;
		case HOME:
			release();
			raise();
			stopRollers();
			setStandby();
			break;
		case GOT_CUBE:
			pinch();
			if (Timer.getFPGATimestamp() > pinchTime + 0.5){
				stopRollers();
				setStandby();
			}
			break;
		case WAIT_FOR_CUBE:
			if(cubeSensor.get()) {
				if(sensorTime < 0) {
					sensorTime = Timer.getFPGATimestamp();
				} else if(Timer.getFPGATimestamp() > sensorTime + 0.1) {
					setCube();
				}
			} else {
				sensorTime = -1;
			}
			break;
		default:
			error("execute", "STATE ERROR");
			break;
		}
		
		rightIntake.set(rightMotorPower);
		leftIntake.set(-leftMotorPower);
		wrist.set(wristPosition);
		pincher.set(pinchPosition);
	}

	/**
	 * Tests all objects of the acquisitions class
	 * @return One debugger result consisting of subsystem name acquisitions, pass, and a message telling user
	 * to ceck that all objects moved.
	 */
	@Override
	public DebuggerResult[] debug() {
		log("debug", "Entered DEBUG");
		wristPosition = !wristPosition;
		pinchPosition = !pinchPosition;
		wrist.set(wristPosition);
		pincher.set(pinchPosition);
		double startingTime = Timer.getFPGATimestamp();
		while(startingTime + 3 > Timer.getFPGATimestamp()){
			rightMotorPower = MOTOR_ACQ;
			leftMotorPower = MOTOR_ACQ;
			rightIntake.set(rightMotorPower);
			leftIntake.set(-leftMotorPower);	
		}
		rightMotorPower = MOTOR_STOP;
		leftMotorPower = MOTOR_STOP;
		rightIntake.set(rightMotorPower);
		leftIntake.set(leftMotorPower);
		DebuggerResult[] debuggerOut = new DebuggerResult[]{new DebuggerResult("Acquitions", true, "Check that both motors, the pincher, and claw moved")};
		return debuggerOut;
	}
	
	/**
	 * Sets acquisition state to lower.
	 */
	public void setAcquire() {
		if (AcqState != State.ACQUIRE){
			AcqState = State.ACQUIRE;
			log("setAcquire", "State set to ACQUIRE");
		}
	}
	
	/**
	 * Sets acquisition state to raise.
	 */
	public void setRaise() {
		if (AcqState != State.RAISE){
			AcqState = State.RAISE;
			pinchTime = Timer.getFPGATimestamp();
			if(pinchPosition == PINCHED) {
				pinchTime -= 1;
			}
			log("setRaise", "State set to RAISE");
		}
	}
	
	/**
	 * Set acquisitions state to regular score
	 */
	public void setScore() {
		if (AcqState != State.SCORE) {
			AcqState = State.SCORE;
			regScoreTime = Timer.getFPGATimestamp();
			log("setScore", "State set to SCORE");
		}
	}
	
	public void setCube() {
		pinchTime = Timer.getFPGATimestamp();
		AcqState = State.GOT_CUBE;
		log("setCube", "State set to GOT_CUBE");
		
	}
	
	public void setLower() {
		AcqState = State.WRIST_LOWER;
	}
	
	/**
	 * Sets the acquisitions to home
	 */
	public void setHome(){
		if (AcqState != State.HOME){
			AcqState = State.HOME;
		}
	}
	
	/**
	 * Sets motors to spit out the cube after moving down wrist (Exchange zone)
	 */
	public void setSpit() {
		if (AcqState != State.SPIT) {
			log("setSpit", "Starting spit");
			AcqState = State.SPIT;
		}
	}
	
	public void setSlowSpit() {
		if (AcqState != State.SLOW_SPIT) {
			log("setSlowSpit", "Starting slow spit");
			AcqState = State.SLOW_SPIT;
		}
	}
	
	/**
	 * Sets acquisition state to waiting for cube.
	 */
	public void setWaitForCube() {
		if (AcqState != State.WAIT_FOR_CUBE){
			AcqState = State.WAIT_FOR_CUBE;
			sensorTime = -1;
			log("setWaitForCube", "State set to WAIT_FOR_CUBE");
		}
	}
	
	/**
	 * Sets acquisition state to standby.
	 */
	public void setStandby() {
		if (AcqState != State.STANDBY){
			AcqState = State.STANDBY;
			log("setStandby", "State set to STANDBY");
		}
	}
	
	/**
	 * Sets the wrist to the raised position
	 */
	private void raise(){
		wristPosition = RAISED;  
	}
	
	/**
	 * Sets the wrist to the lowered position
	 */
	private void lower(){
		wristPosition = LOWERED;
	}
	
	/**
	 * Sets the claw to pinch
	 */
	private void pinch(){
		pinchPosition = PINCHED;
	}
	
	/**
	 * Sets the claw to released
	 */
	private void release(){
		pinchPosition = RELEASED;
	}
	
	/**
	 * Sets the pincher to the opposite of what it is
	 */
	public void togglePinch() {
		pinchPosition = !pinchPosition;
		pincher.set(pinchPosition);
	}
	
	/**
	 * Reverses the intake motors to score the cube
	 */
	private void rollerScore(){
		rightMotorPower = -MOTOR_LAUNCH;
		leftMotorPower = -MOTOR_LAUNCH;
	}
	/**
	 * Reverses the intake motors to score the cube but slow
	 */
	private void slowRollerScore() {
		rightMotorPower = -MOTOR_LOW;
		leftMotorPower = -MOTOR_LOW;
	}
	
	/**
	 * Turns intake motors on to acquire cube
	 */
	private void rollerAcq(){
		rightMotorPower = MOTOR_ACQ;
		leftMotorPower = MOTOR_ACQ;
	}
	
	/**
	 * Sets the intake motors to off
	 */
	private void stopRollers(){
		rightMotorPower = MOTOR_STOP;
		leftMotorPower = MOTOR_STOP;
	}

	/**
	 * Sets motors and actuators to default positions
	 */
	@Override
	public void forceStandby() {
		rightMotorPower = MOTOR_STOP;
		leftMotorPower = MOTOR_STOP;
		wristPosition = RAISED;
		pinchPosition = RELEASED;
		setStandby();
		rightIntake.set(rightMotorPower);
		leftIntake.set(leftMotorPower);
		wrist.set(wristPosition);
		pincher.set(pinchPosition);
	}

	/**
	 * Checks to see if Acquisitions is in Standby
	 */
	@Override
	public boolean isDone() {
		if (AcqState == State.STANDBY){
			return true;
		}
		else{
			
			return false;
		}
	}

	@Override
	public long sleepTime() {
		return 20;
	}

	@Override
	public void toTele() {
		
	}
	
	public void periodicLogs() {
		try {
			Logger.getInstance().logPeriodically(this, Tag.STATUS, this, this.getClass().getMethod("getAcqPowers", new Class[] {}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getAcqPowers() {
		return "Acq powers left side {leftAcqIntake,0,"+leftIntake.get()+"} and right power {rightAcqIntake,0,"+rightIntake.get()+"}\nCurrents: {rightIntakeCurr,3"+rightIntake.getOutputCurrent()+"}{leftIntakeCurr,3"+leftIntake.getOutputCurrent()+"}";
	}
}
