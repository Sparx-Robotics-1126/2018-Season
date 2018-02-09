package src.org.gosparx.team1126.util;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class MotorGroup extends SpeedControllerGroup{
	public SpeedController[] speedControllers;
	
	/**
	 * MotorGroup extends speedControllerGroup, so it implements sendable and can control multiple motors at once
	 * @param arg0 - the first motor in this group
	 * @param arg1 - the second motor in this group
	 */
	public MotorGroup(SpeedController arg0, SpeedController arg1) {
		super(arg0, arg1);
		speedControllers = new SpeedController[]{arg0, arg1};
	}
	
	/**
	 * MotorGroup extends speedControllerGroup, so it implements sendable and can control multiple motors at once
	 * @param arg0 - the first motor in this group
	 * @param arg1 - the second motor in this group
	 * @param arg2 - the third motor in this group
	 */
	public MotorGroup(SpeedController arg0, SpeedController arg1, SpeedController arg2) {
		super(arg0, arg1, arg2);
		speedControllers = new SpeedController[]{arg0, arg1, arg2};	
	}

	/**
	 * Gets the speed controller at motorNum, if motorNum is bigger than the number of 
	 		motors in this motorGroup return null. Motor numbering starts at 0.
	 * @param motorNum - the number of the motor in this group
	 * @return - the motorNumth motor in this group
	 */
	public SpeedController getSpeedController(int motorNum) {
		if(motorNum > speedControllers.length)
			return null;
		return speedControllers[motorNum];
	}
	
	/**
	 * Get the size of this motor group
	 * @return - the number of motors in this group
	 */
	public int getMtrCount() {
		return speedControllers.length;
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
	
	/**
	 * gets the specific motor
	 * @param motorNum - the motor number
	 * @return - the motor
	 */
	public SpeedController getMotor(int motorNum) {
		if(motorNum >= speedControllers.length) {
			return null;
		}
		return speedControllers[motorNum];
	}
	
	/**
	 * inverts each individual motor when called
	 * @param arg0 - the desired state of the motor group
	 */
	public void setInverted(boolean arg0) {
		for(int i = 0; i < speedControllers.length; i++) {
			speedControllers[i].setInverted(arg0);
		}
	}
	

}