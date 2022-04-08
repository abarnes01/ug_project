package eclipse.swing.imagegrid;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ImageGridSurfer extends JFrame implements ActionListener {

	private static final long serialVersionUID = 8175558208810106053L;
	private JPanel contentPane, mainPane, bottomPane;
	private JButton closeBtn;
	private Integer entryCount;
	private String randomOrPreset;

	public ImageGridSurfer(String randomOrPreset) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setLocation(800, 100);
		setTitle("(Digraph) Image Grid Shoulder Surfer");
		
		this.randomOrPreset = randomOrPreset;
		entryCount = 0;
		
		mainPane = new JPanel();
		bottomPane = new JPanel();

		closeBtn = new JButton("Close");
		closeBtn.addActionListener(this);
		
		bottomPane.add(closeBtn);
		try {
			bottomPane.add(generateSurferImg());
		} catch (Exception e) {
			System.err.println("Error: Could not retrieve surfer image.");
		}
		
		contentPane.add(mainPane, BorderLayout.CENTER);
		contentPane.add(bottomPane, BorderLayout.SOUTH);
	}
	
	public void updateSurfer(List<BufferedImage> matchesInput) {
		mainPane.removeAll();
		for (BufferedImage img : matchesInput) {
			JLabel label = new JLabel(new ImageIcon(img));
			mainPane.add(label);
		}
		entryCount += 1;
		mainPane.revalidate();
		mainPane.repaint();
	}
	
	public void surferEvaluate(long secondsTaken) {
		mainPane.removeAll();
		JEditorPane evaPane = new JEditorPane();
		evaPane.setContentType("text/html");
		String evaText = "<html>"
				+ "<h2>Average time of each login: " + (secondsTaken/entryCount) + " seconds.</h2>"
				+ "<h2>What the shoulder surfer can gather</h2>"
				+ "<p>This is mimicking a shoulder surfer who already knows the algorithm."
				+ " Those who do not know the algorithm could have any idea of what the pass image entry means."
				+ " In the worst case scenario, the shoulder surfer already knows scenarios A, B and C. </p>"
				+ "<p>In this example it took <strong>" + entryCount + "</strong> logins for the surfer to discover your two pass images.";
		if (randomOrPreset.equals("random")) {
			evaText += " The fact you had <strong>random</strong> images each time meant that only your two preselected images came up as the duplicates; "
					+ "making it easier for the shoulder surfer to identify them.</p>";
		} else {
			evaText += " The fact you have <strong>preset</strong> images selected meant there were more duplicates in each login, widening the range of "
					+ " identifiable images to the shoulder surfer.</p>";
		}
		evaPane.setText(String.format(evaText));
		evaPane.setBackground(SystemColor.window);
		evaPane.setEditable(false);
		mainPane.add(evaPane);
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		mainPane.add(Box.createRigidArea(new Dimension(0,5)));
		mainPane.revalidate();
		mainPane.repaint();
	}
	
	public JLabel generateSurferImg() throws Exception {
		BufferedImage shoSurBI = ImageIO.read(ImageGridSurfer.class.getResource("/Images/shouldersurfer.png"));
		Image img = shoSurBI.getScaledInstance(100, 45, java.awt.Image.SCALE_SMOOTH);
		JLabel shoulderSurferLbl = new JLabel(new ImageIcon(img));
		return shoulderSurferLbl;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton)e.getSource();
		if (btn.equals(closeBtn)) {
			int ans = JOptionPane.showConfirmDialog(closeBtn, "Do you wish to close the shoulder surfer?");
			if (ans == JOptionPane.YES_OPTION) {
				dispose();
			}
		}
	}
}
