package src.org.gosparx.team1126.controls;

public class TeleOP extends Controls{

	private State state;

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
	
}
