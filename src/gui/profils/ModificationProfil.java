package gui.profils;

import gui.profils.CreerProfil.ButtonAnnulerListener;
import gui.profils.CreerProfil.ButtonCreerListener;
import gui.profils.CreerProfil.MonList1SelectionListener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Tag;

public class ModificationProfil extends JDialog {

	
	
	private JPanel panelPrincipal, panel1, panel2;
    private JDialog dialog1;
	private JLabel labelAjout, labelDispo, labelNom ;
	private JTextField nomTextField;
	private JButton buttonModifier, buttonAnnuler, buttonGauche, buttonDroite ;
	private JList list1, list2;
	private Tag[] listPuces1, listPuces2 ;
	
	
	
	
	
	public ModificationProfil(JFrame gestionProfilParent, String title) {
		
		
		super(gestionProfilParent, title);
		
		panelPrincipal = new JPanel();
		panel1= new JPanel();
		panel2= new JPanel();
		dialog1 = new JDialog();
		labelAjout = new JLabel();
		labelDispo = new JLabel();
		labelNom = new JLabel();
		nomTextField = new JTextField();
		buttonModifier = new JButton();
		buttonAnnuler = new JButton();
		buttonGauche = new JButton();
		buttonDroite = new JButton();
		
		
		listPuces1 = new Tag[25];
	    listPuces2 = new Tag[25];
		list1 = new JList (listPuces1);
		list2 = new JList(listPuces2);
		
		
		
		//======== dialog1 ========
		
		
			dialog1.setBackground(new Color(0, 153, 153));
			dialog1.setLocationRelativeTo(null);
			dialog1.setResizable(false);
			
			dialog1.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			
			
		//====panelPrincipal=========
			

			//====panel1=========
			
			

			//====panel2=========
			
			
			
		//====listes=====
			
			
			JScrollPane spList1 = new JScrollPane( list1); // à ajouter dans un panel !!
			
			list1.setVisibleRowCount( 10 );
			list1.setFixedCellHeight( 2 );
			list1.setFixedCellWidth(8);
			list1.addListSelectionListener( new MonList1SelectionListener());
			list1.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			list1.setLayoutOrientation(VERTICAL);
			list1.setSelectionForeground(new Color(0,255,255));
			list1.setDragEnabled(true);
			
			
            JScrollPane spList2 = new JScrollPane( list2); // à ajouter dans un panel !!
			
			list2.setVisibleRowCount( 10 );
			list2.setFixedCellHeight( 2 );
			list2.setFixedCellWidth(8);
			list2.addListSelectionListener( new MonList2SelectionListener());
			list2.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			
			
			
			
		//=====label=====
			
			labelAjout.setText("Puces ajoutée");
			labelDispo.setText("Puces disponibles");
			labelNom.setText("Nom du profil");
			
			
			//======button=====
			
		buttonAnnuler.setText("Annuler");
		buttonAnnuler.addActionListener(new ButtonAnnulerListener());
		
		
		buttonModifier.setText("Creer Compte");
		buttonModifier.addActionListener( new ButtonCreerListener()) ;
	
		
			

}
	
	
	public void setList1(Tag[] listModif)
	{
		//modification de la list1
	}
	
	public void setList2(Tag[] listModif)
	{
		//modification de la list2
	}
	
	
	class ButtonAnnulerListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// action si clic bouton annuler
			
			
			ModificationProfil.this.setVisible(false);
			
		}
		
	}
		
		
	class ButtonCreerListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// action si clic ok
			
		}
		
	}
	
	
		
	class ButtonDroiteListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			
			
				
				
			Tag[] tab1= new Tag[20];	
		    tab1 = (Tag[]) list1.getSelectedValues();
		    
		    Tag[] tab2= new Tag[20];	
		    tab2 = listPuces2.concanate(tab1); //méthode concataner tableaux
			
		    Tag[] tab3= new Tag[20];
		    tab3 = //listePuces1 - tab1 ;
		    
			list1.setListData(tab3);
			list2.setListData(tab1);
			
			list1.clearSelection();
			
			//faire des exceptions
			
		}
		
		
	}
	
	class ButtonGaucheListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			Tag[] tab1= new Tag[20];	
		    tab1 = (Tag[]) list2.getSelectedValues();
		    
		    Tag[] tab2= new Tag[20];	
		    tab2 = listPuces1.concanate(tab1); //méthode concataner tableaux
			
		    Tag[] tab3= new Tag[20];
		    tab3 = //listePuces2 - tab1 ;
		    
			list2.setListData(tab3);
			list1.setListData(tab1);
			
			list2.clearSelection();
			
			//faire des exceptions
			
		}
	}
	
	 class MonList1SelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			// stocke les puces à enlever du profil
			
			Tag[] tab1 = new Tag[25];
			tab1 = (Tag[]) list1.getSelectedValues();
		}

		
	}
	
	
	
	class MonListSelection2Listener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			
			//stocker les puces à ajouter au profil
			
			Tag[] tab2 = new Tag[25];
			tab2 = (Tag[]) list2.getSelectedValues();
			
		}	
	}
		
	
	
	
	
}


