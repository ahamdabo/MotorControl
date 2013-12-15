import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Ahmed
 * 
 */
public class MainClass implements Runnable {

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
	static final int FPS_MIN = 0;
	static final int FPS_MAX = 30;
	static final int FPS_INIT = 15; // initial speed
	public static JSlider slider = null;
	public static int flag = 0;
	static JPanel pane = null;
	static ActionAdapter buttonListener = null, button2Listener = null;

	// Values which is passed to direction and speed labels..
	static int dir, sp;
	static ActionListener leftl = null;
	static ActionListener rightl = null;
	static ChangeListener l = null;

	/*
	 * This returns the available COMs public static List<String>
	 * getAvailablePorts() {
	 * 
	 * List<String> list = new ArrayList<String>();
	 * 
	 * Enumeration portList = CommPortIdentifier.getPortIdentifiers();
	 * 
	 * while (portList.hasMoreElements()) { CommPortIdentifier portId =
	 * (CommPortIdentifier) portList .nextElement(); if (portId.getPortType() ==
	 * CommPortIdentifier.PORT_SERIAL) { list.add(portId.getName()); } }
	 * 
	 * return list; }
	 */
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
					motorLeft.setDirection("Left");
					// constants.Direction = "Left";
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
		SpeedLabel.setEnabled(true);
		// direction label
		DirectionLabel = new JLabel("Direction:  ");
		DirectionLabel.setEnabled(true);
		// left radio button
		left = new JRadioButton("left");
		left.addActionListener(leftl);
		left.setSelected(true);
		left.setEnabled(false);

		// right radio button
		right = new JRadioButton("Right");
		right.addActionListener(rightl);
		right.setEnabled(true);
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
		motorLeft = new Motor(constants.COM);
		// motorRight = new Motor("COM38");
		try {
			(new Thread(new MainClass())).start();
			StartStop.setEnabled(true);
		} catch (Exception ex) {
		}

		connectButton.setText("connected..");
		connectButton.setEnabled(false);
	}

	static void StartStp() {
		if (flag == 0) {
			try {
				motorLeft.start();
				DirectionLabel.setText("Direction: " + "Left");
				SpeedLabel.setText("Speed: " + motorLeft.getSpeed());
				StartStop.setText("Stop");
				left.setEnabled(true);
				right.setEnabled(true);
				flag = 1;
				slider.setEnabled(true);

			} catch (Exception e) {

			}
		}

		else {
			try {
				motorLeft.stop();
				slider.setEnabled(false);
				left.setEnabled(false);
				right.setEnabled(false);
				DirectionLabel.setText("Direction: Stopped");
				SpeedLabel.setText("Speed: Stopped");
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
		mainPane.add(optionsPane, BorderLayout.CENTER);
		mainPane.add(sliderPane, BorderLayout.EAST);
		mainPane.setSize(500, 500);
		// Set up the main frame
		mainFrame = new JFrame("Motor Control");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setContentPane(mainPane);
		mainFrame.setLocation(400, 400);
		mainFrame.setSize(450, 450);
		mainFrame.setVisible(true);
		mainFrame.setResizable(false);
	}

	public static void main(String[] args) {
		// List<String> x = getAvailablePorts();
		// for (int i = 0; i < x.size(); i++) {
		// System.out.println(x.get(i));
		// }
		initGUI();
		SpeedLabel.setText("Speed: " + "Not Connected");
		DirectionLabel.setText("Not Connected");
	}

	// This Thread is used to view the data captured from the motor
	// The speed and the direction..
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

		}
	}
}

class ActionAdapter implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	}

}