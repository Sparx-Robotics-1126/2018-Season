package src.org.gosparx.team1126.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import src.org.gosparx.team1126.subsytems.Elevations;

public class Robot extends IterativeRobot{

	private Elevations elevator; 
	@Override
	public void robotInit() {
		elevator = new Elevations();
		elevator.init();
		elevator.start();
	}
	
	@Override
	public void autonomousInit() {
		
	}
	

	@Override
	public void teleopPeriodic() {
		Joystick joy = new Joystick(1);
		while(true) {
			if(joy.getRawButton(1)) {
				elevator.goSwitch();
			}
			else if(joy.getRawButton(2)) {
				elevator.goScale();
			}
			else if(joy.getRawButton(3)) {
				elevator.goFloor();
			}
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
