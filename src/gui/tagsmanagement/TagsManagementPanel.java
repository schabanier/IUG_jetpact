package gui.tagsmanagement;

import gui.Constants;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class TagsManagementPanel extends JPanel
{
	private static final long serialVersionUID = -7659217016124788668L;
	private JLabel tagsListLabel;
	private DefaultListModel<Tag> tagsListModel;
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
		JPanel tagsListPanel = new JPanel(true);
		tagsListPanel.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(7, 7, 7, 7)));
		tagsListPanel.setLayout(new BoxLayout(tagsListPanel, BoxLayout.PAGE_AXIS));
		tagsListPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		tagsListLabel = new JLabel("X tags linked with account");
		tagsListLabel.setDoubleBuffered(true);
		
		JList<Tag> tagsList = new JList<>();
		tagsListModel = new DefaultListModel<Tag>();
		tagsList.setModel(tagsListModel);
		tagsList.setDoubleBuffered(true);
		tagsList.setCellRenderer(new TagRenderer());
		
		JScrollPane scrollPane = new JScrollPane(tagsList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setDoubleBuffered(true);
		
		JButton addTagButton = new JButton(Constants.MainFrame.ADD_TAG_BUTTON_NAME);
		addTagButton.setDoubleBuffered(true);
		addTagButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addTagButton.addActionListener(new ActionAddNewTag());
		
		tagsListPanel.add(tagsListLabel);
		tagsListPanel.add(Box.createVerticalStrut(10));
		tagsListPanel.add(scrollPane);
		tagsListPanel.add(Box.createVerticalStrut(10));
		tagsListPanel.add(addTagButton);
		
		return tagsListPanel;
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
		tagIdLabel.setToolTipText("Tag indentifier.");
		
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
		
		return tagInformationsPanel;
	}
	
	class ActionAddNewTag implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		}
	}

}
