package gui.authentification;

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
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import data.Account;
import engine.FieldVerifier;
import engine.NetworkServiceProvider;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import static gui.Constants.*;


public class AccountCreation extends JDialog
                            
{
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 3159383612353771662L;
	
	private JDialog dialog1;
	private JLabel labelNom;
	private JTextField textFieldNom;
	private JLabel labelPrenom;
	private JTextField textFieldPrenom;
	private JLabel labelMail;
	private JTextField textFieldMail;
	private JLabel labelPseudo;
	private JTextField textFieldPseudo;
	private JLabel labelPassword1;
	private JPasswordField passwordField2;
	private JLabel labelPassword2;
	private JPasswordField passwordField3;
	private JButton button3Annuler;
	private JButton button4Ok;

	
	public AccountCreation(JFrame identificationParent, String title) {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
				// Generated using JFormDesigner Evaluation license - SolÃ¨ne Chabanier
			    super(identificationParent, title, true);
			    
				dialog1 = this;
				labelNom = new JLabel();
				textFieldNom = new JTextField();
				labelPrenom = new JLabel();
				textFieldPrenom = new JTextField();
				labelMail = new JLabel();
				textFieldMail = new JTextField();
				labelPseudo = new JLabel();
				textFieldPseudo = new JTextField();
				labelPassword1 = new JLabel();
				passwordField2 = new JPasswordField();
				labelPassword2 = new JLabel();
				passwordField3 = new JPasswordField();
				button3Annuler = new JButton();
				button4Ok = new JButton();
				
				
				//======== dialog1 ========
				{
				
					dialog1.setBackground(new Color(0, 153, 153));
					dialog1.setLocationRelativeTo(null);
					
					dialog1.setResizable(false);
					
					dialog1.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					
					
					
					//====== panel =========
					
					
					JPanel panelDialog = new JPanel();
					dialog1.setContentPane(panelDialog);
					
					panelDialog.setLayout(new FormLayout(
						"8*(default, $lcgap), default",
						"8*(default, $lgap), default"));
				
					panelDialog.setBorder(new EmptyBorder(5,15,5,5));

					//---- label8 ----
					labelNom.setText(Fields.LASTNAME);
					panelDialog.add(labelNom, CC.xy(3, 1));
					panelDialog.add(textFieldNom, CC.xywh(7, 1, 7, 1));

					//---- label9 ----
					labelPrenom.setText(Fields.FIRSTNAME);
					panelDialog.add(labelPrenom, CC.xy(3, 3));
					
					panelDialog.add(textFieldPrenom, CC.xywh(7, 3, 7, 1));

					//---- label10 ----
					labelMail.setText(Fields.EMAIL_ADDRESS);
					panelDialog.add(labelMail, CC.xy(3, 5));
					panelDialog.add(textFieldMail, CC.xywh(7, 5, 7, 1));

					//---- label5 ----
					labelPseudo.setText(Fields.PSEUDO);
					panelDialog.add(labelPseudo, CC.xy(3, 7));
					panelDialog.add(textFieldPseudo, CC.xywh(7, 7, 7, 1));

					//---- label6 ----
					labelPassword1.setText(Fields.PASSWORD_LABEL);
					panelDialog.add(labelPassword1, CC.xy(3, 9));
					panelDialog.add(passwordField2, CC.xywh(7, 9, 7, 1));

					//---- label7 ----
					labelPassword2.setText(Fields.PASSWORD_CONFIRMATION_LABEL);
					panelDialog.add(labelPassword2, CC.xy(3, 11));
					panelDialog.add(passwordField3, CC.xywh(7, 11, 7, 1));

					//---- button3 ----
					button3Annuler.setText(UserInformationsManagement.CANCEL_BUTTON_NAME);
					panelDialog.add(button3Annuler, CC.xy(3, 13));
					
					button3Annuler.addActionListener(new Button3Listener());

					//---- button4 ----
					button4Ok.setText(UserInformationsManagement.VALIDATE_ACCOUNT_CREATION_BUTTON_NAME);
					button4Ok.addActionListener(new Button4Listener());
					
					
					
					
					
					panelDialog.add(button4Ok, CC.xy(11, 13));
					dialog1.pack();
					dialog1.setLocationRelativeTo(dialog1.getOwner());
					
					
				}
				// JFormDesigner - End of component initialization  //GEN-END:initComponents
			}

	
	
	class Button3Listener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	
			//action si clic sur bouton "annuler"
			AccountCreation.this.setVisible(false);
		
	}		
	
	}
	
	class Button4Listener implements ActionListener {

		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			//action si clic sur bouton ok
			
			if (textFieldNom.getText().length() == 0 ) // il manque le nom
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getEmptyFieldErrorMessage(Fields.LASTNAME), CommonErrorMessages.getFieldErrorTitle(Fields.LASTNAME), JOptionPane.ERROR_MESSAGE);
			else if(! FieldVerifier.verifyName(textFieldNom.getText()))
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.LASTNAME), CommonErrorMessages.getFieldErrorTitle(Fields.LASTNAME), JOptionPane.ERROR_MESSAGE);
			
			else if (textFieldPrenom.getText().length() == 0) //il manque le prenom
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getEmptyFieldErrorMessage(Fields.FIRSTNAME), CommonErrorMessages.getFieldErrorTitle(Fields.FIRSTNAME), JOptionPane.ERROR_MESSAGE);
			else if(! FieldVerifier.verifyName(textFieldPrenom.getText()))
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.FIRSTNAME), CommonErrorMessages.getFieldErrorTitle(Fields.FIRSTNAME), JOptionPane.ERROR_MESSAGE);
			
			
			else if(textFieldMail.getText().length() == 0) // il manque le mail
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getEmptyFieldErrorMessage(Fields.EMAIL_ADDRESS), CommonErrorMessages.getFieldErrorTitle(Fields.EMAIL_ADDRESS), JOptionPane.ERROR_MESSAGE);
			else if(! FieldVerifier.verifyEMailAddress(textFieldMail.getText()))
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.EMAIL_ADDRESS), CommonErrorMessages.getFieldErrorTitle(Fields.EMAIL_ADDRESS), JOptionPane.ERROR_MESSAGE);
			
			else if(textFieldPseudo.getText().length() == 0) // il manque le pseudo
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getEmptyFieldErrorMessage(Fields.PSEUDO), CommonErrorMessages.getFieldErrorTitle(Fields.PSEUDO), JOptionPane.ERROR_MESSAGE);
			else if(! FieldVerifier.verifyName(textFieldPseudo.getText()))
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.PSEUDO), CommonErrorMessages.getFieldErrorTitle(Fields.PSEUDO), JOptionPane.ERROR_MESSAGE);
				
			
			else if(passwordField2.getPassword().length == 0)	//il manque mdp1
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getEmptyFieldErrorMessage(Fields.PASSWORD), CommonErrorMessages.getFieldErrorTitle(Fields.PASSWORD), JOptionPane.ERROR_MESSAGE);
			else if(! FieldVerifier.verifyPassword(new String(passwordField2.getPassword())))
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.PASSWORD), CommonErrorMessages.getFieldErrorTitle(Fields.PASSWORD), JOptionPane.ERROR_MESSAGE);
			
			else if (passwordField3.getPassword().length == 0) //il manque mdp2
				JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getEmptyFieldErrorMessage(Fields.PASSWORD_CONFIRMATION), CommonErrorMessages.getFieldErrorTitle(Fields.PASSWORD_CONFIRMATION), JOptionPane.ERROR_MESSAGE);
			
			else if (!(new String(passwordField3.getPassword()).equals(new String(passwordField2.getPassword())))) // les mdp ne sont pas identiques
				JOptionPane.showMessageDialog(AccountCreation.this, UserInformationsManagement.PASSWORD_AND_CONFIRMATION_NOT_EQUAL_MESSAGE, CommonErrorMessages.getFieldErrorTitle(Fields.PASSWORD_CONFIRMATION), JOptionPane.ERROR_MESSAGE);
				
			else	{ //creation du compte
				
				Account account = new Account(textFieldPseudo.getText(), textFieldPrenom.getText(),textFieldNom.getText(), textFieldMail.getText());
				 
				try {
					
					String passwordString = new String (passwordField2.getPassword());
					NetworkServiceProvider.getNetworkService().createAccount(account,passwordString);

					AccountCreation.this.setVisible(false); //ferme la fenetre
					
				} catch (IllegalFieldException e1) {
					
					switch(e1.getFieldId())
					{
						case IllegalFieldException.PSEUDO :
							if(e1.getReason() == IllegalFieldException.REASON_VALUE_ALREADY_USED)
								JOptionPane.showMessageDialog(AccountCreation.this, UserInformationsManagement.PSEUDO_ALREADY_USED_MESSAGE, CommonErrorMessages.getFieldErrorTitle(Fields.PSEUDO), JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.PSEUDO), CommonErrorMessages.getFieldErrorTitle(Fields.PSEUDO), JOptionPane.ERROR_MESSAGE);
						break;
						case IllegalFieldException.FIRSTNAME :
							JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.FIRSTNAME), CommonErrorMessages.getFieldErrorTitle(Fields.FIRSTNAME), JOptionPane.ERROR_MESSAGE);
						break;
						case IllegalFieldException.LASTNAME :
							JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.LASTNAME), CommonErrorMessages.getFieldErrorTitle(Fields.LASTNAME), JOptionPane.ERROR_MESSAGE);
						break;
						case IllegalFieldException.EMAIL_ADDRESS :
							JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.EMAIL_ADDRESS), CommonErrorMessages.getFieldErrorTitle(Fields.EMAIL_ADDRESS), JOptionPane.ERROR_MESSAGE);
						break;
						case IllegalFieldException.PASSWORD :
							JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.PASSWORD), CommonErrorMessages.getFieldErrorTitle(Fields.PASSWORD), JOptionPane.ERROR_MESSAGE);
						break;
					}
					
					
				} catch (NetworkServiceException e1) {
					// TODO Auto-generated catch block
			
					JOptionPane.showMessageDialog(AccountCreation.this, CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
				} 
				
			}
			
			
		}
	
		
			
		
	}


}
