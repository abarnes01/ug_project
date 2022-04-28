package eclipse.swing.colourgrid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import eclipse.sql.DatabaseRunner;
import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.Welcome;

public class ColourGridLogin extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private GridBagConstraints gbc = new GridBagConstraints();
	private DatabaseRunner dbRunner;
	private JPanel contentPane, formPanel, BtnPanel, gridPanel, mainPanel;
	private JPasswordField pColField;
	private JButton loginBtn, backBtn, cancelBtn;
	private JLabel pColLbl, colToLetterLbl;
	private GridLayout gridLayout;
	private String patternPass;
	private long startTime;
	// array of all the colour tiles that need to be placed on the grid
	private List<Color> ColsOnGrid;
	private Map<String,Color> charMap = new HashMap<>();
	
	private static List<String> alphabetNumbers = new ArrayList<String>( Arrays.asList("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split("")));
	private static Map<Color, String> colMap;
	static {
		// mapping all the colours to their first letter
		colMap = new HashMap<>();
		colMap.put(Color.RED, "R");
		colMap.put(Color.BLUE, "B");
		colMap.put(Color.PINK, "P");
		colMap.put(Color.WHITE, "W");
		colMap.put(Color.GREEN, "G");
		colMap.put(Color.YELLOW, "Y");
	}

	public ColourGridLogin(DatabaseRunner dbRunner, String pp) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 380, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Colour Grid Login");
		this.dbRunner = dbRunner;
		
		// set pattern pass
		setPatternPass(pp);
		
		// create elements
		formPanel = new JPanel();
		BtnPanel = new JPanel();
		pColLbl = new JLabel("Enter: ");
		pColField = new JPasswordField(10);
		colToLetterLbl = new JLabel("Red-R, Blue-B, Pink-P, White-W, Green-G, Yellow-Y");
		mainPanel = new JPanel();
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		backBtn = new JButton("\u2190");
		backBtn.addActionListener(this);
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(this);
		
		// add elements to window
		mainPanel.add(colToLetterLbl);
		formPanel.setLayout(new GridBagLayout());
		gbc.gridx = 0; gbc.gridy = 0;
		formPanel.add(pColLbl, gbc);
		gbc.gridx = 1; gbc.gridy = 0;
		formPanel.add(pColField, gbc);
		BtnPanel.add(backBtn);
		BtnPanel.add(loginBtn);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(BtnPanel, BorderLayout.SOUTH);
		contentPane.add(cancelBtn, BorderLayout.NORTH);
		
		makeGrid();
		startTime = System.nanoTime();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		if (btn.equals(loginBtn)) {
			// value of user input
			String ppInput = String.valueOf(pColField.getPassword());
			
			String ppResult = "";
			
			List<String> patternPassSplit = new ArrayList<String>( Arrays.asList(patternPass.split("")));
			
			for (int i = 0; i < patternPassSplit.size(); i++) {
				patternPassSplit.get(i);
				ppResult += colMap.get(charMap.get(patternPassSplit.get(i).toUpperCase()));
				System.out.println(patternPassSplit.get(i).toUpperCase());
				System.out.println(charMap.get(patternPassSplit.get(i).toUpperCase()));
				System.out.println(colMap.get(charMap.get(patternPassSplit.get(i).toUpperCase())));
			}
			
			System.out.println(ppResult);
		
			if (ppInput.toUpperCase().equals(ppResult)) {
				
				long stopTime = System.nanoTime()-startTime;
				long seconds = TimeUnit.SECONDS.convert(stopTime, TimeUnit.NANOSECONDS);
				String colGridLoginResultHtml = "<html><h1>Colour Grid Login</h1>"
						+ "<p> For a shoulder surfer who knows the algorithm and can see the entry of the colour (first letter), <br>"
						+ " for this example they have under " + (float)seconds/6 + " seconds to memorise each letter of the users password.</p>"
						+ "<p> <strong>Total time for login:</strong> " + seconds + " seconds.</p>";
				JOptionPane.showMessageDialog(loginBtn, String.format(colGridLoginResultHtml));
				Welcome welcome = new Welcome(dbRunner);
				welcome.setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(loginBtn, "Incorrect password.");
			}
		} else if (btn.equals(backBtn)) {
			new InitialLogin(dbRunner, Method.COLOURGRID).setVisible(true);
			dispose();
		} else if (btn.equals(cancelBtn)) {
			Welcome welcome = new Welcome(dbRunner);
			welcome.setVisible(true);
			dispose();
		}
	}
	
	public void setPatternPass(String str) {
		patternPass = str;
	}
	
	public Boolean makeGrid() {
		// create 6 by 6 grid to add to the main panel
		gridPanel = new JPanel();
		gridLayout = new GridLayout(6,6);
		
		ColsOnGrid = new ArrayList<Color>( Arrays.asList(Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED,
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
			int randColIndex = rand.nextInt(ColsOnGrid.size());
			
			
			gridPanel.setLayout(gridLayout);
			for (int i = 0; i < 36; i++) {
				// for each element, create text field and assign random colour
				JTextField tf = new JTextField(null, 2);
				randColIndex = rand.nextInt(ColsOnGrid.size());
				tf.setBackground(ColsOnGrid.get(randColIndex));
				
				tf.setText(alphabetNumbers.get(i));
				charMap.put(alphabetNumbers.get(i), ColsOnGrid.get(randColIndex));
				
				// set to white text if colour is blue. Accessibility
				if (ColsOnGrid.get(randColIndex) == Color.BLUE) {
					tf.setForeground(Color.WHITE);
				}
				tf.setEditable(false);
				gridPanel.add(tf);
				// to make sure all colour are evenly distributed
				ColsOnGrid.remove(randColIndex);
			}
			mainPanel.add(gridPanel);
			mainPanel.add(formPanel);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

