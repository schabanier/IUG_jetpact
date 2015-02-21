package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import engine.NetworkServiceProvider;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import gui.authentification.Identification;
import gui.infoperso.InfoPerso;
import gui.tagsmanagement.TagsManagementPanel;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = -8026416994513756565L;
	private static MainFrame instance;

	private JPanel centerPanel;
	private InfoPerso userInformationsPanel;
	private TagsManagementPanel tagsPanel;
	private JPanel profilesPanel;

	private JList<String> panelList;

	private Identification authenticationPanel;
	private JPanel managementPanel;
	

	public MainFrame() throws HeadlessException
	{
		super("StufFinder");
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		instance = this;
		
		authenticationPanel = new Identification(this);
		
		createManagementPanel();
		
		
		setContentPane(authenticationPanel);
		pack();
		setResizable(false);
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
		panelList.setSelectedIndex(0);
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
				setResizable(false);
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
		

		userInformationsPanel = new InfoPerso(this);
		
		tagsPanel = new TagsManagementPanel();
		
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
	
	public static MainFrame getInstance()
	{
		return instance;
	}
	
	/**
	 * Loads the tag list and displays the management panel, with the focus on the personnal informations panel.
	 */
	public void authenticationDone()
	{
		try {
			tagsPanel.reloadTagsList(); // to load the tag list.
			userInformationsPanel.reloadDisplayedInformations();
			
			
			setResizable(true);
			setContentPane(managementPanel);
			panelList.setSelectedIndex(0);
			
			((CardLayout) centerPanel.getLayout()).show(centerPanel, Constants.MainFrame.USER_INFORMATIONS_PANEL_NAME);
			pack();
			
		} catch (NotAuthenticatedException e1) { // this error can't occur because authentication is successful. thrown by the method reloadTagsList().
			JOptionPane.showMessageDialog(getInstance(), "An abnormal error has occured. Please restart the application to try to solve the problem.", "Abnormal error", JOptionPane.ERROR_MESSAGE);
		} catch (NetworkServiceException e1) {
			JOptionPane.showMessageDialog(getInstance(), "A network error has occured. Maybe you're not connected to internet.", "Abnormal error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
