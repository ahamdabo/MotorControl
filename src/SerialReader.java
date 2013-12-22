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

import java.io.IOException;
import java.io.InputStream;

public class SerialReader implements Runnable {
	InputStream in;

	/**
	 * 
	 * @param in
	 */

	public SerialReader(InputStream in) {
		this.in = in;
	}

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
			System.out.println("Error in reading.. ");
		}
	}
}