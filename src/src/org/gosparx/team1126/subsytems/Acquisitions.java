package src.org.gosparx.team1126.subsytems;
import java.lang.Thread.State;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import src.org.gosparx.team1126.util.DebuggerResult;
public class Acquisitions extends GenericSubsytem{

	//Objects
	private WPI_TalonSRX leftIntake;
	
	private WPI_TalonSRX rightIntake;
	
	private Solenoid pincher;
	
	private Solenoid raise;
	
	
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
	
	
	
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
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
