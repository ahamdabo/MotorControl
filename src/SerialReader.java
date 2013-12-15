import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Ahmed
 *
 */

public class SerialReader implements Runnable {
	InputStream in;
/**
 * 
 * @param in
 */
	public SerialReader(InputStream in) {
		this.in = in;
	}

	// String msg;
	
	public void run() {
		byte[] buffer = new byte[1024];
		int len = -1;
		try {
			while ((len = this.in.read(buffer)) > -1) {
				// msg = new String(buffer, 0, len);
				// String[] s = msg.split("\\.");
				// constants.Speed = Integer.parseInt(s[0]);
				// constants.Direction = Integer.parseInt(s[1]);
				System.out.print(new String(buffer, 0, len));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}