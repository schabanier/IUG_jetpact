package gui;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class UserNotifier
{
	private static JDialog activeDialog = null;
	
	public static void setActiveDialog(JDialog dialog)
	{
		activeDialog = dialog;
	}
	
	public static void removeActiveDialog()
	{
		activeDialog = null;
	}
	
	public static void showErrorMessage(final String message)
	{
		new Thread(new Runnable() {
			
			public void run()
			{
				if(activeDialog != null)
					JOptionPane.showMessageDialog(activeDialog, message, "Error", JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(MainFrame.getInstance(), message, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}).start();
	}
	
	public static void askPassword(final String pseudo)
	{
		new Thread(new Runnable() {
			
			public void run()
			{
				if(activeDialog != null)
					new AskPasswordDialog(activeDialog, pseudo).setVisible(true);
				else
					new AskPasswordDialog(MainFrame.getInstance(), pseudo).setVisible(true);
			}
		}).start();
	}
}
