package gui.tagsmanagement;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import data.Tag;
import engine.FieldVerifier;
import engine.NetworkServiceProvider;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;

public class TagManagerDialog extends JDialog
{
	private static final long serialVersionUID = 882425066241522172L;
	
	private JTextField objectNameTextField;
	private JTextField objectPictureTextField;
	private JTextField idTagTextField;
	
	/**
	 * true if this dialog box is used to add a tag.
	 * false if this dialog box is used to modify a tag.
	 */
	private boolean AddOrModify;
	
	/**
	 * the tag which is modified in this dialog box.
	 */
	private Tag currentTag;
	private boolean isTagModified;
	
	private Tag tagAdded;

	private JButton validateButton;

	private JFileChooser fileChooser;


	public TagManagerDialog(Frame owner)
	{
		super(owner, true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel contentPane = new JPanel(true);
		contentPane.setBorder(new EmptyBorder(12, 7, 12, 7));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setContentPane(contentPane);
		
		JPanel fieldsPanel = new JPanel(true);
		fieldsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

		GroupLayout layout = new GroupLayout(fieldsPanel);
		fieldsPanel.setLayout(layout);
		
//		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel idTagLabel = new JLabel("Tag id");
		idTagLabel.setDoubleBuffered(true);
		idTagTextField = new JTextField();
		idTagTextField.setDoubleBuffered(true);

//		fieldsPanel.add(idTagLabel);
//		fieldsPanel.add(idTagTextField);
		
		
		JLabel objectNameLabel = new JLabel("Object name");
		objectNameLabel.setDoubleBuffered(true);
		objectNameTextField = new JTextField();
		objectNameTextField.setDoubleBuffered(true);

//		fieldsPanel.add(objectNameLabel);
//		fieldsPanel.add(objectNameTextField);
		
		
		JLabel ObjectPictureLabel = new JLabel("Object image");
		ObjectPictureLabel.setDoubleBuffered(true);
		objectPictureTextField = new JTextField();
		objectPictureTextField.setDoubleBuffered(true);
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle("Image file selection");
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
		fileChooser.setFileFilter(new FileNameExtensionFilter("jpeg, png, tiff", "jpeg", "JPEG", "jpg", "JPG", "png", "tiff"));
		JButton fileChooserButton = new JButton("...");
		fileChooserButton.setDoubleBuffered(true);
		fileChooserButton.addActionListener(new ActionChooseImageFile());

//		fieldsPanel.add(ObjectPictureLabel);
//		fieldsPanel.add(objectPictureTextField);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(idTagLabel)
						.addComponent(idTagTextField)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(objectNameLabel)
						.addComponent(objectNameTextField)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(ObjectPictureLabel)
						.addComponent(objectPictureTextField)
						.addComponent(fileChooserButton)
						)
				);
		
		layout.linkSize(SwingConstants.VERTICAL, idTagTextField, objectNameTextField, objectPictureTextField, fileChooserButton);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(idTagLabel)
						.addComponent(objectNameLabel)
						.addComponent(ObjectPictureLabel)
						)
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(idTagTextField)
						.addComponent(objectNameTextField)
						.addComponent(objectPictureTextField)
						)
				.addComponent(fileChooserButton)
				);
		
		getContentPane().add(fieldsPanel);
		
		
		// buttons
		
		JPanel buttonsPanel = new JPanel(true);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setDoubleBuffered(true);
		cancelButton.addActionListener(new ActionCancel());
		
		validateButton = new JButton("Validate");
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
	
	class ActionChooseImageFile implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int option = fileChooser.showDialog(TagManagerDialog.this, "Validate");
			
			if(option == JFileChooser.APPROVE_OPTION)
				try {
					objectPictureTextField.setText(fileChooser.getSelectedFile().getCanonicalPath());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(TagManagerDialog.this, "The specified image file doesn't exist.", "Error on specified image file", JOptionPane.ERROR_MESSAGE);
				}
		}
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
				actionAddTag();
			else
				actionModifyTag();
		}
	}
	
	private void actionAddTag()
	{
		String id = idTagTextField.getText();
		String objectName = objectNameTextField.getText();
		String objectImageFileName = objectPictureTextField.getText().length() > 0 ? objectPictureTextField.getText() : null;

		if(id.length() == 0)
			JOptionPane.showMessageDialog(this, "The field tag id can't be empty.", "Error on field id", JOptionPane.WARNING_MESSAGE);
		else if(! FieldVerifier.verifyTagUID(id))
			JOptionPane.showMessageDialog(this, "The tag id \"" + id + "\" is incorrect.", "Error on field id", JOptionPane.WARNING_MESSAGE);
		else if(objectName.length() == 0)
			JOptionPane.showMessageDialog(this, "The field object name can't be empty", "Error on field object name", JOptionPane.WARNING_MESSAGE);
		else if(! FieldVerifier.verifyTagName(objectName))
			JOptionPane.showMessageDialog(this, "The object name \"" + objectName + "\" is incorrect.", "Error on field object name", JOptionPane.WARNING_MESSAGE);
		else if(objectImageFileName!= null && ! FieldVerifier.verifyImageFileName(objectImageFileName))
			JOptionPane.showMessageDialog(this, "The image filename \"" + objectImageFileName + "\" is incorrect.", "Error on field object name", JOptionPane.WARNING_MESSAGE);
		else
		{
			Tag tag = new Tag(id, objectName, objectImageFileName);
			
			try {
				NetworkServiceProvider.getNetworkService().addTag(tag);
				tagAdded = tag;
				setVisible(false);
			} catch (IllegalFieldException e) {
				switch(e.getFieldId())
				{
					case IllegalFieldException.TAG_UID :
						if(e.getReason() == IllegalFieldException.REASON_VALUE_ALREADY_USED)
							JOptionPane.showMessageDialog(this, "The tag which has id \"" + id + "\" is already used.", "Error on field id", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, "The tag id \"" + id + "\" is incorrect : " + e.getMessage(), "Error on field id", JOptionPane.ERROR_MESSAGE);
					break;
					case IllegalFieldException.TAG_OBJECT_NAME :
						if(e.getReason() == IllegalFieldException.REASON_VALUE_ALREADY_USED)
							JOptionPane.showMessageDialog(this, "The  object name \"" + objectName + "\" is already used for another tag.", "Error on field object name", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, "The object name \"" + objectName + "\" is incorrect : " + e.getMessage(), "Error on field object name", JOptionPane.ERROR_MESSAGE);
					break;
					case IllegalFieldException.TAG_OBJECT_IMAGE :
						JOptionPane.showMessageDialog(this, "The image filename is incorrect : " + e.getMessage(), "Error on field image filename", JOptionPane.ERROR_MESSAGE);
					break;
					default:
						JOptionPane.showMessageDialog(this, "Unknown error has occured", "Unknown error", JOptionPane.ERROR_MESSAGE);
					break;
				}
			} catch (NotAuthenticatedException e) { // abnormal error.
				JOptionPane.showMessageDialog(this, "An abnormal error has occured.\nPlease restart the application to solve this problem.", "Abnormal error", JOptionPane.ERROR_MESSAGE);
			} catch (NetworkServiceException e) {
				JOptionPane.showMessageDialog(this, "A network error has occured.", "Network error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void actionModifyTag()
	{
		boolean fieldToBeModified = false;
		
		String objectName = objectNameTextField.getText();
		String objectImageFileName = objectPictureTextField.getText().length() > 0 ? objectPictureTextField.getText() : null;

		if(objectName.length() == 0)
			JOptionPane.showMessageDialog(this, "The field object name can't be empty", "Error on field object name", JOptionPane.WARNING_MESSAGE);
		else if(! FieldVerifier.verifyTagName(objectName))
			JOptionPane.showMessageDialog(this, "The object name \"" + objectName + "\" is incorrect.", "Error on field object name", JOptionPane.WARNING_MESSAGE);
		else if(objectImageFileName != null && ! FieldVerifier.verifyImageFileName(objectImageFileName))
			JOptionPane.showMessageDialog(this, "The image filename \"" + objectImageFileName + "\" is incorrect.", "Error on field object name", JOptionPane.WARNING_MESSAGE);
		else
		{
			if(! objectName.equals(currentTag.getObjectName())) // the object name is modified.
			{
				fieldToBeModified = true;
				try {
					currentTag = NetworkServiceProvider.getNetworkService().modifyObjectName(currentTag, objectName);
					isTagModified = true;
					setVisible(false);
				} catch (IllegalFieldException e) {
					switch(e.getFieldId())
					{
						case IllegalFieldException.TAG_UID :
							if(e.getReason() == IllegalFieldException.REASON_VALUE_NOT_FOUND)
								JOptionPane.showMessageDialog(this, "Unable to modify : this tag has been removed by an user on another application instance.", "Error", JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(this, "A abnormal error has occured. Please restart the application to try to solve this problem.", "Error", JOptionPane.ERROR_MESSAGE);
						break;
						case IllegalFieldException.TAG_OBJECT_NAME :
							if(e.getReason() == IllegalFieldException.REASON_VALUE_ALREADY_USED)
								JOptionPane.showMessageDialog(this, "The  object name \"" + objectName + "\" is already used for another tag.", "Error on field object name", JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(this, "The object name \"" + objectName + "\" is incorrect : " + e.getMessage(), "Error on field object name", JOptionPane.ERROR_MESSAGE);
						break;
						default:
							JOptionPane.showMessageDialog(this, "Unknown error has occured", "Unknown error", JOptionPane.ERROR_MESSAGE);
						break;
					}
				} catch (NotAuthenticatedException e) {// abnormal error.
					JOptionPane.showMessageDialog(this, "An abnormal error has occured.\nPlease restart the application to solve this problem.", "Abnormal error", JOptionPane.ERROR_MESSAGE);
				} catch (NetworkServiceException e) {
					JOptionPane.showMessageDialog(this, "A network error has occured.", "Network error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			if((objectImageFileName != null && ! objectImageFileName.equals(currentTag.getObjectImageName())) || (objectImageFileName == null && objectImageFileName != currentTag.getObjectImageName()))
			{
				fieldToBeModified = true;
				try {
					currentTag = NetworkServiceProvider.getNetworkService().modifyObjectImage(currentTag, objectImageFileName);
					isTagModified = true;
					setVisible(false);
					return;
				} catch (IllegalFieldException e) {
					switch(e.getFieldId())
					{
						case IllegalFieldException.TAG_UID :
							if(e.getReason() == IllegalFieldException.REASON_VALUE_NOT_FOUND)
								JOptionPane.showMessageDialog(this, "Unable to modify : this tag has been removed by an user on another application instance.", "Error", JOptionPane.ERROR_MESSAGE);
							else // tag uid is incorrect.
								JOptionPane.showMessageDialog(this, "A abnormal error has occured. Please restart the application to try to solve this problem.", "Error", JOptionPane.ERROR_MESSAGE);
						break;
						case IllegalFieldException.TAG_OBJECT_IMAGE :
							JOptionPane.showMessageDialog(this, "The image filename is incorrect : " + e.getMessage(), "Error on field image filename", JOptionPane.ERROR_MESSAGE);
						break;
						default:
							JOptionPane.showMessageDialog(this, "Unknown error has occured", "Unknown error", JOptionPane.ERROR_MESSAGE);
						break;
					}
				} catch (NotAuthenticatedException e) {// abnormal error.
					JOptionPane.showMessageDialog(this, "An abnormal error has occured.\nPlease restart the application to solve this problem.", "Abnormal error", JOptionPane.ERROR_MESSAGE);
				} catch (NetworkServiceException e) {
					JOptionPane.showMessageDialog(this, "A network error has occured.", "Network error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			if(fieldToBeModified == false)
				setVisible(false);
		}
	}
	
	
	/**
	 * To add a new tag to the current account.
	 * @return the new tag added if there is one, null otherwise.
	 */
	public Tag addTag()
	{
		AddOrModify = true;
		tagAdded = null;
		
		setTitle("Tag addition");
		objectNameTextField.setText("");
		objectPictureTextField.setText("");
		idTagTextField.setText("");
		idTagTextField.setEditable(true);
		
		validateButton.setText("Add");
		
		setLocation(getParent().getX() + (getParent().getWidth() - getWidth())/2, getParent().getY() + (getParent().getHeight() - getHeight())/2);
		// repositionnement de la fenêtre si elle sort de l'écran en haut ou à gauche.
		if(getLocation().getX() < 0)
			setLocation(0, (int) getLocation().getY());
		if(getLocation().getY() < 0)
				setLocation((int) getLocation().getX(), 0);
		
		setVisible(true);
		return tagAdded;
	}
	

	/**
	 * To modify a tag of the current account.
	 * @param tag the tag to modify
	 * @return the tag modified if there is a modification, null otherwise.
	 */
	public Tag modifyTag(Tag tag)
	{
		if(tag == null)
			throw new NullPointerException();
		
		currentTag = tag;
		isTagModified = false;
		AddOrModify = false;
		
		setTitle("Tag modification");
		objectNameTextField.setText(tag.getObjectName());
		objectPictureTextField.setText(tag.getObjectImageName());
		idTagTextField.setText(tag.getUid());
		idTagTextField.setEditable(false);
		
		validateButton.setText("Modify");

		setLocation(getParent().getX() + (getParent().getWidth() - getWidth())/2, getParent().getY() + (getParent().getHeight() - getHeight())/2);
		// repositionnement de la fenêtre si elle sort de l'écran en haut ou à gauche.
		if(getLocation().getX() < 0)
			setLocation(0, (int) getLocation().getY());
		if(getLocation().getY() < 0)
				setLocation((int) getLocation().getX(), 0);
		
		setVisible(true);
		
		return isTagModified ? currentTag : null;
	}
}