package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.gosparx.team1126.robot.IO;
import src.org.gosparx.team1126.sensors.EncoderData;
import src.org.gosparx.team1126.util.DebuggerResult;
import src.org.gosparx.team1126.util.MotorGroup;

public class Drives extends GenericSubsytem {

	public Drives() {
		super("Drives");
	}

	//-----------------------------------------------------Motors/Sensors---------------------------------------------------------

	private WPI_TalonSRX rightMtr1;

	private WPI_TalonSRX rightMtr2;

	private WPI_TalonSRX rightMtr3;

	private WPI_TalonSRX leftMtr1;

	private WPI_TalonSRX leftMtr2;

	private WPI_TalonSRX leftMtr3;

	private EncoderData rightEnc;

	private EncoderData leftEnc;

	private Encoder rawRightEnc;

	private Encoder rawLeftEnc;

	private Solenoid ptoSwitch;

	private AHRS gyro;

	private MotorGroup leftDrives;

	private MotorGroup rightDrives;

	//-------------------------------------------------------Constants------------------------------------------------------------

	private final double DECIMAL_TO_SLOW = .7;			//What part of the way to destination in auto we start moving at a slow speed (move)

	private final double DECIMAL_ANGLE_TO_SLOW = .2;	//What decimal part of the way through a turn we start moving at slow speed (turn)
	
	private final double SLOW_SPEED = .2;				//The speed we move at in auto when almost at destination to achieve higher accuracy (turn+move)

	private final int DEADBAND_TELL_NO_TALES = 20;		//The deadband inside which a turn will stop, so robot doesn't over-turn
		
	private final double KEVIN = .7;					//Sets the over-performing motor in auto to this percentage of its speed until within allowable error

	private final double ALLOWABLE_ERROR = 2;			//Degrees robot can be off in move auto before straightening
	
	//-------------------------------------------------------Variables------------------------------------------------------------

	private boolean isMoving;

	private double speedRight;

	private double speedLeft;

	private DriveState state;

	private double turnSpeed;

	private int turnAngle;

	private double moveDist;

	private double moveSpeed;

	//---------------------------------------------------------Code---------------------------------------------------------------

	@Override
	/**
	 * initializes all variables
	 */
	public void init() {
		rightMtr1 = new WPI_TalonSRX(IO.rightDriveCIM1);
		rightMtr2 = new WPI_TalonSRX(IO.rightDriveCIM2);
		rightMtr3 = new WPI_TalonSRX(IO.rightDriveCIM3);
		leftMtr1 = new WPI_TalonSRX(IO.leftDriveCIM1);
		leftMtr2 = new WPI_TalonSRX(IO.leftDriveCIM2);
		leftMtr3 = new WPI_TalonSRX(IO.leftDriveCIM3);
		rawRightEnc = new Encoder(IO.rightDriveEncoderChannel1, IO.rightDriveEncoderChannel2);
		rawLeftEnc = new Encoder(IO.leftDriveEncoderChannel1, IO.leftDriveEncoderChannel2);
		//ptoSwitch = new Solenoid(0);
		leftEnc = new EncoderData(rawLeftEnc, -0.032);
		rightEnc = new EncoderData(rawRightEnc, 0.032);
		gyro = new AHRS(SerialPort.Port.kUSB);
		isMoving = false;
		speedRight = 0;
		speedLeft = 0;
		moveSpeed = 0;
		rightDrives = new MotorGroup(rightMtr1, rightMtr2, rightMtr3);
		leftDrives = new MotorGroup(leftMtr1, leftMtr2, leftMtr3);
		rightDrives.setNeutralMode(NeutralMode.Brake);
		rightDrives.setInverted(true);
		leftDrives.setNeutralMode(NeutralMode.Brake);
		changeState(DriveState.STANDBY);
		addObjectsToShuffleboard();
	}

	/**
	 * contains the possible states of drives
	 * STANDBY - drives is off
	 * TELEOP - motors are set by user input
	 * TURN_STATES - turns robot by specified angle and speed
	 * MOVE_STATES - move the robot by specified dist and speed
	 */
	public enum DriveState{
		STANDBY,
		TELEOP,
		MOVE_FWRD,
		MOVE_BKWD,
		TURN_R,
		TURN_L;
	}

	/**
	 * Adds all the sendable objects to shuffleboard
	 */
	private void addObjectsToShuffleboard() {
		SmartDashboard.putData(rawLeftEnc);
		SmartDashboard.putData(rawRightEnc);
		SmartDashboard.putData(gyro);
		SmartDashboard.putData(rightDrives);
		SmartDashboard.putData(leftDrives);
		SmartDashboard.putData(ptoSwitch);	
		SmartDashboard.updateValues();
	}

	/**
	 * runs drives code based on state
	 * When standby - do nothing
	 * When teleop - sets motor speeds based on joystick values
	 * when in any auto method - waits till task is complete and switches to standby
	 */
	@Override
	public void execute() {
		switch(state) {
		case STANDBY:  
			break;
		case TELEOP:
			rightDrives.set(speedRight);
			leftDrives.set(speedLeft);
			leftEnc.calculateSpeed();
			rightEnc.calculateSpeed();
			//print("Left Distance: " + leftEnc.getDistance() + " Right Distance: " + rightEnc.getDistance());
			break;
		case TURN_R:
			//print("Gyro Angle: " + gyro.getAngle());
			if(gyro.getAngle() > turnAngle - DEADBAND_TELL_NO_TALES) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else if(gyro.getAngle() > turnAngle * DECIMAL_ANGLE_TO_SLOW){
				turnSpeed = SLOW_SPEED;
				leftDrives.set(-turnSpeed);
				rightDrives.set(turnSpeed);
			}else {
				//print("angle: " + gyro.getAngle());
				leftDrives.set(-turnSpeed);
				rightDrives.set(turnSpeed);
			}
			break;
		case TURN_L:
			if(gyro.getAngle() < turnAngle + DEADBAND_TELL_NO_TALES) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else if(gyro.getAngle() < turnAngle * DECIMAL_ANGLE_TO_SLOW) {
				turnSpeed = SLOW_SPEED;
				leftDrives.set(turnSpeed);
				rightDrives.set(-turnSpeed);
			}else {
				//print("angle: " + gyro.getAngle());
				leftDrives.set(turnSpeed);
				rightDrives.set(-turnSpeed);
			}
			break;
		case MOVE_FWRD:
			leftEnc.calculateSpeed();
			rightEnc.calculateSpeed();
			if(moveDist < (rightEnc.getDistance() + leftEnc.getDistance())/2) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else if((moveDist*DECIMAL_TO_SLOW) < (rightEnc.getDistance() + leftEnc.getDistance())/2){
				moveSpeed = SLOW_SPEED;
				speedRight = moveSpeed;
				speedLeft = moveSpeed;
				straightenForward();
				rightDrives.set(speedRight);
				leftDrives.set(speedLeft);
			}else {
				straightenForward();
				leftDrives.set(speedLeft);
				rightDrives.set(speedRight);
			}
			//print("Left Distance: " + leftEnc.getDistance() + " Right Distance: " + rightEnc.getDistance());
			break;
		case MOVE_BKWD:
			if(moveDist > (rightEnc.getDistance() + leftEnc.getDistance())/2) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else if((moveDist*DECIMAL_TO_SLOW)<rightEnc.getDistance() + leftEnc.getDistance()/2){
				moveSpeed = SLOW_SPEED;
				speedRight = moveSpeed;
				speedLeft = moveSpeed;
				straightenBackward();
				rightDrives.set(-speedRight);
				leftDrives.set(-speedLeft);
			}else {
				straightenBackward();
				leftDrives.set(-speedLeft);
				rightDrives.set(-speedRight);
				leftEnc.calculateSpeed();
				rightEnc.calculateSpeed();
			}
				//print("Speed left: " + speedLeft + " Speed right: " + speedRight);
			break;
		}
	}

	/**
	 * changes drives state
	 * @param st - the state to switch to
	 */
	public void changeState(DriveState st) {
		state = st;
		if(state == DriveState.TELEOP) {
			leftDrives.setNeutralMode(NeutralMode.Coast);
			rightDrives.setNeutralMode(NeutralMode.Coast);
		}
	}

	/**
	 * moves right motors to joystick value, -1 to 1
	 * @param speedR - the right joystick speed
	 */
	public void joystickRight(double speedR) {
		speedRight = speedR;
	}
	
	/**
	 * moves left motors to joystick value, -1 to 1
	 * @param speedL - the left joystick speed
	 */
	public void joystickLeft(double speedL) {
		speedLeft = speedL;
	}

	/**
	 * sets the variables and changes the state for turning
	 * @param degree - the degree amount 
	 * @param speed - the speed amount, -100 to 100
	 */
	public void turn(int degree, int speed) {
		gyro.zeroYaw();
		turnAngle = degree;
		turnSpeed = speed/100.;
		isMoving = true;
		if(degree > 0) {
			changeState(DriveState.TURN_R);
		}else
			changeState(DriveState.TURN_L);
	}

	/**
	 * sets the parameters to variables and changes the state for moving
	 * @param dist - value in inches
	 * @param speed - the speed amount, -100 to 100
	 */
	public void move(int dist, int speed) {
		gyro.zeroYaw();
		rightEnc.reset();
		leftEnc.reset();
		moveDist = dist;
		moveSpeed = speed/100.;
		speedLeft = moveSpeed;
		speedRight = moveSpeed;
		isMoving = true;
		//print("MOVING");
		if(dist > 0) {
			changeState(DriveState.MOVE_FWRD);
		}else {
			changeState(DriveState.MOVE_BKWD);
		}
	}

	/**
	 * makes sure the robot is straight
	 * @return a boolean, true if robot was straightened
	 */
	private boolean straightenForward() {
		if(gyro.getAngle() > ALLOWABLE_ERROR) {
			speedRight = moveSpeed * KEVIN;
			return true;
		}else if(gyro.getAngle() < -ALLOWABLE_ERROR) {
			speedLeft = moveSpeed * KEVIN;
			return true;
		}else {
			speedLeft = moveSpeed;
			speedRight = moveSpeed;
		}
		return false;

	}

	/**
	 * makes sure the robot is straight
	 * @return a boolean, true if robot was straightened
	 */
	private boolean straightenBackward() {
		if(gyro.getAngle() > ALLOWABLE_ERROR) {
			speedLeft = moveSpeed * KEVIN;
			return true;
		}else if(gyro.getAngle() < -ALLOWABLE_ERROR) {
			speedRight = moveSpeed * KEVIN;
			return true;
		}else {
			speedLeft = moveSpeed;
			speedRight = moveSpeed;
		}
		return false;
	}

	/**
	 * stops all motors
	 */
	public void stopMotors() {
		rightDrives.set(0);
		leftDrives.set(0);
	}

	/**
	 * switches between driving and climbing
	 * @param switchingToClimb - true if driving, false if climbing
	 */
	public void PTOSwitch(boolean switchingToClimb) {
		ptoSwitch.set(switchingToClimb);
	}

	@Override
	/**
	 * debugs the code to make sure motors are spinning correctly and encoders are reading correctly 
	 */
	public DebuggerResult[] debug() {	
		DebuggerResult[] results = new DebuggerResult[leftDrives.getMtrCount()+rightDrives.getMtrCount()];

		for(int i = 0; i < leftDrives.getMtrCount(); i++) {
			results[i] = testMotor((WPI_TalonSRX)leftDrives.getSpeedController(i), leftEnc, i);		
		}
		for(int i = 0; i < rightDrives.getMtrCount(); i++) {
			results[i+results.length/2] = testMotor((WPI_TalonSRX)rightDrives.getSpeedController(i), rightEnc, i);		
		}

		return results;
	}

	/**
	 * Tests a motor with a controller
	 * @param mtrTesting - motor to test
	 * @param encoder - encoder on the same side as the motors
	 * @return- a debug result for the motor
	 */
	private DebuggerResult testMotor(WPI_TalonSRX mtrTesting, EncoderData encoder, int i) {
		WPI_TalonSRX mtr = mtrTesting;
		long time = System.currentTimeMillis();
		encoder.reset();
		encoder.calculateSpeed();
		if(mtrTesting == null) {
			return new DebuggerResult("Drives", false, "The motor " + i + " was null");
		}
		mtr.set(.8);

		while(System.currentTimeMillis() < time + 2000) { 
		}

		mtr.set(0);
		encoder.calculateSpeed();
		//print("Encoder: " + encoder.getDistance());
		if(encoder.getDistance() > 0) {
			return new DebuggerResult("Drives", true, "Encoder worked on motor " + i);
		}else {
			return new DebuggerResult("Drives", false, "Encoder failed on motor " + i);
		}

	}

	@Override
	/**
	 * sets state to standby and stops all motors
	 */
	public void forceStandby() {
		changeState(DriveState.STANDBY);
		stopMotors();
	}

	@Override
	/**
	 * if in standby
	 */
	public boolean isDone() {
		return !isMoving;
	}

	@Override
	/**
	 * the time in milliseconds between each call of execute
	 */
	public long sleepTime() {
		return 20;
	}

}