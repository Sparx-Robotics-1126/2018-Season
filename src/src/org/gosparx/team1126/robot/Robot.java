package src.org.gosparx.team1126.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import src.org.gosparx.team1126.controls.Autonomous;
import src.org.gosparx.team1126.controls.TeleOP;

public class Robot extends IterativeRobot{

	private Autonomous tele;
	
	@Override
	public void robotInit() {
		
	}
	
	@Override
	public void autonomousInit() {
		tele = new Autonomous();
	}
	
	@Override
	public void autonomousPeriodic() {
		tele.execute();
	}
	
	@Override
	public void teleopInit() {
	}
	
	@Override
	public void teleopPeriodic() {
	}
	
	@Override
	public void disabledInit() {
		
	}
}
