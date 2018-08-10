package src.org.gosparx.team1126.controls;

import src.org.gosparx.team1126.controls.Automation.AutoState;

public class TeleAutomation {

	private Automation automation;
	
	private State state;
	
//	private int[][] currentTele;
	
	private final int[][] CLIMB = {
		//	{AutoState.DRIVES_TIMED.toInt(), 1000, -10},
		//	{AutoState.ELE_CLIMB.toInt()},
		//	{AutoState.ELE_DONE.toInt()},
			{AutoState.ELE_FLOOR.toInt()},
			{AutoState.ELE_DONE.toInt()},
			{AutoState.ELE_STAYDOWN.toInt()},
//			{AutoState.DRIVES_WAIT.toInt()},
			{AutoState.CLIMBING_ARMS.toInt(), 1},
			{AutoState.CLIMBING_PTO.toInt(), 1},
			{AutoState.DRIVES_CLIMB.toInt(), 1}
	};
	
	public enum State{

		STANDBY,
		CLIMBINGAUTO;
		
	}
	
	public TeleAutomation(Automation automation) {
		this.automation = automation;
		
		state = State.STANDBY;
	}
	
	public void init() {
		automation.setAuto(CLIMB);
		state = State.CLIMBINGAUTO;
		//add in an enum thingy soon?
	}
	
	public void execute() {
		switch(state) {
		case STANDBY:
			return;
		case CLIMBINGAUTO:
			if(automation.getState() != Automation.State.STANDBY) {
				automation.execute();
			} else {
				state = State.STANDBY;
			}
			return;
		default:
			break;
		}
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
}
