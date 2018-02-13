package src.org.gosparx.team1126.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import src.org.gosparx.team1126.subsytems.Climbing;

public class Robot extends IterativeRobot{
private Joystick Joy;
private Climbing Climb;
	@Override
	public void robotInit() {
		Joy = new Joystick(0);
		Climb = new Climbing();
		Climb.init();
	}
	
	@Override
	public void autonomousInit() {
		
	}
	
	@Override
	public void teleopInit() {
		while(true) {
			Climb.enableClimbing(Joy.getRawButton(1));
		}
	}
	
	@Override
	public void disabledInit() {
		
	}
}
