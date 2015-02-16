package gui.tagsmanagement;

import gui.Constants;
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

import data.Tag;
import engine.NetworkServiceProvider;
import exceptions.IllegalFieldException;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import exceptions.TagNotFoundException;

public class TagsManagementPanel extends JPanel
{
	private static final long serialVersionUID = -7659217016124788668L;
	
	// Left panel elements.
	private JLabel tagsListLabel;
//	private DefaultListModel<Tag> tagsListModel;
	private JPanel tagsListPanel;
	private TagRenderer selectedTag;
	private int tagsNumber;
	
	// right panel elements
	private JLabel objectImage;
	private JLabel objectLabel;
	private JLabel tagIdLabel;
	private DefaultListModel<String> profilesListModel;
	
	
	private TagManagerDialog tagManagerDialog;
	
	public TagsManagementPanel()
	{
		super(true);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(createTagsListPanel());
		add(createTagInformationsPanel());
		
		tagManagerDialog = new TagManagerDialog(MainFrame.getInstance());
	}

	private JPanel createTagsListPanel()
	{
		JPanel tagsListManagementPanel = new JPanel(true);
		tagsListManagementPanel.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(7, 7, 7, 7)));
		tagsListManagementPanel.setLayout(new BoxLayout(tagsListManagementPanel, BoxLayout.PAGE_AXIS));
		tagsListManagementPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		tagsListLabel = new JLabel("X tags linked with account");
		tagsListLabel.setDoubleBuffered(true);
		
//		JList<Tag> tagsList = new JList<>();
//		tagsListModel = new DefaultListModel<Tag>();
//		tagsList.setModel(tagsListModel);
//		tagsList.setDoubleBuffered(true);
//		tagsList.setCellRenderer(new TagRenderer());
		
		selectedTag = null;
		tagsNumber = 0;
		
		tagsListPanel = new JPanel(true);
		tagsListPanel.setLayout(new BoxLayout(tagsListPanel, BoxLayout.Y_AXIS));
		tagsListPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		tagsListPanel.setOpaque(true);
		tagsListPanel.setBackground(Color.WHITE);
		tagsListPanel.addMouseListener(new tagsListMouseListener());
		
		JScrollPane scrollPane = new JScrollPane(tagsListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setDoubleBuffered(true);
		
		JButton addTagButton = new JButton(Constants.MainFrame.ADD_TAG_BUTTON_NAME);
		addTagButton.setDoubleBuffered(true);
		addTagButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addTagButton.addActionListener(new ActionAddNewTag());
		
		tagsListManagementPanel.add(tagsListLabel);
		tagsListManagementPanel.add(Box.createVerticalStrut(10));
		tagsListManagementPanel.add(scrollPane);
		tagsListManagementPanel.add(Box.createVerticalStrut(10));
		tagsListManagementPanel.add(addTagButton);
		
		return tagsListManagementPanel;
	}
	
	private JPanel createTagInformationsPanel()
	{
		JPanel tagInformationsPanel = new JPanel(true);
		tagInformationsPanel.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(12, 2, 9, 2)));
		tagInformationsPanel.setLayout(new BoxLayout(tagInformationsPanel, BoxLayout.Y_AXIS));
		
		objectImage = new JLabel();
		objectImage.setBorder(new LineBorder(Color.BLACK, 1));
		objectImage.setAlignmentX(CENTER_ALIGNMENT);
		objectImage.setDoubleBuffered(true);

		objectLabel = new JLabel();
		objectLabel.setAlignmentX(CENTER_ALIGNMENT);
		objectLabel.setDoubleBuffered(true);

		tagIdLabel = new JLabel();
		tagIdLabel.setDoubleBuffered(true);
		tagIdLabel.setAlignmentX(CENTER_ALIGNMENT);
		tagIdLabel.setToolTipText("Tag identifier.");
		
		JList<String> profilesList = new JList<>();
		profilesListModel = new DefaultListModel<>();
		profilesList.setModel(profilesListModel);
		profilesList.setDoubleBuffered(true);
		profilesList.setCellRenderer(new DefaultListCellRenderer(){ // set custom renderer to disable selection on graphical view.
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("unused")
			public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus)
			{
				return super.getListCellRendererComponent(list, value, index, false, cellHasFocus);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(profilesList, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setDoubleBuffered(true);
		scrollPane.setBorder(new TitledBorder("linked profiles"));
		
		tagInformationsPanel.add(objectImage);
		tagInformationsPanel.add(Box.createVerticalStrut(15));
		tagInformationsPanel.add(objectLabel);
		tagInformationsPanel.add(Box.createVerticalStrut(10));
		tagInformationsPanel.add(tagIdLabel);
		tagInformationsPanel.add(Box.createVerticalStrut(15));
		tagInformationsPanel.add(scrollPane);
		
		tagInformationsPanel.setMaximumSize(new Dimension(120, tagInformationsPanel.getMaximumSize().height));
		
		return tagInformationsPanel;
	}
	
	public void reloadTagsList() throws NotAuthenticatedException
	{
		// remove all from the graphical tags list and reloads it from the account.
		tagsListPanel.removeAll();
		removeTagDetails();
		
		List<Tag> list;
		
		try {
			list = NetworkServiceProvider.getNetworkService().getCurrentAccount().getTags();
			for(Tag tag : list)
				addTag(tag);
			
			tagsListPanel.add(Box.createVerticalGlue());
		} catch (NotAuthenticatedException e) {
			tagsListPanel.removeAll();
			tagsListLabel.setText("");
			throw e;
		}
		
		
		// reinitialize the right panel.
		objectImage.setIcon(null);
		objectLabel.setText("Object name");
		tagIdLabel.setText("Tag uid");
	}
	
	private void addTag(Tag tag)
	{
		TagRenderer renderer = new TagRenderer(tag, this);
		
		tagsListPanel.add(renderer, tagsNumber);
		tagsListPanel.repaint();
		
		tagsNumber++;
		tagsListLabel.setText(tagsNumber + " tags");
	}
	
	class tagsListMouseListener extends MouseAdapter implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			Component component = tagsListPanel.getComponentAt(e.getPoint());
			
			if(component != null && component instanceof TagRenderer)
			{
				TagRenderer renderer = (TagRenderer) component;
				
				if(renderer != selectedTag)
				{
					renderer.setSelected(true);
					
					if(selectedTag != null)
						selectedTag.setSelected(false);
					
					selectedTag = renderer;
					displayTagDetails(selectedTag.getTag());
				}
			}
		}
	}
	
	private void displayTagDetails(Tag tag)
	{
		Image image = null;
		
		// get the associated image or the default image if there is no one.
		if(tag.getObjectImageName() != null)
		{
			int width, height;
			try {
				BufferedImage bufImage = ImageIO.read(new File(tag.getObjectImageName()));
				width = bufImage.getWidth();
				height = bufImage.getHeight();

				// resize image to fill the ocjectImage label.
				if(width < height)
				{
					image = bufImage.getScaledInstance(IconsProvider.OBJECT_BIG_IMAGE_WIDTH * width / height, IconsProvider.OBJECT_BIG_IMAGE_HEIGHT, Image.SCALE_FAST);
				}
				else
				{
					image = bufImage.getScaledInstance(IconsProvider.OBJECT_BIG_IMAGE_WIDTH, IconsProvider.OBJECT_BIG_IMAGE_HEIGHT * height / width, Image.SCALE_FAST);
				}
			} catch (IOException e) { // an error occured while reading the image file. the default object image will be displayed.
				image = IconsProvider.defaultObjectImageBig;
			}
		}
		else
			image = IconsProvider.defaultObjectImageBig;
		
		objectImage.setIcon(new ImageIcon(image));
		
		
		objectLabel.setText(tag.getObjectName());
		tagIdLabel.setText(tag.getUid());
		
		// profile list processing will be implemented later.
	}
	
	private void removeTagDetails()
	{
		objectImage.setIcon(null);
		objectLabel.setText("");
		tagIdLabel.setText("");
		
		profilesListModel.clear();
	}
	
	class ActionAddNewTag implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Tag tag = tagManagerDialog.addTag();
			
			if(tag != null)
			{
				addTag(tag);
				tagsListPanel.repaint(); // to refresh the tags list panel.
			}
		}
	}

	
	public Tag runTagEditor(TagRenderer tagRenderer)
	{
		Tag tag = tagManagerDialog.modifyTag(tagRenderer.getTag());
		if(tag != null && tagRenderer.isSelected())
			this.displayTagDetails(tag);
		
		return tag;
	}
	
	public void removeTag(TagRenderer renderer)
	{
		if(renderer == null)
			throw new NullPointerException();
		
		try {
			NetworkServiceProvider.getNetworkService().removeTag(renderer.getTag());

			if(selectedTag == renderer)
			{
				selectedTag = null;
				removeTagDetails();
			}
			
			tagsListPanel.remove(renderer);
			tagsNumber--;

			tagsListLabel.setText(tagsNumber + " tags");
			tagsListPanel.repaint(); // To refresh the graphical interface.
		} catch (IllegalFieldException e) { // Abnormal exception in this case. Will not occur.
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "A abnormal error has occured. Please relaunch the application to try to solve this problem", "Abnormal error", JOptionPane.ERROR_MESSAGE);
		} catch (NotAuthenticatedException e) { // Abnormal exception in this case. Will not occur.
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "A abnormal error has occured. Please relaunch the application to try to solve this problem", "Abnormal error", JOptionPane.ERROR_MESSAGE);
		} catch (TagNotFoundException e) { // Can occur if the tag is removed on another computer or on a smartphone after the tags list was loaded.
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "The selected tag semms to be already removed from your account.", "Error", JOptionPane.ERROR_MESSAGE);
			
			if(selectedTag == renderer)
				selectedTag = null;
			
			tagsListPanel.remove(renderer);
			tagsListPanel.repaint();
		} catch (NetworkServiceException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "A network error has occured.", "Network error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
