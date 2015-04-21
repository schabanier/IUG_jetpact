/**
 * 
 */
package gui;

import engine.EngineServiceProvider;
import engine.FieldVerifier;
import exceptions.NotAuthenticatedException;
import gui.Constants.CommonErrorMessages;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

/**
 * @author propriétaire
 *
 */
public class AskPasswordDialog extends JDialog
{
	private static final long serialVersionUID = 4862202537420017454L;

	private JPanel panel1;
	private JLabel labelPseudo;
	private JTextField textFieldPseudo;
	private JLabel labelMdp;
	private JPasswordField passwordField1;
	private JButton buttonCancel;
	private JButton buttonValidate;


	public AskPasswordDialog(Dialog parent, String pseudo)
	{
		super(parent, true);
		init(pseudo);
	}

	public AskPasswordDialog(Frame parent, String pseudo)
	{
		super(parent, true);
		init(pseudo);
	}
	
	private void init(String pseudo)
	{
		setTitle("Mot de passe incorrect");
		
		
		labelPseudo = new JLabel();
		labelPseudo.setText(Constants.Fields.PSEUDO);
		textFieldPseudo = new JTextField();
		textFieldPseudo.setText(pseudo);
		textFieldPseudo.setEditable(false);
		
		labelMdp = new JLabel();
		labelMdp.setText(Constants.Fields.PASSWORD);
		passwordField1 = new JPasswordField();
		
		buttonCancel = new JButton();
		buttonValidate = new JButton();
		buttonValidate.setAlignmentX(RIGHT_ALIGNMENT);

		panel1 = new JPanel(true);

		panel1.setBorder(new EmptyBorder(5,15,5,5));

		GroupLayout layout = new GroupLayout(panel1);
		panel1.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(labelPseudo)
							.addComponent(labelMdp)
							.addComponent(buttonValidate)
						)
					.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(textFieldPseudo)
							.addComponent(passwordField1)
							.addComponent(buttonCancel)
						)
				);
		
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(labelPseudo)
							.addComponent(textFieldPseudo)
						)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(labelMdp)
							.addComponent(passwordField1)
						)
					.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(buttonValidate)
							.addComponent(buttonCancel)
						)
				);

		buttonCancel.setText(Constants.AskPassword.CANCEL_BUTTON_NAME);

		buttonCancel.addActionListener(new Button1Listener());

		buttonValidate.setText(Constants.AskPassword.VALIDATE_BUTTON_NAME);

		buttonValidate.addActionListener(new Button2Listener());

		setContentPane(panel1);
		
		pack();
	}
	
	class Button1Listener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}

	}

	class Button2Listener implements ActionListener {


		public void actionPerformed(ActionEvent e) {
			String password = new String(passwordField1.getPassword());
			
			if(! FieldVerifier.verifyPassword(password))	//il manque mdp1
				JOptionPane.showMessageDialog(AskPasswordDialog.this, Constants.CommonErrorMessages.getDefaultFieldErrorMessage(Constants.Fields.PASSWORD), Constants.CommonErrorMessages.getFieldErrorTitle(Constants.Fields.PASSWORD), JOptionPane.ERROR_MESSAGE);
			
			else
			{
				try {
					EngineServiceProvider.getEngineService().resolveAutoSynchronizationErrorOnPassword(password);
					
					setVisible(false);
					
				} catch (NotAuthenticatedException e1) {// this error will never occur.
					e1.printStackTrace();
					JOptionPane.showMessageDialog(AskPasswordDialog.this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
