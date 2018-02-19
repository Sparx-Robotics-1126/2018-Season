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

	private AHRS gyro;

	private MotorGroup leftDrives;

	private MotorGroup rightDrives;

	private Solenoid drivesPTO;

	//-------------------------------------------------------Constants------------------------------------------------------------

	private final double EVERYTHING = .85;			//What part of the way to destination in auto we start moving at a slow speed (move)

	private final double DIZZY_SPINNER = .3;		//What decimal part of the way through a turn we start moving at slow speed (turn)

	private final double SCHOOL_WIFI = .25;			//The speed we move at in auto when almost at destination to achieve higher accuracy (turn+move)

	private final double TURN_SPEED = .40;			//The speed we want when turning after we went the DIZZY_SPINNER

	private final int DEADBAND_TELL_NO_TALES = 5;	//The deadband inside which a turn will stop, so robot doesn't over-turn (was 12)

	private final double KEVIN = .8;				//Sets the over-performing motor in auto to this percentage of its speed until within allowable error

	private final double UNFORTUNATE_FEW = .1;		//Degrees robot can be off in move auto before straightening

	private final double DEADLOCK = 0.35;			//The minimum speed the robot will ever move

	private final double DIST1 = 0.15;				//The distance where it changed to autonomous speed

	private final double DIST2 = 0.8;				//The distance where it changes to rampdown

	private final double DIST3 = 1;					//The distance where it changes to stop

	//-------------------------------------------------------Variables------------------------------------------------------------

	private boolean isMoving;

	private double speedRight;

	private double speedLeft;

	private DriveState state;

	private double turnSpeed;

	private int turnAngle;

	private double moveDist;

	private double moveSpeed;

	private boolean slow;

	private double lastAngle;

	private boolean notRightYet; //for climb init
	
	private boolean notLeftYet; //for climb init
	
	private double highestAmp;
	
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
		leftEnc = new EncoderData(rawLeftEnc, -0.033860431);
		rightEnc = new EncoderData(rawRightEnc, 0.033860431);
		gyro = new AHRS(SerialPort.Port.kUSB);
		drivesPTO = new Solenoid(IO.PTO_PNU);
		isMoving = false;
		speedRight = 0;
		speedLeft = 0;
		moveSpeed = 0;
		rightDrives = new MotorGroup(rightMtr1, rightMtr2, rightMtr3);
		leftDrives = new MotorGroup(leftMtr1, leftMtr2, leftMtr3);
		rightDrives.setInverted(true);
		rightDrives.setNeutralMode(NeutralMode.Brake);
		leftDrives.setNeutralMode(NeutralMode.Brake);
		changeState(DriveState.STANDBY);
		slow = false;
		notLeftYet = true;
		notRightYet = true;
		lastAngle = 0;
		highestAmp = 0;
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
		CLIMB_INIT,
		CLIMB,
		MOVE_FWRD,
		MOVE_BKWD,
		TURN_R,
		TURN_L;
	}

	/**
	 * Adds all the sendable objects to shuffleboard
	 */
	public void addObjectsToShuffleboard() {
		SmartDashboard.putData("Drives PTO", drivesPTO);
		SmartDashboard.putData("LeftEnc", rawLeftEnc);
		SmartDashboard.putData("RightEnc", rawRightEnc);
		SmartDashboard.putData("Gyro", gyro);
		SmartDashboard.putData("Right Drives", rightDrives);
		SmartDashboard.putData("Left Drives", leftDrives);
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
			//			leftEnc.calculateSpeed();
			//			rightEnc.calculateSpeed();
			//			print("Left Distance: " + leftEnc.getDistance() + " Right Distance: " + rightEnc.getDistance());
			//			print("Gyro: " + getAngle());
			break;
		case CLIMB_INIT:
			boolean right = isTaught(rightDrives);
			boolean left = isTaught(leftDrives);
			if(left && right) {
				System.out.print(highestAmp);
				rightEnc.reset();
				leftEnc.reset();
				leftDrives.set(0);
				rightDrives.set(0);
				changeState(DriveState.CLIMB);
			}
			if(!right && notRightYet){
				rightDrives.set(-.2);
			}else {
				rightDrives.set(0);
				notRightYet = false;
			}
			if(!left && notLeftYet){
				leftDrives.set(-.2);
			}else {
				leftDrives.set(0);
				notRightYet = false;
			}
			System.out.println("Right side: " + (right && !notRightYet));
			System.out.println("Left side: " + (left && !notLeftYet));
			System.out.println(highestAmp);
			break;
		case CLIMB:		//using only right joystick
			if(rightEnc.getDistance() - leftEnc.getDistance() < 3) {
				rightDrives.set(speedRight * .7);
				leftDrives.set(speedRight);
			} else if(rightEnc.getDistance() - leftEnc.getDistance() > 3) {
				rightDrives.set(speedRight);
				leftDrives.set(speedRight * .7);	
			}else {
				rightDrives.set(speedRight);
				leftDrives.set(speedLeft);
			}
			break;
		case TURN_R:
			print("Gyro Angle: " + getAngle());
			if(getAngle() > turnAngle - DEADBAND_TELL_NO_TALES) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
				System.out.println("Turn left finished");
			}else if(getAngle() > turnAngle * DIZZY_SPINNER){
				turnSpeed = TURN_SPEED;
				slow = true;
				leftDrives.set(-turnSpeed);
				rightDrives.set(turnSpeed);
			}else {
				leftDrives.set(-turnSpeed);
				rightDrives.set(turnSpeed);
			}
			break;
		case TURN_L:
			print("Gyro Angle: " + getAngle());
			if(getAngle() < turnAngle + DEADBAND_TELL_NO_TALES) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else if(getAngle() < turnAngle * DIZZY_SPINNER) {
				turnSpeed = TURN_SPEED;
				slow = true;
				leftDrives.set(turnSpeed);
				rightDrives.set(-turnSpeed);
			}else {
				leftDrives.set(turnSpeed);
				rightDrives.set(-turnSpeed);
			}
			break;
		case MOVE_FWRD:
			double currentDistance = distance();
			if (currentDistance > DIST3 * moveDist) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			} else if (DIST1 * moveDist > currentDistance) { //ramp up
				//				System.out.println("D1111111111111111");
				speedLeft = rampUp();
				speedRight = rampUp();
				straightenForward();
				leftDrives.set(speedLeft);
				rightDrives.set(speedRight);
			} else if (DIST2 * moveDist > currentDistance) {  //hold speed
				//				System.out.println("D22222222222222222222222222222");
				speedRight = moveSpeed;
				speedLeft = moveSpeed;
				straightenForward();
				leftDrives.set(speedLeft);
				rightDrives.set(speedRight);
			} else if (DIST3 * moveDist > currentDistance) {  //ramp down
				//				System.out.println("D3333333333333333333333333");
				speedLeft = rampDown();
				speedRight = rampDown();
				straightenForward();
				leftDrives.set(speedLeft);
				rightDrives.set(speedRight);
				slow = true;
			}
			//			print("Right speed: " + speedRight + " Left speed: " + speedLeft);
			print("Left Distance: " + leftEnc.getDistance() + " Right Distance: " + rightEnc.getDistance() + " Gyro: " + getAngle());
			break;
		case MOVE_BKWD:
			leftEnc.calculateSpeed();
			rightEnc.calculateSpeed();
			if(moveDist > (rightEnc.getDistance() + leftEnc.getDistance())/2) {
				stopMotors();
				changeState(DriveState.STANDBY);
				isMoving = false;
			}else if((moveDist*EVERYTHING) > (rightEnc.getDistance() + leftEnc.getDistance())/2){
				moveSpeed = SCHOOL_WIFI;
				slow = true;
				speedRight = moveSpeed;
				speedLeft = moveSpeed;
				straightenBackward();
				rightDrives.set(-speedRight);
				leftDrives.set(-speedLeft);
			}else {
				straightenBackward();
				leftDrives.set(-speedLeft);
				rightDrives.set(-speedRight);
			}
			//print("Left Distance: " + leftEnc.getDistance() + " Right Distance: " + rightEnc.getDistance());
			//print("Speed left: " + speedLeft + " Speed right: " + speedRight);
			break;
		}
	}

	/**
	 * Changes state to teleop
	 */
	public void toTeleop() {
		changeState(DriveState.TELEOP);
		leftDrives.setNeutralMode(NeutralMode.Coast);
		rightDrives.setNeutralMode(NeutralMode.Coast);
	}
	
	/**
	 * Changes state to auto
	 */
	public void toAuto() {
		changeState(DriveState.STANDBY);
		leftDrives.setNeutralMode(NeutralMode.Brake);
		rightDrives.setNeutralMode(NeutralMode.Brake);
	}

	/**
	 * changes drives state
	 * @param st - the state to switch to
	 */
	private void changeState(DriveState st) {
		state = st;
	}

	/**
	 * Disables or enables PTO that controls drives
	 * @param disabing - true if disabling drives, false if enabling
	 */
	private void enablePTO(boolean disabling) {
		drivesPTO.set(disabling);
	}

	/**
	 * Checks if robot side is almost off ground 
	 * @param side - the side we're checking
	 * @return true if side is almost off ground
	 */
	private boolean isTaught(MotorGroup side) {
		double motor1Amp = ((WPI_TalonSRX)side.getSpeedController(0)).getOutputCurrent();
		double motor2Amp = ((WPI_TalonSRX)side.getSpeedController(1)).getOutputCurrent();
		if(motor1Amp > highestAmp)
			highestAmp = motor1Amp;
		if(motor2Amp > highestAmp)
			highestAmp = motor2Amp;
		if(motor1Amp > 2.5 || motor2Amp > 2.5)
			return true;
		return false;
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
		resetGyroAngle();
		slow = false;
		System.out.println("Gyro angle after zeroYaw: " + getAngle());
		turnAngle = degree;
		turnSpeed = speed/100.;
		isMoving = true;
		if(degree > 0) {
			changeState(DriveState.TURN_R);
		}else
			changeState(DriveState.TURN_L);
	}

	/**
	 * Climbs from floor to top
	 * @param climb: True if want to climb, false if getting out of climb 
	 */
	public void enableClimb(boolean climb){
		if(climb) {
			enablePTO(true);
			changeState(DriveState.CLIMB_INIT);
		}else {
			enablePTO(false);
			changeState(DriveState.TELEOP);
		}
	}

	/**
	 * sets the parameters to variables and changes the state for moving
	 * @param dist - value in inches
	 * @param speed - the speed amount, -100 to 100
	 */
	public void move(int dist, int speed) {
		resetGyroAngle();
		slow = false;
		rightEnc.reset();
		leftEnc.reset();
		moveDist = dist;
		moveSpeed = speed/100.;
		speedLeft = moveSpeed;
		speedRight = moveSpeed;
		isMoving = true;
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
		if(getAngle() > UNFORTUNATE_FEW) {
			speedRight *= KEVIN;
			return true;
		}else if(getAngle() < -UNFORTUNATE_FEW) {
			speedLeft *= KEVIN;
			return true;
		}
		return false;

	}

	/**
	 * makes sure the robot is straight
	 * @return a boolean, true if robot was straightened
	 */
	private boolean straightenBackward() {
		if(getAngle() > UNFORTUNATE_FEW) {
			speedLeft = moveSpeed * KEVIN;
			speedRight = moveSpeed;
			return true;
		}else if(getAngle() < -UNFORTUNATE_FEW) {
			speedRight = moveSpeed * KEVIN;
			speedLeft = moveSpeed;
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
	 * returns if drives is in it's slow state
	 * @return - a boolean
	 */
	public boolean driveSlow() {
		return slow || isDone();
	}

	/**
	 * ramps up the motors
	 * @return - the motor speedf
	 */
	public double rampUp() {
		double rampUp = (moveSpeed - DEADLOCK)/(DIST1 * moveDist);
		return (rampUp * distance()) + DEADLOCK;
	}

	/**
	 * ramps the motors down
	 * @return - the motor speed
	 */
	public double rampDown() {
		double rampDown = (DEADLOCK - moveSpeed)/(moveDist-DIST2 * moveDist);
		return (rampDown * (distance() - DIST2*moveDist)) + moveSpeed;
	}

	/**
	 * gets the current distance
	 * @return - the average distance
	 */
	public double distance() {
		rightEnc.calculateSpeed();
		leftEnc.calculateSpeed();
		return (rightEnc.getDistance() + leftEnc.getDistance())/2;
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

	private double getAngle() {
		return gyro.getAngle() - lastAngle;
	}

	private void resetGyroAngle() {
		lastAngle = gyro.getAngle();
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