package src.org.gosparx.team1126.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import src.org.gosparx.team1126.util.Logger;


public class Robot extends IterativeRobot{
	
	private RobotSystem system;
	
	@Override
	public void robotInit() {
		system = new RobotSystem();
		system.init();
		system.start();
		Logger.getInstance().log("ROBOT", "robotInit", "***INIT ROBOT COMPLETE***");
	}
	
	@Override
	public void autonomousInit() {
		Logger.getInstance().log("ROBOT", "autonomousInit", "Auto Started");
		system.autoStart();
	}
	
	@Override
	public void teleopInit() {
		Logger.getInstance().log("ROBOT", "autonomousInit", "Tele Started");
		system.teleStart();
	}
	
	@Override
	public void disabledInit() {
		Logger.getInstance().log("ROBOT", "disabledInit", "Disabled Started");
		system.disable();
	}
	
	@Override
	public void testInit() {
		system.test();
	}
}