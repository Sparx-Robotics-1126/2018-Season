package src.org.gosparx.team1126.robot;

import src.org.gosparx.team1126.controls.Autonomous;
import src.org.gosparx.team1126.controls.Controls;
import src.org.gosparx.team1126.controls.TeleOP;

public class RobotSystem extends Thread{

	private RobotState currentState;

	private Controls currentControl;
	private TeleOP teleopControl;
	private Autonomous autoControl;

	public RobotSystem(){
		//ALL THE SUBSYSTEMS
		currentState = RobotState.STANDBY;
		autoControl = new Autonomous();
		teleopControl = new TeleOP();
		currentControl = null;
	}

	public void init(){

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
