package tests.gui;

<<<<<<< HEAD
import webservice.NetworkService;
=======
import tests.NetworkServiceEmulator;
import engine.EngineService;
import engine.EngineServiceProvider;
>>>>>>> refs/heads/GUIBranch
import engine.NetworkServiceProvider;
import exceptions.EngineServiceException;
import exceptions.NetworkServiceException;
import gui.MainFrame;

public class MainFrameTest
{
	public static void main(String args[])
	{
<<<<<<< HEAD
		NetworkServiceProvider.setNetworkService(NetworkService.getInstance());
=======
		NetworkServiceProvider.setNetworkService(NetworkServiceEmulator.getInstance());
		EngineServiceProvider.setEngineService(EngineService.getInstance());
>>>>>>> refs/heads/GUIBranch
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
