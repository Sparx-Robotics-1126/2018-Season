package src.org.gosparx.team1126.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.Compressor;
import src.org.gosparx.team1126.controls.Autonomous;
import src.org.gosparx.team1126.controls.Controls;
import src.org.gosparx.team1126.controls.TeleOP;
import src.org.gosparx.team1126.subsytems.Acquisitions;
import src.org.gosparx.team1126.subsytems.Drives;
import src.org.gosparx.team1126.subsytems.Drives.DriveState;
import src.org.gosparx.team1126.util.DebuggerResult;

public class RobotSystem extends Thread{

	private RobotState currentState;

	private Drives drives;
	private Acquisitions acq;
	
	private Controls currentControl;
	private TeleOP teleopControl;
	private Autonomous autoControl;

	public RobotSystem(){
		//ALL THE SUBSYSTEMS
		drives = new Drives();
		acq = new Acquisitions();
		drives.init();
		acq.init();
		currentState = RobotState.STANDBY;
		autoControl = new Autonomous(drives, acq);
		teleopControl = new TeleOP(drives, acq);
		currentControl = null;
		Compressor compress = new Compressor(0);
		compress.setClosedLoopControl(true);
	}

	public void init(){
		drives.start();
		acq.start();
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
	
	public void test() {
		drives.forceStandby();
		System.out.println(Arrays.toString(drives.debug()));
		
	}

}
