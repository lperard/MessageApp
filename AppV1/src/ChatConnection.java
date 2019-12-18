import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JSlider;

public class ChatConnection {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatConnection window = new ChatConnection();
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
	public ChatConnection() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 514, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(98, 107, 322, 456);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JList list = new JList();
		list.setBounds(321, 454, -321, -449);
		panel.add(list);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(307, 0, 15, 456);
		panel.add(scrollBar);
		
		JLabel lblPseudo = new JLabel("Pseudo :");
		lblPseudo.setBounds(98, 41, 107, 16);
		frame.getContentPane().add(lblPseudo);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(158, 41, 135, 16);
		frame.getContentPane().add(textArea);
		
		JLabel lblUtilisateursEnLigne = new JLabel("Utilisateurs en ligne:");
		lblUtilisateursEnLigne.setBounds(97, 79, 196, 16);
		frame.getContentPane().add(lblUtilisateursEnLigne);
		
		JButton btnChangerDePseudo = new JButton("Changer de pseudo");
		btnChangerDePseudo.setBounds(98, 574, 322, 48);
		frame.getContentPane().add(btnChangerDePseudo);
	}
}
