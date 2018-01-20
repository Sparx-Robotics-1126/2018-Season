package src.org.gosparx.team1126.controls;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class Controls {

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
		joysticks = new Joystick[] {new Joystick(0), new Joystick(1), new Joystick(2)};
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
	
}
