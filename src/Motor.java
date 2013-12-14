import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Motor implements Runnable {

	int speed;
	String COM;
	String Direction;
	boolean connected = false;
	SerialPort serialPort;
	InputStream in;
	OutputStream out;
	CommPortIdentifier portIdentifier;
	CommPort commPort;

	Motor(String COM) {
		// speed = 50;
		// Direction = constants.Left;
		this.COM = COM;
		try {
			connect(COM);
			connected = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connected = false;
		}
	}

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

	void writer(OutputStream out, String msg) {
		try {
			this.out.write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	boolean isConnected() {
		return connected;
	}

	void setSpeed(String spd) {
		writer(out, spd);
		speed = Integer.parseInt(spd);
	}

	int getSpeed() {
		return Integer.parseInt(constants.Speed);
	}

	void setDirection(String dir) {
		writer(out, dir);
		Direction = dir;
	}

	int getDirection() {

		return Integer.parseInt(constants.Direction);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

}
