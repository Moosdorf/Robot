package robot;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class HitWall implements Behavior {
	SensorMode button = new EV3TouchSensor(SensorPort.S2).getTouchMode(); // button
	float[] buttonPressed = new float[button.sampleSize()];
	@Override
	public boolean takeControl() {
		button.fetchSample(buttonPressed, 0);
		if (buttonPressed[0] == 1) {
			System.out.println("Hit wall behavior");
		}
		return buttonPressed[0] == 1;
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
