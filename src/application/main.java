package application;

import engine.*;

public class main
{
	public static void main(String[] args)
	{
		ApplicationInitializer initializer = new ApplicationInitializer();
		Application application = new Application(initializer);
		application.start();
	}
}
