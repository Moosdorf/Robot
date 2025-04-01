package robot;

import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;

public class DriveForward implements Behavior {

	@Override
	public boolean takeControl() {
		System.out.println("Drive forward");
		return true;
	}

	@Override
	public void action() {
		Motor.A.setSpeed(500);  // Set Motor A speed to 720 degrees per second
        Motor.C.setSpeed(500);  // do some for other motor

		Motor.A.forward();  // Motors will start moving forward
        Motor.C.forward(); 
	}

	@Override
	public void suppress() {
        Motor.A.flt();  // Motors will start moving forward
        Motor.C.flt(); 
		
	}

}
