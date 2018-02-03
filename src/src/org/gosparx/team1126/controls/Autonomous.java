package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous implements Controls {

	private boolean isRightAllySwitch;
	private boolean isRightScale;
	private boolean isRightOpponentSwitch;

	private AutoSelected selectedAuto;

	private int autoStep;

	private SendableChooser<AutoSelected> autoChooser;

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

	private final int[][] TESTAUTO4 = {
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 10},
			{stateToInt(AutoState.DRIVES_TURNLEFT), 45, 10},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 45, 10},
	};

	public Autonomous() {
		autoStep = 0;
		isRightAllySwitch = false;
		isRightScale = false;
		isRightOpponentSwitch = false;

		autoChooser = new SendableChooser<AutoSelected>();

		autoChooser.addDefault("Do Nothing", AutoSelected.NOTHING);
		autoChooser.addObject("Cross The Border", AutoSelected.CROSSBORDER);
		autoChooser.addObject("Score The Scale", AutoSelected.SCALE);
		autoChooser.addObject("Score The Switch", AutoSelected.SWITCH);
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

	private void setAuto() {
		selectedAuto = autoChooser.getSelected();
		if(isRightAllySwitch) {
			if(isRightScale) {
				switch(selectedAuto) {
				case NOTHING:
					//currentAuto = TESTAUTO;
					currentAuto = TESTAUTO;
					break;
				case SCALE:
					currentAuto = TESTAUTO4;
					break;
				case CROSSBORDER:
					currentAuto = TESTAUTO2;
					break;
				case SWITCH:
					currentAuto = TESTAUTO3;
					break;
				}
			} else {
				switch(selectedAuto) {
				case NOTHING:
					currentAuto = TESTAUTO;
					break;
				case SCALE:
					currentAuto = TESTAUTO1;
					break;
				case CROSSBORDER:
					currentAuto = TESTAUTO2;
					break;
				case SWITCH:
					currentAuto = TESTAUTO3;
					break;
				}
			}
		} else {
			if(isRightScale) {
				switch(selectedAuto) {
				case NOTHING:
					currentAuto = TESTAUTO;
					break;
				case SCALE:
					currentAuto = TESTAUTO1;
					break;
				case CROSSBORDER:
					currentAuto = TESTAUTO2;
					break;
				case SWITCH:
					currentAuto = TESTAUTO3;
					break;
				}
			} else {
				switch(selectedAuto) {
				case NOTHING:
					currentAuto = TESTAUTO;
					break;
				case SCALE:
					currentAuto = TESTAUTO1;
					break;
				case CROSSBORDER:
					currentAuto = TESTAUTO2;
					break;
				case SWITCH:
					currentAuto = TESTAUTO3;
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
		SWITCHSCALE;

	}

}
