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
	}
	
	@Override
	public void execute() {
		
		
	}
	
	/**
	 * turns the robot the specified degrees
	 * @param degree - the degree amount 
	 */
	public void turn(int degree) {
		isMoving = true;
		gyro.reset();
		do {
			
		}while(degree > gyro.getAngle());
		isMoving = false;
	}
	
	/**
	 * moves the robot a specified distance
	 * @param dist - decimal value in feet
	 */
	public void move(double dist) {
		isMoving = true;
		
		isMoving = false;
	}
	
	/**
	 * stops all motors
	 */
	public void stop() {
		
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
