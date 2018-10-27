package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.Timer;
import src.org.gosparx.team1126.subsytems.Acquisitions;
import src.org.gosparx.team1126.subsytems.Climbing;
import src.org.gosparx.team1126.subsytems.Drives;
import src.org.gosparx.team1126.subsytems.Elevations;
import src.org.gosparx.team1126.util.Logger;
import src.org.gosparx.team1126.util.Logger.Tag;

public class Automation {

	private Drives drives;
	private Acquisitions acq;
	private Elevations ele;
	private Climbing climb;
	
	private int updateAutoStep;
	private int autoStep;
	private int[][] currentAuto;
	
	private double startingBackgroundTime;
	private boolean isBackgroundTimer;
	private int timerStep;	
	private double startingTime;
	private boolean hasInit;
	
	private State state;
	
	public enum State{
		STANDBY,
		AUTO;
	}
	
	public enum AutoState{

		DRIVES_FORWARD(0), //@param - distance, speed
		DRIVES_BACKWARD(1), //@param - distance, speed
		DRIVES_TIMED(2),	//@param-  time, speed
		DRIVES_TURNLEFT(3), //@param - degrees to turn, speed
		DRIVES_TURNRIGHT(4), //@param - degrees to turn, speed
		DRIVES_WAIT(5),
		DRIVES_CLIMB(6),
		DRIVES_SLOW(7),
		DRIVES_STOP(8),
		ACQ_ACQUIRE(9),
		ACQ_RAISE(10),
		ACQ_LOWER(11),
		ACQ_SCORE(12),
		ACQ_REGSCORE(13),
		ACQ_SLOW_SPIT(14),
		ACQ_SPIT(15),
		ACQ_HOME(16),
		ACQ_DONE(17),
		ELE_SWITCH(18),
		ELE_SCALE(19),
		ELE_FLOOR(20),
		ELE_CLIMB(21),
		ELE_STAYDOWN(22),
		ELE_DONE(23),
		CLIMBING_PTO(24),
		CLIMBING_ARMS(25),
		TIMER(26),
		BGR_TIMER(27);
		
		private final int value;
		private AutoState(int val) {
			value = val;
		}
		public int toInt() {
			return value;
		}
		
	}


	public Automation(Drives drives, Acquisitions acq, Elevations ele, Climbing climb) {
		this.drives = drives;
		this.acq = acq;
		this.ele = ele;
		this.climb = climb;
		
		autoStep = 0;
		startingTime = -1;
		updateAutoStep = -1;
	}
	
	public void setAuto(int[][] autoSequence) {
		currentAuto = autoSequence;
		state = State.AUTO;
		autoStep = 0;
		hasInit = true;
		isBackgroundTimer = false;
		startingBackgroundTime = Timer.getFPGATimestamp();
		startingTime = -1;
		updateAutoStep = -1;
		drives.toAuto();
		acq.toTele();
		ele.toTele();
		climb.toTele();
		Logger.getInstance().log("Automation", "setAuto", "Starting automated mode");
	}
	
	public void execute() {
		switch(state) {
		case STANDBY:
			return;
		case AUTO:
			if(currentAuto == null) {
				Logger.getInstance().log("Automation", "execute", Tag.ERROR, "Auto not set!");
				return;
			}
			if(isBackgroundTimer && startingBackgroundTime + currentAuto[timerStep][1] < Timer.getFPGATimestamp()) {
				autoStep = currentAuto[timerStep][2];
				isBackgroundTimer = false;
			}
			if(currentAuto.length > autoStep) {
				if(updateAutoStep != autoStep) {
					Logger.getInstance().log("Automation", "execute", "Current auto step:" + autoStep);
					Logger.getInstance().log("Automation", "execute", "Current auto function: " + currentAuto[autoStep][0]);
					updateAutoStep = autoStep;
				}
				switch(currentAuto[autoStep][0]) {
				case 0: //DRIVES_FORWARD
					drives.move(currentAuto[autoStep][1], currentAuto[autoStep][2]);
					autoStep++;
					break;
				case 1: //DRIVES_BACKWARD
					drives.move(currentAuto[autoStep][1], -currentAuto[autoStep][2]);
					autoStep++;
					break;
				case 2: //DRIVES_TIMED
					drives.moveTimed(currentAuto[autoStep][1], currentAuto[autoStep][2]);
					autoStep++;
					break;
				case 3: //DRIVES_TURNLEFT
					drives.turn(-currentAuto[autoStep][1], currentAuto[autoStep][2]);
					autoStep++;
					break;
				case 4: //DRIVES_TURNRIGHT
					drives.turn(currentAuto[autoStep][1], currentAuto[autoStep][2]);
					autoStep++;
					break;
				case 5: //DRIVES_WAIT
					if(drives.isDone()) {
						autoStep++;
						Logger.getInstance().log("Automation", "execute", "DRIVES COMPLETED");
					}
					break;
				case 6: //DRIVES_CLIMB
					drives.enableClimb(currentAuto[autoStep][1] == 1 ? true : false);
					autoStep++;
					break;
				case 7: //DRIVES_SLOW
					if(drives.driveSlow()) {
						autoStep++;
					}
					break;
				case 8: //DRIVES_STOP
					drives.stopMotors();
					autoStep++;
					break;
				case 9: //ACQ_ACQUIRE
					acq.setAcquire();
					autoStep++;
					break;
				case 10: //ACQ_RAISE
					acq.setRaise();
					autoStep++;
					break;
				case 11: //ACQ_LOWER
					acq.setLower();
					autoStep++;
					break;
				case 12: //ACQ_SCORE
					acq.setScore();
					autoStep++;
					break;
				case 13: //ACQ_REGSCORE
					acq.setScore();
					autoStep++;
					break;
				case 14: //ACQ_SLOW_SPIT
					acq.setSlowSpit();
					autoStep++;
					break;
				case 15: //ACQ_SPIT
					acq.setSpit();
					autoStep++;
					break;
				case 16: //ACQ_HOME
					acq.setHome();
					autoStep++;
					break;
				case 17: //ACQ_DONE
					if(acq.isDone()){
						autoStep++;
					}
					break;
				case 18: //ELE_SWITCH
					ele.setSwitch();
					autoStep++;
					break;
				case 19: //ELE_SCALE
					ele.setScale();
					autoStep++;
					break;
				case 20: //ELE_FLOOR
					ele.setFloor();
					autoStep++;
					break;
				case 21: //ELE_CLIMB
					ele.setClimb();
					autoStep++;
					break;
				case 22:
					ele.setStayDown();
					autoStep++;
					break;
				case 23: //ELE_DONE
					if(ele.isDone()){
						autoStep++;
					}
					break;
				case 24: //CLIMBNG_PTO
					climb.enableClimbing(currentAuto[autoStep][1] == 1 ? true : false);
					autoStep++;
					break;
				case 25: //CLIMBING_ARMS
					climb.climbingArms(currentAuto[autoStep][1] == 1 ? true : false);
					autoStep++;
					break;
				case 26: //TIMER
					if(startingTime < 0) {
						startingTime = Timer.getFPGATimestamp() * 1000;
					} else if(startingTime + currentAuto[autoStep][1] < Timer.getFPGATimestamp()*1000) {
						autoStep++;
						startingTime = -1;
					}
					break;
				case 27: //BGR_TIMER
					isBackgroundTimer = true;
					timerStep = autoStep;
					autoStep++;
					break;
				default:
					break;
				}
			} else {
				Logger.getInstance().log("Automation", "execute", "Autonomous mode finished");
				drives.toTele();
				hasInit = false;
				state = State.STANDBY;
				currentAuto = null;
			}
			break;
		}
	
	}
	
	public State getState() {
		return state;
	}

	
}
