package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public abstract class Controls {
	
	//protected Drives drives;
	//protected Elevations elevations;
	//protected Acquisitions acquisitions;
	//protected Climbing climbing;
	protected DriverStation ds;
	
	private Joystick[] joysticks; //leftJoy port 0, rightJoy port 1, xboxController port 2
	
	public Controls() {
		//drives = new Drives();
		//elevations = new Elevations();
		//acquisitions = new Acquisitions();
		//climbing = new Climbing();
		ds = DriverStation.getInstance();
		joysticks = new Joystick[] {new Joystick(CtrlMap.LEFTJOYSTICK), new Joystick(CtrlMap.RIGHTJOYSTICK), new Joystick(CtrlMap.XBOXCONTROLLER)};
	}
	
	public abstract void execute();
	
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
		return joysticks[joy].getRawAxis(axis);
	}
	
	public boolean isOffZeroAxis(int joy, int axis) {
		return getAxis(joy, axis) > CtrlMap.DEADBAND || getAxis(joy, axis) < -CtrlMap.DEADBAND;
	}
	
	public boolean isAutonomous() {
		return ds.isAutonomous();
	}
	
	public boolean isTeleOP() {
		return ds.isOperatorControl();
	}
	
	public boolean isTest() {
		return ds.isTest();
	}
	
	public boolean isDisabled() {
		return ds.isDisabled();
	}

	public boolean isEnabled() {
		return ds.isEnabled();
	}
}
