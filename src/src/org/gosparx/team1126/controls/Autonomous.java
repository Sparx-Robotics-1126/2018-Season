 package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.gosparx.team1126.controls.Automation.AutoState;

public class Autonomous implements Controls {
	
	private Automation automation;
	
	private boolean isRightAllySwitch;
	private boolean isRightScale;
	private boolean isRightOpponentSwitch;
		
	private AutoSelected selectedAuto;
	private PositionSelected selectedPosition;
	
	private boolean firstRun;

	private SendableChooser<AutoSelected> autoChooser;
	private SendableChooser<PositionSelected> posChooser;

	private int[][] currentAuto;
	
	
	private final int[][] DEFAULT_AUTO = {
			
	};
	
	private final int[][] CROSS_AUTO_LINE = {
			{AutoState.DRIVES_FORWARD.toInt(), 128, 40},
			{AutoState.DRIVES_WAIT.toInt()}
	};
	
	private final int[][] CUBE_ON_LEFT_SWITCH_FROM_LEFT = {
//			{AutoState.BGR_TIMER.toInt(), 12, 9},
			{AutoState.DRIVES_FORWARD.toInt(), 158, 65},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_SWITCH.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 90, 55},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.DRIVES_TIMED.toInt(), 1000, 45},	//{AutoState.DRIVES_FORWARD.toInt(), 17, 40},
			{AutoState.DRIVES_WAIT.toInt()},			
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_HOME.toInt()},
			{AutoState.DRIVES_BACKWARD.toInt(), 18, 50},
			{AutoState.ELE_FLOOR.toInt()},
			{AutoState.ELE_DONE.toInt()},
	};
	
	private final int[][] CUBE_ON_LEFT_SCALE_FROM_LEFT = {
			{AutoState.DRIVES_FORWARD.toInt(), 280, 95},
			{AutoState.DRIVES_SLOW.toInt()},
			{AutoState.ELE_SCALE.toInt()},
			{AutoState.ACQ_RAISE.toInt()},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_STOP.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 44, 50},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ACQ_DONE.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_HOME.toInt()},
			{AutoState.ACQ_DONE.toInt()},
			{AutoState.DRIVES_BACKWARD.toInt(), 18, 50},
			{AutoState.ELE_FLOOR.toInt()},
	};
	
	private final int[][] CUBE_ON_LEFT_SCALE_FROM_LEFT_AND_SWITCH = {
			{AutoState.DRIVES_FORWARD.toInt(), 158, 90},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 17, 40},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 120, 80},
			{AutoState.ELE_SCALE.toInt()},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_HOME.toInt()},
			{AutoState.ELE_FLOOR.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 145, 65},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ACQ_ACQUIRE.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 46, 50}, //{AutoState.DRIVES_FORWARD.toInt(), 52, 60},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ACQ_RAISE.toInt()},
			{AutoState.ELE_SWITCH.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 3, 31},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()}
	};
	
	private final int[][] CUBE_ON_LEFT_SCALE_FROM_LEFT_AND_SCALE = {
			{AutoState.DRIVES_FORWARD.toInt(), 158, 95},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_SCALE.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 13, 40},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 98, 70}, //110
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.DRIVES_SLOW.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_HOME.toInt()},
			{AutoState.ACQ_DONE.toInt()},
			{AutoState.DRIVES_BACKWARD.toInt(), 6, 50},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 114, 65},
			{AutoState.DRIVES_SLOW.toInt()},
			{AutoState.ELE_FLOOR.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ACQ_ACQUIRE.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 45, 50}, 
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ACQ_RAISE.toInt()},
			{AutoState.ACQ_DONE.toInt()},
			{AutoState.ELE_SCALE.toInt()},
			{AutoState.DRIVES_TURNLEFT.toInt(), 170, 95},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 40, 65},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_HOME.toInt()},
			{AutoState.ACQ_DONE.toInt()},			
			{AutoState.ELE_FLOOR.toInt()},
			{AutoState.DRIVES_BACKWARD.toInt(), 30, 50},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 85, 60},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 70, 50},
			{AutoState.DRIVES_WAIT.toInt()}
	};
	
	private final int[][] CUBE_ON_RIGHT_SCALE_FROM_LEFT_AND_SCALE = {
			{AutoState.DRIVES_FORWARD.toInt(), 226, 90},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 84, 70},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 182, 90}, //dist = 194
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_SCALE.toInt()},
			{AutoState.DRIVES_TURNLEFT.toInt(), 95, 70},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 30, 50}, //44
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.DRIVES_BACKWARD.toInt(), 12, 50},
			{AutoState.ACQ_HOME.toInt()},		
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_TURNLEFT.toInt(), 170, 95},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_FLOOR.toInt()},
			{AutoState.ACQ_ACQUIRE.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.DRIVES_TIMED.toInt(), 500, 40}, 
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ACQ_RAISE.toInt()},
			{AutoState.ACQ_DONE.toInt()},
			{AutoState.ELE_SCALE.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 170, 95},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 40, 65},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_HOME.toInt()},
			{AutoState.ACQ_DONE.toInt()},			
			{AutoState.ELE_FLOOR.toInt()},
			{AutoState.DRIVES_BACKWARD.toInt(), 30, 50},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_TURNLEFT.toInt(), 100, 95}
	};
	
	private final int[][] CUBE_ON_RIGHT_SWITCH_FROM_LEFT = {
			{AutoState.DRIVES_FORWARD.toInt(), 226, 90},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 84, 70},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 150, 70}, //180
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_SWITCH.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 90, 75},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_TIMED.toInt(), 1000, 35}, //AutoState.DRIVES_FORWARD.toInt(), 14, 35},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_HOME.toInt()},
			{AutoState.ACQ_DONE.toInt()},
			{AutoState.DRIVES_BACKWARD.toInt(), 18, 50},
			{AutoState.ELE_FLOOR.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.ACQ_ACQUIRE.toInt()}, 
			{AutoState.DRIVES_TIMED.toInt(), 1000, 35},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ACQ_RAISE.toInt()}
	};
	
	private final int[][] CUBE_ON_RIGHT_SCALE_FROM_LEFT = {
			{AutoState.DRIVES_FORWARD.toInt(), 226, 70},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 90, 70},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 194, 70},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_SCALE.toInt()},
			{AutoState.DRIVES_TURNLEFT.toInt(), 90, 50},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 44, 35},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_HOME.toInt()}
	};
	
	private final int[][] CUBE_ON_RIGHT_SWITCH_FROM_RIGHT = {
			{AutoState.BGR_TIMER.toInt(), 12, 9},
			{AutoState.DRIVES_FORWARD.toInt(), 146, 65},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_SWITCH.toInt()},
			{AutoState.DRIVES_TURNLEFT.toInt(), 90, 55},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_HOME.toInt()},
			{AutoState.ACQ_DONE.toInt()},
			{AutoState.DRIVES_BACKWARD.toInt(), 18, 50},
			{AutoState.ELE_FLOOR.toInt()}
		};
	
	private final int[][] CUBE_ON_RIGHT_SCALE_FROM_RIGHT = {
			{AutoState.DRIVES_FORWARD.toInt(), 280, 95},
			{AutoState.DRIVES_SLOW.toInt()},
			{AutoState.ELE_SCALE.toInt()},
			{AutoState.ACQ_RAISE.toInt()},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_STOP.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.DRIVES_TURNLEFT.toInt(), 43, 50},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 5, 45},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ACQ_DONE.toInt()},
			{AutoState.ACQ_LOWER.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_SLOW_SPIT.toInt()},
			{AutoState.TIMER.toInt(), 500},
			{AutoState.ACQ_HOME.toInt()},
			{AutoState.DRIVES_BACKWARD.toInt(), 18, 50},
			{AutoState.ELE_FLOOR.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.DRIVES_WAIT.toInt()}
		};
	
	private final int[][] CUBE_ON_LEFT_SWITCH_FROM_MIDDLE = {
			{AutoState.DRIVES_FORWARD.toInt(), 50, 50},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_TURNLEFT.toInt(), 15, 60},
			{AutoState.DRIVES_SLOW.toInt()},
			{AutoState.ELE_SWITCH.toInt()},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 92, 50},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.ACQ_SCORE.toInt()},
			{AutoState.DRIVES_TURNRIGHT.toInt(), 105, 50},
			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.DRIVES_FORWARD.toInt(), 15, 50},
			{AutoState.ACQ_DONE.toInt()},
			{AutoState.DRIVES_BACKWARD.toInt(), 18, 50},
			{AutoState.ELE_FLOOR.toInt()}
	};

	public Autonomous(Automation automation) {
		isRightAllySwitch = false;
		isRightScale = false;
		isRightOpponentSwitch = false;
		
		this.automation = automation;

		firstRun = false;
		
		autoChooser = new SendableChooser<AutoSelected>();

		autoChooser.addDefault("Do Nothing", AutoSelected.NOTHING);
		autoChooser.addObject("Cross The Border", AutoSelected.CROSSBORDER);
		autoChooser.addObject("Score The Switch", AutoSelected.SWITCH);
		autoChooser.addObject("Score The Scale", AutoSelected.SCALE);
		autoChooser.addObject("Score The Scale + Switch", AutoSelected.SWITCHSCALE);
		autoChooser.addObject("Score The Double Scale", AutoSelected.DOUBLESCALE);
		
		posChooser = new SendableChooser<PositionSelected>();
		
		posChooser.addDefault("Start from Left", PositionSelected.LEFT);
		posChooser.addDefault("Start from Middle", PositionSelected.MIDDLE);
		posChooser.addDefault("Start from Right", PositionSelected.RIGHT);
		
		SmartDashboard.putData("AutoChooser", autoChooser);
		SmartDashboard.putData("PosChooser", posChooser);
	}


	public boolean initAuto(){
		if(setFieldConditions()) {
			setAuto();
			System.out.println("Selected auto is " + selectedAuto.name() + " at " + selectedPosition.name());
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
		//System.out.println("t");
		if(!firstRun && setFieldConditions()) {
			setAuto();
			System.out.println("Selected auto is " + selectedAuto.name() + " at " + selectedPosition.name());
			firstRun = true;
			automation.setAuto(currentAuto);
		} else {
			automation.execute();
		}
	} 

	
	private void setAuto() {
		selectedAuto = autoChooser.getSelected();
		selectedPosition = posChooser.getSelected();
		if(selectedAuto == AutoSelected.NOTHING) {
			currentAuto = DEFAULT_AUTO;
			return;
		}
		if(selectedAuto == AutoSelected.CROSSBORDER) {
			currentAuto = CROSS_AUTO_LINE;
			return;
		}
		switch(selectedPosition) {
		case LEFT:
			switch(selectedAuto) {
			case SCALE:
				currentAuto = isRightScale ? CUBE_ON_RIGHT_SCALE_FROM_LEFT : CUBE_ON_LEFT_SCALE_FROM_LEFT;
				return;
			case SWITCH:
				currentAuto = isRightAllySwitch ? CUBE_ON_RIGHT_SWITCH_FROM_LEFT : CUBE_ON_LEFT_SWITCH_FROM_LEFT;
				return;
			case SWITCHSCALE:
				if(isRightScale) {
					currentAuto = CUBE_ON_RIGHT_SCALE_FROM_LEFT;
					return;
				}
				if(isRightAllySwitch) {
					currentAuto = CUBE_ON_LEFT_SCALE_FROM_LEFT;
				} else {
					currentAuto = CUBE_ON_LEFT_SCALE_FROM_LEFT_AND_SWITCH;
				}
				return;
			case DOUBLESCALE:
				if(isRightScale) {
					currentAuto = CUBE_ON_RIGHT_SCALE_FROM_LEFT_AND_SCALE;
				} else {
					currentAuto = CUBE_ON_LEFT_SCALE_FROM_LEFT_AND_SCALE;
				}
				break;
			default:
				System.out.println("Invalid auto; tried: " + selectedAuto.name() + " at " + selectedPosition.name());
				break;
			}
		case RIGHT: 
			if(selectedAuto != AutoSelected.SCALE && selectedAuto != AutoSelected.SWITCH) {
				System.out.println("Invalid auto; tried: " + selectedAuto.name() + " at " + selectedPosition.name());
				return;
			}
			if(selectedAuto == AutoSelected.SCALE && isRightScale) {
				currentAuto = CUBE_ON_RIGHT_SCALE_FROM_RIGHT;
				return;
			}
			if(isRightAllySwitch) {
				currentAuto = CUBE_ON_RIGHT_SWITCH_FROM_RIGHT;
				return;
			}
			currentAuto = CROSS_AUTO_LINE;
			break;
		case MIDDLE:
			currentAuto = CUBE_ON_LEFT_SWITCH_FROM_MIDDLE;
		default:
			System.out.println("Invalid auto; tried: " + selectedAuto.name() + " at " + selectedPosition.name());
			break;
		}
	}

	public enum AutoSelected{

		NOTHING,
		SCALE,
		CROSSBORDER,
		SWITCH,
		SWITCHSCALE,
		DOUBLESCALE;

	}
	
	public enum PositionSelected{
		
		LEFT,
		RIGHT,
		MIDDLE;
		
	}
	
	

}