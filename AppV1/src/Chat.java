import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JLabel;

public class Chat {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat window = new Chat();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Chat() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 860, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(18, 20, 824, 358);
		panel.setBackground(Color.WHITE);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JList list = new JList();
		list.setBounds(6, 6, 812, 346);
		panel.add(list);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(803, 6, 15, 346);
		panel.add(scrollBar);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(18, 437, 573, 93);
		frame.getContentPane().add(textArea);
		
		JButton btnEnvoyer = new JButton("Envoyer");
		btnEnvoyer.setBounds(603, 437, 239, 93);
		frame.getContentPane().add(btnEnvoyer);
		
		JLabel lblConnectEnTant = new JLabel("Connecté en tant que :");
		lblConnectEnTant.setBounds(18, 576, 142, 16);
		frame.getContentPane().add(lblConnectEnTant);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(164, 576, 163, 16);
		frame.getContentPane().add(textArea_1);
		
		JButton btnDconnexion = new JButton("Déconnexion");
		btnDconnexion.setBounds(603, 563, 239, 29);
		frame.getContentPane().add(btnDconnexion);
	}

}
