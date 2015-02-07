package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import engine.NetworkServiceProvider;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = -8026416994513756565L;

	private JPanel centerPanel;
	private JPanel userInformationsPanel;
	private JPanel tagsPanel;
	private JPanel profilesPanel;

	private JList<String> panelList;

	private JPanel authenticationPanel;
	private JPanel managementPanel;
	

	public MainFrame() throws HeadlessException
	{
		super("StufFinder");
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		authenticationPanel = new JPanel(); // will be replaced by the real authentication panel.
		authenticationPanel.setLayout(new FlowLayout());
		JButton login = new JButton("Log in");
		login.setDoubleBuffered(true);
		login.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
				setContentPane(managementPanel);
				pack();
			}
		});
		authenticationPanel.add(login);
		
		createManagementPanel();
		
		
		setContentPane(authenticationPanel);
		pack();
		setVisible(true);
	}
	
	private void createManagementPanel()
	{

		managementPanel = new JPanel(true);
		managementPanel.setLayout(new BorderLayout());
		
		managementPanel.add(createLeftPanel(), BorderLayout.WEST);
		managementPanel.add(createCenterPanel(), BorderLayout.CENTER);
	}
	
	private  JPanel createLeftPanel()
	{
		JPanel panel = new JPanel(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(new EmptyBorder(7, 7, 7, 2));
		
		panelList = new JList<>(new DefaultListModel<String>());
		((DefaultListModel<String>) panelList.getModel()).addElement(Constants.MainFrame.USER_INFORMATIONS_PANEL_NAME);
		((DefaultListModel<String>) panelList.getModel()).addElement(Constants.MainFrame.TAGS_PANEL_NAME);
		((DefaultListModel<String>) panelList.getModel()).addElement(Constants.MainFrame.PROFILES_PANEL_NAME);
		
		panelList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panelList.setDoubleBuffered(true);
		panelList.addListSelectionListener(new PanelsListSelectionListener());
		
		JScrollPane scrollPane = new JScrollPane(panelList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setDoubleBuffered(true);
		scrollPane.setOpaque(false);
		
		JButton logoutButton = new JButton(Constants.MainFrame.LOGOUT_BUTTON_NAME);
		logoutButton.setDoubleBuffered(true);
		logoutButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
				NetworkServiceProvider.getNetworkService().logOut();
				setContentPane(authenticationPanel);
				pack();
			}
		});
		
		panel.add(scrollPane);
		panel.add(Box.createVerticalStrut(20));
		panel.add(logoutButton);
		
		return panel;
	}
	
	private JPanel createCenterPanel()
	{
		centerPanel = new JPanel(true);
		centerPanel.setLayout(new CardLayout());
		

		userInformationsPanel = new JPanel(true);
		userInformationsPanel.setBorder(new TitledBorder("user informations"));
		
		tagsPanel = new JPanel(true);
		tagsPanel.setBorder(new TitledBorder("tags panel"));
		
		profilesPanel = new JPanel(true);
		profilesPanel.setBorder(new TitledBorder("profiles panel"));
		
		
		centerPanel.add(profilesPanel, Constants.MainFrame.PROFILES_PANEL_NAME);
		centerPanel.add(tagsPanel, Constants.MainFrame.TAGS_PANEL_NAME);
		centerPanel.add(userInformationsPanel, Constants.MainFrame.USER_INFORMATIONS_PANEL_NAME);
		
		((CardLayout) centerPanel.getLayout()).show(centerPanel, Constants.MainFrame.USER_INFORMATIONS_PANEL_NAME);
		
		return centerPanel;
	}
	
	class PanelsListSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			((CardLayout) centerPanel.getLayout()).show(centerPanel, panelList.getSelectedValue());
		}
	}
}
