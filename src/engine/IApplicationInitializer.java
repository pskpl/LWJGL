package engine;

import org.lwjgl.LWJGLException;

/**
 * An object that implements this interface gets passed to Application to
 * start a game.
 */
public interface IApplicationInitializer
{
	public Window initWindow() throws LWJGLException;
	public Camera initCamera();
	public Scene initScene();
}
