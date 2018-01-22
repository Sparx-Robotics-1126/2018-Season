package src.org.gosparx.team1126.controls;

public class TeleOP extends Controls{

	private State state;

	public TeleOP() {
		state = State.STANDBY;
	}
	
	@Override
	public void execute() {
		switch(state) {
		case STANDBY:
			break;
		case RUNNING: //comment out unused buttons
			//Joystick Buttons Left
			if(isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_LEFT)) {
				
			}
			if(isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_MIDDLE)) {
				
			}
			if(isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_RIGHT)) {
				
			}
			if(isPressedButton(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_TRIGGER)) {
				
			}
			//Axis Left
			if(isOffZeroAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_X_AXIS)) {
				//getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_X_AXIS);
			}
			if(isOffZeroAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS)) {
				//getAxis(CtrlMap.LEFTJOYSTICK, CtrlMap.JOY_Y_AXIS);
			}
			//POV Left
			if(isPressedPOV(CtrlMap.LEFTJOYSTICK, CtrlMap.POV_UP)) {
				
			}
			if(isPressedPOV(CtrlMap.LEFTJOYSTICK, CtrlMap.POV_RIGHT)) {
				
			}
			if(isPressedPOV(CtrlMap.LEFTJOYSTICK, CtrlMap.POV_DOWN)) {
				
			}
			if(isPressedPOV(CtrlMap.LEFTJOYSTICK, CtrlMap.POV_LEFT)) {
				
			}
			//Joystick Buttons Right
			if(isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_LEFT)) {
				
			}
			if(isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_MIDDLE)) {
				
			}
			if(isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_RIGHT)) {
				
			}
			if(isPressedButton(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_TRIGGER)) {
				
			}
			//Axis Right
			if(isOffZeroAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_X_AXIS)) {
				//getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_X_AXIS);
			}
			if(isOffZeroAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS)) {
				//getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS);
			}
			//POV Right
			if(isPressedPOV(CtrlMap.RIGHTJOYSTICK, CtrlMap.POV_UP)) {
				
			}
			if(isPressedPOV(CtrlMap.RIGHTJOYSTICK, CtrlMap.POV_RIGHT)) {
				
			}
			if(isPressedPOV(CtrlMap.RIGHTJOYSTICK, CtrlMap.POV_DOWN)) {
				
			}
			if(isPressedPOV(CtrlMap.RIGHTJOYSTICK, CtrlMap.POV_LEFT)) {
				
			}
			//xBox Buttons
			if(isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_A)) {
				
			}
			if(isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_B)) {
				
			}
			if(isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_X)) {
				
			}
			if(isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_Y)) {
				
			}
			if(isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_L1)) {
				
			}
			if(isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_R1)) {
				
			}
			if(isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_BACK)) {
				
			}
			if(isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_START)) {
				
			}
			if(isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_L3)) {
				
			}
			if(isPressedButton(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_R3)) {
				
			}
			//Axis Right
			if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_LEFT_X)) {
				//getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_X_AXIS);
			}
			if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_LEFT_Y)) {
				//getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS);
			}
			if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_RIGHT_X)) {
				//getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS);
			}
			if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_RIGHT_Y)) {
				//getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS);
			}
			if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_L2)) {
				//getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS);
			}
			if(isOffZeroAxis(CtrlMap.XBOXCONTROLLER, CtrlMap.XBOX_R2)) {
				//getAxis(CtrlMap.RIGHTJOYSTICK, CtrlMap.JOY_Y_AXIS);
			}
			//POV Right
			if(isPressedPOV(CtrlMap.XBOXCONTROLLER, CtrlMap.POV_UP)) {
				
			}
			if(isPressedPOV(CtrlMap.XBOXCONTROLLER, CtrlMap.POV_RIGHT)) {
				
			}
			if(isPressedPOV(CtrlMap.XBOXCONTROLLER, CtrlMap.POV_DOWN)) {
				
			}
			if(isPressedPOV(CtrlMap.XBOXCONTROLLER, CtrlMap.POV_LEFT)) {
				
			}
			break;
		default:
			System.out.println("oops");
			break;
		}
	}

	public enum State{
		RUNNING,
		STANDBY;
	}
	
	public void setState(State states) {
		state = states;
	}
	
}
