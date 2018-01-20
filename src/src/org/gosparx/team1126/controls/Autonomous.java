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
		
		isRightAllySwitch = false;
		isRightScale = false;
		isRightOppenentSwitch = false;
		
		autoChooser = new SendableChooser();
		autoChooser.addDefault("Do Nothing", new Integer(0));
		autoChooser.addObject("Example1", new Integer(1));
		//Use these to create the default and buttons that are to be selected

		//DriverStation.getInstance().getGameSpecificMessage(); //-> 3 character string
	}
	
	public boolean setFieldConditions() {
		String fieldConditions = DriverStation.getInstance().getGameSpecificMessage();
		if(!fieldConditions.equals("")) {
			if (fieldConditions.charAt(0) == 'r') {
				isRightAllySwitch = true;
			}
			if (fieldConditions.charAt(1) == 'r') {
				isRightScale = true;
			}
			if (fieldConditions.charAt(2) == 'r') {
				isRightOppenentSwitch = true;
			}
			return true;
		}
		return false;

	}
	
}
