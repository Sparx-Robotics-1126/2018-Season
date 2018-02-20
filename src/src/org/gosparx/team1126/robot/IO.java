package src.org.gosparx.team1126.robot;

public class IO {
	public static final int PTO_PNU 														= 7; //Drives
	public static final int PTO_ELE 														= 6; //Climbing PTO
	public static final int CLIMBINGLATCH												 	= 2;
	public static final int CLIMBINGARMS 													= 3;
	public static final int ELEVATIONSRIGHT													= 4;
	public static final int ELEVATIONSLEFT													= 9;
	public static final int MAGNETICSENSOR													= 14;
	public static final int ELEVATIONSENCODER1												= 22;
	public static final int ELEVATIONSENCODER2												= 23;	
	

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
//FLIPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP
public static final int leftDriveEncoderChannel1																	= 12; //CHANNELS SWAPPED ON BOTH
public static final int leftDriveEncoderChannel2																	= 13; //CHANGE BACK
public static final int rightDriveEncoderChannel1																	= 10; //CHANGE BACK
public static final int rightDriveEncoderChannel2																	= 11; //CHANGE BACK
//CHANGE BACK
//--------------------------------------------------Pneumatics--------------------------------------------------------------------=

public static final int ptoSwitch																					= 7;


}