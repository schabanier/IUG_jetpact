package gui.profils;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

public class GestionDeProfils extends JPanel 
{
	private JPanel principalPanel;
	private JPanel gestionProfilsPanel;
	private JPanel infoPanel ;
	private JList<String> profilsList ;
	private JButton buttonAjouterProfil ;
	private JFrame frameContainer;
	
	


	
	
	public GestionDeProfils(JFrame frameContainer) {
		
		super(true);
		this.frameContainer= frameContainer ;
		
		principalPanel = new JPanel();
		gestionProfilsPanel = new JPanel() ;
		infoPanel = new JPanel();
		profilsList = new JList();
		buttonAjouterProfil = new JButton() ;
		
		//==== Panel Principal ======
		
		principalPanel.setBorder(new EmptyBorder(5,15,5,5));
		
		
		// ==== Panel Gestion Profil =====
		
		gestionProfilsPanel.setBorder(new EmptyBorder(5,15,5,5));
		
		
		// ===== profilsList ====
		
		
		
		//==== Panel informations ====
		
		infoPanel.setBorder(new EmptyBorder(5,15,5,5));
		
		
		
		//====button Ajouter Profil ====
		
		buttonAjouterProfil.setText("Ajouter Profil");
		
	}
}
