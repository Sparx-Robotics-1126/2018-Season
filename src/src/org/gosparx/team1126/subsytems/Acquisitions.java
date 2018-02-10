package src.org.gosparx.team1126.subsytems;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import src.org.gosparx.team1126.robot.IO;
import src.org.gosparx.team1126.util.DebuggerResult;


/**
 * This class allows for the control of the acquisitions subsystem.
 * @author Aidan Cheeseman (cheesemanaidan@gmail.com)
 * @author Andrew Thompson (andrewt015@gmail.com)
 */
public class Acquisitions extends GenericSubsytem{

	//Objects
	private static WPI_TalonSRX leftIntake;
	
	private static WPI_TalonSRX rightIntake;
	
	private static Solenoid pincher;
	
	private static Solenoid wrist;
	
	
	//Constants
	private static final boolean PINCHED = false;
	
	private static final boolean RELEASED = !PINCHED;
	
	private static final boolean RAISED = false;
	
	private static final boolean LOWERED = !RAISED;
	
	private static final double MOTOR_ON = 0.8;  //TODO get actual power
	
	private static final double MOTOR_STOP = 0.0;
	
	
	//Variables
	private static State AcqState;
	
	private static double rightMotorPower;
	
	private static double leftMotorPower;
	
	private static double pinchTime;
	
	private static double scoreTime;
		
	private static boolean pinchPosition;  //true = pinched 
	
	private static boolean wristPosition;  //true = lowered
		
	
	public Acquisitions() {
		super("Acquisitions");
	}
	
	
	public enum State{
		STANDBY,
		RAISE,
		ACQUIRE,
		SCORE;
	}
	
	
	@Override
	public void init() {
		AcqState = State.STANDBY;
		rightMotorPower = MOTOR_STOP;
		leftMotorPower = MOTOR_STOP;
		pinchPosition = RELEASED;
		wristPosition = RAISED;
		leftIntake = new WPI_TalonSRX(IO.CAN_ACQ_LEFT_INTAKE);
		rightIntake = new WPI_TalonSRX(IO.CAN_ACQ_RIGHT_INTAKE);
		wrist = new Solenoid(IO.PNU_WRIST);
		pincher = new Solenoid(IO.PNU_PINCHER);
		wrist.set(wristPosition);
		pincher.set(pinchPosition);
		log("Successfully INITIALIZED");
	}
	
	/**
	 * The actual code of the robot. Constantly looping.
	 */
	@Override
	public void execute() {
		
		switch(AcqState){
		
		case STANDBY:
			return;
		
		case ACQUIRE:
			lower();
			release();
			rollerAcq();
			setStandby();
			break;
			
		case RAISE:
			pinch();
			rollerAcq();
			if (Timer.getFPGATimestamp() > pinchTime + 1){ //TODO get actual time
				stopRollers();
				raise();
				setStandby();
			}
			break;
			
		case SCORE:
			lower();
			rollerScore();
			if (Timer.getFPGATimestamp() > scoreTime + 2){ //TODO get actual time
				release();
				setStandby();
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
			rightMotorPower = MOTOR_ON;
			leftMotorPower = MOTOR_ON;
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
	 * Sets acquisition state to score.
	 */
	public void setScore() {
		if (AcqState != State.SCORE){
			AcqState = State.SCORE;
			scoreTime = Timer.getFPGATimestamp();
			log("State set to SCORE");
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
	 * Reverses the intake motors to score the cube
	 */
	private void rollerScore(){
		rightMotorPower = -MOTOR_ON;
		leftMotorPower = -MOTOR_ON;
	}
	
	/**
	 * Turns intake motors on to acquire cube
	 */
	private void rollerAcq(){
		rightMotorPower = MOTOR_ON;
		leftMotorPower = MOTOR_ON;
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
