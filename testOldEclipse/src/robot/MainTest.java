package robot;

import lejos.*;  // Import the Motor class
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;          // Import Delay class for pauses

public class MainTest {
	// home bool
	static boolean goHome = false;
	// get port instances
	static Port port1 = LocalEV3.get().getPort("S1"); // light sensor
	static Port port2 = LocalEV3.get().getPort("S2"); // button
	static Port port3 = LocalEV3.get().getPort("S3"); // infrared

	// SENSOR creation
	// Get an instance of the color EV3 sensor
	static BaseSensor colorSensor = new EV3ColorSensor(port1);
	
	// sensor for button press, pressed will be 0 or 1
	static BaseSensor buttonSensor = new EV3TouchSensor(port2);
	
	// ir sensor, can do seekmode or distance mode, will use seek for homebase
	static BaseSensor IRSensor = new EV3IRSensor(port3);
	
	// create the sample providers for each sensor
	static SampleProvider color = ((EV3ColorSensor) colorSensor).getColorIDMode();
	static SampleProvider distance = ((EV3IRSensor) IRSensor).getDistanceMode();
	static SampleProvider seeker = ((EV3IRSensor) IRSensor).getSeekMode();
	static SampleProvider pressed = ((EV3TouchSensor) buttonSensor).getTouchMode();
	
	// arrays to hold the sensor data
	static float[] colorSample = new float[color.sampleSize()];
	static float[] distanceSample = new float[distance.sampleSize()];
	static float[] pressedSample = new float[pressed.sampleSize()];
	static float[] seekerSample = new float[seeker.sampleSize()];
	
	static int speed = 400;
    public static void main(String[] args) {
    	//mainProgram();
    	testProgram();
    }
    
    public static void goBackward(double sec) {
        Motor.A.setSpeed(speed);  // Set Motor A speed to 720 degrees per second
        Motor.C.setSpeed(speed);  // do some for other motor

        Motor.A.backward();  // Motor A will start moving forward
        Motor.C.backward(); 

        Delay.msDelay((int) sec*1000);  
        
        // Stop Motor A and C
        Motor.A.flt(true);  
        Motor.C.flt(true);
    }
    
    public static void goForward() {

        Motor.A.setSpeed(speed);  // Set Motor A speed to 720 degrees per second
        Motor.C.setSpeed(speed);  // do some for other motor

        Motor.A.forward();  // Motors will start moving forward
        Motor.C.forward(); 
    }
    
    public static void goRight(double sec) {
    	Motor.C.setSpeed(500);
    	Motor.A.setSpeed(500);
    	
        Motor.A.forward();
        Motor.C.backward();
        
        Delay.msDelay((int) sec*1000);  

        // Stop Motor 
        Motor.C.flt(true);
        Motor.A.flt(true);  
    }
    
    public static void stop() {
    	Motor.A.flt(true);
    	Motor.C.flt(true);
    }
    
    public static void mainProgram() {
    	while(true) {

    		fetchData();
    		if (goHome) {
    			printSensorData();
    			if (seekerSample[0] == 0) {
    				if (seekerSample[1] < 5 && seekerSample[1] > 0) {
    					stop();
    					break;
    				}
    				goForward();
    			} else {
    				goRight(0.4);
    			}
    		}
    		else if (!Motor.A.isMoving()) {
    			goForward();
    		}
    		
    		// adjust movement based on obstacle or line
    		if (pressedSample[0] == 1.0) {
    			goBackward(1);
    			goRight(1.5);
    		} 
    		else if (distanceSample[0] < 14) {
    			stop();
    			goRight(1.5);
    		}
    		
    		// change course based on color, if correct color, beep.
    		if (colorSample[0] == 6.0) {
    			goBackward(1);
    			goRight(1.5);
    		}
    		else if (colorSample[0] == 2.0) {   
    			stop();
    			Sound.twoBeeps();
    			Delay.msDelay(1000); 
    			Sound.twoBeeps();
    			goForward();
    		}
    		if (Button.ENTER.isDown()) break; // hold enter down on the ev3 to stop the program
    		if (Button.LEFT.isDown()) goHome = true;
    	}
    }
    
    public static void testProgram() {
		
		while (true) {
			// clear ev3 monitor
			LCD.clear();

			// fetch sensor data
			fetchData();
			printSensorData();
			


			Delay.msDelay(2000); // delay the loop 3sec
			
			if (Button.ENTER.isDown()) break; // hold enter down on the ev3 to stop the program
		}

    }
    public static void printSensorData() {
		// print everything on the monitor
    	//System.out.println("Color: " + getColor());
		//System.out.println(distanceSample[0]);
		// System.out.println(pressedSample[0]);
    	System.out.println(seekerSample[0]);
    	System.out.println(seekerSample[1]);
    	
    }
    public static String getColor() {
    	String[] colors = {"NONE", "BLACK", "BLUE", "GREEN", "YELLOW", "RED", "WHITE", "BROWN"};
    	if (colorSample[0] >= 0 && colorSample[0] <= 7) 
    		return colors[(int) colorSample[0]];
    	return colorSample[0] + "";
    }
    
    public static void fetchData() {
		color.fetchSample(colorSample, 0);
		distance.fetchSample(distanceSample, 0);
		pressed.fetchSample(pressedSample, 0);
		seeker.fetchSample(seekerSample, 0);
    }

}