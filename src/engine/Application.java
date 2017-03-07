package engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;

/**
 * Holds the main window and coordinates the run loop.
 */
public class Application
{
    public static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;

	public static Window window;
	public static Camera mainCamera;
	public static Scene currentScene;
	
	private IApplicationInitializer initializer;
	
	public Application(IApplicationInitializer initializer)
	{
		this.initializer = initializer;
	}
	
	public void start()
	{
		try
		{
			window = initializer.initWindow();
			mainCamera = initializer.initCamera();
			currentScene = initializer.initScene();
			RunLoop.run();
		}
		catch (LWJGLException e)
		{
            Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
            Debug.logError(this, "Initialization failed.\n\n" + e.getMessage());
			System.exit(0);
		}
		
		// run loop ended, so quit
		window.close();
	}
}
