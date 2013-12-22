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
import java.io.OutputStream;

public class SerialWriter {
	OutputStream out;
	String msg;

/**
/ * 
 * @param out
 * @param msg
 */
	
	/**
 	 * @return 
	 * @category
	 * @param out
	 * @param msg
	 */
	public SerialWriter(OutputStream out, String msg) {
		this.out = out;
		this.msg = msg;

		try {
		this.out.write(this.msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}