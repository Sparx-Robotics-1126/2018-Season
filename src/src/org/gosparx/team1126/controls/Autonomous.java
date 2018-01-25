package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Autonomous extends Controls {

	private boolean isRightAllySwitch;
	private boolean isRightScale;
	private boolean isRightOpponentSwitch;
	
	private AutoSelected selectedAuto;
	
	private int autoStep;
	
	private String fieldConditions = DriverStation.getInstance().getGameSpecificMessage();
	
	private boolean firstRun;
	
	private SendableChooser<AutoSelected> autoChooser;
	
	//private int selectedAuto;
	//used to hold a reference to a sendableChooser Object
	
	private int[][] currentAuto;
	
	private final int[][] TESTAUTO = {
		{stateToInt(AutoState.DRIVES_TURNLEFT), 45, 10},
		{stateToInt(AutoState.DRIVES_WAIT)},
		{stateToInt(AutoState.DRIVES_FORWARD), 10, 10},
		{stateToInt(AutoState.DRIVES_WAIT)},
		{stateToInt(AutoState.DRIVES_TURNRIGHT), 45, 10},
		{stateToInt(AutoState.DRIVES_WAIT)},
		{stateToInt(AutoState.DRIVES_FORWARD), 10, 10},
		{stateToInt(AutoState.DRIVES_WAIT)},
	};
	
	private final int[][] TESTAUTO1 = {
			{stateToInt(AutoState.DRIVES_TURNLEFT), 45, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 45, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
		};
	
	private final int[][] TESTAUTO2 = {
			{stateToInt(AutoState.DRIVES_TURNLEFT), 45, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 45, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
		};
	
	private final int[][] TESTAUTO3 = {
			{stateToInt(AutoState.DRIVES_TURNLEFT), 45, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 45, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 10},
			{stateToInt(AutoState.DRIVES_WAIT)},
		};
	
	public Autonomous() {
		
		firstRun = false;
		autoStep = 0;
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
		//kill thread after auto done?
		if(isAutonomous() && isEnabled()) {
			if(!firstRun) {
				if(setFieldConditions()) {
					firstRun = true;
					if(isRightAllySwitch) {
						if(isRightScale) {
							switch(selectedAuto) {
							case NOTHING:
								currentAuto = TESTAUTO;
							case SCALE:
								currentAuto = TESTAUTO1;
							case CROSSBORDER:
								currentAuto = TESTAUTO2;
							case SWITCH:
								currentAuto = TESTAUTO3;
							}
						} else {
							switch(selectedAuto) {
							case NOTHING:
								currentAuto = TESTAUTO;
							case SCALE:
								currentAuto = TESTAUTO1;
							case CROSSBORDER:
								currentAuto = TESTAUTO2;
							case SWITCH:
								currentAuto = TESTAUTO3;
							}
						}
					} else {
						if(isRightScale) {
							switch(selectedAuto) {
							case NOTHING:
								currentAuto = TESTAUTO;
							case SCALE:
								currentAuto = TESTAUTO1;
							case CROSSBORDER:
								currentAuto = TESTAUTO2;
							case SWITCH:
								currentAuto = TESTAUTO3;
							}
						} else {
							switch(selectedAuto) {
							case NOTHING:
								currentAuto = TESTAUTO;
							case SCALE:
								currentAuto = TESTAUTO1;
							case CROSSBORDER:
								currentAuto = TESTAUTO2;
							case SWITCH:
								currentAuto = TESTAUTO3;
							}
						}
					}
				}
			} else {
				if(currentAuto.length > autoStep) {
					switch(currentAuto[autoStep][0]) {
					case 0: //DRIVES_FORWARD
						//code
						autoStep++;
						break;
					case 1: //DRIVES_BACKWARD
						autoStep++;
						break;
					case 2: //DRIVES_TURNLEFT
						autoStep++;
						break;
					case 3: //DRIVES_TURNRIGHT
						autoStep++;
						break;
					case 4: //DRIVES_WAIT
						autoStep++;
						break;
					case 5: //DRIVES_STOP
						autoStep++;
						break;
					default:
						break;
					}
				}
			}
		}
	} 

	public enum AutoState{
		
		DRIVES_FORWARD, //@param - distance, speed
		DRIVES_BACKWARD, //@param - distance, speed
		DRIVES_TURNLEFT, //@param - degrees to turn, speed
		DRIVES_TURNRIGHT, //@param - degrees to turn, speed
		DRIVES_WAIT,
		DRIVES_STOP;
		
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
