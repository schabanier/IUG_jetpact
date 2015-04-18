package gui.profils;

import gui.Constants.CommonErrorMessages;
import gui.MainFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import data.Profile;
import data.Tag;
import engine.NetworkServiceProvider;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;

public class ProfileManagementPanel extends JPanel
{
	private static final long serialVersionUID = 1849949611958795029L;
	
	// Left panel elements.
	private JLabel profilesListLabel;
//	private DefaultListModel<Tag> tagsListModel;
	private JPanel profilesListPanel;
	private ProfilRenderer selectedProfile;
	private int profilesNumber;
	
	// right panel elements
	
	private JLabel profileLabel;
	private DefaultListModel<Tag> tagsListModel;
	
	private ProfileManagerDialog profileManagerDialog;
	
	public ProfileManagementPanel()
	{
		super(true);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(createProfilesListPanel());
		add(createProfileInformationsPanel());
		
		profileManagerDialog = new ProfileManagerDialog(MainFrame.getInstance());
		
	}

	private JPanel createProfilesListPanel()
	{
		JPanel profilesListManagementPanel = new JPanel(true);
		profilesListManagementPanel.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(7, 7, 7, 7)));
		profilesListManagementPanel.setLayout(new BoxLayout(profilesListManagementPanel, BoxLayout.PAGE_AXIS));
		profilesListManagementPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		profilesListLabel = new JLabel(profilesNumber+""+"profiles linked with account");
		profilesListLabel.setDoubleBuffered(true);
		
		selectedProfile = null;
		profilesNumber = 0;
		
		profilesListPanel = new JPanel(true);
		profilesListPanel.setLayout(new BoxLayout(profilesListPanel, BoxLayout.Y_AXIS));
		profilesListPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		profilesListPanel.setOpaque(true);
		profilesListPanel.setBackground(Color.WHITE);
		profilesListPanel.addMouseListener(new ProfilesListMouseListener());
		
		JScrollPane scrollPane = new JScrollPane(profilesListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setDoubleBuffered(true);
		
		JButton addProfileButton = new JButton("Add Profile");
		addProfileButton.setDoubleBuffered(true);
		addProfileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addProfileButton.addActionListener(new ActionAddNewProfile());
		
		profilesListManagementPanel.add(profilesListLabel);
		profilesListManagementPanel.add(Box.createVerticalStrut(10));
		profilesListManagementPanel.add(scrollPane);
		profilesListManagementPanel.add(Box.createVerticalStrut(10));
		profilesListManagementPanel.add(addProfileButton);
		
		return profilesListManagementPanel;
	}
	
	private JPanel createProfileInformationsPanel()
	{
		JPanel profilesInformationsPanel = new JPanel(true);
		profilesInformationsPanel.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(12, 2, 9, 2)));
		profilesInformationsPanel.setLayout(new BoxLayout(profilesInformationsPanel, BoxLayout.Y_AXIS));
		
	

		profileLabel = new JLabel();
		profileLabel.setAlignmentX(CENTER_ALIGNMENT);
		profileLabel.setDoubleBuffered(true);

		
		JList<Tag> tagsList = new JList<>();
		tagsListModel = new DefaultListModel<>();
		tagsList.setModel(tagsListModel);
		tagsList.setDoubleBuffered(true);
		tagsList.setCellRenderer(new DefaultListCellRenderer(){ // set custom renderer to disable selection on graphical view.
		
			private static final long serialVersionUID = -5561167800506161082L;

			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				return super.getListCellRendererComponent(list, ((Tag) value).getObjectName(), index, false, cellHasFocus);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(tagsList, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setDoubleBuffered(true);
		scrollPane.setBorder(new TitledBorder("Linked tags"));
		
		
		profilesInformationsPanel.add(Box.createVerticalStrut(15));
		profilesInformationsPanel.add(profileLabel);
		profilesInformationsPanel.add(Box.createVerticalStrut(10));
		
		profilesInformationsPanel.add(Box.createVerticalStrut(15));
		profilesInformationsPanel.add(scrollPane);
		
		profilesInformationsPanel.setMinimumSize(new Dimension(80,profilesInformationsPanel.getMaximumSize().height));
		
		return profilesInformationsPanel;
	}
	
	public void reloadProfilesList() throws NotAuthenticatedException, NetworkServiceException
	{
		// remove all from the graphical tags list and reloads it from the account.
		profilesListPanel.removeAll();
		profilesNumber = 0;
		removeProfileDetails();
		
		List<Profile> list;
		
		try {
			list = NetworkServiceProvider.getNetworkService().getProfiles();
			
			for(Profile profile : list)
				addProfile(profile);
			
			profilesListPanel.add(Box.createVerticalGlue());
		} catch (NotAuthenticatedException e) {
			profilesListPanel.removeAll();
			profilesListLabel.setText("");
			throw e;
		} catch (NetworkServiceException e) {
			profilesListPanel.removeAll();
			profilesListLabel.setText("");
			throw e;
		}
		
		
		// reinitialize the right panel.
		
		profileLabel.setText("Profile Name");
		
	}
	
	
	private void addProfile(Profile profile)
	{
		ProfilRenderer renderer = new ProfilRenderer(profile, this);
		
		profilesListPanel.add(renderer, profilesNumber);
		profilesListPanel.repaint();
		
		profilesNumber++;
		profilesListLabel.setText(profilesNumber + " profiles");
	}
	
	class ProfilesListMouseListener extends MouseAdapter implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			Component component = profilesListPanel.getComponentAt(e.getPoint());
			
			if(component != null && component instanceof ProfilRenderer)
			{
				ProfilRenderer renderer = (ProfilRenderer) component;
				
				if(renderer != selectedProfile)
				{
					renderer.setSelected(true);
					
					if(selectedProfile != null)
						selectedProfile.setSelected(false);
					
					selectedProfile = renderer;
					displayProfileDetails(selectedProfile.getProfile());
				}
			}
		}
	}
	
	private void displayProfileDetails(Profile profile)
	{
		tagsListModel.clear();
		
		
		List<Tag> tags = profile.getTags();
		
		for(Tag tag : tags)
			tagsListModel.addElement(tag);
		
		
		profileLabel.setText(profile.getName());
		
		
	}
	
	private void removeProfileDetails()
	{
		
		profileLabel.setText("");
	
		
		tagsListModel.clear();
	}
	
	class ActionAddNewProfile implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		 Profile newProfile = profileManagerDialog.addProfile();
			
			if(newProfile != null)
			{
				addProfile(newProfile);
				profilesListPanel.repaint(); // to refresh the tags list panel.
			}
		}
	}

	
	public Profile runProfileEditor(ProfilRenderer ProfilRenderer)
	{
		//cest renderer modification qui appelle, on ouvre la jdialog qui modifie (on passe par management car il faut modifie le jpanelinfo)
		Profile profile = null;
		try {
			profile = profileManagerDialog.modifyProfile(ProfilRenderer.getProfile());
		}  catch (NotAuthenticatedException e) {// abnormal error.
			JOptionPane.showMessageDialog(this, CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		} catch (NetworkServiceException e) {
			JOptionPane.showMessageDialog(this, CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
		
		if(profile != null && ProfilRenderer.isSelected())
			this.displayProfileDetails(profile);
		
		return profile;
	}
	
	public void removeProfile(ProfilRenderer renderer)
	{
		if(renderer == null)
			throw new NullPointerException();
		
		try {
			NetworkServiceProvider.getNetworkService().removeProfile(renderer.getProfile());

			if(selectedProfile == renderer)
			{
				selectedProfile = null;
				removeProfileDetails();
			}
			
			profilesListPanel.remove(renderer);
			profilesNumber--;

			profilesListLabel.setText(profilesNumber +""+"profiles");
			profilesListPanel.repaint(); // To refresh the graphical interface.
		} catch (IllegalFieldException e) { // Abnormal exception in this case. Will not occur.

			if (e.getReason()== IllegalFieldException.REASON_VALUE_INCORRECT)
			JOptionPane.showMessageDialog(this, "this is syntactically incorrect", "ERROR on field", JOptionPane.ERROR_MESSAGE);
			else 
				JOptionPane.showMessageDialog(this, "there is no profile with this name", "ERROR on field", JOptionPane.ERROR_MESSAGE);
			
		} catch (NotAuthenticatedException e) { // Abnormal exception in this case. Will not occur.
			JOptionPane.showMessageDialog(MainFrame.getInstance(), CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		} catch (NetworkServiceException e) {
		
			JOptionPane.showMessageDialog(MainFrame.getInstance(), CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
}
