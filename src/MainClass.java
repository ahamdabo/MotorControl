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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainClass implements Runnable {// implements Runnable {

	static Motor motorLeft; // , motorRight;
	// Various GUI components and info
	public static JFrame mainFrame = null;
	public static JButton connectButton = null;
	public static JToggleButton StartStop = null;
	public static JLabel DirectionLabel = null;
	public static JLabel SpeedLabel = null;
	public static JRadioButton left = null;
	public static JRadioButton right = null;
	public static JDialog messageBox = null;
	static final int FPS_MIN = 0;
	static final int FPS_MAX = 9;
	static final int FPS_INIT = 4; // initial speed
	public static JSlider slider = null;
	public static int flag = 0;
	static JPanel pane = null;
	static int Error = 0;

	// Values which is passed to direction and speed labels..
	static ActionListener leftl = null;
	static ActionListener rightl = null;
	static ChangeListener l = null;
	static ActionListener connectB = null, startB = null;
	static int conflag = 0;

	/**
	 * 
	 * @return List<String>
	 * 
	 */
	static List<String> getAvailablePorts() {
		List<String> list = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier
				.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				list.add(portId.getName());
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
				motorLeft.setDirection(constants.Left);
				DirectionLabel.setText("Direction: Left");
				// constants.Direction = "Left";
				constants.Direction = "Left";

			}
		};

		// Right Direction
		rightl = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				left.setSelected(false);
				right.setSelected(true);
				motorLeft.setDirection(constants.Right);
				DirectionLabel.setText("Direction: Right");
				constants.Direction = "Right";
			}
		};

		connectB = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				connect();
			}
		};

		startB = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				StartStp();
			}
		};

		// Connect and StartStop listener
		JPanel optionsPane = new JPanel(new GridLayout(4, 1));
		pane = new JPanel(new GridLayout(1, 2));
		optionsPane.add(pane);
		JPanel buttonPane = new JPanel(new GridLayout(1, 2));

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
		connectButton.addActionListener(connectB);
		connectButton.setEnabled(true);
		// start stop button
		StartStop = new JToggleButton("Start");
		StartStop.addActionListener(startB);
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
		motorLeft.setSpeed(Integer.toString(slider.getValue()));
		SpeedLabel.setText("Speed: " + slider.getValue());
	}

	static void connect() {
		try {
			motorLeft = new Motor(constants.COM);
			StartStop.setEnabled(true);
			conflag = 1;
			(new Thread(new MainClass())).start();
			connectButton.setText("connected..");
			DirectionLabel.setText("Connected");
			SpeedLabel.setText("Connected");
			connectButton.setEnabled(false);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			JOptionPane.showMessageDialog(mainFrame,
					"Motor is disconnected..  ", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	static void StartStp() {
		if (flag == 0) {
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
		}

		else {
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
		mainPane.setSize(450, 450);
		mainPane.add(optionsPane, BorderLayout.WEST);
		mainPane.add(sliderPane, BorderLayout.EAST);
		// Set up the main frame
		mainFrame = new JFrame("Motor Control");

		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				try {
					motorLeft.stop();
					// motorLeft.disconnect();
				} catch (Exception ex) {
					System.out.println("It's not connected yet ");
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				mainFrame.dispose();
			}
		});
		mainFrame.setVisible(true);
		mainFrame.setContentPane(mainPane);
		mainFrame.setLocation(200, 200);
		mainFrame.setSize(450, 450);
		mainFrame.setVisible(true);
		mainFrame.setResizable(false);
	}

	/**
	 * EditCom .. This method is used for Getting the number of available serial
	 * ports.
	 */

	static void EditCom() {
		String Coms[];
		List<String> ComsList;
		ComsList = getAvailablePorts();
		Coms = new String[ComsList.size()];
		for (int c = 0; c < Coms.length; c++) {
			Coms[c] = ComsList.get(c);
			System.out.println("port " + c + " is : " + Coms[c]);
		}
		while (true) {
			if (Coms.length > 0) {
				Object[] possibilities = Coms;
				String s = (String) JOptionPane.showInputDialog(mainFrame,
						"Please select the motor:\n" + "\"\"", "",
						JOptionPane.PLAIN_MESSAGE, null, possibilities, "ham");

				if ((s != null) && (s.length() > 0)) {
					constants.COM = s;
					return;
				}
			} else {

				Error = constants.NoMotorsConnectedError;
				JOptionPane.showConfirmDialog(mainFrame, "Sorry .. "
						+ "No motors connected !!", "Error",
						JOptionPane.CLOSED_OPTION);
				System.exit(0);
			}

		}

	}

	static void intialize() {
		EditCom();
		if (Error == constants.NoMotorsConnectedError) {
			System.exit(0);
		} else {
			initGUI();
			flag = 0;
			SpeedLabel.setText("Speed: " + "Not Connected");
			DirectionLabel.setText("Direction: " + "Not Connected");
			conflag = 0;
			connectButton.setEnabled(true);
			slider.setEnabled(false);
			left.setEnabled(false);
			right.setEnabled(false);
			DirectionLabel.setEnabled(false);
			SpeedLabel.setEnabled(false);
			left.setSelected(true);
			connectButton.setEnabled(true);
			right.setSelected(false);
		}
	}

	public static void main(String[] args) {
		intialize();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

			if (conflag == 1) {
				try {
					motorLeft.write("%");
					Thread.sleep(500);
				} catch (Throwable x) {
					// motorLeft.disconnect();
					motorLeft.closePort();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(mainFrame,
							"Motor is disconnected.. "
									+ "Please plug motor in and press OK..  ",
							"Error", JOptionPane.ERROR_MESSAGE);
					mainFrame.setEnabled(false);
					mainFrame.setVisible(false);
					intialize();
					conflag = 0;
				}

			}
		}

	}
}