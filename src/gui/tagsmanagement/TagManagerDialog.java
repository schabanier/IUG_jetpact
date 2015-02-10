package gui.tagsmanagement;

import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class TagManagerDialog extends JDialog
{
	private static final long serialVersionUID = 882425066241522172L;
	
	private JTextField objectNameTextField;
	private JTextField objectPictureTextField;
	private JTextField idTagTextField;


	public TagManagerDialog(Frame owner)
	{
		super(owner);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel contentPane = new JPanel(true);
		contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setContentPane(contentPane);
		
		JPanel fieldsPanel = new JPanel(new GridLayout(0, 2), true);
		
		
		JLabel idTagLabel = new JLabel("Tag id");
		idTagLabel.setDoubleBuffered(true);
		idTagTextField = new JTextField();
		idTagTextField.setDoubleBuffered(true);

		fieldsPanel.add(idTagLabel);
		fieldsPanel.add(idTagTextField);
		
		
		JLabel objectNameLabel = new JLabel("Object name");
		objectNameLabel.setDoubleBuffered(true);
		objectNameTextField = new JTextField();
		objectNameTextField.setDoubleBuffered(true);

		fieldsPanel.add(objectNameLabel);
		fieldsPanel.add(objectNameTextField);
		
		
		JLabel ObjectPictureLabel = new JLabel("Object image");
		ObjectPictureLabel.setDoubleBuffered(true);
		objectPictureTextField = new JTextField();
		objectPictureTextField.setDoubleBuffered(true);

		fieldsPanel.add(ObjectPictureLabel);
		fieldsPanel.add(objectPictureTextField);
		
		
		getContentPane().add(fieldsPanel);
		
		
		// buttons
		
		JPanel buttonsPanel = new JPanel(true);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setDoubleBuffered(true);
		
		JButton validateButton = new JButton("Validate");
		validateButton.setDoubleBuffered(true);

		buttonsPanel.add(Box.createHorizontalGlue());
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(Box.createHorizontalStrut(10));
		buttonsPanel.add(validateButton);
		buttonsPanel.add(Box.createHorizontalGlue());
		
		getContentPane().add(Box.createVerticalStrut(15));
		getContentPane().add(buttonsPanel);
		
		
		pack();
		setResizable(false);
	}
}
