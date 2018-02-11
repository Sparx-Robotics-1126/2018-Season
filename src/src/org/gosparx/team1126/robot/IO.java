package src.org.gosparx.team1126.robot;

public class IO {
	
	public static final int elevationsRight													= 4;
	public static final int elevationsLeft													= 9;
	public static final int magneticSensor													= 14;
	public static final int elevationsEncoder1												= 22;
	public static final int elevationsEncoder2												= 23;	
	public static final int elevationsPneumatic												= 6;

	//CAN
	public static final int CAN_ACQ_RIGHT_INTAKE                  = 7;
	public static final int CAN_ACQ_LEFT_INTAKE					  = 8;
	
	//Solenoids
	public static final int PNU_WRIST                             = 1;
	public static final int PNU_PINCHER                           = 0;

//----------------------------------------------------Motors-----------------------------------------------------------------------

public static final int leftDriveCIM1																				= 10;	//motor 0 top	
public static final int leftDriveCIM2																				= 11;	//motor 1 back
public static final int leftDriveCIM3																				= 12;	//motor 2 front
public static final int rightDriveCIM1																				= 1;	//motor 3 top
public static final int rightDriveCIM2																				= 2;	//motor 4 back
public static final int rightDriveCIM3																				= 3;	//motor 5 front

//----------------------------------------------------Sensors----------------------------------------------------------------------

public static final int leftDriveEncoderChannel1																	= 12;
public static final int leftDriveEncoderChannel2																	= 13;
public static final int rightDriveEncoderChannel1																	= 10;
public static final int rightDriveEncoderChannel2																	= 11;

//--------------------------------------------------Pneumatics--------------------------------------------------------------------=

public static final int ptoSwitch																					= 7;


}