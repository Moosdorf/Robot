package robot;

import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.subsumption.*;
import lejos.utility.Delay;

public class ColorFound implements Behavior {
	EV3ColorSensor color;
	public ColorFound(EV3ColorSensor color) {
		this.color = color;
	}
	@Override
	public boolean takeControl() {
		if (color.getColorID() == Color.BLUE) {
			System.out.println("Color found behavior");
		}
		return color.getColorID() == Color.BLUE;
	}

	@Override
	public void action() {
		Sound.setVolume(100);
		Sound.twoBeeps();
		Motor.A.stop();
		Motor.C.stop();
		Delay.msDelay(1000); 
		Sound.twoBeeps();
		Sound.setVolume(10);
	}

	@Override
	public void suppress() {
        Motor.A.flt();  // Motors will start moving forward
        Motor.C.flt(); 	
	}
}
