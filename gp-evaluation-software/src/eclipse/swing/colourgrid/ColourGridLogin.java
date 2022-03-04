package eclipse.swing.colourgrid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.ArrayUtils;

import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.Welcome;

public class ColourGridLogin extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, formPanel, buttonPanel, gridPanel, mainPanel;
	private JPanel headerPanel;
	private JPasswordField pColourField;
	private JButton loginButton, backBtn;
	private JLabel headerLabel, pColourLabel;
	private GridLayout gridLayout;
	private String patternPass;
	private Color patternPassColour;
	private long startTime;

	public ColourGridLogin(String pp) {
		// auto
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// set pattern pass
		setPatternPass(pp);
		
		// create elements
		headerPanel = new JPanel();
		headerLabel = new JLabel("Login Form");
		formPanel = new JPanel();
		buttonPanel = new JPanel();
		pColourLabel = new JLabel("Enter: ");
		pColourField = new JPasswordField(10);
		mainPanel = new JPanel();
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		backBtn = new JButton("<");
		backBtn.addActionListener(this);
		
		// add elements to window
		headerPanel.add(backBtn);
		headerPanel.add(headerLabel);
		formPanel.setLayout(new GridLayout(3,1,10,10));
		formPanel.add(pColourLabel);
		formPanel.add(pColourField);
		buttonPanel.add(loginButton);
		mainPanel.add(formPanel);
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		//setResizable(false);
		
		startTime = System.nanoTime();
		makeGrid();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		if (btn.equals(loginButton)) {
			// value of user input
			String ppInput = String.valueOf(pColourField.getPassword());
			
			// mapping all the colours to their first letter
			Map<Color, String> colourMap = new HashMap<>();
			colourMap.put(Color.RED, "R");
			colourMap.put(Color.BLUE, "B");
			colourMap.put(Color.PINK, "P");
			colourMap.put(Color.WHITE, "W");
			colourMap.put(Color.GREEN, "G");
			colourMap.put(Color.YELLOW, "Y");
			
			// if user input equals the first letter of the colour their pattern pass lies in
			if (ppInput.toUpperCase().equals(colourMap.get(patternPassColour))) {
				long stopTime = System.nanoTime()-startTime;
				long seconds = TimeUnit.SECONDS.convert(stopTime, TimeUnit.NANOSECONDS);
				JOptionPane.showMessageDialog(loginButton, "Successfully logged in. Took " + seconds + "s");
				System.out.println("For a shoulder surfer who knows the colour grid algorithm and spots the colour first letter input they have " + (float)seconds/6 + "s to memorise each letter");
				System.out.println("For a unknowing shoulder surfer ... \n");
				Welcome welcome = new Welcome();
				welcome.setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(loginButton, "Incorrect password.");
			}
		} else if (btn.equals(backBtn)) {
			new InitialLogin(Method.COLOURGRID).setVisible(true);
			dispose();
		}
	}
	
	public void setPatternPass(String str) {
		patternPass = str;
	}
	
	public void makeGrid() {
		// create 6 by 6 grid to add to the main panel
		gridPanel = new JPanel();
		gridLayout = new GridLayout(6,6);
		
		// array of all the colour tiles that need to be placed on the grid
		List<Color> coloursOnGrid = new ArrayList<Color>( Arrays.asList(Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED,
								Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE,
								Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK,
								Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE,
								Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN,
								Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW));
		
		// in case patternPass exists
		try {
			// primitive char array of pattern pass turned into array list with object Character
			char[] rawPPCharArray = patternPass.toCharArray();
			List<Character> ppCharArray = new ArrayList<Character>();
			ppCharArray.addAll(Arrays.asList(ArrayUtils.toObject(rawPPCharArray)));
			// random generator
			Random rand = new Random();
			int randColourIndex = rand.nextInt(coloursOnGrid.size());
			// the colour the password will lie in
			patternPassColour = coloursOnGrid.get(randColourIndex);
			
			gridPanel.setLayout(gridLayout);
			for (int i = 1; i <= 36; i++) {
				// for each element, create text field and assign random colour
				JTextField tf = new JTextField(null, 2);
				randColourIndex = rand.nextInt(coloursOnGrid.size());
				tf.setBackground(coloursOnGrid.get(randColourIndex));
				// add pattern password letters if it is the correct colour
				if (coloursOnGrid.get(randColourIndex) == patternPassColour) {
					// set to random character of pattern pass and remove from pattern pass array
					int randPPIndex = rand.nextInt(ppCharArray.size());
					tf.setText(Character.toString(ppCharArray.get(randPPIndex)));
					ppCharArray.remove(randPPIndex);
				} else {
					// otherwise set random letter from the alphabet
					tf.setText((Character.toString((char)rand.nextInt(26) + 'a')));
				}
				// set to white text if colour is blue. Accessibility
				if (coloursOnGrid.get(randColourIndex) == Color.BLUE) {
					tf.setForeground(Color.WHITE);
				}
				tf.setEditable(false);
				gridPanel.add(tf);
				// to make sure all colours are evenly distributed
				coloursOnGrid.remove(randColourIndex);
			}
		// IF no pattern pass, which is also dealt with in simple login
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		mainPanel.add(gridPanel);
	}

}

