package src.org.gosparx.team1126.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot{

	private RobotSystem system;
	
	@Override
	public void robotInit() {
		system = new RobotSystem();
		system.init();
		system.start();
		System.out.println("***INIT ROBOT COMPLETE***");
	}
	
	@Override
	public void autonomousInit() {
		System.out.println("Auto Started");
		system.autoStart();
	}
	
	@Override
	public void teleopInit() {
		System.out.println("Tele Started");
		system.teleStart();
	}
	
	@Override
	public void disabledInit() {
		System.out.println("Disabled Started");
		system.disable();
	}
}
