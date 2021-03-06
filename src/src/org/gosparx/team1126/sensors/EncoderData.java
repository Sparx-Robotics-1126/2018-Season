package src.org.gosparx.team1126.sensors;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * Class for obtaining more reliable data from an encoder.
 * @author Mike Bortfeldt
 * @author Solis Knight
 * @version 1.5 Pre 2012
 */
public class EncoderData extends SendableBase{
    private Encoder controlled;
    private Counter counter;
    private double distPerTickForward;
    private double distPerTickReverse;
    private long lastTime;
    private long lastEncoderCount = 0;
    private long lastSpeedCount = 0;
    private double speed = 0;
    private long forwardCount = 0;
    private long reverseCount = 0;
    private long deltaCount = 0;
    
    private boolean USE_COUNTER;
    
    /**
     * Constructor for the EncoderData.
     * @param controlled - the Encoder we wish to obtain accurate speeds for.
     * @param distPerTick - the distance per tick of this particular encoder.
     */
    public EncoderData(Encoder controlled, double distPerTick){
        this.controlled = controlled;
        controlled.setDistancePerPulse(distPerTick);
        distPerTickForward = distPerTick;
        distPerTickReverse = distPerTick;
        lastTime = getCurrentTime();
        USE_COUNTER = false;
    }
    
    public EncoderData(Counter controlled, double distPerTick){
        this.counter = controlled;
        distPerTickForward = distPerTick;
        distPerTickReverse = distPerTick;
        lastTime = getCurrentTime();
        USE_COUNTER = true;
    }
    
    /**
     * Method to accurately calculate speeds based on an encoder.  This routine
     * should be run with a minimum of 18 milliseconds between executions
     * to allow for enough time to get an accurate speed calculation.  This routine
     * would probably benefit from being run as a task within a timer object every
     * 5 milliseconds or so utilizing data from the past 20 milliseconds (or making
     * it a user defined interval)
     */
    public void calculateSpeed() {
        long currentTime = getCurrentTime();
        long encoderCount = USE_COUNTER ? counter.get() : controlled.get();
        long elapsedTime = currentTime - lastTime;
        long tempCount;
        
        deltaCount = encoderCount - lastEncoderCount;
        lastEncoderCount = encoderCount;
        
        if (deltaCount >= 0)
        	forwardCount += deltaCount;
        else
        	reverseCount += deltaCount;
        
        if (elapsedTime < 18000)
            return;
        
        tempCount = encoderCount - lastSpeedCount;
        lastSpeedCount = encoderCount;
        lastTime = currentTime;
        
        speed = ((tempCount > 0 ? distPerTickForward : 
        	distPerTickReverse) * tempCount) /
        	(elapsedTime / 1000000.0);
    }
    
    /**
     * Getter method for the calculated speed.
     * @return speed - the 'speed' the tracked object is moving based on the encoder.
     */
    public double getSpeed(){
        return speed;
    }
    
    /**
     * Get the distance the robot has driven since the last reset.
     *
     * @return The distance driven since the last reset as scaled by the value from setDistancePerPulse().
     */
    public double getDistance() {
        return (USE_COUNTER ? counter.get() * distPerTickForward : 
        	((forwardCount * distPerTickForward) + (
        	reverseCount * distPerTickReverse)));
    }

    /**
     * Calculates the incremental distance traveled for the interval defined by calls to 
     * calculateSpeed.
     */
    
    public double getIncrementalDistance() {
    	return (((double) deltaCount) * ((deltaCount > 0) ? 
        		distPerTickForward : distPerTickReverse));
    }
    
    /**
     * Set the Reverse Distance per Tick.  Only required if the reverse distance is different
     * from the forward distance
     */
    
    public void setReverseDistancePerPulse ( double reverseDist ){
    	distPerTickReverse = reverseDist;
    }
    
    /**
     * Resets the encoder data
     */
    public void reset(){
        if (USE_COUNTER)
            counter.reset();
        else
            controlled.reset();
        
        lastTime = getCurrentTime();
        lastEncoderCount =  USE_COUNTER ? counter.get() : controlled.get();
        forwardCount = 0;
        reverseCount = 0;
        deltaCount = 0;
    }
    
    /**
     * Returns the distance per tick forward.
     * @return
     */
    private double getDistPerTickForward() {
    	return distPerTickForward;
    }
    
    /**
     * @return the time (in seconds) that the encoder has updated its values 
     */
    public double getLastReadingTime(){
        return getCurrentTime() - lastTime;
    }
    
    /**
     * Get the current time 
     * @return - the time in milliseconds
     */
    private long getCurrentTime() {
    	return (long)(Timer.getFPGATimestamp()*(Math.pow(10., -6.)));
    }

	@Override
	public void initSendable(SendableBuilder arg0) {
		arg0.setSmartDashboardType("Encoder");
		arg0.addDoubleProperty("Speed", this::getSpeed, null);
		arg0.addDoubleProperty("Distance", this::getDistance, null);
		arg0.addDoubleProperty("Distance per Tick", this::getDistPerTickForward, null);
		
		
	}

//	@Override
//	public void initSendable(SendableBuilder arg0) {
//	}
}
