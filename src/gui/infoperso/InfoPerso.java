package gui.infoperso;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class InfoPerso extends JPanel {
	
	
	private JPanel infoPersoPanel;
	private JLabel labelPseudo, labelMail, labelMdp, labelNombreDePuces, labelNombreDeProfils;
	private JLabel labelPseudoInfo, labelMailInfo, labelMdpInfo, labelNombreDePucesInfo, labelNombreDeProfilsInfo;
	private JButton buttonModifier ;
	private JFrame frameContainer;
	
	public InfoPerso(JFrame frameContainer) {
		
		
		super(true);
		this.frameContainer = frameContainer;
		infoPersoPanel = this;
		labelPseudo = new JLabel(); 
		labelMail = new JLabel(); 
		labelMdp = new JLabel();
		labelNombreDePuces = new JLabel();
		labelNombreDeProfils = new JLabel();
		
		//======== infoPersoPanel ====
		
		infoPersoPanel.setBorder(new EmptyBorder(5,15,5,5));
		
		
		//==== label======
		
		labelPseudo.setText("Pseudo");
		labelMail.setText("Mail");
		labelMdp.setText("Mot de passe");
		labelNombreDePuces.setText("Nombre de puces");
		labelNombreDeProfils.setText("Nombre de profils");
		labelPseudoInfo.setText("Thomas35");
		labelMailInfo.setText("thomas.roger@gmail.fr");
		labelNombreDePucesInfo.setText("5");
		labelNombreDeProfilsInfo.setText("2");
		
		
		//=====buttonModifier=====
		
		buttonModifier.setText("Modifier");
		buttonModifier.addActionListener(new ButtonModifierListener());
		
		
		
		
		
	}
	
	
	
  class ButtonModifierListener implements ActionListener {
	  
	  
	  public void actionPerformed(ActionEvent e) {
		  
		  //action si on clic sur bouton modifier 

			 ModifierInfo modifierInfoFenetre = new ModifierInfo(frameContainer, "Modifier Informations Personnelles");
		     modifierInfoFenetre.setVisible(true);
			
	        }

  }
  
}  
