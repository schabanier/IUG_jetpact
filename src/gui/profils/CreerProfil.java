package gui.profils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Profile;
import data.Tag;
import engine.NetworkServiceProvider;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import gui.Constants.TagsManagement;
import gui.infoperso.ModifierInfo;

public class CreerProfil extends JDialog {
	
	
	private JPanel panelPrincipal, panelL, panelR;
    private JDialog dialog1;
	private JLabel labelAjout, labelDispo, labelNom ;
	private JTextField nomTextField;
	private JButton buttonCreer, buttonAnnuler, buttonGauche, buttonDroite ;
	private JList<Tag> listAdd, listAvailable;
	private DefaultListModel<Tag> listModelAdd, listModelAvailable;
	
	private Profile profile;
	private Profile profileAdded;
	
	
	
	
	
	public CreerProfil(JFrame owner, String title) {
		
		
		super(owner, title);
		
		panelPrincipal = new JPanel();
		panelL= new JPanel();
		panelR= new JPanel();
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
			
			labelAjout.setText("Tags added");
			labelDispo.setText("Tags availbale");
			labelNom.setText("Profile's name");
			
			
			//======button=====
			
		buttonAnnuler.setText("Cancel");
		buttonAnnuler.addActionListener(new ButtonAnnulerListener());
		
		
		buttonCreer.setText("Creer Compte");
		buttonCreer.addActionListener( new ButtonCreerListener()) ;
		
		buttonDroite.addActionListener( new ButtonDroiteListener(listAdd,listAvailable, listModelAdd, listModelAvailable, nomTextField));
		
		buttonGauche.addActionListener( new ButtonGaucheListener(listAdd, listAvailable, listModelAdd, listModelAvailable, nomTextField));
	
		
			

}
	
	private void actionAddProfile()
	
	{
	    String nomProfile = nomTextField.getText();
	    List<Tag> listTags = new ArrayList<>() ;
	    
	    for(Object tag:listModelAdd.toArray())
	    	listTags.add((Tag) tag);
	    

		
		 try {
			NetworkServiceProvider.getNetworkService().createProfile(nomProfile,listTags);
			
			//addTagFromProfile
		} catch (NotAuthenticatedException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Vous n'êtes pas correctement authentifié", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NetworkServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		}
	
	
	public Profile addProfile()
	{
		
		profileAdded = null;
		
		
		
		setLocation(getParent().getX() + (getParent().getWidth() - getWidth())/2, getParent().getY() + (getParent().getHeight() - getHeight())/2);
		// repositionnement de la fenêtre si elle sort de l'écran en haut ou à gauche.
		if(getLocation().getX() < 0)
			setLocation(0, (int) getLocation().getY());
		if(getLocation().getY() < 0)
				setLocation((int) getLocation().getX(), 0);
		
		setVisible(true);
		return profileAdded;
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
			
			actionAddProfile();
			
		}
		
	}
		
		
			
		
		
	
	
	
		
	class ButtonDroiteListener implements ActionListener {
		
		private DefaultListModel listModelAdd, listModelAvailable;
		private JList listAdd, listAvailable;
		private JTextField nomTextField;
		
		public ButtonDroiteListener(JList listAdd, JList listAvailable, DefaultListModel listModelAdd, DefaultListModel listModelAvailable, JTextField nomTextField) 
		{
			this.listAdd= listAdd ;
			this.listAvailable = listAvailable;
			this.listModelAdd = listModelAdd;
			this.listModelAvailable = listModelAvailable;
			this.nomTextField = nomTextField;
		}

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
				
				try {
					
				 NetworkServiceProvider.getNetworkService().removeTagFromProfile(nomTextField.getText(), (Tag) listModelAdd.get(i));
				}catch(NotAuthenticatedException e1)
				{
					JOptionPane.showMessageDialog(null, "Vous n'êtes pas correctement authentifié", "Erreur", JOptionPane.ERROR_MESSAGE);
			
				}
				 
			}
			
		}
		
			
			
		} 
		
		
	}
	
	class ButtonGaucheListener implements ActionListener {
		
		private JList listAdd, listAvailable;
		private DefaultListModel listModelAdd, listModelAvailable;
		private JTextField nomTextField;
		
		public ButtonGaucheListener(JList listAdd, JList listAvailable, DefaultListModel listModelAdd, DefaultListModel listModelAvailable, JTextField nomTextField)
		{
			this.listAdd = listAdd;
			this.listAvailable = listAvailable ;
			this.listModelAdd = listModelAdd;
			this.listModelAvailable = listModelAvailable;
			this.nomTextField = nomTextField;
			
		}
		
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
				
				try {
					
				 NetworkServiceProvider.getNetworkService().addTagToProfile(nomTextField.getText(),listAvailable.getM);
				} catch(NotAuthenticatedException e1)
				{
					JOptionPane.showMessageDialog(null, "Vous n'êtes pas correctement authentifié", "Erreur", JOptionPane.ERROR_MESSAGE);
			
				}
			}
			
		}
			
		
			
		}
	}
	
	 class MonList1SelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			// stocke les puces � enlever du profil
			
			

		
	}
	}
	
	
	
	class MonListSelection2Listener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			
			//stocker les puces � ajouter au profil
			
			
		}	
	}
		
	
	
	
	}	


