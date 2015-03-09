package gui.profils;

import gui.infoperso.ModifierInfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GestionDeProfils extends JPanel 
{
	private JPanel principalPanel;
	private JPanel panelLeft, panelRight;
	
	private JButton buttonAjouterProfil ;
	private JFrame frameContainer;
	
	


	
	
	public GestionDeProfils(JFrame frameContainer) {
		
		super(true);
		this.frameContainer= frameContainer ;
		
		principalPanel = new JPanel();
		panelLeft = new JPanel() ;
		panelRight = new JPanel();
		
		
		buttonAjouterProfil = new JButton() ;
		
		//==== Panel Principal ======
		
		principalPanel.setBorder(new EmptyBorder(5,15,5,5));
		
		
		// ==== Panel Gestion Profil =====
		
		panelLeft.setBorder(new EmptyBorder(5,15,5,5));
		
		
		// ===== profilsList ====
		
		
		
		//==== Panel informations ====
		
		panelRight.setBorder(new EmptyBorder(5,15,5,5));
		
		
		
		//====button Ajouter Profil ====
		
		buttonAjouterProfil.setText("Ajouter Profil");
		
	}
	
	
	class buttonAjoutProfilListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			CreerProfil creerProfilFenetre = new CreerProfil(frameContainer, "Créer un profil");
		     creerProfilFenetre.setVisible(true);
			
		
			
			
		}
		
	}
}
