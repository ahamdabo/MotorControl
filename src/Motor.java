/**
 * 
 * @author Ahmad AboELhassan
 * @version 1.01
 * @category Utilities
 * @since Dec 15th 2013
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
	 */

	Motor(String COM) throws Exception {
		this.COM = COM;
		connect(this.COM);
		connected = true;
	}

	public void disconnect() {
		if (serialPort != null) {
			// close the i/o streams.
			try {
				out.close();
				in.close();
				Thread.sleep(1000);
				serialPort.close();
				Thread.sleep(1000);

			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.print("Disconnected fails");
			}
			connected = false;
		}
	}
	
	public void closePort() {
		System.out.println("Closing: " );
		        new Thread(){
		        @Override
		        public void run(){
		            try{
		            in.close();
		            serialPort.close();
		            }catch (IOException ex) {}
		        }
		        }.start();
		    }

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

	int getSpeed() {
		return Integer.parseInt(this.speed);
	}

	/**
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

	int getDirection() {
		return Integer.parseInt(this.Direction);
	}

	/**
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

			//	(new Thread(new SerialReader(in))).start();

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

	void write(String str) throws Throwable {
		// TODO Auto-generated method stub
		writer(out, str);
	}

	/**
	 * 
	 * @param out
	 * @param msg
	 * @throws IOException
	 */
	private void writer(OutputStream out, String msg) throws IOException {
		this.out.write(msg.getBytes());
	}

}