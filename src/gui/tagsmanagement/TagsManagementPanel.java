package gui.tagsmanagement;

import gui.Constants;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import data.Tag;
import engine.NetworkServiceProvider;
import exceptions.NotAuthenticatedException;

public class TagsManagementPanel extends JPanel
{
	private static final long serialVersionUID = -7659217016124788668L;
	
	// Left panel elements.
	private JLabel tagsListLabel;
//	private DefaultListModel<Tag> tagsListModel;
	private JPanel tagsListPanel;
	private List<TagRenderer> tagRendererComponents;
	private int selectedTagIndex;
	
	// right panel elements
	private JLabel objectImage;
	private JLabel objectLabel;
	private JLabel tagIdLabel;
	private DefaultListModel<String> profilesListModel;
	
	public TagsManagementPanel()
	{
		super(true);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(createTagsListPanel());
		add(createTagInformationsPanel());
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
		
		tagRendererComponents = new ArrayList<>();
		selectedTagIndex = -1;
		
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
		tagInformationsPanel.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(4, 2, 6, 2)));
		tagInformationsPanel.setLayout(new BoxLayout(tagInformationsPanel, BoxLayout.Y_AXIS));
		
		objectImage = new JLabel();
		objectImage.setBorder(new LineBorder(Color.BLACK, 1));
		objectImage.setDoubleBuffered(true);

		objectLabel = new JLabel();
		objectLabel.setDoubleBuffered(true);

		tagIdLabel = new JLabel();
		tagIdLabel.setDoubleBuffered(true);
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
		
		tagsListLabel.setText(list.size() + " tags");
		
		// reinitialize the right panel.
		objectImage.setIcon(null);
		objectLabel.setText("Object name");
		tagIdLabel.setText("Tag uid");
	}
	
	private void addTag(Tag tag)
	{
		int index = tagRendererComponents.size();
		TagRenderer renderer = new TagRenderer(tag, index);
		
		tagsListPanel.add(renderer);
		tagRendererComponents.add(renderer);
	}
	
	class tagsListMouseListener extends MouseAdapter implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			Component component = tagsListPanel.getComponentAt(e.getPoint());
			
			if(component != null && component instanceof TagRenderer)
			{
				TagRenderer renderer = (TagRenderer) component;
				
				if(selectedTagIndex != renderer.getTagIndex()) // if the selection has changed.
				{
					renderer.setSelected(true);
					
					if(selectedTagIndex >= 0) // becasue negative value if no tag selected.
						tagRendererComponents.get(selectedTagIndex).setSelected(false);
					
					selectedTagIndex = renderer.getTagIndex();
				}
			}
		}
	}
	
	class ActionAddNewTag implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		}
	}

}
