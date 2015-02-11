package gui.tagsmanagement;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import data.Tag;

public class TagRenderer extends JPanel implements ListCellRenderer<Tag>
{
	private static final long serialVersionUID = 5689628426198840798L;
	
	private JLabel tagLabel;
	private JButton editButton;
	private JButton deleteButton;
	
	private Tag currentTag = null;
	
	public TagRenderer()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(new EmptyBorder(3, 3, 3, 3));
		
		tagLabel = new JLabel();
		tagLabel.setDoubleBuffered(true);

		editButton = new JButton();
		editButton.setDoubleBuffered(true);
		
		deleteButton = new JButton();
		deleteButton.setDoubleBuffered(true);
		
		setBorder(new EmptyBorder(4, 4, 4, 4));
		
		add(tagLabel);
		add(Box.createHorizontalStrut(10));
		add(editButton);
		add(Box.createHorizontalStrut(10));
		add(deleteButton);
		
	}
	
	public Component getListCellRendererComponent(JList<? extends Tag> list, Tag value, int index, boolean isSelected, boolean cellHasFocus)
	{
		currentTag = value;
		
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
