package tests;

import engine.NetworkServiceProvider;
import gui.MainFrame;

public class MainFrameTest
{
	public static void main(String args[])
	{
		NetworkServiceProvider.setNetworkService(NetworkServiceEmulator.getInstance());
		
		new MainFrame();
	}
}
