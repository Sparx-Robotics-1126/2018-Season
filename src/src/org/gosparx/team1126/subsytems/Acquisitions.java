package src.org.gosparx.team1126.subsytems;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.gosparx.team1126.robot.IO;
import src.org.gosparx.team1126.sensors.AnalogSensor;
import src.org.gosparx.team1126.util.DebuggerResult;


/**
 * This class allows for the control of the acquisitions subsystem.
 * @author Aidan Cheeseman (cheesemanaidan@gmail.com)
 * @author Andrew Thompson (an
 * 
 * drewt015@gmail.com)
 */
public class Acquisitions extends GenericSubsytem{

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
	
	private static final double MOTOR_ACQ = .7; //originally 0.9 
	
	private static final double MOTOR_LAUNCH = .8;
	
	private static final double MOTOR_STOP = 0.0;
	
	private static final double MOTOR_LOW = .7;
	
	private static final double CUBE_SENSOR_THRESHOLD = 2.4;
	
	
	//Variables
	private State AcqState;
	
	private double rightMotorPower;
	
	private double leftMotorPower;
	
	private double pinchTime;
	
	private double scoreTime;
	
	private double regScoreTime;
	
	private double spitTime;

	private boolean pinchPosition;  //true = pinched 
	
	private boolean wristPosition;  //true = lowered
		
	
	public Acquisitions() {
		super("Acquisitions");
	}
	
	
	public enum State{
		STANDBY,
		RAISE,
		ACQUIRE,
		LAUNCH_SCORE,
		SLOW_LAUNCH_SCORE,
		REGULAR_SCORE,
		HOME,
		SPIN,
		SPIT,
		SLOW_SPIT,
		WAIT_FOR_CUBE;
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
		log("Successfully INITIALIZED");
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
			
		case SPIN:
			lower();
			release();
			spin();
			setStandby();
			break;
			
		case RAISE:
			pinch();
			rollerAcq();
			if (Timer.getFPGATimestamp() > pinchTime + .5){ //.5
				stopRollers();
				raise();
				setStandby();
			}
			break;
			
		case LAUNCH_SCORE:
			lowLaunch();
			if(Timer.getFPGATimestamp() > scoreTime + 1) {
				release();
				setStandby();
			}
			break;
		case SLOW_LAUNCH_SCORE:
			slowRollerScore();
			if(Timer.getFPGATimestamp() > scoreTime + 1) {
				release();
				setStandby();
			}
			break;
		case REGULAR_SCORE:
			lower();
			if (Timer.getFPGATimestamp() > regScoreTime + .5) {
			release();
			setStandby();
			}
			break;
			
		case SPIT:
			lower();
			if (Timer.getFPGATimestamp() > spitTime + 0.5) {
				rollerScore();
				setStandby();
			}
			break;
		case SLOW_SPIT:
			lower();
			if (Timer.getFPGATimestamp() > spitTime + 0.5) {
				System.out.println("iteration++");
				slowRollerScore();
				setStandby();
			}
			break;
		case HOME:
			raise();
			release();
			stopRollers();
			break;
			
		case WAIT_FOR_CUBE:
			if(cubeSensor.get()) {
				setRaise();
			}
			break;
		default:
			log("STATE ERROR");
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
		log("Entered DEBUG");
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
			log("State set to ACQUIRE");
		}
	}
	
	/**
	 * Sets acquisition state to spin.
	 */
	public void setSpin() {
		if (AcqState != State.SPIN){
			AcqState = State.SPIN;
			log("State set to SPIN");
		}
	}
	
	/**
	 * Sets acquisition state to raise.
	 */
	public void setRaise() {
		if (AcqState != State.RAISE){
			AcqState = State.RAISE;
			pinchTime = Timer.getFPGATimestamp();
			log("State set to RAISE");
		}
	}

	/**
	 * Sets acquisition state to launching score.
	 */
	public void setLaunchScore() {
		if (AcqState != State.LAUNCH_SCORE){
			AcqState = State.LAUNCH_SCORE;
			scoreTime = Timer.getFPGATimestamp();
			log("State set to LAUNCH_SCORE");
		}
	}
	
	/**
	 * sets acquisition state to slow launching score
	 */
	public void setSlowLaunchScore() {
		if (AcqState != State.SLOW_LAUNCH_SCORE){
			AcqState = State.SLOW_LAUNCH_SCORE;
			scoreTime = Timer.getFPGATimestamp();
			log("State set to SLOW_LAUNCH_SCORE");
		}
	}
	
	
	/**
	 * Set acquisitions state to regular score
	 */
	public void setRegScore() {
		if (AcqState != State.REGULAR_SCORE) {
			AcqState = State.REGULAR_SCORE;
			regScoreTime = Timer.getFPGATimestamp();
			log("State set to REGULAR_SCORE");
		}
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
			System.out.println("Starting spit");
			AcqState = State.SPIT;
			spitTime = Timer.getFPGATimestamp();
		}
	}
	
	public void setSlowSpit() {
		if (AcqState != State.SLOW_SPIT) {
			System.out.println("Starting slow spit");
			AcqState = State.SLOW_SPIT;
			spitTime = Timer.getFPGATimestamp();
		}
	}
	
	/**
	 * Sets acquisition state to waiting for cube.
	 */
	public void setWaitForCube() {
		if (AcqState != State.WAIT_FOR_CUBE){
			AcqState = State.WAIT_FOR_CUBE;
			log("State set to WAIT_FOR_CUBE");
		}
	}
	
	/**
	 * Sets acquisition state to standby.
	 */
	public void setStandby() {
		if (AcqState != State.STANDBY){
			AcqState = State.STANDBY;
			log("State set to STANDBY");
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
	 * Reverses the intake motors to score the cube at launching speed.
	 */
	private void lowLaunch() {
		rightMotorPower = -MOTOR_LAUNCH;
		leftMotorPower = -MOTOR_LAUNCH;
	}
	
	/**
	 * Turns intake motors on to acquire cube
	 */
	private void rollerAcq(){
		rightMotorPower = MOTOR_ACQ;
		leftMotorPower = MOTOR_ACQ;
	}
	
	/**
	 * Turns intake motors in a certain direction to acquire
	 */
	private void spin() {
		rightMotorPower = MOTOR_ACQ;
		leftMotorPower = MOTOR_ACQ/2;
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
}
