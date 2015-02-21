package gui.authentification;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import engine.NetworkServiceProvider;
import exceptions.AccountNotFoundException;
import exceptions.NetworkServiceException;
import gui.MainFrame;

public class Identification extends JPanel

{
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -1602551956906897982L;

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - SolÃ¨ne Chabanier

	private JPanel panel1;
	private JLabel labelPseudo;
	private JTextField textFieldPseudo;
	private JLabel labelMdp;
	private JPasswordField passwordField1;
	private JButton buttonCreerCompte;
	private JButton buttonConnexion;
	private JFrame frameContainer;


	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Identification(JFrame frameContainer)
	{
		super(true);
		this.frameContainer = frameContainer;
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - SolÃ¨ne Chabanier

		panel1 = this;
		labelPseudo = new JLabel();
		textFieldPseudo = new JTextField();
		labelMdp = new JLabel();
		passwordField1 = new JPasswordField();
		buttonCreerCompte = new JButton();
		buttonConnexion = new JButton();
		buttonConnexion.setAlignmentX(RIGHT_ALIGNMENT);


		//======== panel1 ========
		{

			panel1.setBorder(new EmptyBorder(5,15,5,5));

//			GroupLayout layout = new GroupLayout(panel1);
//			panel1.setLayout(layout);
//			
//			layout.setAutoCreateGaps(true);
//			layout.setAutoCreateContainerGaps(true);
//			
//			layout.setHorizontalGroup(
//					layout.createSequentialGroup()
//						.addGroup(
//							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//								.addComponent(labelPseudo)
//								.addComponent(labelMdp)
//								.addComponent(buttonConnexion)
//							)
//						.addGroup(
//							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//								.addComponent(textFieldPseudo)
//								.addComponent(passwordField1)
//								.addComponent(buttonCreerCompte)
//							)
//					);
//			
//			
//			layout.setVerticalGroup(
//					layout.createSequentialGroup()
//						.addGroup(
//							layout.createParallelGroup(Alignment.BASELINE)
//								.addComponent(labelPseudo)
//								.addComponent(textFieldPseudo)
//							)
//						.addGroup(
//							layout.createParallelGroup(Alignment.BASELINE)
//								.addComponent(labelMdp)
//								.addComponent(passwordField1)
//							)
//						.addGroup(
//							layout.createParallelGroup(Alignment.BASELINE)
//								.addComponent(buttonConnexion)
//								.addComponent(buttonCreerCompte)
//							)
//					);

			panel1.setLayout(new FormLayout(
					"10*(default, $lcgap), default",
					"11*(default, $lgap), default"));

			panel1.setVisible(true);

			//---- label3 ----
			labelPseudo.setText("Pseudo:");
			panel1.add(labelPseudo, CC.xywh(3, 5, 5, 2));
			panel1.add(textFieldPseudo, CC.xy(9, 5));

			//---- label4 ----
			labelMdp.setText("Mot de passe:");
			panel1.add(labelMdp, CC.xy(3, 9));
			panel1.add(passwordField1, CC.xy(9, 9));

			//---- button1 ----
			buttonCreerCompte.setText("Cr\u00e9er un compte");
			panel1.add(buttonCreerCompte, CC.xy(3, 15));

			buttonCreerCompte.addActionListener(new Button1Listener(frameContainer));

			//---- button2 ----
			buttonConnexion.setText("Connexion");
			panel1.add(buttonConnexion, CC.xy(9, 15));

			buttonConnexion.addActionListener(new Button2Listener());
		}


	}
	
	class Button1Listener implements ActionListener {

		private JFrame frameContainer;

		public Button1Listener(JFrame frameContainer) {
			this.frameContainer = frameContainer;
		}


		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub


			// action si clic sur bouton "creer compte"

			AccountCreation accountCreationFenetre = new AccountCreation(frameContainer, "Creer compte");
			accountCreationFenetre.setVisible(true);


		}

	}

	class Button2Listener implements ActionListener {


		public void actionPerformed(ActionEvent e) {
			String pseudo = textFieldPseudo.getText();
			String password = new String(passwordField1.getPassword());
			
			if(pseudo.length() == 0) // il manque le pseudo
				JOptionPane.showMessageDialog(null, "Veuillez rentrer votre pseudo.", "Erreur", JOptionPane.ERROR_MESSAGE);
			
			else if(password.length() == 0)	//il manque mdp1
				JOptionPane.showMessageDialog(null, "Veuillez rentrer votre mot de passe", "Erreur", JOptionPane.ERROR_MESSAGE);
			
			else
			{
				try {
					NetworkServiceProvider.getNetworkService().authenticate(pseudo, password);
					
					// pour nettoyer les champs pseudo et mot de passe.
					textFieldPseudo.setText("");
					passwordField1.setText("");
					
					MainFrame.getInstance().authenticationDone();
					
				} catch (AccountNotFoundException e1) {
					// TODO Auto-generated catch block

					JOptionPane.showMessageDialog(frameContainer, "Ce compte n'existe pas", "Erreur", JOptionPane.ERROR_MESSAGE);


				} catch (NetworkServiceException e1) {
					// TODO Auto-generated catch block

					JOptionPane.showMessageDialog(frameContainer, "Erreur réseau", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}

	




