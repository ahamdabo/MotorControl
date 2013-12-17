import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ahmed
 * 
 */
public class SerialWriter {
	OutputStream out;
	String msg;

/**
 * 
 * @param out
 * @param msg
 */
	
	/**
	 * 
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