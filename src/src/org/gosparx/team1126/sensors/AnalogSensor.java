package src.org.gosparx.team1126.sensors;

import edu.wpi.first.wpilibj.AnalogInput;

public class AnalogSensor {
	
	private AnalogInput analogSensor;
	private double threshold;
	
	public AnalogSensor(int channel, double threshold) {
		analogSensor = new AnalogInput(channel);
		this.threshold = threshold;
	}
	
	/**
	 * Checks if the voltage is greater than the threshold.
	 * @return if the voltage is greater than the threshold.
	 */
	public boolean get() {
		return analogSensor.getVoltage() > threshold;
	}
	
	public double getVoltage(){
		return analogSensor.getVoltage();
	}
	
}
