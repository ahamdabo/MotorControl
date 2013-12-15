import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author Ahmed
 * 
 */
public class Motor implements Runnable {
	private String speed;
	private String COM;
	private String Direction;
	private boolean connected = false;
	private SerialPort serialPort;
	private InputStream in;
	private OutputStream out;
	private CommPortIdentifier portIdentifier;
	private CommPort commPort;

	/**
	 * 
	 * @param COM
	 */
	Motor(String COM) {

		this.COM = COM;

		try {
			connect(this.COM);
			connected = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connected = false;
		}
	}

	/**
 * 
 */
	void start() {
		this.speed = "0";
		this.Direction = "31";
		writer(out, constants.start);
	}

	void stop() {
		writer(out, constants.stop);
		this.Direction = "0";
		this.speed = "0";
	}

	boolean isConnected() {
		return connected;
	}

	/**
	 * 
	 * @param spd
	 */
	void setSpeed(String spd) {
		writer(out, spd);
		this.speed = spd;
	}

	int getSpeed() {
		return Integer.parseInt(this.speed);
	}

	/**
	 * 
	 * @param dir
	 */
	void setDirection(String dir) {
		writer(out, dir);
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

				serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				(new Thread(new SerialReader(in))).start();
			} else {
				System.out.println("Error: Only serial ports are handled.");
			}
		}
	}

	/**
	 * 
	 * @param out
	 * @param msg
	 */
	void writer(OutputStream out, String msg) {
		try {
			this.out.write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

}
