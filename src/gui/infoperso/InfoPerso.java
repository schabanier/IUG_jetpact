package gui.infoperso;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import data.Account;
import engine.NetworkServiceProvider;
import exceptions.NotAuthenticatedException;


public class InfoPerso extends JPanel {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -1176567312519132553L;
	
	private JPanel infoPersoPanel;
	private JLabel labelPseudo, labelPrenom, labelNom, labelMail, labelMdp, labelNombreDePuces, labelNombreDeProfils;
	private JTextField textfieldPseudo, textfieldPrenom, textfieldNom, textfieldMail, textfieldMdp;
	private JLabel labelNombreDePucesInfo, labelNombreDeProfilsInfo;
	private JButton buttonModifier ;
	private JFrame frameContainer;
	
	public InfoPerso(JFrame frameContainer) {
		
		
		super(true);
		this.frameContainer = frameContainer;
		infoPersoPanel = this;
		
		labelPseudo = new JLabel();
		labelPrenom = new JLabel();
		labelNom = new JLabel();
		labelMail = new JLabel(); 
		labelMdp = new JLabel();
		labelNombreDePuces = new JLabel();
		labelNombreDeProfils = new JLabel();
		labelNombreDeProfilsInfo = new JLabel();
		labelNombreDePucesInfo = new JLabel();
		
		//======== infoPersoPanel ====
		
		infoPersoPanel.setBorder(new EmptyBorder(5,15,5,5));
		
		
		//==== label======
		
		labelPseudo.setText("Pseudo");
		labelMail.setText("Mail");
		labelMdp.setText("Mot de passe");
		labelNombreDePuces.setText("Nombre de puces");
		labelNombreDeProfils.setText("Nombre de profils");
		labelNombreDePucesInfo.setText("5");
		labelNombreDeProfilsInfo.setText("2");
		
		Dimension maxDimension = new Dimension(2000, 20);
		// textfield
		textfieldPseudo = new JTextField();
		textfieldPseudo.setEditable(false);
		textfieldPseudo.setMaximumSize(maxDimension);
		
		textfieldPrenom = new JTextField();
		textfieldPrenom.setEditable(false);
		textfieldPrenom.setMaximumSize(maxDimension);
		
		textfieldNom = new JTextField();
		textfieldNom.setEditable(false);
		textfieldNom.setMaximumSize(maxDimension);
		
		textfieldMail = new JTextField();
		textfieldMail.setEditable(false);
		textfieldMail.setMaximumSize(maxDimension);
		
		textfieldMdp = new JTextField();
		textfieldMdp.setEditable(false);
		textfieldMdp.setMaximumSize(maxDimension);
		
		
		//=====buttonModifier=====
		
		buttonModifier = new JButton();
		buttonModifier.setText("Modifier");
		buttonModifier.addActionListener(new ButtonModifierListener());
		
		
		JPanel infosPanel = new JPanel();
		
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
						.addComponent(labelMdp)
						.addComponent(labelNombreDePuces)
						.addComponent(labelNombreDeProfils)
					)
				.addGroup(
					layout.createParallelGroup()
						.addComponent(textfieldPseudo)
						.addComponent(textfieldPrenom)
						.addComponent(textfieldNom)
						.addComponent(textfieldMail)
						.addComponent(textfieldMdp)
						.addComponent(labelNombreDePucesInfo)
						.addComponent(labelNombreDeProfilsInfo)
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
							.addComponent(labelMdp)
							.addComponent(textfieldMdp)
					)
					.addGroup(
						layout.createParallelGroup()
							.addComponent(labelNombreDePuces)
							.addComponent(labelNombreDePucesInfo)
					)
					.addGroup(
						layout.createParallelGroup()
							.addComponent(labelNombreDeProfils)
							.addComponent(labelNombreDeProfilsInfo)
					)
		);
		
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(infosPanel);
		add(buttonModifier);
		add(Box.createVerticalGlue());
		
	}
	
	
	
  class ButtonModifierListener implements ActionListener {
	  
	  
	  public void actionPerformed(ActionEvent e) {
		  
		  //action si on clic sur bouton modifier 

			 ModifierInfo modifierInfoFenetre = new ModifierInfo(frameContainer, "Modifier Informations Personnelles");
		     modifierInfoFenetre.setVisible(true);
			
	        }

  }
  
  public void reloadDisplayedInformations() throws NotAuthenticatedException
  {
	  textfieldPseudo.setText("");
	  textfieldPrenom.setText("");
	  textfieldNom.setText("");
	  textfieldMail.setText("");
	  textfieldMdp.setText("");
	  
	  Account account = NetworkServiceProvider.getNetworkService().getCurrentAccount();

	  textfieldPseudo.setText(account.getPseudo());
	  textfieldPrenom.setText(account.getFirstName());
	  textfieldNom.setText(account.getLastName());
	  textfieldMail.setText(account.getEMailAddress());
	  textfieldMdp.setText("********");
  }
  
}  
