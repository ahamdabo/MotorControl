/**
 * 
 * @author Ahmed AbdelRazikAssaf 
 * @author Mohammed Alaa
 * @author Afaf Hassan
 * @author Ahmad AboELhassan
 * @version 1.01
 * @category Utilities
 * @since Dec 15th 2013
 * @see 
 * 
 *  This Java source code provides a solution for controlling DC motors through
 * PCs serial port.
 * 
 * The project includes 5 classes 
 * MainClass.java which includes the GUI and the main() method
 * Motor.java which represents the motor with its different characterstics 
 * SerialReader.java to read from the serial port
 * SerialWriter.java to write to the serial port
 * Constants.java which includes the different constants used with the code
 * 
 * 
 * The class constants includes the different constants used within the other classes
 * 
 * 
 */


// Modify this section for your application..

public class constants {

	static String COM = "COM38";
	final static String start = "4";
	final static String stop = "0";
	final static String Left = "c";
	final static String Right = "d";
	final static int paudRate = 9600;
	final static int NoMotorsConnectedError = 3;
	static String Direction = "Left";

}