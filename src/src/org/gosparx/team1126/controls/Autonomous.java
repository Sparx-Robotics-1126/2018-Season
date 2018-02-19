 package src.org.gosparx.team1126.controls;

import java.util.Arrays;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.gosparx.team1126.subsytems.Acquisitions;
import src.org.gosparx.team1126.subsytems.Drives;
import src.org.gosparx.team1126.subsytems.Elevations;

public class Autonomous implements Controls {

	private Drives drives;
	private Acquisitions acq;
	private Elevations ele;
	
	private boolean isRightAllySwitch;
	private boolean isRightScale;
	private boolean isRightOpponentSwitch;

	private double startingTime;
	private boolean isBackgroundTimer;
	private int timerStep;
	
	private AutoSelected selectedAuto;
	private PositionSelected selectedPosition;
	
	private boolean firstRun;

	private int autoStep;

	private SendableChooser<AutoSelected> autoChooser;
	private SendableChooser<PositionSelected> posChooser;

	private int[][] currentAuto;

	private final int[][] DEFAULT_AUTO = {
			
	};
	
	private final int[][] CROSS_AUTO_LINE = {
			{stateToInt(AutoState.DRIVES_FORWARD), 128, 40},
			{stateToInt(AutoState.DRIVES_WAIT)}
	};
	
//	private final int[][] CROSS_AUTO_LINE = {
//			{stateToInt(AutoState.DRIVES_FORWARD), 140, 70},
//			{stateToInt(AutoState.DRIVES_WAIT)}
//	};
	
	private final int[][] CUBE_ON_LEFT_SWITCH_FROM_LEFT = {
			{stateToInt(AutoState.BGR_TIMER), 12, 9},
			{stateToInt(AutoState.DRIVES_FORWARD), 158, 65},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_SWITCH)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 55},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.DRIVES_FORWARD), 17, 40},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_SCORE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_HOME)}
	};
	
	private final int[][] CUBE_ON_RIGHT_SWITCH_FROM_RIGHT = {
			{stateToInt(AutoState.BGR_TIMER), 12, 9},
			{stateToInt(AutoState.DRIVES_FORWARD), 146, 65},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_SWITCH)},
			{stateToInt(AutoState.DRIVES_TURNLEFT), 90, 55},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 40},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_SCORE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_HOME)}
	};
	
	private final int[][] CUBE_ON_LEFT_SWITCH_FROM_MIDDLE = {
			{stateToInt(AutoState.DRIVES_FORWARD), 80, 70},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNLEFT), 90, 40},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 80, 70},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_SWITCH)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 12, 40},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_SCORE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_HOME)}
	};
	
	private final int[][] CUBE_ON_RIGHT_SWITCH_FROM_LEFT = {
			{stateToInt(AutoState.DRIVES_FORWARD), 226, 60},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 80, 75},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 180, 40},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_SWITCH)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 75},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 35},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)}
	};
	
	private final int[][] CUBE_ON_LEFT_SWITCH_FROM_RIGHT = {
			{stateToInt(AutoState.DRIVES_FORWARD), 226, 60},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNLEFT), 80, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 180, 40},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_SWITCH)},
			{stateToInt(AutoState.DRIVES_TURNLEFT), 90, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 10, 35},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)}
	};
	
	private final int[][] CUBE_ON_RIGHT_SCALE_FROM_LEFT = {
			{stateToInt(AutoState.DRIVES_FORWARD), 226, 60},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 70},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 206, 55},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_SCALE)},
			{stateToInt(AutoState.DRIVES_TURNLEFT), 90, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 34, 35},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)}
			//{stateToInt(AutoState.ACQ_LAUNCHSCORE)}
	};
	
	private final int[][] CUBE_ON_LEFT_SCALE_FROM_RIGHT = {
			{stateToInt(AutoState.DRIVES_FORWARD), 226, 60},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNLEFT), 90, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 206, 55},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_SCALE)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 90, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 34, 35},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)}
	};

	private final int[][] CUBE_ON_LEFT_SWITCH_FROM_LEFT_AND_ACQUIRE = {
			{stateToInt(AutoState.DRIVES_FORWARD), 12, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 7, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 261, 70},
			{stateToInt(AutoState.ELE_SCALE)},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_HOME)},
			{stateToInt(AutoState.ELE_FLOOR)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 158, 65},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_ACQUIRE)},
			{stateToInt(AutoState.DRIVES_FORWARD), 42, 60},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_RAISE)},
			{stateToInt(AutoState.ACQ_DONE)},
	};
	
	/*private final int[][] CUBE_ON_LEFT_SCALE_FROM_LEFT = {
			{stateToInt(AutoState.DRIVES_FORWARD), 316, 95},
			{stateToInt(AutoState.DRIVES_SLOW)},
			{stateToInt(AutoState.ELE_SCALE)},
			{stateToInt(AutoState.ACQ_RAISE)},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_STOP)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 75, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_HOME)}
	};*/
	
	private final int[][] CUBE_ON_LEFT_SCALE_FROM_LEFT = {
			{stateToInt(AutoState.DRIVES_FORWARD), 280, 95},
			{stateToInt(AutoState.DRIVES_SLOW)},
			{stateToInt(AutoState.ELE_SCALE)},
			{stateToInt(AutoState.ACQ_RAISE)},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_STOP)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 43, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_HOME)}
	};
	
	private final int[][] CUBE_ON_RIGHT_SCALE_FROM_RIGHT = {
			{stateToInt(AutoState.DRIVES_FORWARD), 316, 50}, //95
			{stateToInt(AutoState.DRIVES_SLOW)},
			{stateToInt(AutoState.ELE_SCALE)},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNLEFT), 82, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.DRIVES_FORWARD), 20, 31},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_REGSCORE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_HOME)}
	};
	
	private final int[][] CUBE_ON_LEFT_SCALE_FROM_LEFT_AND_SWITCH = {
			{stateToInt(AutoState.DRIVES_FORWARD), 12, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 7, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 261, 70},
			{stateToInt(AutoState.ELE_SCALE)},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_HOME)},
			{stateToInt(AutoState.ELE_FLOOR)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 158, 65},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_ACQUIRE)},
			{stateToInt(AutoState.DRIVES_FORWARD), 42, 60},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_RAISE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ELE_SWITCH)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)},
			{stateToInt(AutoState.DRIVES_FORWARD), 3, 31}
			/*{stateToInt(AutoState.DRIVES_TURNRIGHT), 82, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_HOME)},
			{stateToInt(AutoState.ELE_FLOOR)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 85, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 92, 60},
			{stateToInt(AutoState.ACQ_ACQUIRE)},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_RAISE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ELE_SWITCH)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)}*/
	};
	
	/*
	 private final int[][] CUBE_ON_LEFT_SCALE_FROM_LEFT_AND_SWITCH = {
			{stateToInt(AutoState.DRIVES_FORWARD), 316, 95},
			{stateToInt(AutoState.DRIVES_SLOW)},
			{stateToInt(AutoState.ELE_SCALE)},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 82, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ACQ_HOME)},
			{stateToInt(AutoState.ELE_FLOOR)},
			{stateToInt(AutoState.DRIVES_TURNRIGHT), 85, 50},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.DRIVES_FORWARD), 92, 60},
			{stateToInt(AutoState.ACQ_ACQUIRE)},
			{stateToInt(AutoState.DRIVES_WAIT)},
			{stateToInt(AutoState.ACQ_RAISE)},
			{stateToInt(AutoState.ACQ_DONE)},
			{stateToInt(AutoState.ELE_SWITCH)},
			{stateToInt(AutoState.ELE_DONE)},
			{stateToInt(AutoState.ACQ_LAUNCHSCORE)}
	};
	 */
	
	public Autonomous(Drives drives, Acquisitions acq, Elevations ele) {
		autoStep = 0;
		isRightAllySwitch = false;
		isRightScale = false;
		isRightOpponentSwitch = false;

		firstRun = false;
		
		this.drives = drives;
		this.acq = acq;
		this.ele = ele;
		
		autoChooser = new SendableChooser<AutoSelected>();

		autoChooser.addDefault("Do Nothing", AutoSelected.NOTHING);
		autoChooser.addObject("Cross The Border", AutoSelected.CROSSBORDER);
		autoChooser.addObject("Score The Scale", AutoSelected.SCALE);
		autoChooser.addObject("Score The Switch", AutoSelected.SWITCH);
		autoChooser.addObject("Score The Switch + Acquire", AutoSelected.SWITCHACQ);
		autoChooser.addObject("Score The Switch + Scale", AutoSelected.SWITCHSCALE);

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
			startingTime = Timer.getFPGATimestamp();
			for(int[] arr: currentAuto) {
				System.out.println(arr);
			}
		} else {
			runAuto();
		}
	} 

	private void runAuto() {
		if(isBackgroundTimer && startingTime + currentAuto[timerStep][1] < Timer.getFPGATimestamp()) {
			autoStep = currentAuto[timerStep][2];
			isBackgroundTimer = false;
		}
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
					System.out.println("DRIVES COMPLETED");
				}
				break;
			case 5: //DRIVES_STOP
				drives.stopMotors();
				autoStep++;
				break;
			case 6: //ACQ_ACQUIRE
				acq.setAcquire();
				autoStep++;
				break;
			case 7: //ACQ_RAISE
				acq.setRaise();
				autoStep++;
				break;
			case 8: //ACQ_SCORE
				acq.setRegScore();
				autoStep++;
				break;
			case 9: //ELE_SWITCH
				ele.goSwitch();
				autoStep++;
				break;
			case 10: //ELE_SCALE
				ele.goScale();
				autoStep++;
				break;
			case 11: //ELE_FLOOR
				ele.goFloor();
				autoStep++;
				break;
			case 12: //ELE_DONE
				if(ele.isDone()){
					autoStep++;
				}
				break;
			case 13: //ACQ_HOME
				acq.setHome();
				autoStep++;
				break;
			case 14: //ACQ_DONE
				if(acq.isDone()){
					autoStep++;
				}
				break;
			case 15: //TIMER
				break;
			case 16: //BGR_TIMER
				isBackgroundTimer = true;
				timerStep = autoStep;
				autoStep++;
				break;
			case 17: //DRIVES_SLOW
				if(drives.driveSlow()) {
					autoStep++;
				}
				break;
			case 18: //ACQ_REGSCORE
				acq.setRegScore();
				autoStep++;
				break;
			case 19: //ACQ_LAUNCHSCORE
				acq.setLaunchScore();
				autoStep++;
				break;
			case 21: //ACQ_SPIN
				acq.setSpin();
				autoStep++;
				break;
			default:
				break;
			}
		}
	}

	private void setAuto() {
		selectedAuto = autoChooser.getSelected();
		selectedPosition = posChooser.getSelected();
		if(selectedAuto == AutoSelected.NOTHING) {
			currentAuto = DEFAULT_AUTO;
			return;
		}
		if(selectedPosition == PositionSelected.MIDDLE || selectedAuto == AutoSelected.CROSSBORDER) {
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
			case SWITCHACQ:
				if(isRightScale) {
					currentAuto = CUBE_ON_RIGHT_SCALE_FROM_LEFT;
				} else {
					currentAuto = CUBE_ON_LEFT_SWITCH_FROM_LEFT_AND_ACQUIRE;
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
		default:
			System.out.println("Invalid auto; tried: " + selectedAuto.name() + " at " + selectedPosition.name());
			break;
				
			
//		if(isRightAllySwitch) {
//			if(isRightScale) {
//				switch()
//				switch(selectedAuto) {
//				case NOTHING:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				case SCALE:
//					currentAuto = CUBE_ON_RIGHT_SCALE_FROM_LEFT;
//					break;
//				case CROSSBORDER:
//					currentAuto = CROSS_AUTO_LINE;
//					break;
//				case SWITCH:
//					currentAuto = CUBE_ON_RIGHT_SWITCH_FROM_LEFT;
//					break;
//				case SWITCHSCALE:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				case SWITCHACQ:
//					currentAuto = CUBE_ON_LEFT_SWITCH_FROM_LEFT_AND_ACQUIRE;
//					break;
//				}
//			} else {
//				switch(selectedAuto) {
//				case NOTHING:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				case SCALE:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				case CROSSBORDER:
//					currentAuto = CROSS_AUTO_LINE;
//					break;
//				case SWITCH:
//					currentAuto = CUBE_ON_RIGHT_SWITCH_FROM_LEFT;
//					break;
//				case SWITCHSCALE:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				case SWITCHACQ:
//					currentAuto = CUBE_ON_LEFT_SWITCH_FROM_LEFT_AND_ACQUIRE;
//					break;
//				}
//			}
//		} else {
//			if(isRightScale) {
//				switch(selectedAuto) {
//				case NOTHING:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				case SCALE:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				case CROSSBORDER:
//					currentAuto = CROSS_AUTO_LINE;
//					break;
//				case SWITCH:
//					currentAuto = CUBE_ON_LEFT_SWITCH_FROM_LEFT;
//					break;
//				case SWITCHSCALE:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				case SWITCHACQ:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				}
//			} else {
//				switch(selectedAuto) {
//				case NOTHING:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				case SCALE:
//					currentAuto = CUBE_ON_LEFT_SCALE_FROM_LEFT;
//					break;
//				case CROSSBORDER:
//					currentAuto = CROSS_AUTO_LINE;
//					break;
//				case SWITCH:
//					currentAuto = CUBE_ON_LEFT_SWITCH_FROM_LEFT;
//					break;
//				case SWITCHSCALE:
//					currentAuto = CUBE_ON_LEFT_SCALE_FROM_LEFT_AND_SWITCH;
//					break;
//				case SWITCHACQ:
//					currentAuto = DEFAULT_AUTO;
//					break;
//				}
//			}
		}
	}

	public enum AutoState{

		DRIVES_FORWARD, //@param - distance, speed
		DRIVES_BACKWARD, //@param - distance, speed
		DRIVES_TURNLEFT, //@param - degrees to turn, speed
		DRIVES_TURNRIGHT, //@param - degrees to turn, speed
		DRIVES_WAIT,
		DRIVES_STOP,
		ACQ_ACQUIRE,
		ACQ_RAISE,
		ACQ_SCORE,
		ELE_SWITCH,
		ELE_SCALE,
		ELE_FLOOR,
		ELE_DONE,
		ACQ_HOME,
		ACQ_DONE,
		TIMER,
		BGR_TIMER,
		DRIVES_SLOW,
		ACQ_REGSCORE,
		ACQ_LAUNCHSCORE,
		ACQ_PINCH,
		ACQ_SPIN;
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
		case ACQ_ACQUIRE:
			return 6;
		case ACQ_RAISE:
			return 7;
		case ACQ_SCORE:
			return 8;
		case ELE_SWITCH:
			return 9;
		case ELE_SCALE:
			return 10;
		case ELE_FLOOR:
			return 11;
		case ELE_DONE:
			return 12;
		case ACQ_HOME:
			return 13;
		case ACQ_DONE:
			return 14;
		case TIMER:
			return 15;
		case BGR_TIMER:
			return 16;
		case DRIVES_SLOW:
			return 17;
		case ACQ_REGSCORE:
			return 18;
		case ACQ_LAUNCHSCORE:
			return 19;
		case ACQ_PINCH:
			return 20;
		case ACQ_SPIN:
			return 21;
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
	
	public enum PositionSelected{
		
		LEFT,
		RIGHT,
		MIDDLE;
		
	}
	
	

}
