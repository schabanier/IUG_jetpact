package gui.infoperso;

import data.Account;
import engine.NetworkServiceProvider;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import gui.Constants.*;
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


public class ModifierInfo extends JDialog {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 6872079205376815700L;
	
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
	
	
	public ModifierInfo(JFrame infoPersoParent, String title) {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - SolÃ¨ne Chabanier
	    super(infoPersoParent, title, true);
	    
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
			textFieldNom.setEditable(false);
			panelDialog.add(textFieldNom, CC.xywh(7, 1, 7, 1));

			//---- label9 ----
			labelPrenom.setText(Fields.FIRSTNAME);
			panelDialog.add(labelPrenom, CC.xy(3, 3));
			textFieldPrenom.setEditable(false);
			panelDialog.add(textFieldPrenom, CC.xywh(7, 3, 7, 1));

			//---- label10 ----
			labelMail.setText(Fields.EMAIL_ADDRESS);
			panelDialog.add(labelMail, CC.xy(3, 5));
			panelDialog.add(textFieldMail, CC.xywh(7, 5, 7, 1));

			//---- label5 ----
			labelPseudo.setText(Fields.PSEUDO);
			panelDialog.add(labelPseudo, CC.xy(3, 7));
			textFieldPseudo.setEditable(false);
			panelDialog.add(textFieldPseudo, CC.xywh(7, 7, 7, 1));

			//---- label6 ----
			labelPassword1.setText(UserInformationsManagement.NEW_PASSWORD_LABEL);
			panelDialog.add(labelPassword1, CC.xy(3, 9));
			panelDialog.add(passwordField2, CC.xywh(7, 9, 7, 1));

			//---- label7 ----
			labelPassword2.setText(UserInformationsManagement.NEW_PASSWORD_CONFIRMATION_LABEL);
			panelDialog.add(labelPassword2, CC.xy(3, 11));
			panelDialog.add(passwordField3, CC.xywh(7, 11, 7, 1));

			//---- button3 ----
			button3Annuler.setText(UserInformationsManagement.CANCEL_BUTTON_NAME);
			panelDialog.add(button3Annuler, CC.xy(3, 13));
			
			button3Annuler.addActionListener(new ButtonAnnulerListener());

			//---- button4 ----
			button4Ok.setText(UserInformationsManagement.VALIDATE_ACCOUNT_MODIFICATION_BUTTON_NAME);
			button4Ok.addActionListener(new ButtonConfirmerListener());
			
			
			
			
			
			panelDialog.add(button4Ok, CC.xy(11, 13));
			dialog1.pack();
			dialog1.setLocationRelativeTo(dialog1.getOwner());
			
			
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
			
	}
	
	
	class ButtonConfirmerListener implements ActionListener {
		

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// action si clic bouton Confirmer
			
			
			if ((passwordField2.getPassword().length > 0 || passwordField3.getPassword().length > 0) && !(new String(passwordField2.getPassword()).equals(new String(passwordField3.getPassword())))) // les mdp ne sont pas identiques
				JOptionPane.showMessageDialog(ModifierInfo.this, UserInformationsManagement.PASSWORD_AND_CONFIRMATION_NOT_EQUAL_MESSAGE, CommonErrorMessages.getFieldErrorTitle(Fields.PASSWORD), JOptionPane.ERROR_MESSAGE);
				
			else
			{

				String newPassword = new String (passwordField2.getPassword());		
				if(newPassword.length() > 0)
				{
					try {		
						   NetworkServiceProvider.getNetworkService().modifyPassword(newPassword);
					} catch (NotAuthenticatedException e1)
					{
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
					} catch (IllegalFieldException e1)
					{
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.PASSWORD), CommonErrorMessages.getFieldErrorTitle(Fields.PASSWORD), JOptionPane.ERROR_MESSAGE);
					} catch (NetworkServiceException e1) {
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
					}
				}
				
				
				if(textFieldMail.getText().length() > 0)
				{
					try {
						NetworkServiceProvider.getNetworkService().modifyEMailAddress(textFieldMail.getText());
					} catch (NotAuthenticatedException e1)
					{
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
					} catch (IllegalFieldException e1)
					{
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.EMAIL_ADDRESS), CommonErrorMessages.getFieldErrorTitle(Fields.EMAIL_ADDRESS), JOptionPane.ERROR_MESSAGE);
					} catch (NetworkServiceException e1) {
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
					}
				}
				
				
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
	
	public void showEditor() throws NotAuthenticatedException
	{
		Account account = NetworkServiceProvider.getNetworkService().getCurrentAccount();
		
		textFieldPseudo.setText(account.getPseudo());
		textFieldPrenom.setText(account.getFirstName());
		textFieldNom.setText(account.getLastName());
		textFieldMail.setText(account.getEMailAddress());

		passwordField2.setText("");
		passwordField3.setText("");
		
		setVisible(true);
	}
}
