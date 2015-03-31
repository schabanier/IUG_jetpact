package gui.profils;



import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
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

	        labelAjout = new JLabel();
	        labelDispo = new JLabel();
	        labelNom = new JLabel();
	        nomTextField = new JTextField();
	        buttonGauche = new JButton("<<");
	        buttonDroite = new JButton(">>");


	        listModelAdd = new DefaultListModel<>();
	        listModelAvailable = new DefaultListModel<>();

	        listAdd = new JList<> (listModelAdd);
	        listAvailable = new JList<>(listModelAvailable);


	        JPanel contentPane = new JPanel(true);
//	        contentPane.setBorder(new EmptyBorder(12, 7, 12, 7));
	        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
	        setContentPane(contentPane);

	        JPanel fieldsPanel = new JPanel(true);
	        fieldsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

	        GroupLayout layout = new GroupLayout(fieldsPanel);
	        fieldsPanel.setLayout(layout);

	        //            layout.setAutoCreateContainerGaps(true);
	        layout.setAutoCreateGaps(true);
	        layout.setAutoCreateContainerGaps(true);


	        //=====label=====

	        labelAjout.setText("Tags added");
	        labelDispo.setText("Tags available");
	        labelNom.setText("Profile's name");


	        //====listes=====


	        JScrollPane spListAdd = new JScrollPane( listAdd); // � ajouter dans un panel !!
	        spListAdd.setPreferredSize(new Dimension(250, 80));
	        listAdd.setCellRenderer(new DefaultListCellRenderer(){ // set custom renderer to disable selection on graphical view.
	            private static final long serialVersionUID = 1L;
	            @SuppressWarnings("unused")
	            public Component getListCellRendererComponent(JList<? extends String> list, Tag value, int index, boolean isSelected, boolean cellHasFocus)
	            {
	                return super.getListCellRendererComponent(list, value.getObjectName(), index, false, cellHasFocus);
	            }
	        });
//	        listAdd.setVisibleRowCount( 10 );
//	        listAdd.setFixedCellHeight( 2 );
//	        listAdd.setFixedCellWidth(8);
	        listAdd.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//	        listAdd.setLayoutOrientation(JList.VERTICAL);

//	        listAdd.setDragEnabled(true);


	        JScrollPane spListAvailable = new JScrollPane( listAvailable); // � ajouter dans un panel !!
	        spListAvailable.setPreferredSize(new Dimension(250, 80));

//	        listAvailable.setVisibleRowCount( 10 );
//	        listAvailable.setFixedCellHeight( 2 );
//	        listAvailable.setFixedCellWidth(8);
	        listAvailable.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//	        listAvailable.setLayoutOrientation(JList.VERTICAL);
	//
//	        listAvailable.setDragEnabled(true);

	        JButton cancelButton = new JButton(TagsManagement.CANCEL_BUTTON_NAME);
	        cancelButton.setDoubleBuffered(true);
	        cancelButton.addActionListener(new ActionCancel());

	        validateButton = new JButton(TagsManagement.VALIDATE_TAG_ADDITION_BUTTON_NAME);
	        validateButton.setDoubleBuffered(true);
	        validateButton.addActionListener(new ActionValidate());
	        
	        
	        layout.setHorizontalGroup(
	            layout.createSequentialGroup()
	                .addGroup(
	                    layout.createParallelGroup()
	                        .addGroup(
	                            layout.createSequentialGroup()
	                                .addComponent(labelNom)
	                                .addComponent(nomTextField)
	                        )
	                        .addGap(10)
	                        .addGroup(
	                            layout.createSequentialGroup()
	                                .addGroup(
	                                    layout.createParallelGroup()
	                                        .addComponent(labelAjout, Alignment.CENTER)
	                                        .addComponent(spListAdd)
	                                )
	                                .addGroup(
	                                    layout.createParallelGroup()
	                                        .addComponent(buttonDroite)
	                                        .addComponent(buttonGauche)
	                                )
	                                .addGroup(
	                                    layout.createParallelGroup()
	                                        .addComponent(labelDispo, Alignment.CENTER)
	                                        .addComponent(spListAvailable)
	                                )
	                        )
	                        .addGap(10)
	                        .addGroup(Alignment.CENTER,
	                            layout.createSequentialGroup()
	                                .addComponent(cancelButton)
	                                .addComponent(validateButton)
	                        )
	                        
	                )
	        );

	        layout.setVerticalGroup(
	            layout.createSequentialGroup()
	                .addGroup(
	                    layout.createParallelGroup()
	                        .addComponent(labelNom)
	                        .addComponent(nomTextField)
	                )
	                .addGap(10)
	                .addGroup(
	                    layout.createParallelGroup()
	                        .addComponent(labelAjout)
	                        .addComponent(labelDispo)
	                )
	                .addGroup(
	                    layout.createParallelGroup()
	                        .addComponent(spListAdd)
	                        .addGroup(Alignment.CENTER,
	                            layout.createSequentialGroup()
	                                .addComponent(buttonDroite)
	                                .addComponent(buttonGauche)
	                        )
	                        .addComponent(spListAvailable)
	                )
	                .addGap(10)
	                .addGroup(
	                    layout.createParallelGroup()
	                        .addComponent(cancelButton)
	                        .addComponent(validateButton)
	                )
	        );
	        
	        layout.linkSize(cancelButton, validateButton);
	        layout.linkSize(SwingConstants.VERTICAL, labelNom, nomTextField);
	        

	        // buttons

	        JPanel buttonsPanel = new JPanel(true);
	        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));


	        getContentPane().add(fieldsPanel);
	        getContentPane().add(Box.createVerticalStrut(10));
	        getContentPane().add(buttonsPanel);

	        setSize(400, 240);
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

		if(nomTextField.getText().length()==0) //le champ du nom est vide
		return;

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



	String profileName = nomTextField.getText();

	if(! profileName.equals(currentProfile.getName())) // the profile name is modified.
	{
		fieldToBeModified = true;
		try {
			currentProfile = NetworkServiceProvider.getNetworkService().modifyProfileName(currentProfile, profileName);
			isProfileModified = true;

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




	List<Tag> listTagsAdded = new ArrayList<>() ;

	for(Object tag:listModelAdd.toArray())
		listTagsAdded.add((Tag) tag);


	if((currentProfile.getTags().containsAll(listTagsAdded) && listTagsAdded.containsAll(currentProfile.getTags())) ) //les deux listes sont identiques
		fieldToBeModified = false;
	else 
	{ 
		try {

			NetworkServiceProvider.getNetworkService().replaceTagListOfProfile(currentProfile,listTagsAdded);
			fieldToBeModified = true;
			isProfileModified = true;
			setVisible(false);

		} catch







	}


	/**
	 * To add a new profile to the current account.
	 * @return the new profile added if there is one, null otherwise.
	 */
	public Profile addProfile()
	{


		listModelAdd.clear();
		listModelAvailable.clear();
		AddOrModify = true;
		profileAdded = null;

		setTitle("New Profile");
		nomTextField.setText("");
		nomTextField.setEditable(true);

		validateButton.setText("Validate");


		try {
			List<Tag> listTags = NetworkServiceProvider.getNetworkService().getTags();

			for(Tag tag : listTags)
				listModelAvailable.addElement(tag);




		} catch (NotAuthenticatedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


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
	 * @throws NetworkServiceException 
	 * @throws NotAuthenticatedException 
	 */
	public Profile modifyProfile(Profile profile) throws NotAuthenticatedException, NetworkServiceException
	{    

		listModelAdd.clear();
		listModelAvailable.clear();


		if(profile == null)
			throw new NullPointerException();

		currentProfile = profile;
		isProfileModified = false;
		AddOrModify = false;

		setTitle("Modification of Profile");
		nomTextField.setText(profile.getName());
		nomTextField.setEditable(true);

		
			List<Tag> listTagsAvailable = NetworkServiceProvider.getNetworkService().getTags();

			for(Tag tag : listTagsAvailable)
				listModelAvailable.addElement(tag);
			for (Tag tag : currentProfile.getTags())
			{
				listModelAvailable.removeElement(tag);
				listModelAdd.addElement(tag);
			}



		



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

		private DefaultListModel<Tag> listModelAdd, listModelAvailable;
		private JList<Tag> listAdd;


		public ButtonDroiteListener(JList<Tag> listAdd,  DefaultListModel<Tag> listModelAdd, DefaultListModel<Tag> listModelAvailable) 
		{
			this.listAdd= listAdd ;
			this.listModelAdd = listModelAdd;
			this.listModelAvailable = listModelAvailable;

		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub



			int indices[] = listAdd.getSelectedIndices();
			int size = indices.length;
			int i = size;

			for(i=size-1; i >=0 ; i--)
			{
				//enleve element de listAdd si il est selectionné et l'ajoute a listAvailable


				listModelAvailable.addElement(listModelAdd.get(indices[i]));
				listModelAdd.remove(indices[i]);


			}



		} 


	}

	class ButtonGaucheListener implements ActionListener {

		private JList<Tag>  listAvailable;
		private DefaultListModel<Tag> listModelAdd, listModelAvailable;


		public ButtonGaucheListener( JList<Tag> listAvailable, DefaultListModel<Tag> listModelAdd, DefaultListModel<Tag> listModelAvailable)
		{

			this.listAvailable = listAvailable ;
			this.listModelAdd = listModelAdd;
			this.listModelAvailable = listModelAvailable;


		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub



			int indices[] = listAvailable.getSelectedIndices();
			int size = indices.length;
			int i = size;

			for(i=size-1; i >=0 ; i--)
			{
				//enleve element de listAvailable si il est selectionné et l'ajoute a listAdd


				listModelAdd.addElement(listModelAvailable.get(indices[i]));
				listModelAvailable.remove(indices[i]);


			}



		} 


	}




	}




