package engine;

import java.util.HashSet;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import math.*;

/**
 * Static class that polls input and can be queried for key status.
 */
public class Input
{	
	// keys pressed this frame
	private static HashSet<Integer> keyPressedMap =
			new HashSet<Integer>();  
	
	// mouse buttons pressed this frame
	private static HashSet<Integer> buttonPressedMap =
			new HashSet<Integer>();
	
	// keys released this frame
	private static HashSet<Integer> keyReleasedMap =
			new HashSet<Integer>(); 
	
	// mouse buttons released this frame
	private static HashSet<Integer> buttonReleasedMap =
			new HashSet<Integer>();
	
	private static Vec2 mouseDelta;
	
	// =======================================================================
	// Polling
	// =======================================================================
	public static void poll()
	{		
		// remove all
		keyPressedMap.clear();
		buttonPressedMap.clear();
		keyReleasedMap.clear();
		buttonReleasedMap.clear();
				
		// poll input events and save to maps
		while (Keyboard.next()) 
		{			
		    if (Keyboard.getEventKeyState())
		    	keyPressedMap.add(Keyboard.getEventKey());
		    else
		    	keyReleasedMap.add(Keyboard.getEventKey());
		}
		
		while (Mouse.next())
		{
			if (Mouse.getEventButtonState())
				buttonPressedMap.add(Mouse.getEventButton());
			else
				buttonReleasedMap.add(Mouse.getEventButton());
		}
		
		mouseDelta = new Vec2(Mouse.getDX(), Mouse.getDY());
	}
	
	// =======================================================================
	// Getting Key Status
	// =======================================================================
	public static boolean isKeyDown(int key)
	{
		return Keyboard.isKeyDown(key);
	}
	
	public static boolean keyPressed(int key)
	{
		if (keyPressedMap.contains(key))
			return true;
		
		return false;
	}
	
	public static boolean keyReleased(int key)
	{
		if (keyReleasedMap.contains(key))
			return true;
		
		return false;
	}
	
	// =======================================================================
	// Getting Mouse Button Status
	// =======================================================================
	public static boolean isMouseButtonDown(int button)
	{
		return Mouse.isButtonDown(button);
	}
	
	public static boolean mouseButtonPressed(int button)
	{
		if (buttonPressedMap.contains(button))
			return true;
		
		return false;
	}
	
	public static boolean mouseButtonReleased(int button)
	{
		if (buttonReleasedMap.contains(button))
			return true;
		
		return false;
	}
	
	public static Vec2 mousePosition()
	{
		return new Vec2(Mouse.getX(), Mouse.getY());
	}
	
	// movement of mouse since last frame
	public static Vec2 mouseDelta()
	{
		return mouseDelta;
	}
}
