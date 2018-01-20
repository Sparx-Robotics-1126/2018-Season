package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Autonomous extends Controls {

	private boolean isRightAllySwitch;
	private boolean isRightScale;
	private boolean isRightOppenentSwitch;
	
	SendableChooser autoChooser;
	//used to hold a reference to a sendableChooser Object
	
	public Autonomous() {
		
		autoChooser = new SendableChooser();
		autoChooser.addDefault("Do Nothing", new Integer(0));
		autoChooser.addDefault("Example1", new Integer(1));
		//Use these to create the default and buttons that are to be selected
		
		//DriverStation.getInstance().getGameSpecificMessage(); //-> 3 character string
	}
	
}
