package src.org.gosparx.team1126.subsytems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import src.org.gosparx.team1126.util.DebuggerResult;

public class PrettyColors extends GenericSubsytem 
{
	private static SpeedController colorController;

	private boolean colorState = false;

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
		if (colorState) 
		{
			colorController.set(0.95);
		}
		else 
		{
			colorController.set(0.61);
		}
		colorState = !colorState;
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
}
