package src.org.gosparx.team1126.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot{

	@Override
	public void robotInit() {
		Joystick joy = new Joystick(0);
	}
	
	@Override
	public void autonomousInit() {
		
	}
	
	@Override
	public void teleopInit() {
		
	}
	
	@Override
	public void disabledInit() {
		
	}
}
