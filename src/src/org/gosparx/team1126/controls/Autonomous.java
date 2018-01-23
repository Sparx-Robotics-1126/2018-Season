package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Autonomous extends Controls {

	private boolean isRightAllySwitch;
	private boolean isRightScale;
	private boolean isRightOpponentSwitch;
	
	private AutoSelected selectedAuto;
	
	private boolean firstRun;
	
	private SendableChooser<AutoSelected> autoChooser;
	
	//private int selectedAuto;
	//used to hold a reference to a sendableChooser Object
	
	private int[][] currentAuto;
	
	private final int[][] TESTAUTO = {
		{stateToInt(AutoState.DRIVES_TURNLEFT), 45, 10},
		{stateToInt(AutoState.DRIVES_FORWARD), 10, 10},
		{stateToInt(AutoState.DRIVES_TURNRIGHT), 45, 10},
		{stateToInt(AutoState.DRIVES_FORWARD), 10, 10}
	};
	
	public Autonomous() {
		
		firstRun = false;
		
		isRightAllySwitch = false;
		isRightScale = false;
		isRightOpponentSwitch = false;
		
		autoChooser = new SendableChooser<AutoSelected>();
		//test
		autoChooser.addDefault("Do Nothing", selectedAuto = AutoSelected.NOTHING);
		autoChooser.addObject("Cross The Border", selectedAuto = AutoSelected.CROSSBORDER);
		autoChooser.addObject("Score The Scale", selectedAuto = AutoSelected.SCALE);
		autoChooser.addObject("Score The Switch", selectedAuto = AutoSelected.SWITCH);
		//Use these to create the default and buttons that are to be selected
		
		

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
		if(isAutonomous() && isEnabled()) {
			if(!firstRun) {
				firstRun = true;
				switch(selectedAuto) {
				case NOTHING:
				case SCALE:
				case CROSSBORDER:
				case SWITCH:
				}
			}
			
			
		} else {
			
		}
		
		
	}
	
	public enum AutoState{
		
		DRIVES_FORWARD, //@param - distance, speed
		DRIVES_BACKWARD, //@param - distance, speed
		DRIVES_TURNLEFT, //@param - degrees to turn, speed
		DRIVES_TURNRIGHT, //@param - degrees to turn, speed
		DRIVES_WAIT;
		
	}
	
	public int stateToInt(AutoState auto) {
		switch(auto) {
		case DRIVES_FORWARD:
			return 1;
		case DRIVES_BACKWARD:
			return 2;
		case DRIVES_TURNLEFT:
			return 3;
		case DRIVES_TURNRIGHT:
			return 4;
		case DRIVES_WAIT:
			return 5;
		default:
			return -999;
		}
	}
	
	public enum AutoSelected{
		
		NOTHING,
		SCALE,
		CROSSBORDER,
		SWITCH;
		
	}
	
	
}
