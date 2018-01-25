package src.org.gosparx.team1126.robot;


import edu.wpi.first.wpilibj.IterativeRobot;
import src.org.gosparx.team1126.subsytems.Elevations;

public class Robot extends IterativeRobot{

	Elevations elevator;
	
	@Override
	public void robotInit() {
		elevator = new Elevations(); //For test	
		System.out.println("Test");
		
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		elevator.init();
	}

	
	@Override
	public void teleopPeriodic() {
		elevator.execute(); //For test
	}

	}
