package gui.profils;



	import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

	import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

	import data.Tag;
import data.Profile;
import engine.FieldVerifier;
import engine.NetworkServiceProvider;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import gui.Constants.CommonErrorMessages;
import gui.Constants.Fields;
import gui.Constants.TagsManagement;
import gui.profils.CreerProfil.MonList1SelectionListener;

	public class ProfileManagerDialog extends JDialog
	{
	
		private JPanel  panelL, panelR;
	    private JDialog dialog1;
		private JLabel labelAjout, labelDispo, labelNom ;
		private JTextField nomTextField;
		private JButton validateButton, cancelButton, buttonGauche, buttonDroite ;
		private JList<Tag> listAdd, listAvailable;
		private DefaultListModel<Tag> listModelAdd, listModelAvailable;
		
		
		
		/**
		 * true if this dialog box is used to add a profile.
		 * false if this dialog box is used to modify a profile.
		 */
		private boolean AddOrModify;
		
		/**
		 * the profile which is modified in this dialog box.
		 */
		private Profile currentProfile;
		private boolean isProfileModified;
		
		private Profile profileAdded;

	


		public ProfileManagerDialog(Frame owner)
		{
			super(owner, true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
		
			panelL= new JPanel();
			panelR= new JPanel();
			labelAjout = new JLabel();
			labelDispo = new JLabel();
			labelNom = new JLabel();
			nomTextField = new JTextField();
			buttonGauche = new JButton();
			buttonDroite = new JButton();
			
			
			
			listAdd = new JList (listModelAdd);
			listAvailable = new JList(listModelAvailable);
			listModelAdd = new DefaultListModel<Tag>();
			listModelAvailable = new DefaultListModel<Tag>();
			
			
			JPanel contentPane = new JPanel(true);
			contentPane.setBorder(new EmptyBorder(12, 7, 12, 7));
			contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
			setContentPane(contentPane);
			
			JPanel fieldsPanel = new JPanel(true);
			fieldsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

			GroupLayout layout = new GroupLayout(fieldsPanel);
			fieldsPanel.setLayout(layout);
			
//			layout.setAutoCreateContainerGaps(true);
			layout.setAutoCreateGaps(true);
			
			
           //=====label=====
			
			labelAjout.setText("Tags added");
			labelDispo.setText("Tags availbale");
			labelNom.setText("Profile's name");
			
			

			
			
			
			
		;
		//====listes=====
		
		
		JScrollPane spListAdd = new JScrollPane( listAdd); // � ajouter dans un panel !!
		spListAdd.setPreferredSize(new Dimension(250, 80));
		
		listAdd.setVisibleRowCount( 10 );
		listAdd.setFixedCellHeight( 2 );
		listAdd.setFixedCellWidth(8);
		listAdd.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listAdd.setLayoutOrientation(JList.VERTICAL);
		listAdd.setSelectionForeground(new Color(0,255,255));
		listAdd.setDragEnabled(true);
		
		
        JScrollPane spListAvailable = new JScrollPane( listAvailable); // � ajouter dans un panel !!
        spListAvailable.setPreferredSize(new Dimension(250, 80));
		
    	listAvailable.setVisibleRowCount( 10 );
		listAvailable.setFixedCellHeight( 2 );
		listAvailable.setFixedCellWidth(8);
		listAvailable.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listAvailable.setLayoutOrientation(JList.VERTICAL);
		listAvailable.setSelectionForeground(new Color(0,255,255));
		listAvailable.setDragEnabled(true);
		

			
			
			// buttons
			
			JPanel buttonsPanel = new JPanel(true);
			buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
			
			JButton cancelButton = new JButton(TagsManagement.CANCEL_BUTTON_NAME);
			cancelButton.setDoubleBuffered(true);
			cancelButton.addActionListener(new ActionCancel());
			
			validateButton = new JButton(TagsManagement.VALIDATE_TAG_ADDITION_BUTTON_NAME);
			validateButton.setDoubleBuffered(true);
			validateButton.addActionListener(new ActionValidate());

			buttonsPanel.add(Box.createHorizontalGlue());
			buttonsPanel.add(cancelButton);
			buttonsPanel.add(Box.createHorizontalStrut(10));
			buttonsPanel.add(validateButton);
			buttonsPanel.add(Box.createHorizontalGlue());
			
			getContentPane().add(Box.createVerticalStrut(10));
			getContentPane().add(buttonsPanel);
			
			setSize(350, 220);
			setResizable(false);
		}
		
		
		
		class ActionCancel implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		}
		
		class ActionValidate implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if(AddOrModify)
					actionAddProfile();
				else
					actionModifyProfile();
			}
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
		
	// the texts below this line are not still processed.
		
		private void actionModifyProfile()
		
	
		{  boolean fieldToBeModified = false;
			
		
		//ajouter, enlever puces du profil
		
			int sizeAdd = listModelAdd.getSize();
			int i = 0;
			
		for(i=0; i < sizeAdd ; i++)
		{
		        //Si la puce n'appartient pas déjà au profil if(puce n'appartient pas au profil)
			
			    
			
				try {
					
				 NetworkServiceProvider.getNetworkService().addTagToProfile(currentProfile, (Tag) listAdd.getComponent(i));
				 
				 
				 
				}catch(NotAuthenticatedException e1)
				{
					JOptionPane.showMessageDialog(null, "Vous n'êtes pas correctement authentifié", "Erreur", JOptionPane.ERROR_MESSAGE);
			
				}
				
			int sizeAvailable =listModelAdd.getSize();
			int j = 0;
			
			
			for(j=0; i<sizeAvailable;j++)
			try {
				
				//si la puce appartient au profil if(puce appartient au profil)
				
				 NetworkServiceProvider.getNetworkService().removeTagFromProfile(currentProfile, (Tag) listAdd.getComponent(j));
				
				 
				}catch(NotAuthenticatedException e1)
				{
					JOptionPane.showMessageDialog(null, "Vous n'êtes pas correctement authentifié", "Erreur", JOptionPane.ERROR_MESSAGE);
			
				}
			
			String profileName = nomTextField.getText();
			
			if(! profileName.equals(currentProfile.getName())) // the profile name is modified.
			{
				fieldToBeModified = true;
				try {
					currentProfile = NetworkServiceProvider.getNetworkService().modifyProfileName(currentProfile, profileName);
					isProfileModified = true;
					setVisible(false);
				} catch (IllegalFieldException e) {
					switch(e.getFieldId())
					{
						case IllegalFieldException.TAG_UID :
							if(e.getReason() == IllegalFieldException.REASON_VALUE_NOT_FOUND)
								JOptionPane.showMessageDialog(this, TagsManagement.MODIFICATION_TAG_NOT_FOUND_MESSAGE, TagsManagement.MODIFICATION_TAG_NOT_FOUND_TITLE, JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
						break;
						case IllegalFieldException.TAG_OBJECT_NAME :
							if(e.getReason() == IllegalFieldException.REASON_VALUE_ALREADY_USED)
								JOptionPane.showMessageDialog(this, TagsManagement.getObjectNameAlreadyUsedMessage(objectName), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_OBJECT_NAME), JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.TAG_OBJECT_NAME), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_OBJECT_NAME), JOptionPane.ERROR_MESSAGE);
						break;
						default:
							JOptionPane.showMessageDialog(this, CommonErrorMessages.UNKNOWN_ERROR_MESSAGE, CommonErrorMessages.UNKNOWN_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
						break;
					}
				} catch (NotAuthenticatedException e) {// abnormal error.
					JOptionPane.showMessageDialog(this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
				} catch (NetworkServiceException e) {
					JOptionPane.showMessageDialog(this, CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}
			
			
				 
			
			
		}
		}
		
		
		/**
		 * To add a new profile to the current account.
		 * @return the new profile added if there is one, null otherwise.
		 */
		public Profile addProfile()
		{
			AddOrModify = true;
			profileAdded = null;
			
			setTitle("New Profile");
			nomTextField.setEditable(true);
			
			validateButton.setText("Validate");
			
			setLocation(getParent().getX() + (getParent().getWidth() - getWidth())/2, getParent().getY() + (getParent().getHeight() - getHeight())/2);
			// repositionnement de la fenêtre si elle sort de l'écran en haut ou à gauche.
			if(getLocation().getX() < 0)
				setLocation(0, (int) getLocation().getY());
			if(getLocation().getY() < 0)
					setLocation((int) getLocation().getX(), 0);
			
			setVisible(true);
			return profileAdded;
		}
		

		/**
		 * To modify a tag of the current account.
		 * @param tag the tag to modify
		 * @return the tag modified if there is a modification, null otherwise.
		 */
		public Profile modifyProfile(Profile profile)
		{
			if(profile == null)
				throw new NullPointerException();
			
			currentProfile = profile;
			isProfileModified = false;
			AddOrModify = false;
			
			setTitle("Modification of Profile");
			nomTextField.setText(profile.getName());
			nomTextField.setEditable(true);
			
			
			validateButton.setText("Valider");

			setLocation(getParent().getX() + (getParent().getWidth() - getWidth())/2, getParent().getY() + (getParent().getHeight() - getHeight())/2);
			// repositionnement de la fenêtre si elle sort de l'écran en haut ou à gauche.
			if(getLocation().getX() < 0)
				setLocation(0, (int) getLocation().getY());
			if(getLocation().getY() < 0)
					setLocation((int) getLocation().getX(), 0);
			
			setVisible(true);
			
			return isProfileModified ? currentProfile : null;
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
				
				if(((ListSelectionModel) listModelAdd).isSelectedIndex(i))
				{
					listModelAvailable.addElement(listAdd.getComponent(i));
					listModelAdd.remove(i);
					
					
					 
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
					
					
				}
				
			}
				
			
				
			}
		}
	}



