package gui.profils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Tag;
import engine.NetworkServiceProvider;
import exceptions.NotAuthenticatedException;

public class CreerProfil extends JDialog {
	
	
	private JPanel panelPrincipal, panel1, panel2;
    private JDialog dialog1;
	private JLabel labelAjout, labelDispo, labelNom ;
	private JTextField nomTextField;
	private JButton buttonCreer, buttonAnnuler, buttonGauche, buttonDroite ;
	private JList listAdd, listAvailable;
	private DefaultListModel listModelAdd, listModelAvailable;
	
	
	
	
	
	public CreerProfil(JFrame gestionProfilParent, String title) {
		
		
		super(gestionProfilParent, title);
		
		panelPrincipal = new JPanel();
		panel1= new JPanel();
		panel2= new JPanel();
		dialog1 = new JDialog();
		labelAjout = new JLabel();
		labelDispo = new JLabel();
		labelNom = new JLabel();
		nomTextField = new JTextField();
		buttonCreer = new JButton();
		buttonAnnuler = new JButton();
		buttonGauche = new JButton();
		buttonDroite = new JButton();
		
		
		
		listAdd = new JList (listModelAdd);
		listAvailable = new JList(listModelAvailable);
		listModelAdd = new DefaultListModel();
		listModelAvailable = new DefaultListModel();
		
		
		
		//======== dialog1 ========
		
		
			dialog1.setBackground(new Color(0, 153, 153));
			dialog1.setLocationRelativeTo(null);
			dialog1.setResizable(false);
			
			dialog1.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			
			
		//====panelPrincipal=========
			

			//====panel1=========
			
			

			//====panel2=========
			
			
			
		//====listes=====
			
			
			JScrollPane spListAdd = new JScrollPane( listAdd); // � ajouter dans un panel !!
			spListAdd.setPreferredSize(new Dimension(250, 80));
			
			listAdd.setVisibleRowCount( 10 );
			listAdd.setFixedCellHeight( 2 );
			listAdd.setFixedCellWidth(8);
			listAdd.addListSelectionListener( new MonList1SelectionListener());
			listAdd.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			listAdd.setLayoutOrientation(JList.VERTICAL);
			listAdd.setSelectionForeground(new Color(0,255,255));
			listAdd.setDragEnabled(true);
			
			
            JScrollPane spListAvailable = new JScrollPane( listAvailable); // � ajouter dans un panel !!
            spListAvailable.setPreferredSize(new Dimension(250, 80));
			
        	listAvailable.setVisibleRowCount( 10 );
			listAvailable.setFixedCellHeight( 2 );
			listAvailable.setFixedCellWidth(8);
			listAvailable.addListSelectionListener( new MonList1SelectionListener());
			listAvailable.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			listAvailable.setLayoutOrientation(JList.VERTICAL);
			listAvailable.setSelectionForeground(new Color(0,255,255));
			listAvailable.setDragEnabled(true);
			

			
			
			
			
		//=====label=====
			
			labelAjout.setText("Puces ajout�e");
			labelDispo.setText("Puces disponibles");
			labelNom.setText("Nom du profil");
			
			
			//======button=====
			
		buttonAnnuler.setText("Annuler");
		buttonAnnuler.addActionListener(new ButtonAnnulerListener());
		
		
		buttonCreer.setText("Creer Compte");
		buttonCreer.addActionListener( new ButtonCreerListener()) ;
	
		
			

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
			
			
			CreerProfil.this.setVisible(false);
			
		}
		
	}
		
		
	class ButtonCreerListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// action si clic ok
			
			 try {
				NetworkServiceProvider.getNetworkService().createProfile(nomTextField.getText());
				
				//addTagFromProfile
			} catch (NotAuthenticatedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
			
		}
		
	}
	
	
		
	class ButtonDroiteListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			int size = listModelAdd.getSize();
			int i = 0;
			
		for(i=0; i < size ; i++)
		{
			//enleve element de listAdd si il est selectionné et l'ajoute a listAvailable
			
			if(listAdd.isSelectedIndex(i))
			{
				listModelAvailable.addElement(listAdd.getComponent(i));
				listModelAdd.remove(i);
			}
			
		}
		
			
			// faire des exceptions
			
		} 
		
		
	}
	
	class ButtonGaucheListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			int size = listModelAvailable.getSize();
			int i = 0;
			
		for(i=0; i < size ; i++)
		{
			//enleve element de listAdd si il est selectionné et l'ajoute a listAvailable
			
			if(listAvailable.isSelectedIndex(i))
			{
				listModelAdd.addElement(listAvailable.getComponent(i));
				listModelAvailable.remove(i);
			}
			
		}
			
		
			
		}
	}
	
	 class MonList1SelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			// stocke les puces � enlever du profil
			
			Tag[] tab1 = new Tag[25];
			tab1 = (Tag[]) listAdd.getSelectedValues();
		}

		
	}
	
	
	
	class MonListSelection2Listener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			
			//stocker les puces � ajouter au profil
			
			Tag[] tab2 = new Tag[25];
			tab2 = (Tag[]) listAvailable.getSelectedValues();
			
		}	
	}
		
	
	
	
	
}
