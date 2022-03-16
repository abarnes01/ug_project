package eclipse.swing.imagegrid;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ImageGridSurfer extends JFrame implements ActionListener {

	private JPanel contentPane, mainPane, bottomPane;
	private JLabel shoulderSurferImg;
	private JButton closeBtn;

	public ImageGridSurfer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("(Digraph) Image Grid Shoulder Surfer");
		
		mainPane = new JPanel();
		bottomPane = new JPanel();

		closeBtn = new JButton("Close");
		closeBtn.addActionListener(this);
		
		bottomPane.add(closeBtn);
		
		contentPane.add(mainPane, BorderLayout.CENTER);
		contentPane.add(bottomPane, BorderLayout.SOUTH);
	}
	
	public void updateSurfer(List<BufferedImage> matchesInput) {
		mainPane.removeAll();
		for (BufferedImage img : matchesInput) {
			JLabel label = new JLabel(new ImageIcon(img));
			mainPane.add(label);
		}
		mainPane.revalidate();
		mainPane.repaint();
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
