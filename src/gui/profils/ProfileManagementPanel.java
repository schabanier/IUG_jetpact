package gui.profils;

import gui.Constants;
import gui.Constants.CommonErrorMessages;
import gui.Constants.Fields;
import gui.Constants.TagsManagement;
import gui.IconsProvider;
import gui.MainFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
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
	// Left panel elements.
	private JLabel profilesListLabel;
//	private DefaultListModel<Tag> tagsListModel;
	private JPanel profilesListPanel;
	private ProfilRenderer selectedProfile;
	private int profilesNumber;
	
	// right panel elements
	
	private JLabel profileLabel;
	private DefaultListModel<String> profilesListModel;
	
	
	// private TagManagerDialog tagManagerDialog;
	
	public ProfileManagementPanel()
	{
		super(true);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(createProfilesListPanel());
		add(createProfileInformationsPanel());
		
		tagManagerDialog = new TagManagerDialog(MainFrame.getInstance());
	}

	private JPanel createProfilesListPanel()
	{
		JPanel profilesListManagementPanel = new JPanel(true);
		profilesListManagementPanel.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(7, 7, 7, 7)));
		profilesListManagementPanel.setLayout(new BoxLayout(profilesListManagementPanel, BoxLayout.PAGE_AXIS));
		profilesListManagementPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		profilesListLabel = new JLabel("X profiles linked with account");
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

		
		JList profilesList = new JList();
		profilesListModel = new DefaultListModel();
		profilesList.setModel(profilesListModel);
		profilesList.setDoubleBuffered(true);
		profilesList.setCellRenderer(new DefaultListCellRenderer(){ // set custom renderer to disable selection on graphical view.
			private static final long serialVersionUID = 1L;
			
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				return super.getListCellRendererComponent(list, ((Tag) value).getObjectName(), index, false, cellHasFocus);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(profilesList, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setDoubleBuffered(true);
		scrollPane.setBorder(new TitledBorder(TagsManagement.LINKED_PROFILES_LIST_TITLE));
		
		
		profilesInformationsPanel.add(Box.createVerticalStrut(15));
		profilesInformationsPanel.add(profileLabel);
		profilesInformationsPanel.add(Box.createVerticalStrut(10));
		
		profilesInformationsPanel.add(Box.createVerticalStrut(15));
		profilesInformationsPanel.add(scrollPane);
		
		profilesInformationsPanel.setMaximumSize(new Dimension(120,profilesInformationsPanel.getMaximumSize().height));
		
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
		
		
		
		profileLabel.setText(profile.getName());
		
		
		// profile list processing will be implemented later.
	}
	
	private void removeProfileDetails()
	{
		
		profileLabel.setText("");
	
		
		profilesListModel.clear();
	}
	
	class ActionAddNewProfile implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Profile profile = profileManagerDialog.addProfile();
			
			if(profile != null)
			{
				addProfile(profile);
				profilesListPanel.repaint(); // to refresh the tags list panel.
			}
		}
	}

	
	public Profile runProfileEditor(ProfilRenderer ProfilRenderer)
	{
		Profile profile = profileManagerDialog.modifyProfile(ProfilRenderer.getProfile());
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

			profilesListLabel.setText(TagsManagement.getprofilesNumberTitle(profilesNumber));
			profilesListPanel.repaint(); // To refresh the graphical interface.
		} catch (IllegalFieldException e) { // Abnormal exception in this case. Will not occur.
			
		
				JOptionPane.showMessageDialog(MainFrame.getInstance(), CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		} catch (NotAuthenticatedException e) { // Abnormal exception in this case. Will not occur.
			JOptionPane.showMessageDialog(MainFrame.getInstance(), CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		} catch (NetworkServiceException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.getInstance(), CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
}
