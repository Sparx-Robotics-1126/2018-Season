package src.org.gosparx.team1126.subsytems;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import src.org.gosparx.team1126.robot.IO;
import src.org.gosparx.team1126.util.DebuggerResult;


/**
 * This class allows for the control of the acquisitions subsystem.
 * @author Aidan Cheeseman (cheesemanaidan@gmail.com)
 * @author Andrew Thompson (andrewt015@gmail.com)
 */
public class Acquisitions extends GenericSubsytem{

	//Objects
	private WPI_TalonSRX leftIntake;
	
	private WPI_TalonSRX rightIntake;
	
	private Solenoid pincher;
	
	private Solenoid wrist;
	
	
	//Constants
	private static final boolean PINCHED = true;
	
	private static final boolean RELEASED = !PINCHED;
	
	private static final boolean RAISED = false;
	
	private static final boolean LOWERED = !RAISED;
	
	private static final double MOTOR_ON = 0.5;  //TODO get actual power
	
	private static final double MOTOR_STOP = 0.0;
	
	
	//Variables
	private State AcqState;
	
	private double rightMotorPower;
	
	private double leftMotorPower;
	
	private boolean pinchPosition;  //true = pinched 
	
	private boolean wristPosition;  //true = lowered
	
	
	
	public Acquisitions() {
		// TODO Auto-generated constructor stub
	}
	
	
	public enum State{
		STANDBY,
		HOME,
		RAISE,
		LOWER,
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
	}
	
	
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
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
