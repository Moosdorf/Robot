package robot;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.subsumption.*;
import lejos.utility.Delay;

public class DistanceCheck implements Behavior {
	SensorMode distance;
	float[] distanceSamples;
	public DistanceCheck(EV3IRSensor distanceSensor) {
		this.distance = distanceSensor.getDistanceMode();
		distanceSamples = new float[distance.sampleSize()];
	}

	
	@Override
	public boolean takeControl() {
		distance.fetchSample(distanceSamples, 0);
		if (distanceSamples[0] < 20) {
			System.out.println("Distance behavior");
		}
		return distanceSamples[0] < 20;
	}

	@Override
	public void action() {
		Motor.A.backward();
		Motor.C.backward();
		Delay.msDelay(1000); 
		Motor.A.flt();
		Delay.msDelay(600); 
		Motor.C.flt();
		
	}

	@Override
	public void suppress() {
        Motor.A.flt();  // Motors will start moving forward
	}
}
