package gui.infoperso;

import gui.authentification.AccountCreation;

import java.awt.Color;
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


public class ModifierInfo extends JDialog {
	
	
	private JPanel panel1;
    private JDialog dialog1;
	private JLabel labelPseudo, labelMail, labelAncienMdp, labelMdp, labelConfirmerMdp ;
	private JLabel labelPseudoInfo;
	private JTextField textFieldMail;
	private JPasswordField password1, password2, ancienPassword;
	private JButton buttonConfirmer, buttonAnnuler ;
	
	
	
	public ModifierInfo(JFrame infoPersoParent, String title) {
		
		
		super(infoPersoParent, title);
		
		panel1 = new JPanel();
		dialog1 = new JDialog();
		labelPseudo = new JLabel();
		labelMail = new JLabel();
		labelAncienMdp = new JLabel();
		labelMdp = new JLabel();
		labelConfirmerMdp= new JLabel() ;
		labelPseudoInfo = new JLabel();
		textFieldMail = new JTextField();
		ancienPassword = new JPasswordField();
		password1 = new JPasswordField ();
		password2 = new JPasswordField();
		buttonConfirmer = new JButton();
		buttonAnnuler = new JButton();
		
		
		
		//======== dialog1 ========
		
		
			dialog1.setBackground(new Color(0, 153, 153));
			dialog1.setLocationRelativeTo(null);
			dialog1.setResizable(false);
			
			dialog1.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			
			
		//====panel=========
			
			
			
		//=====label=====
			
			labelPseudo.setText("Pseudo");
			labelMail.setText("E-mail");
			labelPseudoInfo.setText("Thomas35");
			labelAncienMdp.setText("Votre ancien mot de passe");
			labelMdp.setText("Tapez votre nouveau mot de passe");
			labelConfirmerMdp.setText("Confirmer votre mot de passe");
			
			
			
			
			
		//====bouton ====
			
			buttonConfirmer.setText("Confirmer");
			buttonAnnuler.setText("Annuler");
			
			buttonConfirmer.addActionListener(new ButtonConfirmerListener());
			buttonAnnuler.addActionListener(new ButtonAnnulerListener());
			
			
			
			
			
			
	}
	
	
	class ButtonConfirmerListener implements ActionListener {
		

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// action si clic bouton Confirmer
			
			
			if(password1.getPassword().length == 0)	//il manque mdp1
				JOptionPane.showMessageDialog(null, "Veuillez rentrer un mot de passe", "Erreur", JOptionPane.ERROR_MESSAGE);
			
			
			else if (password2.getPassword().length == 0) //il manque mdp2
				JOptionPane.showMessageDialog(null, "Veuillez confirmer votre mot de passe", "Erreur", JOptionPane.ERROR_MESSAGE);
			
			
			else if (!(new String(password1.getPassword()).equals(new String(password2.getPassword())))) // les mdp ne sont pas identiques
				JOptionPane.showMessageDialog(null, "Les mots de passes ne sont pas identiques", "Erreur", JOptionPane.ERROR_MESSAGE);
				
			else	{
				
				ModifierInfo.this.setVisible(false);
			}
			
			
			
		}
		
		
		
	}
	
	class ButtonAnnulerListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// action si clic bouton Annuler
			
			ModifierInfo.this.setVisible(false);
			
		}
		
		
	}
			
		
			
			
			
		
		
	}

}
