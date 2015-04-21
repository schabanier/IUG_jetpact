package gui.infoperso;

import data.Account;
import engine.EngineServiceProvider;
import engine.FieldVerifier;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import gui.Constants.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class ModifierInfo extends JDialog {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 6872079205376815700L;
	
	private JLabel labelPseudo, labelPrenom, labelNom, labelMail, labelMdp, labelMdp2, labelBracelet;
	private JTextField textfieldPseudo, textfieldPrenom, textfieldNom, textfieldMail, textfieldBracelet;
	private JPasswordField textfieldMdp, textfieldMdp2;
	
	private JButton button3Annuler;
	private JButton button4Ok;
	
	
	public ModifierInfo(JFrame infoPersoParent, String title) {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - SolÃ¨ne Chabanier
	    super(infoPersoParent, title, true);

		labelPseudo = new JLabel();
		labelPrenom = new JLabel();
		labelNom = new JLabel();
		labelMail = new JLabel(); 
		labelBracelet = new JLabel(); 
		labelMdp = new JLabel(); 
		labelMdp2 = new JLabel();
		
		//======== infoPersoPanel ====
		
		
		
		//==== label======
		
		labelPseudo.setText(Fields.PSEUDO);
		labelPrenom.setText(Fields.FIRSTNAME);
		labelNom.setText(Fields.LASTNAME);
		labelMail.setText(Fields.EMAIL_ADDRESS);
		labelBracelet.setText(Fields.BRACELET);
		labelMdp.setText(Fields.PASSWORD);
		labelMdp2.setText(Fields.PASSWORD_CONFIRMATION_LABEL);
		
		Dimension maxDimension = new Dimension(2000, 20);
		// textfield
		textfieldPseudo = new JTextField();
		textfieldPseudo.setMaximumSize(maxDimension);
		
		textfieldPrenom = new JTextField();
		textfieldPrenom.setMaximumSize(maxDimension);
		
		textfieldNom = new JTextField();
		textfieldNom.setMaximumSize(maxDimension);
		
		textfieldMail = new JTextField();
		textfieldMail.setMaximumSize(maxDimension);

		textfieldBracelet = new JTextField();
		textfieldBracelet.setMaximumSize(maxDimension);

		textfieldMdp = new JPasswordField();
		textfieldMdp.setMaximumSize(maxDimension);

		textfieldMdp2 = new JPasswordField();
		textfieldMdp2.setMaximumSize(maxDimension);
		
		
		//=====buttonModifier=====

		button3Annuler = new JButton();
		button3Annuler.setText(UserInformationsManagement.CANCEL_BUTTON_NAME);
		button3Annuler.addActionListener(new ButtonAnnulerListener());
		
		button4Ok = new JButton();
		button4Ok.setText(UserInformationsManagement.MODIFY_BUTTON_NAME);
		button4Ok.addActionListener(new ButtonConfirmerListener());
		
		
		JPanel infosPanel = new JPanel();
		infosPanel.setBorder(new EmptyBorder(5,15,5,5));
		
		GroupLayout layout = new GroupLayout(infosPanel);
		infosPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup()
						.addComponent(labelPseudo)
						.addComponent(labelPrenom)
						.addComponent(labelNom)
						.addComponent(labelMail)
						.addComponent(labelBracelet)
						.addComponent(labelMdp)
						.addComponent(labelMdp2)
					)
				.addGroup(
					layout.createParallelGroup()
						.addComponent(textfieldPseudo)
						.addComponent(textfieldPrenom)
						.addComponent(textfieldNom)
						.addComponent(textfieldMail)
						.addComponent(textfieldBracelet)
						.addComponent(textfieldMdp)
						.addComponent(textfieldMdp2)
				)
		);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(
						layout.createParallelGroup()
							.addComponent(labelPseudo)
							.addComponent(textfieldPseudo)
					)
					.addGroup(
						layout.createParallelGroup()
							.addComponent(labelPrenom)
							.addComponent(textfieldPrenom)
					)
					.addGroup(
						layout.createParallelGroup()
							.addComponent(labelNom)
							.addComponent(textfieldNom)
					)
					.addGroup(
						layout.createParallelGroup()
							.addComponent(labelMail)
							.addComponent(textfieldMail)
					)
					.addGroup(
							layout.createParallelGroup()
								.addComponent(labelBracelet)
								.addComponent(textfieldBracelet)
					)
					.addGroup(
							layout.createParallelGroup()
								.addComponent(labelMdp)
								.addComponent(textfieldMdp)
					)
					.addGroup(
							layout.createParallelGroup()
								.addComponent(labelMdp2)
								.addComponent(textfieldMdp2)
					)
		);
		
		JPanel jPanel = new JPanel(true);

		setContentPane(jPanel);
		
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));
		jPanel.add(infosPanel);
		
		JPanel buttonPanel = new JPanel(true);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		buttonPanel.add(button3Annuler);
		buttonPanel.add(Box.createHorizontalStrut(15));
		buttonPanel.add(button4Ok);
		
		jPanel.add(buttonPanel);
		jPanel.add(Box.createVerticalGlue());
		
		setSize(new Dimension(400, 380));
	}
	
	
	class ButtonConfirmerListener implements ActionListener {
		

		public void actionPerformed(ActionEvent e) {
			
			// action si clic bouton Confirmer
			
			if ((textfieldMdp.getPassword().length > 0 || textfieldMdp2.getPassword().length > 0) && !(new String(textfieldMdp.getPassword()).equals(new String(textfieldMdp2.getPassword())))) // les mdp ne sont pas identiques
				JOptionPane.showMessageDialog(ModifierInfo.this, UserInformationsManagement.PASSWORD_AND_CONFIRMATION_NOT_EQUAL_MESSAGE, CommonErrorMessages.getFieldErrorTitle(Fields.PASSWORD), JOptionPane.ERROR_MESSAGE);
				
			else
			{

				String newPassword = new String (textfieldMdp.getPassword());		
				if(newPassword.length() > 0)
				{
					try {		
						EngineServiceProvider.getEngineService().modifyPassword(newPassword);
					} catch (NotAuthenticatedException e1)
					{
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
						return;
					} catch (IllegalFieldException e1)
					{
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.PASSWORD), CommonErrorMessages.getFieldErrorTitle(Fields.PASSWORD), JOptionPane.ERROR_MESSAGE);
						return;
					} catch (NetworkServiceException e1) {
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				
				if(textfieldMail.getText().length() > 0)
				{
					try {
						EngineServiceProvider.getEngineService().modifyEMailAddress(textfieldMail.getText());
					} catch (NotAuthenticatedException e1)
					{
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
						return;
					} catch (IllegalFieldException e1)
					{
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.EMAIL_ADDRESS), CommonErrorMessages.getFieldErrorTitle(Fields.EMAIL_ADDRESS), JOptionPane.ERROR_MESSAGE);
						return;
					} catch (NetworkServiceException e1) {
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				
				if(textfieldBracelet.getText().length() > 0)
				{
					if(! FieldVerifier.verifyTagUID(textfieldBracelet.getText()))
						JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.BRACELET), CommonErrorMessages.getFieldErrorTitle(Fields.BRACELET), JOptionPane.ERROR_MESSAGE);
					else
					{
						try {
							EngineServiceProvider.getEngineService().modifyBraceletUID(textfieldBracelet.getText());
						} catch (NotAuthenticatedException e1)
						{
							JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
							return;
						} catch (IllegalFieldException e1)
						{
							JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.BRACELET), CommonErrorMessages.getFieldErrorTitle(Fields.BRACELET), JOptionPane.ERROR_MESSAGE);
							return;
						} catch (NetworkServiceException e1) {
							JOptionPane.showMessageDialog(ModifierInfo.this, CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
							return;
						}
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
		Account account = EngineServiceProvider.getEngineService().getCurrentAccount();
		
		textfieldPseudo.setText(account.getPseudo());
		textfieldPrenom.setText(account.getFirstName());
		textfieldNom.setText(account.getLastName());
		textfieldMail.setText(account.getEMailAddress());
		textfieldBracelet.setText(account.getBraceletUID() == null ? "" : account.getBraceletUID());

		textfieldMdp.setText("");
		textfieldMdp2.setText("");
		
		setVisible(true);
	}
}
