package gui.profils;


	
	import gui.IconsProvider;
	import gui.MainFrame;

	import java.awt.Color;
	import java.awt.Dimension;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import javax.swing.Box;
	import javax.swing.BoxLayout;
	import javax.swing.JButton;
	import javax.swing.JComponent;
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.border.BevelBorder;
	import javax.swing.border.CompoundBorder;
	import javax.swing.border.EmptyBorder;
	import javax.swing.border.LineBorder;

	import data.Profile;

	public class ProfilRenderer extends JPanel 
	{
		private static final long serialVersionUID = 7784578881238369368L;
		
		private JLabel profilLabel;
		private JButton editButton;
		private JButton removeButton;
		
		private Profile currentProfile = null;
		
		private boolean isSelected;
		private ProfileManagementPanel profileManagementPanel;
		
		public ProfilRenderer(Profile profile, ProfileManagementPanel profileManagementPanel)
		{
			if(profile == null)
				throw new NullPointerException("profile value can't be null.");
			
			if(profileManagementPanel == null)
				throw new NullPointerException();
			
			
			isSelected = false;
			this.profileManagementPanel = profileManagementPanel;
			currentProfile = profile;
			
			setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			setBorder(new EmptyBorder(3, 3, 3, 3));
			
			profilLabel = new JLabel();
			profilLabel.setOpaque(false);
			profilLabel.setDoubleBuffered(true);

			editButton = new JButton(IconsProvider.iconEditElement);
			editButton.setDoubleBuffered(true);
			editButton.addActionListener(new ActionEditProfile());
			editButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
			
			removeButton = new JButton(IconsProvider.iconDeleteElement);
			removeButton.setDoubleBuffered(true);
			removeButton.addActionListener(new ActionRemoveProfile());
			removeButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
			
			add(profilLabel);
			add(Box.createHorizontalGlue());
			add(editButton);
			add(Box.createHorizontalStrut(10));
			add(removeButton);
			

			profilLabel.setText(currentProfile.getName()); 
			
			

			setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(3, 2, 3, 2)));
			setOpaque(false);

			setMinimumSize(new Dimension(getMinimumSize().width, 34));
			setMaximumSize(new Dimension(getMaximumSize().width, 35));
		}
		
		
		class ActionEditProfile implements ActionListener 
		{
			public void actionPerformed(ActionEvent e)
			{
				Profile ret = profileManagementPanel.runProfileEditor(ProfilRenderer.this);
				
				if(ret != null)
				{
					currentProfile = ret;
					profilLabel.setText(currentProfile.getName());

				
				}
			}
		}
		
		class ActionRemoveProfile implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED), new EmptyBorder(2, 2, 2, 2)));
				setOpaque(true);
				setBackground(Color.RED);
				int returnValue = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "Are you sure you want to delete this profile ?", "Delete profile", JOptionPane.YES_NO_OPTION);
				
				setSelected(isSelected);
				if(returnValue == JOptionPane.YES_OPTION)
				{
					profileManagementPanel.removeProfile(ProfilRenderer.this);
				}
			}
		}
		
		
		public Profile getProfile()
		{
			return currentProfile;
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



