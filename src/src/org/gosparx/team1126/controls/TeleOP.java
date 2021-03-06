package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.Joystick;
import src.org.gosparx.team1126.subsytems.Acquisitions;
import src.org.gosparx.team1126.subsytems.Climbing;
import src.org.gosparx.team1126.subsytems.Drives;
import src.org.gosparx.team1126.subsytems.Elevations;

public class TeleOP implements Controls{

	private Joystick[] joysticks;
	
	private Drives drives;
	private Acquisitions acq;
	private Climbing climbing;
	private Elevations ele;
	
	private boolean finishedClimbing;

	private boolean[][] buttonStates =
		{{false, false}, //LEFTJOY_LEFT
				{false, false},  //LEFTJOY_MIDDLE
				{false, false},  //LEFTJOY_RIGHT
				{false, false},  //LEFTJOY_TRIGGER
				{false, false},  //RIGHTJOY_LEFT
				{false, false},  //RIGHTJOY_MIDDLE
				{false, false},  //RIGHTJOY_RIGHT
				{false, false},  //RIGHTJOY_TRIGGER
				{false, false},  //XBOX_A
				{false, false},  //XBOX_B
				{false, false},  //XBOX_X
				{false, false},  //XBOX_Y
				{false, false},  //XBOX_L1
				{false, false},  //XBOX_R1
				{false, false},  //XBOX_BACK
				{false, false},  //XBOX_START
				{false, false},  //XBOX_L2
				{false, false},  //XBOX_L2
				{false, false},  //XBOX_L3
				{false, false}};  //XBOX_R3

	private boolean[][] povStates =
		{{false, false}, //LEFTJOY_UP
				{false, false},  //LEFTJOY_RIGHT
				{false, false},  //LEFTJOY_DOWN
				{false, false},  //LEFTJOY_LEFT
				{false, false},  //RIGHTJOY_UP
				{false, false},  //RIGHTJOY_RIGHT
				{false, false},  //RIGHTJOY_DOWN
				{false, false},  //RIGHTJOY_LEFT
				{false, false},  //XBOX_UP
				{false, false},  //XBOX_RIGHT
				{false, false},  //XBOX_DOWN
				{false, false}};  //XBOX_LEFT
	
	/**
	 * Initializes stuff in TeleOP
	 * @param drives - an instance of Drives created by RobotSystem.
	 * @param acq - an instance of Acquisitions created by RobotSystem.
	 * @param ele - an instance of Elevations created by RobotSystem.
	 * @param climb - an instance of Climbing created by RobotSystem.
	 */
	public TeleOP(Drives drives, Acquisitions acq, Elevations ele, Climbing climb) {
		this.drives = drives;
		this.acq = acq;
		this.ele = ele;
		climbing = climb;
		finishedClimbing = false;
		joysticks = new Joystick[] {new Joystick(CtrlMap.RIGHTJOYSTICK), new Joystick(CtrlMap.LEFTJOYSTICK), new Joystick(CtrlMap.XBOXCONTROLLER)};
	}

	/**
	 * Execute - 2 mana destroy a damaged enemy minion.
	 */
	@Override
	public void execute() {
		//Joystick Buttons Left
		setJoystickStates();
		if(!finishedClimbing && drives.hasClimbed()) {
			finishedClimbing = true;
			climbing.climbingLatch(true);
		}
		if(isRisingEdgeButton(0)) { //right joystick left button
			climbing.climbingArms(true);
		}
//		if(isRisingEdgeButton(1)) { //right joystick middle button
//			System.out.println("right joystick middle button");
//		}
		if(isRisingEdgeButton(1)) { //right joystick middle button
			finishedClimbing = false;
			climbing.enableClimbing(true);
			drives.enableClimb(true);
		}
		if(isFallingEdgeButton(1)) {
			climbing.enableClimbing(false);
			drives.enableClimb(false);
		}
		if(isRisingEdgeButton(2)) { //right joystick right button
			climbing.climbingLatch(!climbing.getClimbingLatch());
		}
//		if(buttonStates[3][0]) { //right joystick trigger
//			isClimbing = true;
//		} else {
//			isClimbing = false;
//		}
//		climbing.enableClimbing(isClimbing);
		//Axis Left
//		if(isOffZeroAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_X_AXIS)) {
//			getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_X_AXIS);
//		}
		if(isOffZeroAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS)) {
			drives.joystickLeft(getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS));
		} else {
			drives.joystickLeft(0);
		}
//		if(isOffZeroAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS)) {
//			if(!isClimbing) {
//				drives.joystickLeft(getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS));
//			} else {
//				drives.joystickLeft(-getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS));
//				drives.joystickRight(-getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS));
//			}
//		} else {
//			if(isClimbing) {
//				drives.joystickRight(0);
//			}
//			drives.joystickLeft(0);
//		}	
//		//POV Left
//		if(isRisingEdgePOV(0)) { //right joystick pov up
//			System.out.println("right joystick pov up");
//		}
//		if(isRisingEdgePOV(1)) { //right joystick pov right
//			System.out.println("right joystick pov right");
//		}
//		if(isRisingEdgePOV(2)) { //right joystick pov down
//			System.out.println("right joystick pov down");
//		}
//		if(isRisingEdgePOV(3)) { //right joystick pov left
//			System.out.println("right joystick pov left");
//		}
//		//Joystick Buttons Right
//		if(isRisingEdgeButton(4)) { //left joystick left button
//			System.out.println("left joystick left button");
//		}
//		if(isRisingEdgeButton(5)) { //left joystick middle button
//			isClimbing = true;
//			climbing.enableClimbing(isClimbing);
//			drives.enableClimb(isClimbing);
//		}
//		if(isFallingEdgeButton(5)) {
//			System.out.println("its falling");
//			isClimbing = false;
//			climbing.enableClimbing(isClimbing);
//			drives.enableClimb(isClimbing);
//		}
//		}
//		if(isRisingEdgeButton(6)) { //left joystick right button
//			System.out.println("left joystick right button");
//		}
//		if(isRisingEdgeButton(7)) { //left joystick trigger
//			System.out.println("left joystick trigger button");
//		}
//		//Axis Right
//		if(isOffZeroAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_X_AXIS)) {
//			//getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_X_AXIS);
//		}
			if(isOffZeroAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS)) {
				drives.joystickRight(getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS));
			} else {
				drives.joystickRight(0);
			}
//		if(!isClimbing) {
//			drives.joystickRight(getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS));
//		} else {
//			drives.joystickRight(-getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS));
//		}
		
		
		//POV Right
//		if(isRisingEdgePOV(4)) { //left joystick pov up
//			System.out.println("left joystick pov up");
//		}
//		if(isRisingEdgePOV(5)) { //left joystick pov right
//			System.out.println("left joystick pov right");
//		}
//		if(isRisingEdgePOV(6)) { //left joystick pov down
//			System.out.println("left joystick pov down");
//		}
//		if(isRisingEdgePOV(7)) { //left joystick pov left
//			System.out.println("left joystick pov left");
//		}
		//xBox Buttons
		 
//		if(isRisingEdgeButton(8)) { //xbox a button
//			acq.setAcquire(); //acquire
//			acq.setSpin();
//		}
//		if(isRisingEdgeButton(9)) { //xbox b button
//			acq.setLaunchScore(); //shoot
//		}
		if(isRisingEdgeButton(10)) { //xbox x button
			acq.setSpit();
		}
//		if(isRisingEdgeButton(11)) { //xbox y button
//			acq.setRaise(); //raise
//		}
		if(isRisingEdgeButton(12)) { //xbox L1 button
			acq.setLaunchScore();
		}
		if(isRisingEdgeButton(13)) { //xbox R1 button
			acq.setRaise();
		}
//		if(isRisingEdgeButton(14)) { //xbox back button
//			acq.setSpit();
//		}
		if(isRisingEdgeButton(15)) { //xbox start button
			climbing.climbingArms(true);
		}
		if(isRisingEdgeButton(16)) { //xbox L2 button
			acq.setRegScore();
		}
		if(isRisingEdgeButton(17)) { //xbox R2 button
			acq.setAcquire();
		}
//		if(isRisingEdgeButton(18)) { //xbox L3 button
//			System.out.println("xbox L3 button");
//		}
//		if(isRisingEdgeButton(19)) { //xbox R3 button
//			System.out.println("xbox R3 button");
//		}
//		//xBox Axis
//		if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_LEFT_X)) {
//			//getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_X_AXIS);
//		}
//		if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_LEFT_Y)) {
//			//getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS);
//		}
//		if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_RIGHT_X)) {
//			//getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS);
//		}
//		if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_RIGHT_Y)) {
//			//getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS);
//		}
//		if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_L2)) {
//			//getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS);
//		}
//		if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_R2)) {
//			//getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS);
//		}
//		//xbox POV
//		 */
		if(isRisingEdgePOV(8)) { //xbox pov up
			ele.goScale();
		}
		if(isRisingEdgePOV(9)) { //xbox pov right
			ele.goSwitch();
		}
		if(isRisingEdgePOV(10)) { //xbox pov down
			ele.goFloor();
		}
		if(isRisingEdgePOV(11)) { //xbox pov left
			ele.goSwitch();
		}
	}

	/**
	 * Updates the previous joystick states to what they currently are now.
	 */
	public void setJoystickStates() {
		for(boolean buttons[]: buttonStates) {
			buttons[1] = buttons[0];
		}
//		for(boolean povs[]: povStates) {
//			povs[1] = povs[0];
//		}
		buttonStates[0][0] = isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_LEFT);
		buttonStates[1][0] = isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_MIDDLE);
		buttonStates[2][0] = isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_RIGHT);
//		buttonStates[3][0] = isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_TRIGGER);
//		buttonStates[4][0] = isPressedButton(CtrlMap.LEFTJOYSTICK2, CtrlMap.JOY_LEFT);
		buttonStates[5][0] = isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_MIDDLE);
//		buttonStates[6][0] = isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_RIGHT);
//		buttonStates[7][0] = isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_TRIGGER);
		
//		buttonStates[8][0] =  isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_A);
//		buttonStates[9][0] =  isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_B);
		buttonStates[10][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_X);
//		buttonStates[11][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_Y);
		
		buttonStates[12][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_L1);
		buttonStates[13][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_R1);
//		buttonStates[14][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_BACK);
//		buttonStates[15][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_START);
		buttonStates[16][0] = isPressedTrigger(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_L2);
		buttonStates[17][0] = isPressedTrigger(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_R2);
//		buttonStates[18][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_L3);
//		buttonStates[19][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_R3);
//
//		povStates[0][0] = isPressedPOV(CtrlMap.RIGHTJOYSTICK, CtrlMap.POV_UP);
//		povStates[1][0] = isPressedPOV(CtrlMap.RIGHTJOYSTICK, CtrlMap.POV_RIGHT);
//		povStates[2][0] = isPressedPOV(CtrlMap.RIGHTJOYSTICK, CtrlMap.POV_DOWN);
//		povStates[3][0] = isPressedPOV(CtrlMap.RIGHTJOYSTICK, CtrlMap.POV_LEFT);
//		povStates[4][0] = isPressedPOV(CtrlMap.LEFTJOYSTICK, CtrlMap.POV_UP);
//		povStates[5][0] = isPressedPOV(CtrlMap.LEFTJOYSTICK, CtrlMap.POV_RIGHT);
//		povStates[6][0] = isPressedPOV(CtrlMap.LEFTJOYSTICK, CtrlMap.POV_DOWN);
//		povStates[7][0] = isPressedPOV(CtrlMap.LEFTJOYSTICK, CtrlMap.POV_LEFT);
		povStates[8][0] = isPressedPOV(CtrlMap.XBOXCONTROLLER, CtrlMap.POV_UP);
		povStates[9][0] = isPressedPOV(CtrlMap.XBOXCONTROLLER, CtrlMap.POV_RIGHT);
		povStates[10][0] = isPressedPOV(CtrlMap.XBOXCONTROLLER, CtrlMap.POV_DOWN);
		povStates[11][0] = isPressedPOV(CtrlMap.XBOXCONTROLLER, CtrlMap.POV_LEFT);
	}

	/**
	 * Return if the specified button was previously not pressed and is now pressed.
	 * @param pos - the button to check in buttonStates.
	 * @return if the button was previously not pressed and is now pressed.
	 */
	public boolean isRisingEdgeButton(int pos) {
		return buttonStates[pos][0] && !buttonStates[pos][1];
	}
	
	/**
	 * Return if the specified button was previously pressed and is now no longer pressed.
	 * @param pos - the button to check in buttonStates.
	 * @return if the button was previously pressed and is no longer pressed.
	 */
	public boolean isFallingEdgeButton(int pos) {
		return !buttonStates[pos][0] && buttonStates[pos][1];
	}

	/**
	 * Returns if the POV was previously not pressed and is now pressed.
	 * @param pos - the POV to check in posStates.
	 * @return if the POV was previously not pressed and is now pressed.
	 */
	public boolean isRisingEdgePOV(int pos) {
		return povStates[pos][0] && !povStates[pos][1];
	}

	/**
	 * Returns whether or not the button on the specified joystick is pressed.
	 * @param joy - the joystick to check.
	 * @param button - the button to check.
	 * @return if the button is pressed.
	 */
	public boolean isPressedButton(int joy, int button) {
		return joysticks[joy].getRawButton(button);
	}
	
	/**
	 * Returns whether or not the trigger on the specific joystick is pressed (deadband of 0.5).
	 * @param joy - the joystick to check.
	 * @param trigger - the trigger to check.
	 * @return if the trigger is pressed.
	 */
	public boolean isPressedTrigger(int joy, int trigger) {
		return joysticks[joy].getRawAxis(trigger) > CtrlMap.TRIGGER_DEADBAND;
	 }

	/**
	 * Returns whether or not the specific POV is selected on the controller; works in angles of 90 (pov 0 -> 0 degrees, pov 1 -> 90 degrees, pov 2 -> 180 degrees, pov 3 = 270 degrees)
	 * @param joy - the joystick.
	 * @param pov - the POV to check.
	 * @return whether or not the POV on the joystick is pressed.
	 */
	public boolean isPressedPOV(int joy, int pov) {
		return joysticks[joy].getPOV() == pov * 90;
	}

	/**
	 * Returns the value of the axis.
	 * @param joy - the joystick.
	 * @param axis - the axis of the joystick.
	 * @return the specified axis's current position (between -1 and 1; inverted).
	 */
	public double getAxis(int joy, int axis) {
		return -joysticks[joy].getRawAxis(axis);
	}

	/**
	 * Returns if the axis of the specified joystick is greater than the DEADBAND as stated in CtrlMap.java.
	 * @param joy - the joystick.
	 * @param axis - the axis on the joystick to check.
	 * @return if the axis of the specified joystick is greater than the DEADBAND.
	 */
	public boolean isOffZeroAxis(int joy, int axis) {
		return getAxis(joy, axis) > CtrlMap.DEADBAND || getAxis(joy, axis) < -CtrlMap.DEADBAND;
	}

}
