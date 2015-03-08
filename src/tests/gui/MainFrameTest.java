package tests.gui;

import webservice.NetworkService;
import engine.NetworkServiceProvider;
import exceptions.NetworkServiceException;
import gui.MainFrame;

public class MainFrameTest
{
	public static void main(String args[])
	{
		NetworkServiceProvider.setNetworkService(NetworkService.getInstance());
		try {
			NetworkServiceProvider.getNetworkService().initNetworkService();
			new MainFrame();
		} catch (NetworkServiceException e) {
			e.printStackTrace();
		}
	}
}
