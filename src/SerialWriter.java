import java.io.IOException;
import java.io.OutputStream;

public class SerialWriter {
	OutputStream out;
	String msg;
	String serialMessage = "AT\r\n";

	public SerialWriter(OutputStream out, String msg) {
		this.out = out;
		this.msg = msg;

		try {
			this.out.write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/*
	 * public void run() { try { this.out.write(msg.getBytes()); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * } }
	 */
}