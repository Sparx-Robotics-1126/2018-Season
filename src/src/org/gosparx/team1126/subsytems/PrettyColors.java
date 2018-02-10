package src.org.gosparx.team1126.subsytems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import src.org.gosparx.team1126.util.DebuggerResult;

public class PrettyColors extends GenericSubsytem 
{
	private static SpeedController colorController;
	
	private State lightState;

	//private boolean colorState = false;

	public PrettyColors ()
	{
		super("Pretty Colors");
	}

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		colorController = new Spark(0);
	}

	@Override
	public void execute() 
	{
		// TODO Auto-generated method stub
		//if (colorState) 
		//{
		//	colorController.set(0.95);
		//}
		//else 
		//{
		//	colorController.set(0.61);
		//}
		//colorState = !colorState;
		switch(lightState)
		{
		case blue:
			colorController.set(0.87);
			break;
			
		case breathingBlue:
			colorController.set(-0.15);
			break;
			
		case breathingRed:
			colorController.set(-0.17);
			break;
			
		case colorOneBreathing:
			colorController.set(0.09);
			break;
			
		case colorOneHeartbeat:
			colorController.set(0.05);
			break;
			
		case colorOneLightChase:
			colorController.set(0.01);
			break;
			
		case colorTwoBreathing:
			colorController.set(0.29);
			break;
			
		case colorTwoHeartbeat:
			colorController.set(0.25);
			break;
			
		case colorTwoLightChase:
			colorController.set(0.21);
			break;
			
		case green:
			colorController.set(0.77);
			break;
			
		case heartbeatBlue:
			colorController.set(-0.23);
			break;
			
		case heartbeatRed:
			colorController.set(-0.25);
			break;
			
		case lighChaseRed:
			colorController.set(-0.31);
			break;
			
		case lightChaseBlue:
			colorController.set(-0.29);
			break;
			
		case orange:
			colorController.set(0.65);
			break;
			
		case red:
			colorController.set(0.61);
			break;
			
		case violet:
			colorController.set(0.91);
			break;
			
		case yellow:
			colorController.set(0.69);
			break;
			
		default:
			break;
		
		}
	}


	@Override
	public void forceStandby() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long sleepTime() 
	{
		// TODO Auto-generated method stub
		return 1000;
	}

	@Override
	public DebuggerResult[] debug() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public enum State
	{
		red,
		orange,
		yellow,
		green,
		blue,
		violet,
		colorOneLightChase,
		colorTwoLightChase,
		lighChaseRed,
		lightChaseBlue,
		colorOneHeartbeat,
		colorTwoHeartbeat,
		heartbeatRed,
		heartbeatBlue,
		colorOneBreathing,
		colorTwoBreathing,
		breathingRed,
		breathingBlue;
	}
}
