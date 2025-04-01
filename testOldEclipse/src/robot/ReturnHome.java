package robot;

import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class ReturnHome implements Behavior {
	SensorMode home;
	float[] homeSamples;
	Home goHome;
	
	public ReturnHome(EV3IRSensor home, Home goHome) {
		this.home = home.getSeekMode();
		homeSamples = new float[this.home.sampleSize()];
		this.goHome = goHome;
	}
	@Override
	public boolean takeControl() {
		return goHome.goHome;
	}

	@Override
	public void action() {
		home.fetchSample(homeSamples, 0);
		
		if (homeSamples[0] > 0) {		
			Motor.A.forward();
		} else if (homeSamples[0] < 0) {
			Motor.C.forward();
		}
		Delay.msDelay(300);
		
		if (homeSamples[0] == 0) {			
			if (homeSamples[1] > 10) {
				Motor.C.forward();		
				Motor.A.forward();				
			} else {
				System.out.println("Home found, shutting down");
				System.exit(0);
			}
		} 
		
		Motor.A.flt();
		Motor.C.flt();
	}

	@Override
	public void suppress() {
        Motor.A.flt();  // Motors will start moving forward
        Motor.C.flt(); 
	}

}
