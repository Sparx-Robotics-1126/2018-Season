package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
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

	//-----------------------------------------------------Motors/Sensors--------------------------------------------------------

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

	private SPI.Port port1;

	private MotorGroup leftDrives;

	private MotorGroup rightDrives;

	//-------------------------------------------------------Variables------------------------------------------------------------

	private boolean isMoving;

	private double speedRight;

	private double speedLeft;

	private DriveState state;

	private double turnSpeed;

	private int turnAngle;

	private double moveSpeed;

	private double moveDist;

	//---------------------------------------------------------Code--------------------------------------------------------------

	@Override
	/**
	 * initializes all variables
	 */
	public void init() {
		rightMtr1 = new WPI_TalonSRX(IO.rightDriveCIM1);
		rightMtr2 = new WPI_TalonSRX(IO.rightDriveCIM2);
		rightMtr3 = new WPI_TalonSRX(IO.rightDriveCIM3);
		leftMtr1 = new WPI_TalonSRX(0);
		leftMtr2 = new WPI_TalonSRX(0);
		leftMtr3 = new WPI_TalonSRX(0);
		rawRightEnc = new Encoder(0, 0);
		rawLeftEnc = new Encoder(0, 0);
		rightEnc = new EncoderData(rawRightEnc, 0);
		leftEnc = new EncoderData(rawLeftEnc, 0);
		ptoSwitch = new Solenoid(0);
		//port1 = new SPI.Port(0);
		gyro = new AHRS(port1);
		isMoving = false;
		speedRight = 0;
		speedLeft = 0;
		rightDrives = new MotorGroup(rightMtr1, rightMtr2, rightMtr3);
		leftDrives = new MotorGroup(leftMtr1, leftMtr2, leftMtr3);
		rightDrives.setNeutralMode(NeutralMode.Brake);
		leftDrives.setNeutralMode(NeutralMode.Brake);
		addObjectsToShuffleboard();
	}

	/**
	 * contains the possible states of drives
	 * STANDBY - drives is off
	 * TELEOP - motors are set by user input
	 * TURN_STATES - turns robot by specified angle and speed
	 * MOVE_STATES - move the robot by specified dist and speed
	 */
	enum DriveState{
		STANDBY,
		TURN_R,
		TURN_L,
		MOVE_FWRD,
		MOVE_BKWD,
		TELEOP;
	}

	/**
	 * Adds all the sendable objects to shuffleboard
	 */
	private void addObjectsToShuffleboard() {
		//SmartDashboard.putData(leftEnc);
		//SmartDashboard.putData(rightEnc);
		SmartDashboard.putData(gyro);
		SmartDashboard.putData(rightDrives);
		SmartDashboard.putData(leftDrives);
		SmartDashboard.putData(ptoSwitch);	
		SmartDashboard.updateValues();
	}

	/**
	 * runs drives code based on state
	 * When standby or auto - do nothing
	 * When Running - sets motor speeds based on joystick values
	 */
	@Override
	public void execute() {
		switch(state) {
		case STANDBY:  
			break;
		case TELEOP:
			rightDrives.set(speedRight);
			leftDrives.set(speedLeft);
			break;
		case TURN_R:
			if(gyro.getAngle() > turnAngle) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else {
				leftDrives.set(turnSpeed);
				rightDrives.set(-turnSpeed);
			}
			break;
		case TURN_L:
			if(gyro.getAngle() < turnAngle) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else {
				leftDrives.set(-turnSpeed);
				rightDrives.set(turnSpeed);
			}
			break;
		case MOVE_FWRD:
			if(moveDist < (rightEnc.getDistance() + leftEnc.getDistance())/2) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else {
				leftDrives.set(moveSpeed);
				rightDrives.set(moveSpeed);
			}
			break;
		case MOVE_BKWD:
			if(moveDist > (rightEnc.getDistance() + leftEnc.getDistance())/2) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else {
				leftDrives.set(-moveSpeed);
				rightDrives.set(-moveSpeed);
			}
			break;
		}
	}

	/**
	 * changes drives state
	 * @param st - the state to switch to
	 */
	public void changeState(DriveState st) {
		state = st;
	}

	/**
	 * moves robot according to joystick values, -100 to 100
	 * @param speedR - the right joystick speed
	 * @param speedL - the left joystick speed
	 */
	public void joysticks(int speedR, int speedL) {
		speedRight = speedR/100.0;
		speedLeft = speedL/100.0;	
	}

	/**
	 * sets the variables and changes the state for turning
	 * @param degree - the degree amount 
	 * @param speed - the speed amount
	 */
	public void turn(int degree, int speed) {
		gyro.zeroYaw();
		turnAngle = degree;
		turnSpeed = speed/100.;
		isMoving = true;
		if(degree < 0) {
			changeState(DriveState.TURN_L);
		}else
			changeState(DriveState.TURN_R);
	}

	/**
	 * sets the variables and changes the state for moving
	 * @param dist - decimal value in feet
	 * @param speed - the speed wanted to move
	 */
	public void move(double dist, int speed) {
		rightEnc.reset();
		leftEnc.reset();
		moveDist = dist;
		moveSpeed = speed/100.;
		isMoving = true;
		if(dist > 0) {
			changeState(DriveState.MOVE_FWRD);
		}else {
			changeState(DriveState.MOVE_BKWD);
		}
	}

//	/**
//	 * makes sure the robot is straight(within -10 to 10)
//	 * @return a boolean, true if robot was straightened
//	 */
//	private boolean straighten() {
//		if(gyro.getAngle() > 10) {
//			speedLeft = speedLeft - (speedLeft * 0.1);
//			return true;
//		}if(gyro.getAngle() < -10) {
//			speedRight = speedRight - (speedRight * 0.1);
//			return true;
//		}
//		return false;
//
//	}

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

	/**
	 * returns if the robot is currently moving during autonomous
	 */
	public boolean getIsMoving() {
		return isMoving;
	}

	@Override
	/**
	 * debugs the code to make sure motors are spinning correctly and encoders are reading correctly 
	 */
	public DebuggerResult[] debug() {		//one CIM is enough to check the encoders per side, needs to be at least 0.5 power
		DebuggerResult[] results = new DebuggerResult[6];
		long time = System.currentTimeMillis();
		WPI_TalonSRX mtrTesting = null;

		for(int i = 0; i < 6; i++) {

			if(i < 3) { //on first part of loop, reset left encoder and test left motors
				leftEnc.reset();
				if(leftDrives.getSpeedController(i) != null)
					mtrTesting = (WPI_TalonSRX)leftDrives.getSpeedController(i);
				else
					break;
			}else {     //on second part of loop, reset right encoder and test right motors
				rightEnc.reset();
				if(rightDrives.getSpeedController(i-3) != null) 
					mtrTesting = (WPI_TalonSRX)rightDrives.getSpeedController(i-3);
				else
					break;
			}

			mtrTesting.set(.5);
			while(System.currentTimeMillis() < time + 5000) {  //After setting speed wait 5 seconds
			}
			if(i<3) {
				if(leftEnc.getDistance() > 0) {
					results[i] = new DebuggerResult("Drives", true, "Left Encoder worked on left motor " + i);
				}else {
					results[i] = new DebuggerResult("Drives", false, "Left Encoder failed on left motor " + i);
				}
			}else {
				if(rightEnc.getDistance() > 0) {
					results[i] = new DebuggerResult("Drives", true, "Right Encoder worked on right motor " + (i - 3));
				}else {
					results[i] = new DebuggerResult("Drives", false, "Right Encoder failed on right motor " + (i - 3));
				}
			}
		}

		if(mtrTesting == null)
			results[5] = new DebuggerResult("Drives", false, "A motor was null");
		return results;
	}

	@Override
	public void forceStandby() {

	}

	@Override
	/**
	 * if in standby
	 */
	public boolean isDone() {
		if(!isMoving)
			return true;
		return false;
	}

	@Override
	public long sleepTime() {
		return 20;
	}

}