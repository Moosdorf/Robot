package robot;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Robot {

	public static void main(String[] args) {
		EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
		EV3IRSensor ir = new EV3IRSensor(SensorPort.S3);
		Home home = new Home();
		
		Behavior b1 = new DriveForward();
		Behavior b2 = new ColorFound(color);
		Behavior b3 = new ReturnHome(ir, home);
		Behavior b4 = new DistanceCheck(ir);
		Behavior b5 = new EdgeDetected(color);
		Behavior b6 = new HitWall();
		Behavior[] bArray = {b1, b2, b3, b4, b5, b6};
		final Arbitrator arby = new Arbitrator(bArray);
		
		Thread arbyThread = new Thread(new Runnable() {
			@Override
			public void run() {
				arby.go(); 
			}
			
		});
		arbyThread.start();
		
		
		
		while (true) {
			if (Button.LEFT.isDown()) {
				home.goHome = true;
			}
			if (Button.ENTER.isDown()) {
				arby.stop();
				System.out.println("Trying to shut down");
				System.exit(0);
			}
		}
	}
}

