package src.org.gosparx.team1126.subsytems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import src.org.gosparx.team1126.util.DebuggerResult;

public class Elevations extends GenericSubsytem {

	float height; //Height of elevator
	float targetHeight;
	WPI_TalonSRX motor1;
	WPI_TalonSRX motor2;
	DigitalInput limitSwitch;
	
	enum State {
		standBy,
		moveUp,
		moveDown
	}
	
	State state;
	
	@Override
	public void init() {
		height = 0;
		targetHeight = 0;
		motor1 = new WPI_TalonSRX(0); //TODO: get actual device ID
		motor2 = new WPI_TalonSRX(0);
		limitSwitch = new DigitalInput(0); //TODO: get channel  
		while(limitSwitch.get())
		{
			
		}
		//Make motor go to the bottom until limit switch is hit
		//Then reset encoder 
	}

	@Override
	public void execute() {
		switch(state)
		{
			case standBy:
			{
				break;
			}
			case moveUp:
			{
				if(targetHeight+1>height && targetHeight-1<height)
				{
					state = State.standBy;
					break;
				}
				break;
			}
			case moveDown:
			{
				if(targetHeight+1>height && targetHeight-1<height)
				{
					state = State.standBy;
					break;
				}
				break;
			}
		}
		
	}


	@Override
	public void logger() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DebuggerResult[] debug() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void move(int newHeight) {
		targetHeight = newHeight; 
	}
	
	public void stop()
	{
		state = State.standBy;
		motor1.stopMotor();
		motor2.stopMotor();
	}
	
}