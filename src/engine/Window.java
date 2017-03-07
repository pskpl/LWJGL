package engine;

import math.*;

import org.lwjgl.opengl.ContextAttribs;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.LWJGLException;

/**
 * Handles window creation and initialization of OpenGL.
 */
public class Window
{
	private String title;
	
	// ========================================================================
	// Property Accessors
	// ========================================================================
	public void setTitle(String title)
	{
		this.title = title;
        Display.setTitle(this.title);
	}
	
	public int getWidth()
	{
		return Display.getDisplayMode().getWidth();
	}
	
	public int getHeight()
	{
		return Display.getDisplayMode().getHeight();
	}
	
	public boolean isCloseRequested()
	{
		return Display.isCloseRequested();
	}
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public Window(int width, int height, String windowTitle, 
					Vec4 CLEAR_COLOR) throws LWJGLException
	{		
		Debug.log(this, "Initializing window.");

		title = windowTitle;		
        
		initDisplay(width, height);
        initGL(CLEAR_COLOR);
        
		Debug.log(this, "OpenGL version: " + glGetString(GL_VERSION));
	}
	
	// =======================================================================
	// Controlling the Window
	// =======================================================================
	public void clear()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void update()
	{
		Display.update();
	}
	
	public void close()
	{
		Debug.log(this, "Closing window.");
		Display.destroy();
	}
	
	// =======================================================================
	// Helper Methods
	// =======================================================================
	private void initDisplay(int width, int height) throws LWJGLException
	{
		PixelFormat pixelFormat = new PixelFormat();
		
		// set openGL version
		ContextAttribs contextAttributes = new ContextAttribs(4, 1)
			.withForwardCompatible(true)
			.withProfileCore(true);
		
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.setVSyncEnabled(true);
        Display.setTitle(title);
        Display.create(pixelFormat, contextAttributes);
	}
	
	private void initGL(Vec4 clearColor)
	{
		// Reset current view port
        glViewport(0, 0, getWidth(), getHeight());
        
        // Set clear color and depth
		glClearColor(clearColor.x, clearColor.y, clearColor.z, 
				clearColor.w);
		glClearDepth(1.0f);
		
        glEnable(GL_CULL_FACE); // activate face culling
        glCullFace(GL_BACK);
        glFrontFace(GL_CCW);
        
        glEnable(GL_DEPTH_TEST); // activate depth testing
        glDepthMask(true);
        glDepthFunc(GL_LEQUAL);
        glDepthRange(0.0f, 1.0f);
	}
}
