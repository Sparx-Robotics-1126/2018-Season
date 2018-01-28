package src.org.gosparx.team1126.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import src.org.gosparx.team1126.subsytems.Elevations;

public class Robot extends IterativeRobot{

	private Elevations elevator; 
	@Override
	public void robotInit() {
		elevator = new Elevations();
		elevator.init();
	}
	
	@Override
	public void autonomousInit() {
		
	}
	

	@Override
	public void teleopPeriodic() {
		while(true) {
			elevator.execute();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void autonomousPeriodic() {
		while(true) {
			elevator.execute();
		}
	}
	
	@Override
	public void teleopInit() {
		
		
	}
	
	@Override
	public void disabledInit() {
		
	}
	

}
