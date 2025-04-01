package robot;

import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class EdgeDetected implements Behavior{
	SampleProvider color;
	float[] samples;
	public EdgeDetected(EV3ColorSensor color) {
		this.color = color.getRedMode();
		samples = new float[this.color.sampleSize()];
	}
	@Override
	public boolean takeControl() {
		color.fetchSample(samples, 0);
		if (samples[0] < 0.23) {
			System.out.println("Edge found behavior");
		}
		return samples[0] < 0.23;
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
        Motor.C.flt(); 	
	}

}
