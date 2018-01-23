package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import src.org.gosparx.team1126.util.DebuggerResult;
import src.org.gosparx.team1126.util.MotorGroup;

public class Drives extends GenericSubsytem {

	//-----------------------------------------------------Motors/Sensors--------------------------------------------------------

	private WPI_TalonSRX rightMtr1;

	private WPI_TalonSRX rightMtr2;

	private WPI_TalonSRX rightMtr3;

	private WPI_TalonSRX leftMtr1;

	private WPI_TalonSRX leftMtr2;

	private WPI_TalonSRX leftMtr3;

	private Encoder rightEnc;

	private Encoder leftEnc;

	private Solenoid cylinder;

	private AHRS gyro;

	private SPI.Port port1;

	private MotorGroup leftDrives;
	
	private MotorGroup rightDrives;
	
	//-------------------------------------------------------Constants------------------------------------------------------------

//	private final double PORPORTIONAL = 1;
//
//	private final double INTEGRAL = 1;
//
//	private final double DIFFERENTIAL = 1;
	
	//-------------------------------------------------------Variables------------------------------------------------------------

	private boolean isMoving;

	// speedRight and speedLeft are magnitudes
	private double speedRight;

	private double speedLeft;

	private DriveState state;

	private double speedRightOffset;
	
	private double speedLeftOffset;
	
//	private  PIDController rightPID;
//	
//	private PIDController leftPID;
	
	//---------------------------------------------------------Code--------------------------------------------------------------

	@Override
	/**
	 * initializes all variables
	 */
	public void init() {
		rightMtr1 = new WPI_TalonSRX(0);
		rightMtr2 = new WPI_TalonSRX(0);
		rightMtr3 = new WPI_TalonSRX(0);
		leftMtr1 = new WPI_TalonSRX(0);
		leftMtr1 = new WPI_TalonSRX(0);
		leftMtr1 = new WPI_TalonSRX(0);
		rightEnc = new Encoder(0, 0);
		leftEnc = new Encoder(0, 0);
		cylinder = new Solenoid(0);
		//port1 = new SPI.Port(0);
		gyro = new AHRS(port1);
		isMoving = false;
		speedRight = 0.5;
		speedLeft = 0.5;
		speedRightOffset = 0;
		speedLeftOffset = 0;
		rightDrives = new MotorGroup(rightMtr1, rightMtr2, rightMtr3);
		leftDrives = new MotorGroup(leftMtr1, leftMtr2, leftMtr3);
//		rightPID = new PIDController(PORPORTIONAL, INTEGRAL, DIFFERENTIAL, rightEnc, rightDrives);
//		leftPID = new PIDController(PORPORTIONAL, INTEGRAL, DIFFERENTIAL, leftEnc, leftDrives);
		rightDrives.setNeutralMode(NeutralMode.Brake);
		leftDrives.setNeutralMode(NeutralMode.Brake);
		
	}

	/**
	 * contains the possible states of drives
	 * STANDBY - drives is off
	 * AUTO - Drives is being run by autonomous code and not by joysticks
	 * RUNNING - motor speeds are set
	 */
	enum DriveState{
		STANDBY,
		AUTO,
		RUNNING;
	}


	/**
	 * runs drives code based on state
	 * When standby or auto - do nothing
	 * When Running - sets motor speeds based on joystick values
	 */
	@Override
	public void execute() {
		switch(state) {
		case AUTO:
		case STANDBY:  break;
		case RUNNING:
			rightDrives.set(speedRight - speedRight*speedRightOffset);
			leftDrives.set(speedLeft - speedLeft*speedLeftOffset);
			if(speedRight == speedLeft)
				straighten();
			break;
		}
	}

	/**
	 * changes drives state
	 * @param st - the state to switch to
	 */
	public void changeState(DriveState st) {
		if(state == DriveState.AUTO && st == DriveState.RUNNING)
			stop();
		state = st;
		if(state == DriveState.RUNNING) {
//			rightPID.enable();
//			leftPID.enable();
//		}
//		else {
//			rightPID.disable();
//			leftPID.disable();
		}
	}

	/**
	 * moves robot according to joystick values, -100 to 100
	 * @param speedR - the right joystick speed
	 * @param speedL - the left joystick speed
	 */
	public void joysticks(double speedR, double speedL) {
		speedRight = speedR/100;
		speedLeft = speedL/100;	
	}

	/**
	 * turns the robot the specified degrees
	 * @param degree - the degree amount 
	 * @param speed - the speed amount
	 */
	public void turn(int degree, int speed) {
		isMoving = true;
		gyro.zeroYaw();
		if(degree > 0) {
			do {
				rightDrives.set(((double) speed)/100);
				leftDrives.set(-((double) speed)/100);
			}while(degree > gyro.getAngle());
		}else {
			do {
				leftDrives.set(((double) speed)/100);
				rightDrives.set(-((double) speed)/100);
			}while(degree < gyro.getAngle());
		}
		stop();
		isMoving = false;
	}

	/**
	 * moves the robot a specified distance
	 * @param dist - decimal value in feet
	 * @param speed - the speed wanted to move
	 */
	public void move(double dist, int speed) {
		isMoving = true;

		boolean straightened = false;

		leftEnc.reset();
		rightEnc.reset();
		gyro.zeroYaw();
		
		speedRight = speed;
		speedLeft = speed;
		
		if(dist > 0) {
			while((leftEnc.getDistance() + rightEnc.getDistance())/2.0 < dist) {
				if(!straightened)
					straightened = straighten();
				rightDrives.set(speedRight);
				leftDrives.set(speedLeft);
			}
		}else {
			while((leftEnc.getDistance() + rightEnc.getDistance())/2 > dist) {
				if(!straightened)
					straightened = straighten();
				rightDrives.set(-speedRight);
				leftDrives.set(-speedLeft);
			}
		}
		stop();
		isMoving = false;
	}

	/**
	 * makes sure the robot is straight(within -10 to 10)
	 * @return a boolean, true if robot was straightened
	 */
	private boolean straighten() {
		if(gyro.getAngle() > 10) {
			speedLeftOffset = speedLeft - (speedLeft * 0.1);
			leftDrives.set(speedLeft);
			return true;
		}if(gyro.getAngle() < -10) {
			speedRightOffset = .1;
			rightDrives.set(speedRight);
			return true;
		}
		return false;

	}

	/**
	 * stops all motors
	 */
	public void stop() {
		rightDrives.set(0);
		leftDrives.set(0);
	}

	/**
	 * switches between driving and climbing
	 * @param switchingToClimb - true if driving, false if climbing
	 */
	public void PTOSwitch(boolean switchingToClimb) {
		cylinder.set(switchingToClimb);
	}

	/**
	 * returns if the robot is currently moving during autonomous
	 */
	public boolean getIsMoving() {
		return isMoving;
	}

	@Override
	/**
	 * posts all logs
	 */
	public void logger() {
		

	}

	@Override
	/**
	 * debuggs the code to make sure motors are spinning correctly
	 */
	public DebuggerResult[] debug() {
		
		return null;
	}

}
