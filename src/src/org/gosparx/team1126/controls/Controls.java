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
	
	public Controls() {
		//drives = new Drives();
		//elevations = new Elevations();
		//acquisitions = new Acquisitions();
		//climbing = new Climbing();
		ds = DriverStation.getInstance();
	}
	
	public abstract void execute();
	
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
