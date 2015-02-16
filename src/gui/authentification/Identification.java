package gui.authentification;

import interfaces.NetworkServiceInterface;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
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

public class Identification extends JPanel
                           
{
	
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
				
				panel1 = new JPanel();
				labelPseudo = new JLabel();
				textFieldPseudo = new JTextField();
				labelMdp = new JLabel();
				passwordField1 = new JPasswordField();
				buttonCreerCompte = new JButton();
				buttonConnexion = new JButton();
			 
			
					//======== panel1 ========
					{

						panel1.setBorder(new EmptyBorder(5,15,5,5));

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
		// TODO Auto-generated method stub
		
		//action si clic sur bouton connexion
		  try {
			NetworkServiceProvider.getNetworkService().authenticate("jdupon", "123456");
		} catch (AccountNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ce compte n'existe pas", "Erreur", JOptionPane.ERROR_MESSAGE);
			
			
			
			
			
		} catch (NetworkServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	  

	  
  
 	

}	




		