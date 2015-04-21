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
import engine.EngineServiceProvider;
import engine.FieldVerifier;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import gui.Constants.CommonErrorMessages;
import gui.Constants.Fields;
import gui.Constants.TagsManagement;

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
		
		JLabel idTagLabel = new JLabel(TagsManagement.TAG_UID_LABEL);
		idTagLabel.setDoubleBuffered(true);
		idTagTextField = new JTextField();
		idTagTextField.setDoubleBuffered(true);

//		fieldsPanel.add(idTagLabel);
//		fieldsPanel.add(idTagTextField);
		
		
		JLabel objectNameLabel = new JLabel(TagsManagement.TAG_OBJECT_NAME_LABEL);
		objectNameLabel.setDoubleBuffered(true);
		objectNameTextField = new JTextField();
		objectNameTextField.setDoubleBuffered(true);

//		fieldsPanel.add(objectNameLabel);
//		fieldsPanel.add(objectNameTextField);
		
		
		JLabel ObjectPictureLabel = new JLabel(TagsManagement.TAG_OBJECT_IMAGE_FILENAME_LABEL);
		ObjectPictureLabel.setDoubleBuffered(true);
		objectPictureTextField = new JTextField();
		objectPictureTextField.setDoubleBuffered(true);
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle(TagsManagement.IMAGE_FILENAME_CHOOSER_WINDOW_TITLE);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
		fileChooser.setFileFilter(new FileNameExtensionFilter("jpeg, png, tiff", "jpeg", "JPEG", "jpg", "JPG", "png", "tiff"));
		
		JButton fileChooserButton = new JButton("...");
		fileChooserButton.setToolTipText(TagsManagement.IMAGE_FILENAME_CHOOSER_BUTTON_TOOLTIP_TEXT);
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
	
	class ActionChooseImageFile implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int option = fileChooser.showDialog(TagManagerDialog.this, "Validate");
			
			if(option == JFileChooser.APPROVE_OPTION)
				try {
					objectPictureTextField.setText(fileChooser.getSelectedFile().getCanonicalPath());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(TagManagerDialog.this, TagsManagement.IMAGE_FILE_NOT_FOUND_MESSAGE, TagsManagement.IMAGE_FILE_NOT_FOUND_TITLE, JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(this, CommonErrorMessages.getEmptyFieldErrorMessage(Fields.TAG_UID), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_UID), JOptionPane.WARNING_MESSAGE);
		else if(! FieldVerifier.verifyTagUID(id))
			JOptionPane.showMessageDialog(this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.TAG_UID), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_UID), JOptionPane.WARNING_MESSAGE);
		else if(objectName.length() == 0)
			JOptionPane.showMessageDialog(this, CommonErrorMessages.getEmptyFieldErrorMessage(Fields.TAG_OBJECT_NAME), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_OBJECT_NAME), JOptionPane.WARNING_MESSAGE);
		else if(! FieldVerifier.verifyTagName(objectName))
			JOptionPane.showMessageDialog(this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.TAG_OBJECT_NAME),  CommonErrorMessages.getFieldErrorTitle(Fields.TAG_OBJECT_NAME), JOptionPane.WARNING_MESSAGE);
		else if(objectImageFileName!= null && ! FieldVerifier.verifyImageFileName(objectImageFileName))
			JOptionPane.showMessageDialog(this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.TAG_OBJECT_FILE_NAME),  CommonErrorMessages.getFieldErrorTitle(Fields.TAG_OBJECT_FILE_NAME), JOptionPane.WARNING_MESSAGE);
		else
		{
			Tag tag = new Tag(id, objectName, objectImageFileName);
			
			try {
				EngineServiceProvider.getEngineService().addTag(tag);
				tagAdded = tag;
				setVisible(false);
			} catch (IllegalFieldException e) {
				switch(e.getFieldId())
				{
					case IllegalFieldException.TAG_UID :
						if(e.getReason() == IllegalFieldException.REASON_VALUE_ALREADY_USED)
							JOptionPane.showMessageDialog(this, TagsManagement.getTagUIDAlreadyUsedMessage(id), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_UID), JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.TAG_UID), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_UID), JOptionPane.ERROR_MESSAGE);
					break;
					case IllegalFieldException.TAG_OBJECT_NAME :
						if(e.getReason() == IllegalFieldException.REASON_VALUE_ALREADY_USED)
							JOptionPane.showMessageDialog(this, TagsManagement.getObjectNameAlreadyUsedMessage(objectName), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_OBJECT_NAME), JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.TAG_OBJECT_NAME), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_OBJECT_NAME), JOptionPane.ERROR_MESSAGE);
					break;
					case IllegalFieldException.TAG_OBJECT_IMAGE :
						JOptionPane.showMessageDialog(this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.TAG_OBJECT_FILE_NAME), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_OBJECT_FILE_NAME), JOptionPane.ERROR_MESSAGE);
					break;
					default:
						JOptionPane.showMessageDialog(this, CommonErrorMessages.UNKNOWN_ERROR_MESSAGE, CommonErrorMessages.UNKNOWN_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
					break;
				}
			} catch (NotAuthenticatedException e) { // abnormal error.
				JOptionPane.showMessageDialog(this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			} catch (NetworkServiceException e) {
				JOptionPane.showMessageDialog(this, CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
// the texts below this line are not still processed.
	
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
					currentTag = EngineServiceProvider.getEngineService().modifyObjectName(currentTag, objectName);
					isTagModified = true;
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
			
			if((objectImageFileName != null && ! objectImageFileName.equals(currentTag.getObjectImageName())) || (objectImageFileName == null && objectImageFileName != currentTag.getObjectImageName()))
			{
				fieldToBeModified = true;
				try {
					currentTag = EngineServiceProvider.getEngineService().modifyObjectImage(currentTag, objectImageFileName);
					isTagModified = true;
					setVisible(false);
					return;
				} catch (IllegalFieldException e) {
					switch(e.getFieldId())
					{
						case IllegalFieldException.TAG_UID :
							if(e.getReason() == IllegalFieldException.REASON_VALUE_NOT_FOUND)
								JOptionPane.showMessageDialog(this, TagsManagement.MODIFICATION_TAG_NOT_FOUND_MESSAGE, TagsManagement.MODIFICATION_TAG_NOT_FOUND_TITLE, JOptionPane.ERROR_MESSAGE);
							else
								JOptionPane.showMessageDialog(this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
						break;
						case IllegalFieldException.TAG_OBJECT_IMAGE :
							JOptionPane.showMessageDialog(this, CommonErrorMessages.getDefaultFieldErrorMessage(Fields.TAG_OBJECT_FILE_NAME), CommonErrorMessages.getFieldErrorTitle(Fields.TAG_OBJECT_FILE_NAME), JOptionPane.ERROR_MESSAGE);
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
		
		setTitle(TagsManagement.TAG_ADDITION_WINDOW_TITLE);
		objectNameTextField.setText("");
		objectPictureTextField.setText("");
		idTagTextField.setText("");
		idTagTextField.setEditable(true);
		
		validateButton.setText(TagsManagement.VALIDATE_TAG_ADDITION_BUTTON_NAME);
		
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
		
		setTitle(TagsManagement.TAG_MODIFICATION_WINDOW_TITLE);
		objectNameTextField.setText(tag.getObjectName());
		objectPictureTextField.setText(tag.getObjectImageName());
		idTagTextField.setText(tag.getUid());
		idTagTextField.setEditable(false);
		
		validateButton.setText(TagsManagement.VALIDATE_TAG_MODIFICATION_BUTTON_NAME);

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
