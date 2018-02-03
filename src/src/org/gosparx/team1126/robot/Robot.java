package src.org.gosparx.team1126.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import src.org.gosparx.team1126.subsytems.PrettyColors;

public class Robot extends IterativeRobot{

	@Override
	public void robotInit() {
		
	}
	
	@Override
	public void autonomousInit() {
		
	}
	
	@Override
	public void teleopInit() {
		PrettyColors Colors = new PrettyColors();
		Colors.run();
	}
	
	@Override
	public void disabledInit() {
		
	}
}
