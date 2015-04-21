package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import engine.EngineServiceProvider;
import exceptions.NetworkServiceException;
import exceptions.NotAuthenticatedException;
import gui.authentification.Identification;
import gui.infoperso.InfoPerso;
import gui.profils.ProfileManagementPanel;
import gui.tagsmanagement.TagsManagementPanel;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = -8026416994513756565L;
	private static MainFrame instance;

	private JPanel centerPanel;
	private InfoPerso userInformationsPanel;
	private TagsManagementPanel tagsPanel;
	private ProfileManagementPanel profilesPanel;

	private JList<String> panelList;

	private Identification authenticationPanel;
	private JPanel managementPanel;
	

	public MainFrame() throws HeadlessException
	{
		super(Constants.MainFrame.WINDOW_TITLE);
		super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent e)
			{
			}
			
			public void windowIconified(WindowEvent e)
			{
			}
			
			public void windowDeiconified(WindowEvent e)
			{
			}
			
			public void windowDeactivated(WindowEvent e)
			{
			}
			
			public void windowClosing(WindowEvent e)
			{
				Logger.getLogger(getClass().getName()).log(Level.INFO, "window will be closed");
				int returnValue = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "Do you really want to quit ?", "Exit", JOptionPane.YES_NO_OPTION);
				
				if(returnValue == JOptionPane.YES_OPTION)
				{
					EngineServiceProvider.getEngineService().logOut();
					setVisible(false);
				}
			}
			
			public void windowClosed(WindowEvent e)
			{
			}
			
			public void windowActivated(WindowEvent e)
			{
			}
		});
		
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
				EngineServiceProvider.getEngineService().logOut();
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
		
		profilesPanel = new ProfileManagementPanel(); // will be replaced by the real version.
		
		
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
			String value = panelList.getSelectedValue();
			
				try {
					if(value.equals(Constants.MainFrame.USER_INFORMATIONS_PANEL_NAME))
						userInformationsPanel.reloadDisplayedInformations();
					else if(value.equals(Constants.MainFrame.TAGS_PANEL_NAME))
						tagsPanel.reloadTagsList();
					else if(value.equals(Constants.MainFrame.PROFILES_PANEL_NAME))
						profilesPanel.reloadProfilesList();
					
					((CardLayout) centerPanel.getLayout()).show(centerPanel, panelList.getSelectedValue());
				} catch (NotAuthenticatedException e1) {
					e1.printStackTrace();
				} catch (NetworkServiceException e1) {
					e1.printStackTrace();
				}
		}
	}
	
	public static MainFrame getInstance()
	{
		return instance;
	}
	
	/**
	 * Loads the tag list and displays the management panel, with the focus on the personnal informations panel.
	 * This method is called after authentication succeeds.
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
			
		} catch (NotAuthenticatedException e1) { // Normally, this error can't occur because authentication is successful. thrown by the method reloadTagsList().
			JOptionPane.showMessageDialog(getInstance(), Constants.CommonErrorMessages.ABNORMAL_ERROR_MESSAGE, Constants.CommonErrorMessages.ABNORMAL_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		} catch (NetworkServiceException e1) {
			JOptionPane.showMessageDialog(getInstance(), Constants.CommonErrorMessages.NETWORK_SERVICE_ERROR_MESSAGE, Constants.CommonErrorMessages.NETWORK_SERVICE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
