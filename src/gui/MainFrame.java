package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.HeadlessException;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = -8026416994513756565L;

	public MainFrame() throws HeadlessException
	{
		super("StufFinder");
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel contentPane = new JPanel(true);
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		
		
		// conception of the left panel.
		JList<String> list = new JList<>();
		list.setDoubleBuffered(true);
		
		JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setDoubleBuffered(true);
		
		contentPane.add(scrollPane, BorderLayout.WEST);
		
		
		// center panel
		
		JPanel centerPanel = new JPanel(true);
		centerPanel.setLayout(new CardLayout());
		
		
		// panel for user informations.
		JPanel userInformationsPanel = new JPanel(true);
		GroupLayout groupLayout1 = new GroupLayout(userInformationsPanel);
		userInformationsPanel.setLayout(groupLayout1);
		groupLayout1.setAutoCreateContainerGaps(true);
		groupLayout1.setAutoCreateGaps(true);

		
		
		// panel for tags management.
		JPanel TagsPanel = new JPanel(true);
		GroupLayout groupLayout2 = new GroupLayout(TagsPanel);
		TagsPanel.setLayout(groupLayout2);
		groupLayout2.setAutoCreateContainerGaps(true);
		groupLayout2.setAutoCreateGaps(true);

		
		
		// panel for profiles management.
		JPanel ProfilesPanel = new JPanel(true);
		GroupLayout groupLayout3 = new GroupLayout(ProfilesPanel);
		ProfilesPanel.setLayout(groupLayout3);
		groupLayout3.setAutoCreateContainerGaps(true);
		groupLayout3.setAutoCreateGaps(true);
		
		
		
		centerPanel.add(ProfilesPanel, Constants.MainFrame.PROFILES_PANEL_NAME);
		centerPanel.add(TagsPanel, Constants.MainFrame.TAGS_PANEL_NAME);
		centerPanel.add(userInformationsPanel, Constants.MainFrame.USER_INFORMATIONS_PANEL_NAME);
		
	}
}
