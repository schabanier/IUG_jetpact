package gui.tagsmanagement;

import gui.IconsProvider;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import data.Tag;

public class TagRenderer extends JPanel implements ListCellRenderer<Tag>
{
	private static final long serialVersionUID = 5689628426198840798L;
	
	private JLabel tagLabel;
	private JButton editButton;
	private JButton removeButton;
	
	private Tag currentTag = null;
	
	public TagRenderer()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(new EmptyBorder(3, 3, 3, 3));
		
		tagLabel = new JLabel();
		tagLabel.setDoubleBuffered(true);
		tagLabel.setIconTextGap(10);

		editButton = new JButton(IconsProvider.iconEditElement);
		editButton.setDoubleBuffered(true);
		editButton.addActionListener(new ActionEditTag());
		editButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		
		removeButton = new JButton(IconsProvider.iconDeleteElement);
		removeButton.setDoubleBuffered(true);
		removeButton.addActionListener(new ActionRemoveTag());
		removeButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		
		setBorder(new EmptyBorder(4, 4, 4, 4));
		
		add(tagLabel);
		add(Box.createHorizontalStrut(10));
		add(editButton);
		add(Box.createHorizontalStrut(10));
		add(removeButton);
		
	}
	
	public Component getListCellRendererComponent(JList<? extends Tag> list, Tag value, int index, boolean isSelected, boolean cellHasFocus)
	{
		currentTag = value;
		
		tagLabel.setText(currentTag.getObjectName());
		
		if(currentTag.getObjectImage() != null)
			tagLabel.setIcon(new ImageIcon(currentTag.getObjectImage().getScaledInstance(20, 20, Image.SCALE_FAST)));
		else
			tagLabel.setIcon(new ImageIcon()); // to remove the current icon.
		
		if(isSelected)
		{
			setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED), new EmptyBorder(3, 3, 3, 3)));
			setOpaque(true);
			setBackground(Color.CYAN);
		}
		else
		{
			setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(3, 3, 3, 3)));
			setOpaque(false);
		}
		
		return this;
	}
	
	class ActionEditTag implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		}
	}
	
	class ActionRemoveTag implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		}
	}
}
