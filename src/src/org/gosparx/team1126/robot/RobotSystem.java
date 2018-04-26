package src.org.gosparx.team1126.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.gosparx.team1126.controls.Automation;
import src.org.gosparx.team1126.controls.Autonomous;
import src.org.gosparx.team1126.controls.Controls;
import src.org.gosparx.team1126.controls.TeleAutomation;
import src.org.gosparx.team1126.controls.TeleOP;
import src.org.gosparx.team1126.subsytems.Acquisitions;
import src.org.gosparx.team1126.subsytems.Climbing;
import src.org.gosparx.team1126.subsytems.Drives;
import src.org.gosparx.team1126.subsytems.Drives.DriveState;
import src.org.gosparx.team1126.subsytems.Elevations;
import src.org.gosparx.team1126.util.DebuggerResult;

public class RobotSystem extends Thread{

	private RobotState currentState;

	private Drives drives;
	private Acquisitions acq;
	private Elevations ele;
	private Climbing climb;
	
	private Automation automation;
	private Controls currentControl;
	private TeleOP teleopControl;
	private Autonomous autoControl;
	
	public RobotSystem(){
		//ALL THE SUBSYSTEMS
		drives = new Drives();
		acq = new Acquisitions();
		ele = new Elevations();
		climb = new Climbing();
		drives.init();
		acq.init();
		ele.init();
		climb.init();
		currentState = RobotState.STANDBY;
		automation = new Automation(drives, acq, ele, climb);
		autoControl = new Autonomous(automation);
		teleopControl = new TeleOP(drives, acq, ele, climb, automation);
		currentControl = null;
		Compressor compress = new Compressor(0);
		compress.setClosedLoopControl(true);
	}

	public void init(){
		drives.start();
		acq.start();
		ele.start();
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
		drives.toAuto();
		ele.startInit();
		currentControl = autoControl;
		currentState = RobotState.AUTO;
	}

	public void teleStart(){
		drives.toTele();
		ele.startInit();
		teleopControl.init();
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
		drives.addObjectsToShuffleboard();
		ele.putThingsOnDashboard();
	}

}
