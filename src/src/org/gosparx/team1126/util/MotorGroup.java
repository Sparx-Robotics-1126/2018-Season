package src.org.gosparx.team1126.util;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class MotorGroup extends SpeedControllerGroup{
	public SpeedController[] speedControllers;
	
	public MotorGroup(SpeedController arg0, SpeedController arg1, SpeedController arg2) {
		super(arg0, arg1);
		speedControllers = new SpeedController[]{arg0, arg1, arg2};
	}

	/**
	 * Sets the mode of operation during neutral throttle output for each speedController that is a WPI_TalonSRX 
	 * @param neutralMode - The desired mode of operation when the Controller output throttle is neutral (ie brake/coast)
	 */
	public void setNeutralMode(NeutralMode neutralMode) {
		for(int i = 0; i < speedControllers.length; i++) {
			if(speedControllers[i].getClass().equals(WPI_TalonSRX.class))
				((WPI_TalonSRX)speedControllers[i]).setNeutralMode(neutralMode);
		}
	}
	

}
