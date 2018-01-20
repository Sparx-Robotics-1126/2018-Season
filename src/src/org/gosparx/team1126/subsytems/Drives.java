package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
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
	}
	
	@Override
	public void execute() {
		
		
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
