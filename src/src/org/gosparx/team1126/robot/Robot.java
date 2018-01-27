package src.org.gosparx.team1126.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import src.org.gosparx.team1126.subsytems.Drives;

public class Robot extends IterativeRobot{
	
	private Joystick joy1;
	
	private Joystick joy2;
	
	private Drives drives;

	@Override
	public void robotInit() {
		joy1 = new Joystick(1);
		joy2 = new Joystick(2);
		drives = new Drives();
		drives.init();
	}
	
	@Override
	public void autonomousInit() {
		drives.start();
		drives.move(120, 60);
		while(!drives.isDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		drives.turn(165, 40);
		while(!drives.isDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		drives.move(120, 60);
	}
	
	@Override
	public void teleopInit() {
		drives.changeState(Drives.DriveState.TELEOP);
		while(true) {
			drives.joysticks((int)(joy1.getY()*100), (int)(joy2.getY()*100));
			drives.execute();
		}
	}
	
	@Override
	public void disabledInit() {
		
	}
}
