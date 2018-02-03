package src.org.gosparx.team1126.robot;

import src.org.gosparx.team1126.controls.Autonomous;
import src.org.gosparx.team1126.controls.Controls;
import src.org.gosparx.team1126.controls.TeleOP;
import src.org.gosparx.team1126.subsytems.Drives;
import src.org.gosparx.team1126.subsytems.Drives.DriveState;

public class RobotSystem extends Thread{

	private RobotState currentState;

	private Drives drives;
	
	private Controls currentControl;
	private TeleOP teleopControl;
	private Autonomous autoControl;

	public RobotSystem(){
		//ALL THE SUBSYSTEMS
		drives = new Drives();
		drives.init();
		currentState = RobotState.STANDBY;
		autoControl = new Autonomous(drives);
		teleopControl = new TeleOP(drives);
		currentControl = null;
	}

	public void init(){
		drives.start();
	}

	@Override
	public void run(){
		while(true){
			switch(currentState){
				case STANDBY:
					break;
				case AUTO:
				case TELE:
					currentControl.execute();
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}
	}

	public void autoStart(){
		autoControl.initAuto();
		currentControl = autoControl;
		currentState = RobotState.AUTO;
	}

	public void teleStart(){
		drives.changeState(DriveState.TELEOP);
		currentControl = teleopControl;
		currentState = RobotState.TELE;
	}

	public void disable(){
		currentState = RobotState.STANDBY;
	}

	public enum RobotState{
		STANDBY,
		AUTO,
		TELE;
	}

}
