package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Autonomous extends Controls {

	private boolean isRightAllySwitch;
	private boolean isRightScale;
	private boolean isRightOpponentSwitch;
	
	SendableChooser autoChooser;
	
	private int selectedAuto;
	//used to hold a reference to a sendableChooser Object
	
	public Autonomous() {
		
		isRightAllySwitch = false;
		isRightScale = false;
		isRightOpponentSwitch = false;
		
		autoChooser = new SendableChooser();
		autoChooser.addDefault("Do Nothing", new Integer(0));
		autoChooser.addObject("Example1", new Integer(1));
		//Use these to create the default and buttons that are to be selected
		selectedAuto = ((Integer) autoChooser.getSelected()).intValue();
		

		//DriverStation.getInstance().getGameSpecificMessage(); //-> 3 character string
	}
	
	public enum Locations {
		//location on the board where the robot can go, NOT where it is
		
		STARTINGPOSITIONLEFT, STARTINGPOSITIONMIDDLE, STARTINGPOSITIONRIGHT,
		NULLLEFT, NULLRIGHT,
		PASTLINELEFTCLOSE, PASTLINELEFTFAR,
		PASTLINERIGHTCLOSE, PASTLINERIGHTFAR,
		PLATFORMZONEOFFRAMP, PLATFORMZONEONRAMP
		
		
	}

	//check for rules before competition
	public boolean setFieldConditions() {
		String fieldConditions = DriverStation.getInstance().getGameSpecificMessage();
		if(!fieldConditions.equals("")) {
			if (fieldConditions.charAt(0) == 'r') {
				isRightAllySwitch = true;
				isRightOpponentSwitch = true;
			}
			if (fieldConditions.charAt(1) == 'r') {
				isRightScale = true;
			}
			return true;
		}
		return false;

	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		
		if(isAutonomous() && isEnabled()) {
			
			
			
		} else {
			selectedAuto = ((Integer) autoChooser.getSelected()).intValue();
		}
		
		
	}
	
}
