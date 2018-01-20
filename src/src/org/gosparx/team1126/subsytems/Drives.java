package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.Solenoid;
import src.org.gosparx.team1126.util.DebuggerResult;

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

	//-------------------------------------------------------Variables------------------------------------------------------------

	private boolean isMoving;

	// speedRight and speedLeft are magnitudes
	private double speedRight;

	private double speedLeft;

	private DriveState state;
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
		speedRight = 1;
		speedLeft = 1;
	}
	
	/**
	 * contains the possible states of drives
	 * STANDBY - drives is off
	 * RUNNING - motor speeds are set
	 */
	enum DriveState{
		STANDBY,
		RUNNING;
	}

	@Override
	/**
	 * sets motor speeds based on joystick values
	 */
	public void execute() {
		switch(state) {
		case STANDBY:  break;
		case RUNNING:
			setRightMtrs(speedRight);
			setLeftMtrs(speedLeft);
			//to-do: PID loop
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
	 * moves robot according to joystick values
	 */
	public void joysticks(double speedR, double speedL) {
		speedRight = speedR;
		speedLeft = speedL;		
	}

	/**
	 * turns the robot the specified degrees
	 * @param degree - the degree amount 
	 */
	public void turn(int degree) {
		isMoving = true;
		gyro.reset();
		if(degree > 0) {
			do {
				setRightMtrs(1);
				setLeftMtrs(-1);
			}while(degree > gyro.getAngle());
		}else
			do {
				setLeftMtrs(1);
				setRightMtrs(-1);
			}while(degree < gyro.getAngle());
		stop();
		isMoving = false;
	}

	/**
	 * moves the robot a specified distance
	 * @param dist - decimal value in feet
	 */
	public void move(double dist) {
		isMoving = true;
		leftEnc.reset();
		if(dist > 0) {
			while(leftEnc.getDistance() < dist) {
				straighten();
				setRightMtrs(speedRight);
				setLeftMtrs(speedLeft);
			}
		}else {
			while(leftEnc.getDistance() > dist) {
				straighten();
				setRightMtrs(-speedRight);
				setLeftMtrs(-speedLeft);
			}
		}
		stop();
		isMoving = false;
	}

	/**
	 * makes sure the robot is straight(within -10 - 10)
	 */
	private void straighten() {
		if(gyro.getAngle() > 10) {
			speedLeft = speedLeft - (speedLeft * 0.1);
			setLeftMtrs(speedLeft);
		}else if(gyro.getAngle() < -10) {
			speedRight = speedRight - (speedRight *  0.1);
			setRightMtrs(speedRight);
		}

	}

	/**
	 * stops all motors
	 */
	public void stop() {
		setRightMtrs(0);
		setLeftMtrs(0);
	}

	/**
	 * switches between driving and climbing
	 * @param isDrive - true if driving, false if climbing
	 */
	public void PTOSwitch(boolean isDrive) {

	}

	/**
	 * returns if the robot is currently moving
	 */
	public boolean getIsMoving() {
		return isMoving;
	}

	/**
	 * sets all right motors to the same speed
	 * @param val - the specified speed
	 */
	private void setRightMtrs(double val) {
		rightMtr1.set(val);
		rightMtr2.set(val);
		rightMtr3.set(val);
	}

	/**
	 * sets all  left motors to the same speed
	 * @param val - the specified speed
	 */
	private void setLeftMtrs(double val) {
		leftMtr1.set(val);
		leftMtr2.set(val);
		leftMtr3.set(val);
	}

	@Override
	public void logger() {
		// TODO Auto-generated method stub

	}

	@Override
	public DebuggerResult[] debug() {
		// TODO Auto-generated method stub
		return null;
	}

}
