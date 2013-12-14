import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Motor {

	int speed;
	String COM;
	int Direction;
	boolean connected = false;
	SerialPort serialPort;
	InputStream in;
	OutputStream out;
	CommPortIdentifier portIdentifier;
	CommPort commPort;

	Motor(String COM) {
		speed = 50;
		Direction = constants.Right;
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
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	boolean isConnected() {
		return connected;
	}

	void setSpeed(String spd) {
		writer(out, spd);
		speed = Integer.parseInt("129");
	}

	void writer(OutputStream out, String msg) {

		try {
			this.out.write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	int getSpeed() {

		return constants.Speed;
	}

	void setDirection(int dir) {
		Direction = dir;
		writer(out, Integer.toString(dir));
	}

	int getDirection() {
		return constants.Direction;
		// return Direction;
	}

}
