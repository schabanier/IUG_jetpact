package tests.gui;

import tests.NetworkServiceEmulator;
import engine.EngineService;
import engine.EngineServiceProvider;
import engine.NetworkServiceProvider;
import exceptions.EngineServiceException;
import exceptions.NetworkServiceException;
import gui.MainFrame;

public class MainFrameTest
{
	public static void main(String args[])
	{
		NetworkServiceProvider.setNetworkService(NetworkServiceEmulator.getInstance());
		EngineServiceProvider.setEngineService(EngineService.getInstance());
		try {
			NetworkServiceProvider.getNetworkService().initNetworkService();
			EngineServiceProvider.getEngineService().initEngineService();
			new MainFrame();
		} catch (NetworkServiceException e) {
			e.printStackTrace();
		} catch (EngineServiceException e) {
			e.printStackTrace();
		}
	}
}
