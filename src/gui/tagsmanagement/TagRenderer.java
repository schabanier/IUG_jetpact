package gui.tagsmanagement;

import gui.Constants.TagsManagement;
import gui.IconsProvider;
import gui.MainFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import data.Tag;
import engine.ImageLoader;

public class TagRenderer extends JPanel //implements ListCellRenderer<Tag>
{
	private static final long serialVersionUID = 5689628426198840798L;
	
	private JLabel tagLabel;
	private JButton editButton;
	private JButton removeButton;
	
	private Tag currentTag = null;
	
	private boolean isSelected;
	TagsManagementPanel managementPanel;
	
	public TagRenderer(Tag tag, TagsManagementPanel managementPanel)
	{
		if(tag == null)
			throw new NullPointerException("tag value can't be null.");
		
		if(managementPanel == null)
			throw new NullPointerException();
		
		
		isSelected = false;
		this.managementPanel = managementPanel;
		currentTag = tag;
		
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(new EmptyBorder(3, 3, 3, 3));
		
		tagLabel = new JLabel();
		tagLabel.setOpaque(false);
		tagLabel.setDoubleBuffered(true);

		editButton = new JButton(IconsProvider.iconEditElement);
		editButton.setDoubleBuffered(true);
		editButton.addActionListener(new ActionEditTag());
		editButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		
		removeButton = new JButton(IconsProvider.iconDeleteElement);
		removeButton.setDoubleBuffered(true);
		removeButton.addActionListener(new ActionRemoveTag());
		removeButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		
		add(tagLabel);
		add(Box.createHorizontalGlue());
		add(editButton);
		add(Box.createHorizontalStrut(10));
		add(removeButton);
		

		tagLabel.setText(currentTag.getObjectName());
		
		if(currentTag.getObjectImageName() != null)
		{
			
			try {
				tagLabel.setIcon(new ImageIcon(ImageLoader.getInstance().getImageAtLowSize(new File(currentTag.getObjectImageName()))));
			} catch (IOException e) { // an error occured while reading the image file. the default object image will be displayed.
				tagLabel.setIcon(new ImageIcon(IconsProvider.defaultObjectImageLittle));
			}
		}
		else
			tagLabel.setIcon(new ImageIcon(IconsProvider.defaultObjectImageLittle));

		setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(3, 2, 3, 2)));
		setOpaque(false);

		setMinimumSize(new Dimension(getMinimumSize().width, 34));
		setMaximumSize(new Dimension(getMaximumSize().width, 35));
	}
	
	
	class ActionEditTag implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Tag ret = managementPanel.runTagEditor(TagRenderer.this);
			
			if(ret != null)
			{
				currentTag = ret;
				tagLabel.setText(currentTag.getObjectName());

				if(currentTag.getObjectImageName() != null)
				{
					
					try {
						tagLabel.setIcon(new ImageIcon(ImageLoader.getInstance().getImageAtLowSize(new File(currentTag.getObjectImageName()))));
					} catch (IOException e1) { // an error occured while reading the image file. the default object image will be displayed.
						tagLabel.setIcon(new ImageIcon(IconsProvider.defaultObjectImageLittle));
					}
				}
				else
					tagLabel.setIcon(new ImageIcon(IconsProvider.defaultObjectImageLittle));
			}
		}
	}
	
	class ActionRemoveTag implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED), new EmptyBorder(2, 2, 2, 2)));
			setOpaque(true);
			setBackground(Color.RED);
			int returnValue = JOptionPane.showConfirmDialog(MainFrame.getInstance(), TagsManagement.CONFIRM_TAG_DELETION_MESSAGE, TagsManagement.CONFIRM_TAG_DELETION_TITLE, JOptionPane.YES_NO_OPTION);
			
			setSelected(isSelected);
			if(returnValue == JOptionPane.YES_OPTION)
			{
				managementPanel.removeTag(TagRenderer.this);
			}
		}
	}
	
	
	public Tag getTag()
	{
		return currentTag;
	}

	public boolean isSelected()
	{
		return isSelected;
	}
	
	public void setSelected(boolean selected)
	{
		isSelected = selected;
		
		if(isSelected)
		{
			setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED), new EmptyBorder(2, 2, 2, 2)));
			setOpaque(true);
			setBackground(Color.CYAN);
		}
		else
		{
			setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(3, 2, 3, 2)));
			setOpaque(false);
		}
	}
}
