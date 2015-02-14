package gui.profils;

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
	private JPanel panel1, panel2;
	private JList<String> profilsList ;
	private JButton buttonAjouterProfil ;
	private JFrame frameContainer;
	
	


	
	
	public GestionDeProfils(JFrame frameContainer) {
		
		super(true);
		this.frameContainer= frameContainer ;
		
		principalPanel = new JPanel();
		panel1 = new JPanel() ;
		panel2 = new JPanel();
		profilsList = new JList();
		
		buttonAjouterProfil = new JButton() ;
		
		//==== Panel Principal ======
		
		principalPanel.setBorder(new EmptyBorder(5,15,5,5));
		
		
		// ==== Panel Gestion Profil =====
		
		panel1.setBorder(new EmptyBorder(5,15,5,5));
		
		
		// ===== profilsList ====
		
		
		
		//==== Panel informations ====
		
		panel2.setBorder(new EmptyBorder(5,15,5,5));
		
		
		
		//====button Ajouter Profil ====
		
		buttonAjouterProfil.setText("Ajouter Profil");
		
	}
	
	
	class buttonAjoutProfilListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			// action si clic bouton ajouter profil
			
			
			
			
			
		}
		
	}
}
