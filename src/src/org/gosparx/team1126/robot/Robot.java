package src.org.gosparx.team1126.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import src.org.gosparx.team1126.subsytems.Acquisitions;

public class Robot extends IterativeRobot{

	
	private Acquisitions acquisition;
	@Override
	public void robotInit() {
		acquisition = new Acquisitions();
		acquisition.init();
	}
	
	@Override
	public void autonomousInit() {
		
	}
	
	@Override
	public void teleopInit() {
		Joystick joy = new Joystick(1);
		acquisition.start();
		System.out.println("I've started");
		while (true){
			
			if(joy.getRawButton(1)){
				
				System.out.print("entering setAcquire");
				acquisition.setAcquire();
			
			}
			else if(joy.getRawButton(2)){
			
				System.out.print("entering setRaise");
				acquisition.setRaise();
				
			}
			else if(joy.getRawButton(3)){
				
				System.out.print("entering setScore");
				acquisition.setScore();
			}
			else if(joy.getRawButton(4)){
				
				System.out.print("entering setStandby");
				acquisition.setStandby();
				
			}
			
			
		}
	}
	
	@Override
	public void disabledInit() {
		
	}
}
