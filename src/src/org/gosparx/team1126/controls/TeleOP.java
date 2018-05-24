package src.org.gosparx.team1126.controls;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.gosparx.team1126.subsytems.Acquisitions;
import src.org.gosparx.team1126.subsytems.Climbing;
import src.org.gosparx.team1126.subsytems.Drives;
import src.org.gosparx.team1126.subsytems.Elevations;

public class TeleOP implements Controls{

	private Joystick[] joysticks;

	private NetworkTableEntry arduinoValue;

	private Drives drives;
	private Acquisitions acq;
	private Climbing climbing;
	private Elevations ele;
	private TeleAutomation teleauto;

	private State state;

	private SendableChooser<Boolean> demoModeSwitch;

	private boolean isInDemoMode;

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

	private boolean[][] axisStates = 
		{{false, false}, //RIGHTJOY_X
				{false, false}, //RIGHTJOY_Y
				{false, false}, //LEFTJOY_X
				{false, false}, //LEFTJOY_Y
				{false, false}, //XBOX_LEFT_X
				{false, false}, //XBOX_LEFT_Y
				{false, false}, //XBOX_RIGHT_X
				{false, false}, //XBOX_RIGHT_Y
		};

	/**
	 * Initializes stuff in TeleOP
	 * @param drives - an instance of Drives created by RobotSystem.
	 * @param acq - an instance of Acquisitions created by RobotSystem.
	 * @param ele - an instance of Elevations created by RobotSystem.
	 * @param climb - an instance of Climbing created by RobotSystem.
	 */
	public TeleOP(Drives drives, Acquisitions acq, Elevations ele, Climbing climb, Automation automation) {
		this.drives = drives;
		this.acq = acq;
		this.ele = ele;
		this.climbing = climb;
		this.teleauto = new TeleAutomation(automation);
		arduinoValue = NetworkTableInstance.getDefault().getTable("arduinoTable").getEntry("arduinoValue");
		arduinoValue.setBoolean(false);
		state = State.TELEOP;
		joysticks = new Joystick[] {new Joystick(CtrlMap.RIGHTJOYSTICK), new Joystick(CtrlMap.LEFTJOYSTICK), new Joystick(CtrlMap.XBOXCONTROLLER)};
		isInDemoMode = false;

		demoModeSwitch = new SendableChooser<Boolean>();

		demoModeSwitch.addDefault("Normal Mode", new Boolean(false));
		demoModeSwitch.addObject("Demo Mode", new Boolean(true));

		SmartDashboard.putData(demoModeSwitch);
	}

	public void init() {
		isInDemoMode = demoModeSwitch.getSelected();
		if(isInDemoMode) {
			state = State.TELEOP;
			drives.toTele(true);
		}
	}

	/**
	 * Execute - 2 mana destroy a damaged enemy minion.
	 */
	@Override
	public void execute() {
		setJoystickStates();
		if(DriverStation.getInstance().getMatchTime() > 50) {
			SmartDashboard.putBoolean("climbingTime", false);
			arduinoValue.setBoolean(false);
		} else {
			if(DriverStation.getInstance().getMatchTime() > 40) {
				joysticks[2].setRumble(RumbleType.kLeftRumble, 1);
				joysticks[2].setRumble(RumbleType.kRightRumble, 1);
			} else {
				joysticks[2].setRumble(RumbleType.kLeftRumble, 0);
				joysticks[2].setRumble(RumbleType.kRightRumble, 0);
			}
			if(DriverStation.getInstance().getMatchTime() % 1 < 0.5) {
				SmartDashboard.putBoolean("climbingTime", false);
				if(DriverStation.getInstance().isFMSAttached()) {
					arduinoValue.setBoolean(true);
				}
			} else {
				SmartDashboard.putBoolean("climbingTime", true);
				arduinoValue.setBoolean(false);
			}
		}
		switch(state) {
		case TELEOP:

			//			Joystick Buttons Left
			if(isRisingEdgeButton(0)) { //right joystick left button
				System.out.println("Right joystick - Left Button pressed, Arms");
				climbing.climbingArms(!climbing.getClimbingArms());
			}
			//			if(isRisingEdgeButton(1)) { //right joystick middle button
			//				System.out.println("right joystick middle button");
			//			}
			//			if(isRisingEdgeButton(1)) { //right joystick middle button or missile switch
			//				System.out.println("Right joystick - Middle Button pressed");
			//				state = State.TELEAUTO;
			//				teleauto.init();
			//				return;
			//			}
			if(!buttonStates[1][0] && buttonStates[4][0]) {
				climbing.enableClimbing(false);
				drives.enableClimb(false);
			}
			//			if(isRisingEdgeButton(2)) { //right joystick right button
			//				climbing.climbingLatch(!climbing.getClimbingLatch());
			//			}
			//			if(buttonStates[3][0]) { //right joystick trigger	
			//			}
			//			Axis Left
			//			if(axisStates[0][0]) {
			//				getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_X_AXIS);
			//			}
			//			//POV Left
			//			if(isRisingEdgePOV(0)) { //right joystick pov up
			//				System.out.println("right joystick pov up");
			//			}
			//			if(isRisingEdgePOV(1)) { //right joystick pov right
			//				System.out.println("right joystick pov right");
			//			}
			//			if(isRisingEdgePOV(2)) { //right joystick pov down
			//				System.out.println("right joystick pov down");
			//			}
			//			if(isRisingEdgePOV(3)) { //right joystick pov left
			//				System.out.println("right joystick pov left");
			//			}
			//Joystick Buttons Right
			//			if(isRisingEdgeButton(4)) { //left joystick left button	
			//				
			//			}
			//			if(isRisingEdgeButton(5)) {
			//
			//			}
			//			if(isFallingEdgeButton(5)) {
			//
			//			}
			//			if(isRisingEdgeButton(5)) { //left joystick middle button
			//
			//			}
			//			if(isFallingEdgeButton(5)) {
			//
			//			}
			//			if(isRisingEdgeButton(6)) { //left joystick right button
			//				System.out.println("left joystick right button");
			//			}
			//			if(isRisingEdgeButton(7)) { //left joystick trigger
			//				System.out.println("left joystick trigger button");
			//			}
			//			//Axis Right
			//			if(axisStates[3][0]) {
			//				//getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_X_AXIS);
			//			}
			//			if(!isClimbing) {
			//				drives.joystickRight(getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS));
			//			} else {
			//				drives.joystickRight(-getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS));
			//			}
			//POV Right
			//			if(isRisingEdgePOV(4)) { //left joystick pov up
			//				System.out.println("left joystick pov up");
			//			}
			//			if(isRisingEdgePOV(5)) { //left joystick pov right
			//				System.out.println("left joystick pov right");
			//			}
			//			if(isRisingEdgePOV(6)) { //left joystick pov down
			//				System.out.println("left joystick pov down");
			//			}
			//			if(isRisingEdgePOV(7)) { //left joystick pov left
			//				System.out.println("left joystick pov left");
			//			}
			//xBox Buttons
			//			if(isRisingEdgeButton(9)) { //xbox b button
			//				
			//			}
			//			if(isFallingEdgeButton(9)) {
			//				
			//			}
			//			if(isRisingEdgeButton(18)) { //xbox L3 button
			//	
			//			}
			//			if(isRisingEdgeButton(19)) { //xbox R3 button
			//	
			//			}
			//			//xBox Axis
			//			if(axisStates[5][0]) {
			//				//getAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.JOY_X_AXIS);
			//			}	
			//			if(axisStates[6][0]) {
			//				//getAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.JOY_Y_AXIS);
			//			}
		case TELEBASE:
			if(demoModeSwitch.getSelected() != isInDemoMode) {
				isInDemoMode = demoModeSwitch.getSelected();
				if(isInDemoMode) {
					drives.toTele(true);
					drives.setSlowFactor(0.4/3);
					state = State.TELEBASE;
				} else {
					drives.toTele();
					state = State.TELEOP;
				}
			}
			//if(demoModeSwitch.getSelected()) {
				//drives.setSlowFactor((getAxis(0, 2)+1)/2);
			//}

			//xBox Buttons
			if(isRisingEdgeButton(8)) { //xbox a button
				System.out.println("XBOX Controller - A Button");
				acq.setHome();
			}
			
			if(isRisingEdgeButton(10)) { //xbox x button
				System.out.println("XBOX Controller - X Button");
				acq.setSpit();
			}
			if(isRisingEdgeButton(11)) { //xbox y button
				System.out.println("XBOX Controller - Y Button");
				acq.setTogglePinch(); //toggles pinch
			}
			if(isRisingEdgeButton(12)) { //xbox L1 button
				System.out.println("XBOX Controller - L1 Button");
				acq.setScore();
			}
			if(isRisingEdgeButton(13)) { //xbox R1 button
				System.out.println("XBOX Controller - R1 Button");
				acq.setRaise();
			}
			if(isRisingEdgeButton(14)) { //xbox back button
				System.out.println("XBOX Controller - Back Button");
				acq.setSlowSpit();
			}   
			if(isRisingEdgeButton(15)) { //xbox start button
				System.out.println("XBOX Controller - Start Button");
				ele.setClimb();
			}
			if(isRisingEdgeButton(16)) { //xbox L2 button
				System.out.println("XBOX Controller - L2 Button");
				acq.setLower();
			}
			if(isRisingEdgeButton(17)) { //xbox R2 button
				System.out.println("XBOX Controller - R2 Button");
				acq.setAcquire();
			}
			if(axisStates[1][0]) {
				drives.joystickLeft(getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS));
			} else {
				drives.joystickLeft(0);
			}
			if(axisStates[3][0]) {
				drives.joystickRight(getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS));
			} else {
				drives.joystickRight(0);
			}
			if(axisStates[7][0]) {
				ele.trim(getAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_RIGHT_Y));
			} else if(isFallingEdgeAxis(7)){
				ele.trim(0);
			}
			//xbox POV		 
			if(isRisingEdgePOV(8)) { //xbox pov up
				System.out.println("XBOX Controller - POV Up");
				ele.setScale();
			}
			if(isRisingEdgePOV(9)) { //xbox pov right
				System.out.println("XBOX Controller - POV Right");
				ele.setSwitch();
			}
			if(isRisingEdgePOV(10)) { //xbox pov down
				System.out.println("XBOX Controller - POV Down");
				ele.setFloor();
			}
			if(isRisingEdgePOV(11)) { //xbox pov left
				System.out.println("XBOX Controller - POV Left");
				ele.setSwitch();
			}

			break;
		case TELEAUTO:
			if(!buttonStates[1][0] && buttonStates[4][0] || teleauto.getState() == TeleAutomation.State.STANDBY) {
				state = State.TELEOP;
				if(!buttonStates[1][0] && buttonStates[4][0]) {
					climbing.enableClimbing(false);
					drives.enableClimb(false);
				}
			} else {
				teleauto.execute();
			}
			break;	
		}
	}

	/**
	 * Updates the previous joystick states to what they currently are now.
	 */
	public void setJoystickStates() {
		for(boolean buttons[]: buttonStates) {
			buttons[1] = buttons[0];
		}
		for(boolean povs[]: povStates) {
			povs[1] = povs[0];
		}
		for(boolean axis[]: axisStates) {
			axis[1] = axis[0];
		}
		buttonStates[0][0] = isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_LEFT);
		buttonStates[1][0] = isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_MIDDLE);
		//		buttonStates[2][0] = isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_RIGHT);
		//		buttonStates[3][0] = isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_TRIGGER);
		//		buttonStates[4][0] = isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_LEFT);
		buttonStates[5][0] = isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_MIDDLE);
		//		buttonStates[6][0] = isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_RIGHT);
		//		buttonStates[7][0] = isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_TRIGGER);

		//buttonStates[8][0] =  isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_A);
		buttonStates[9][0] =  isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_B);
		buttonStates[10][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_X);
		buttonStates[11][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_Y);

		buttonStates[12][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_L1);
		buttonStates[13][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_R1);
		buttonStates[14][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_BACK);
		buttonStates[15][0] = isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_START);
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
		//		axisStates[0][0] = isOffZeroAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_X_AXIS);
		axisStates[1][0] = isOffZeroAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS);
		//		axisStates[2][0] = isOffZeroAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_X_AXIS);
		axisStates[3][0] = isOffZeroAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS);
		//		axisStates[4][0] = isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_LEFT_X);
		//		axisStates[5][0] = isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_RIGHT_X);
		//		axisStates[6][0] = isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_LEFT_Y);
		axisStates[7][0] = isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_RIGHT_Y);

	}

	public enum State {
		TELEOP,
		TELEAUTO,
		TELEBASE;
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

	public boolean isRisingEdgeAxis(int pos) {
		return axisStates[pos][0] && !axisStates[pos][1];
	}

	public boolean isFallingEdgeAxis(int pos) {
		return !axisStates[pos][0] && axisStates[pos][1];
	}

	public void rumbleXbox(double rumble) {
		joysticks[2].setRumble(RumbleType.kLeftRumble, rumble);
	}

}
