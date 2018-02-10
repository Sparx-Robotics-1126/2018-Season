package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.gosparx.team1126.subsytems.Drives;

public class Autonomous implements Controls {

	private Drives drives;
	
	private boolean isRightAllySwitch;
	private boolean isRightScale;
	private boolean isRightOpponentSwitch;

	private AutoSelected selectedAuto;

	private int autoStep;

	private SendableChooser<AutoSelected> autoChooser;

	private int[][] currentAuto;

	private final int[][] DEFAULT_AUTO = {
			
	};
	
	private final int[][] CROSS_AUTO_LINE = {
			{stateToInt(AutoState.DRIVES_FORWARD), 24, 30},
			{stateToInt(AutoState.DRIVES_WAIT)}
	};
	
//	private final int[][] CROSS_AUTO_LINE = {
//			{stateToInt(AutoState.DRIVES_FORWARD), 140, 70},
//			{stateToInt(AutoState.DRIVES_WAIT)}
//	};
	
	private final int[][] CUBE_ON_LEFT_SWITCH_FROM_LEFT = {
			{stateToInt(AutoState.DRIVES_FORWARD), 172, 70},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 12, 40},
			{stateToInt(AutoState.DRIVES_WAIT)}
	};
	
	private final int[][] CUBE_ON_RIGHT_SWITCH_FROM_LEFT = {
			{stateToInt(AutoState.DRIVES_FORWARD), 262, 70},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 88, 40},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 35},
			{stateToInt(AutoState.DRIVES_WAIT)}
	};

	private final int[][] CUBE_ON_LEFT_SWITCH_FROM_LEFT_AND_ACQUIRE = {
			{stateToInt(AutoState.DRIVES_FORWARD), 262, 70},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 70, 40},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 35},
			{stateToInt(AutoState.DRIVES_WAIT)}
	};
	
	private final int[][] CUBE_ON_LEFT_SCALE_FROM_LEFT = {
			{stateToInt(AutoState.DRIVES_FORWARD), 392, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 40},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 35, 31},
			{stateToInt(AutoState.DRIVES_WAIT)}
	};
	
	public Autonomous(Drives drives) {
		autoStep = 0;
		isRightAllySwitch = false;
		isRightScale = false;
		isRightOpponentSwitch = false;

		this.drives = drives;
		
		autoChooser = new SendableChooser<AutoSelected>();

		autoChooser.addDefault("Do Nothing", AutoSelected.NOTHING);
		autoChooser.addObject("Cross The Border", AutoSelected.CROSSBORDER);
		autoChooser.addObject("Score The Scale", AutoSelected.SCALE);
		autoChooser.addObject("Score The Switch", AutoSelected.SWITCH);
		autoChooser.addObject("Score The Switch + Acquire", AutoSelected.SWITCHACQ);
		autoChooser.addObject("Score The Switch + Scale", AutoSelected.SWITCHSCALE);

		SmartDashboard.putData(autoChooser);
	}


	public boolean initAuto(){
		if(setFieldConditions()) {
			setAuto();
			return true;
		}else{
			return false;
		}
	}


	//check for rules before competition
	public boolean setFieldConditions() {
		String fieldConditions = DriverStation.getInstance().getGameSpecificMessage();
		SmartDashboard.putString("Game Message", fieldConditions);
		if(!fieldConditions.equals("")) {
			if (fieldConditions.charAt(0) == 'r' || fieldConditions.charAt(0) == 'R') {
				isRightAllySwitch = true;
				isRightOpponentSwitch = true;
			}
			if (fieldConditions.charAt(1) == 'r' || fieldConditions.charAt(1) == 'R') {
				isRightScale = true;
			}
			return true;
		}
		return false;

	}

	@Override
	public void execute() {
		runAuto();
	} 

	private void runAuto() {
		if(currentAuto.length > autoStep) {
			switch(currentAuto[autoStep][0]) {
			case 0: //DRIVES_FORWARD
				drives.move(currentAuto[autoStep][1], currentAuto[autoStep][2]);
				autoStep++;
				break;
			case 1: //DRIVES_BACKWARD
				drives.move(currentAuto[autoStep][1], -currentAuto[autoStep][2]);
				autoStep++;
				break;
			case 2: //DRIVES_TURNLEFT
				drives.turn(-currentAuto[autoStep][1], currentAuto[autoStep][2]);
				autoStep++;
				break;
			case 3: //DRIVES_TURNRIGHT
				drives.turn(currentAuto[autoStep][1], currentAuto[autoStep][2]);
				autoStep++;
				break;
			case 4: //DRIVES_WAIT
				if(drives.isDone()) {
					autoStep++;
				}
				break;
			case 5: //DRIVES_STOP
				drives.stopMotors();
				autoStep++;
				break;
			default:
				break;
			}
		}
	}

	private void setAuto() {
		selectedAuto = autoChooser.getSelected();
		if(isRightAllySwitch) {
			if(isRightScale) {
				switch(selectedAuto) {
				case NOTHING:
					currentAuto = DEFAULT_AUTO;
					break;
				case SCALE:
					currentAuto = DEFAULT_AUTO;
					break;
				case CROSSBORDER:
					currentAuto = CROSS_AUTO_LINE;
					break;
				case SWITCH:
					currentAuto = CUBE_ON_RIGHT_SWITCH_FROM_LEFT;
					break;
				case SWITCHSCALE:
					currentAuto = DEFAULT_AUTO;
					break;
				case SWITCHACQ:
					currentAuto = CUBE_ON_LEFT_SWITCH_FROM_LEFT_AND_ACQUIRE;
					break;
				}
			} else {
				switch(selectedAuto) {
				case NOTHING:
					currentAuto = DEFAULT_AUTO;
					break;
				case SCALE:
					currentAuto = DEFAULT_AUTO;
					break;
				case CROSSBORDER:
					currentAuto = CROSS_AUTO_LINE;
					break;
				case SWITCH:
					currentAuto = CUBE_ON_RIGHT_SWITCH_FROM_LEFT;
					break;
				case SWITCHSCALE:
					currentAuto = DEFAULT_AUTO;
					break;
				case SWITCHACQ:
					currentAuto = CUBE_ON_LEFT_SWITCH_FROM_LEFT_AND_ACQUIRE;
					break;
				}
			}
		} else {
			if(isRightScale) {
				switch(selectedAuto) {
				case NOTHING:
					currentAuto = DEFAULT_AUTO;
					break;
				case SCALE:
					currentAuto = DEFAULT_AUTO;
					break;
				case CROSSBORDER:
					currentAuto = CROSS_AUTO_LINE;
					break;
				case SWITCH:
					currentAuto = CUBE_ON_LEFT_SWITCH_FROM_LEFT;
					break;
				case SWITCHSCALE:
					currentAuto = DEFAULT_AUTO;
					break;
				case SWITCHACQ:
					currentAuto = DEFAULT_AUTO;
					break;
				}
			} else {
				switch(selectedAuto) {
				case NOTHING:
					currentAuto = DEFAULT_AUTO;
					break;
				case SCALE:
					currentAuto = CUBE_ON_LEFT_SCALE_FROM_LEFT;
					break;
				case CROSSBORDER:
					currentAuto = CROSS_AUTO_LINE;
					break;
				case SWITCH:
					currentAuto = CUBE_ON_LEFT_SWITCH_FROM_LEFT;
					break;
				case SWITCHSCALE:
					currentAuto = DEFAULT_AUTO;
					break;
				case SWITCHACQ:
					currentAuto = DEFAULT_AUTO;
					break;
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
			return 0;
		case DRIVES_BACKWARD:
			return 1;
		case DRIVES_TURNLEFT:
			return 2;
		case DRIVES_TURNRIGHT:
			return 3;
		case DRIVES_WAIT:
			return 4;
		case DRIVES_STOP:
			return 5;
		default:
			return -999;
		}
	}

	public enum AutoSelected{

		NOTHING,
		SCALE,
		CROSSBORDER,
		SWITCH,
		SWITCHSCALE,
		SWITCHACQ;

	}

}
