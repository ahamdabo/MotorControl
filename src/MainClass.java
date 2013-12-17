/**
 * 
 * @author Ahmad AboELhassan
 * @version 1.00
 * @category Utilities
 * @since Dec 15th 2013
 * 
 * 
 */

import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainClass {// implements Runnable {

	static Motor motorLeft; // , motorRight;
	// Various GUI components and info
	public static JFrame mainFrame = null;
	public static JTextField serialCommand = null;
	public static JButton connectButton = null;
	public static JToggleButton StartStop = null;
	public static JLabel DirectionLabel = null;
	public static JLabel SpeedLabel = null;
	public static JLabel CommandLine = null;
	public static JRadioButton left = null;
	public static JRadioButton right = null;
	public static JDialog messageBox = null;
	static final int FPS_MIN = 0;
	static final int FPS_MAX = 9;
	static final int FPS_INIT = 4; // initial speed
	public static JSlider slider = null;
	public static int flag = 0;
	static JPanel pane = null;
	static ActionAdapter buttonListener = null, button2Listener = null;

	// Values which is passed to direction and speed labels..
	static int dir, sp;
	static ActionListener leftl = null;
	static ActionListener rightl = null;
	static ChangeListener l = null;

	static List<String> getAvailablePorts() {
		List<String> list = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier
				.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				list.add(portId.getName() );
			}
		}
		return list;
	}

	private static JPanel initOptionsPane() {

		// Slider listener
		l = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				speedControl();
			}
		};

		// Left Direction
		leftl = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				left.setSelected(true);
				right.setSelected(false);
				try {
					motorLeft.setDirection(constants.Left);
					DirectionLabel.setText("Direction: Left");
					// constants.Direction = "Left";
					constants.Direction = "Left";
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		};

		// Right Direction
		rightl = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				left.setSelected(false);
				right.setSelected(true);
				try {
					motorLeft.setDirection(constants.Right);
					DirectionLabel.setText("Direction: Right");
					constants.Direction = "Right";
				}

				catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		};

		// Connect and StartStop listener
		JPanel optionsPane = new JPanel(new GridLayout(4, 1));
		pane = new JPanel(new GridLayout(1, 2));
		optionsPane.add(pane);
		JPanel buttonPane = new JPanel(new GridLayout(1, 2));

		buttonListener = new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
				// custom title, error icon

				new MainClass();

				connect();
			}
		};

		button2Listener = new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
				new MainClass();
				StartStp();

			}
		};

		// slider
		slider = new JSlider(JSlider.VERTICAL, FPS_MIN, FPS_MAX, FPS_INIT);
		slider.addChangeListener(l);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setEnabled(false);

		// connect button
		connectButton = new JButton("Connect");
		connectButton.setMnemonic(KeyEvent.VK_C);
		connectButton.setActionCommand("Send");
		connectButton.addActionListener(buttonListener);
		connectButton.setEnabled(true);
		// start stop button
		StartStop = new JToggleButton("Start");
		StartStop.setMnemonic(KeyEvent.VK_C);
		StartStop.setActionCommand("Start");
		StartStop.addActionListener(button2Listener);
		StartStop.setEnabled(false);

		// speed label
		SpeedLabel = new JLabel("Speed:  ");
		SpeedLabel.setEnabled(false);

		// direction label
		DirectionLabel = new JLabel("Direction:  ");
		DirectionLabel.setEnabled(false);

		// left radio button
		left = new JRadioButton("left");
		left.addActionListener(leftl);
		left.setSelected(true);
		left.setEnabled(false);

		// right radio button
		right = new JRadioButton("Right");
		right.addActionListener(rightl);
		right.setEnabled(false);

		// add to pane
		buttonPane.add(connectButton);
		buttonPane.add(StartStop);
		buttonPane.add(left);
		buttonPane.add(right);

		// add to pane
		optionsPane.add(slider);
		optionsPane.add(DirectionLabel);
		optionsPane.add(SpeedLabel);
		optionsPane.add(buttonPane);
		return optionsPane;

	}

	static void speedControl() {
		try {

			motorLeft.setSpeed(Integer.toString(slider.getValue()));
			SpeedLabel.setText("Speed: " + slider.getValue());

		} catch (Exception e) {
		}
	}

	static void connect() {
		try {
			motorLeft = new Motor(constants.COM);
			// (new Thread(new MainClass())).start();
			StartStop.setEnabled(true);
			connectButton.setText("connected..");
			DirectionLabel.setText("Connected");
			SpeedLabel.setText("Connected");
			connectButton.setEnabled(false);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainFrame,
					"Unfortunately counldn't connect to motor.. ",
					"Insane error", JOptionPane.ERROR_MESSAGE);
		}
	}

	static void StartStp() {
		if (flag == 0) {
			try {
				motorLeft.start(slider.getValue());
				DirectionLabel.setEnabled(true);
				SpeedLabel.setEnabled(true);
				DirectionLabel.setText("Direction: " + "Left");
				SpeedLabel.setText("Speed: " + motorLeft.getSpeed());
				StartStop.setText("Stop");
				left.setEnabled(true);
				right.setEnabled(true);
				flag = 1;
				slider.setEnabled(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(mainFrame,
						"Unfortunately couldn't start.. ", "Inane error",
						JOptionPane.ERROR_MESSAGE);
				// System.out.println("Couldn't start");
			}
		}

		else {
			try {
				motorLeft.stop();
				slider.setEnabled(false);
				left.setEnabled(false);
				right.setEnabled(false);
				DirectionLabel.setEnabled(false);
				SpeedLabel.setEnabled(false);
				DirectionLabel.setText("Direction: Stopped");
				SpeedLabel.setText("Speed: Stopped");
				left.setSelected(true);
				right.setSelected(false);
			} catch (Exception e) {
			}
			StartStop.setText("Start");
			flag = 0;
		}
	}

	private static void initGUI() {
		// Set up the options pane
		JPanel optionsPane = initOptionsPane();
		// Set up the slider pane
		JPanel sliderPane = new JPanel(new BorderLayout());
		sliderPane.add(slider);
		// Set up the main pane
		JPanel mainPane = new JPanel(new BorderLayout());
		mainPane.setSize(450,450);
		mainPane.add(optionsPane, BorderLayout.WEST);
		mainPane.add(sliderPane, BorderLayout.EAST);
		// Set up the main frame
		mainFrame = new JFrame("Motor Control");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setContentPane(mainPane);
		mainFrame.setLocation(400, 400);
		mainFrame.setSize(450, 450);
		mainFrame.setVisible(true);
		mainFrame.setResizable(false);
	}

	static String xxx[];
	static List<String> xx;

	static void EditCom(String[] x) {
		
		Object[] possibilities = x;
		
		String s = (String) JOptionPane.showInputDialog(mainFrame,
				"Complete the sentence:\n" + "\"Green eggs and...\"",
				"Customized Dialog", JOptionPane.PLAIN_MESSAGE, null,
				possibilities, "ham");

		if ((s != null) && (s.length() > 0)) {
			constants.COM = s;
			return;
		}

	}

	public static void main(String[] args) {
		//System.out.println("edit");
		xx = getAvailablePorts();
		xxx = new String[xx.size()];
		//System.out.println("not viewed yet");
		for (int c = 0; c < xxx.length; c++) {
			xxx[c] = xx.get(c);
			System.out.println("port " + c + " is : " + xxx[c]);
		}
		EditCom(xxx);
		initGUI();

		SpeedLabel.setText("Speed: " + "Not Connected");

		DirectionLabel.setText("Direction: " + "Not Connected");

	}

}

class ActionAdapter implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	}
}