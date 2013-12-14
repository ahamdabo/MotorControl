import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainClass implements Runnable {

	static Motor motorLeft, motorRight;

	// Speed and Direction monitoring
	Thread th = new Thread(this);
	// Various GUI components and info
	public static JFrame mainFrame = null;
	public static JTextField serialCommand = null;
	public static JButton connectButton = null;
	public static JToggleButton StartStop = null;
	public static JLabel DirectionLabel = null;
	public static JLabel SpeedLabel = null;
	public static JLabel CommandLine = null;
	static final int FPS_MIN = 0;
	static final int FPS_MAX = 30;
	static final int FPS_INIT = 15; // initial frames per second
	public static JSlider slider = null;
	public static int flag = 0;
	int dir, sp;

	private static JPanel initOptionsPane() {
		JPanel pane = null;
		ChangeListener l = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub

				speedDisplay();
			}
		};

		ActionAdapter buttonListener = null, button2Listener = null;
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

		slider = new JSlider(JSlider.VERTICAL, FPS_MIN, FPS_MAX, FPS_INIT);
		slider.addChangeListener(l);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		connectButton = new JButton("Connect");
		connectButton.setMnemonic(KeyEvent.VK_C);
		connectButton.setActionCommand("Send");
		connectButton.addActionListener(buttonListener);
		connectButton.setEnabled(true);

		StartStop = new JToggleButton("Start");
		StartStop.setMnemonic(KeyEvent.VK_C);
		StartStop.setActionCommand("Start");
		StartStop.addActionListener(button2Listener);
		StartStop.setEnabled(true);

		SpeedLabel = new JLabel("Speed:  ");
		SpeedLabel.setEnabled(true);

		DirectionLabel = new JLabel("Direction:  ");
		DirectionLabel.setEnabled(true);

		buttonPane.add(connectButton);
		buttonPane.add(StartStop);
		// buttonPane.add(slider);

		optionsPane.add(slider);
		optionsPane.add(DirectionLabel);
		optionsPane.add(SpeedLabel);
		optionsPane.add(buttonPane);
		return optionsPane;
	}

	// Action adapter for easy event-listener coding

	static void speedDisplay() {
		// SpeedLabel.setText("Speed: " + motorLeft.getSpeed());
		SpeedLabel.setText("Speed: " + slider.getValue());
		try {
			motorLeft.setSpeed(Integer.toString(slider.getValue()));
		} catch (Exception e) {
			SpeedLabel.setText("Speed: Motor isn't connected");
			DirectionLabel.setText("Direction: Motor isn't connected");
		}
	}

	static void connect() {
		motorLeft = new Motor("COM37");
		motorRight = new Motor("COM38");
		try {
			(new Thread(new MainClass())).start();
		} catch (Exception ex) {
		}
		connectButton.setText("connected..");
		connectButton.setEnabled(false);
	}

	static void StartStp() {
		if (flag == 0) {
			motorLeft.setSpeed("154.100");
			System.out.println("00");
			StartStop.setText("Stop");
			flag = 1;
		}

		else {
			System.out.println("11");
			StartStop.setText("Start");
			flag = 0;
		}

	}

	private static void initGUI() {
		// Set up the options pane
		JPanel optionsPane = initOptionsPane();
		// Set up the chat pane
		JPanel chatPane = new JPanel(new BorderLayout());
		// serialCommand = new JTextField();
		// serialCommand.setEnabled(true);
		// chatPane.add(serialCommand, BorderLayout.SOUTH);
		chatPane.add(slider);
		chatPane.setSize(200, 400);
		optionsPane.setSize(300, 400);
		// Set up the main pane
		JPanel mainPane = new JPanel(new BorderLayout());
		mainPane.add(optionsPane, BorderLayout.CENTER);
		mainPane.add(chatPane, BorderLayout.EAST);
		mainPane.setBackground(Color.RED);
		mainPane.setSize(500, 500);
		// Set up the main frame
		mainFrame = new JFrame("Motor Control");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setContentPane(mainPane);
		mainFrame.setSize(mainFrame.getPreferredSize());
		// mainFrame.setSize(mainFrame.getPreferredSize());
		mainFrame.setLocation(400, 400);
		mainFrame.setBackground(Color.RED);
		mainFrame.setSize(400, 400);
		// mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public static void main(String[] args) {
		initGUI();
	}

	// This Thread is used to view the data captured from the motor
	// The speed and the direction..
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			/*
			 * if (motorLeft.isConnected()) { sp = motorLeft.getSpeed(); dir =
			 * motorLeft.getDirection(); System.out.println("speed :  " + sp +
			 * " " + ",Direction: " + dir); }
			 */
		}
	}
}

class ActionAdapter implements ActionListener {
	public void actionPerformed(ActionEvent e) {

	}
}