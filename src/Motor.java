/**
 *  
 * @author Ahmed AbdelRazikAssaf 
 * @author Mohammed Alaa
 * @author Afaf Hassan
 * @author Ahmad AboELhassan
 * @version 1.01
 * @category Utilities
 * @since Dec 15th 2013
 * ITI - Embedded Systems 2013 - Intake34
 * 
 */

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Motor {// implements Runnable {
	private String speed = "4";
	private String COM;
	private String Direction;
	private boolean connected = false;
	private SerialPort serialPort;
	private InputStream in;
	private OutputStream out;
	private CommPortIdentifier portIdentifier;
	private CommPort commPort;

	/**
	 * @param COM
	 * @throws Throwable
	 *             The Motor constructor initialize a connection between PC &
	 *             Kit through the given COM port
	 */

	Motor(String COM) throws Exception {
		this.COM = COM;
		connect(this.COM);
		connected = true;
	}

	/**
	 * This method is used to close the port..
	 * 
	 */
	void closePort() {
		System.out.println("Closing..");
		new Thread() {
			@Override
			public void run() {
				try {
					in.close();
					serialPort.close();
					System.out.println("Closed.");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Start Motor sets direction to left and speed to 4
	 * 
	 * @param speed
	 * 
	 */
	void start(int speed) {

		this.speed = Integer.toString(speed);
		this.Direction = constants.Left;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer(out, this.Direction);
			writer(out, this.speed);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		constants.Direction = "Left";
	}

	/**
	 * Stop the motor - sets the speed to 0 and the direction to left
	 */
	void stop() {
		try {
			writer(out, constants.stop);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.Direction = constants.Left;
		this.speed = "0";
	}

	boolean isConnected() {
		return connected;
	}

	/**
	 * set speed
	 * 
	 * @param spd
	 * @throws Throwable
	 */
	void setSpeed(String spd) {
		try {
			writer(out, spd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.speed = spd;
	}

	/**
	 * returns speed value
	 * 
	 * @return int
	 */
	int getSpeed() {
		return Integer.parseInt(this.speed);
	}

	/**
	 * set direction of the motor
	 * 
	 * @param dir
	 * @throws Throwable
	 */
	void setDirection(String dir) {
		try {
			writer(out, dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Direction = dir;
	}

	/**
	 * gets direction of the motor
	 * 
	 * @return int
	 */
	int getDirection() {
		return Integer.parseInt(this.Direction);
	}

	/**
	 * Connect to the given serial port
	 * 
	 * @param portName
	 * @throws Exception
	 */
	void connect(String portName) throws Exception {
		portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			commPort = portIdentifier.open(this.getClass().getName(), 1000);
			if (commPort instanceof SerialPort) {
				serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(constants.paudRate,
						SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				// In case we're planning to read values..
				// (new Thread(new SerialReader(in))).start();
			} else {
				System.out.println("Error: Only serial ports are handled.");
			}
		}
	}

	/**
	 * 
	 * @param str
	 * @throws Exception
	 */
	/**
	 * this method is to check if the Kit still connected to the PC
	 * 
	 * @param str
	 * @throws Throwable
	 */
	void test(String str) throws Throwable {
		// TODO Auto-generated method stub
		writer(out, str);
	}

	/**
	 * this method is to write to the serial port directly
	 * 
	 * @param out
	 * @param msg
	 * @throws IOException
	 */
	private void writer(OutputStream out, String msg) throws IOException {
		this.out.write(msg.getBytes());
	}

}