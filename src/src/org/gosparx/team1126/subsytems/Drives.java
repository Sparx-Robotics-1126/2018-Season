package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
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

//	private SPI.Port port1;

	private MotorGroup leftDrives;

	private MotorGroup rightDrives;

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
		//rightMtr3 = new WPI_TalonSRX(IO.rightDriveCIM3);
		leftMtr1 = new WPI_TalonSRX(IO.leftDriveCIM1);
		leftMtr2 = new WPI_TalonSRX(IO.leftDriveCIM2);
		//leftMtr3 = new WPI_TalonSRX(IO.leftDriveCIM3);
		rawRightEnc = new Encoder(IO.rightDriveEncoderChannel1, IO.rightDriveEncoderChannel2);
		rawLeftEnc = new Encoder(IO.leftDriveEncoderChannel1, IO.leftDriveEncoderChannel2);
		rightEnc = new EncoderData(rawRightEnc, 0.032);
		leftEnc = new EncoderData(rawLeftEnc, -0.032);
		//ptoSwitch = new Solenoid(IO.ptoSwitch);
		//port1 = new SPI.Port(0);
		gyro = new AHRS(SerialPort.Port.kUSB);
		isMoving = false;
		speedRight = 0;
		speedLeft = 0;
		moveSpeed = 0;
		rightDrives = new MotorGroup(rightMtr1, rightMtr2);
		leftDrives = new MotorGroup(leftMtr1, leftMtr2);
		rightDrives.setNeutralMode(NeutralMode.Brake);
		rightDrives.setInverted(true);
		leftDrives.setNeutralMode(NeutralMode.Brake);
//		addObjectsToShuffleboard();
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
			leftEnc.calculateSpeed();
			rightEnc.calculateSpeed();
			break;
		case TURN_R:
			if(gyro.getAngle() > turnAngle) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else if(gyro.getAngle() > turnAngle * 0.8){
				turnSpeed = 0.2;
				leftDrives.set(-turnSpeed);
				rightDrives.set(turnSpeed);
			}else {
				print("angle: " + gyro.getAngle());
				leftDrives.set(-turnSpeed);
				rightDrives.set(turnSpeed);
			}
			break;
		case TURN_L:
			if(gyro.getAngle() < turnAngle) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else if(gyro.getAngle() < turnAngle * 0.8) {
				turnSpeed = 0.2;
				leftDrives.set(turnSpeed);
				rightDrives.set(-turnSpeed);
			}else {
				print("angle: " + gyro.getAngle());
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
			}else if((moveDist*.8)<rightEnc.getDistance() + leftEnc.getDistance()/2){
				moveSpeed = .3;
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
			print("Speed left: " + speedLeft + " Speed right: " + speedRight);
			break;
		case MOVE_BKWD:
			if(moveDist > (rightEnc.getDistance() + leftEnc.getDistance())/2) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else if((moveDist*.8)<rightEnc.getDistance() + leftEnc.getDistance()/2){
				moveSpeed = .3;
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
			print("Speed left: " + speedLeft + " Speed right: " + speedRight);
			break;
		}
	}

	/**
	 * changes drives state
	 * @param st - the state to switch to
	 */
	public void changeState(DriveState st) {
		print("change state");
		state = st;
		if(state == DriveState.TELEOP) {
			leftDrives.setNeutralMode(NeutralMode.Coast);
			rightDrives.setNeutralMode(NeutralMode.Coast);
		}
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
	 * @param dist - decimal value in inches
	 * @param speed - the speed wanted to move
	 */
	public void move(double dist, int speed) {
		gyro.zeroYaw();
		rightEnc.reset();
		leftEnc.reset();
		moveDist = dist;
		moveSpeed = speed/100.;
		speedLeft = moveSpeed;
		speedRight = moveSpeed;
		isMoving = true;
		if(dist > 0) {
			changeState(DriveState.MOVE_FWRD);
			print("moving frwd");
		}else {
			changeState(DriveState.MOVE_BKWD);
			print("moving bkwd");
		}
	}

	/**
	 * makes sure the robot is straight
	 * @return a boolean, true if robot was straightened
	 */
	private boolean straightenForward() {
		if(gyro.getAngle() > 2) {
			speedRight = moveSpeed * 0.2;
			return true;
		}else if(gyro.getAngle() < -2) {
			speedLeft = moveSpeed * 0.2;
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
		if(gyro.getAngle() > 2) {
			speedLeft = moveSpeed * 0.2;
			return true;
		}else if(gyro.getAngle() < -2) {
			speedRight = moveSpeed * 0.2;
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
		DebuggerResult[] results = new DebuggerResult[leftDrives.getMtrCount()+rightDrives.getMtrCount()];
		
		for(int i = 0; i < leftDrives.getMtrCount(); i++) {
			results[i] = testMotor((WPI_TalonSRX)leftDrives.getSpeedController(i), leftEnc, i);		
		}
		for(int i = 0; i < rightDrives.getMtrCount(); i++) {
			results[i+results.length/2] = testMotor((WPI_TalonSRX)rightDrives.getSpeedController(i), rightEnc, i);		
		}
		
		for(int i = 0; i < results.length; i++) {
			print("results "  + i + ": " + results[i].getMessage());
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
		if(mtrTesting == null) {
			return new DebuggerResult("Drives", false, "The motor " + i + " was null");
		}
		mtr.set(.5);
		
		while(System.currentTimeMillis() < time + 1500) {  //After setting speed wait 5 seconds
			encoder.calculateSpeed();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			print("encoder: " + encoder.getDistance());
//			print("right encoder: " + rightEnc.getDistance());
		}
		
		mtr.set(0);
		print("Encoder: " + encoder.getDistance());
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
		if(!isMoving)
			return true;
		return false;
	}

	@Override
	/**
	 * the time in milliseconds between each call of execute
	 */
	public long sleepTime() {
		return 20;
	}

}