package tests.gui;

import javax.swing.JFrame;

import gui.tagsmanagement.TagManagerDialog;

public class TestTagManagerDialog
{

	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		TagManagerDialog dialog = new TagManagerDialog(frame);
		
		dialog.setVisible(true);
	}

}
