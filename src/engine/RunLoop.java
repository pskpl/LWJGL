package engine;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import engine.physics.*;

/**
 * Static class that coordinates updating and drawing of all objects in 
 * the scene.
 */
public class RunLoop
{	
	// all private times in milliseconds
	private static long thisFrameTime; // relative to arbitrary origin
	private static long lastFrameTime; // relative to arbitrary origin
	private static long deltaTime;
	private static long timeSinceStart;
	
	private static boolean quitWasCalled;
			
	// =======================================================================
	// Property Accessors
	// =======================================================================
	// returns time since app start in seconds
	public static float getTimeSinceStart()
	{
		return timeSinceStart / 1000f;
	}
	
	// returns time since last frame in seconds
	public static float getDeltaTime()
	{
		return deltaTime / 1000f;
	}
	
	// =======================================================================
	// Run Loop
	// =======================================================================
	public static void run()
	{				
		initTime();
		
		while (!shouldExit())
		{
			stepTime();
			Input.poll();
			castMouseIfNeeded();
			update();
//			handleCollisions();
			draw();
			Application.window.update();
		}
	}
	
	public static void quit()
	{
		quitWasCalled = true;
	}
	
	private static void castMouseIfNeeded()
	{
		if (Input.mouseButtonPressed(0)) 
		{
			// call handleClick() on first hit object
			ArrayList<RaycastHit> hits = 
					Raycaster.castThroughScreen(Input.mousePosition());
			
			if (!hits.isEmpty())
				hits.get(0).gameObject.handleClick(0);
		}
	}
	
	private static void update()
	{
		Application.mainCamera.update();
		Application.currentScene.getRootObject().updateGraph();
	}
	
//	private static void handleCollisions()
//	{
//		// O(n^2). Major bottleneck without some kind of space partitioning
//		ArrayList<GameObject> gameObjects = GameObject.getGameObjects();
//		for (int i = 0; i < gameObjects.size() - 1; i++)
//		{
//			GameObject a = gameObjects.get(i);
//			AABoxCollider aCollider = a.getCollider();
//			if (aCollider == null)
//				continue;
//
//			for (int j = i + 1; j < gameObjects.size(); j++)
//			{
//				GameObject b = gameObjects.get(j);
//				AABoxCollider bCollider = b.getCollider();
//				if (bCollider == null)
//					continue;
//				
//				Vec3 collisionSurface = 
//						aCollider.collisionSurfaceWithBox(bCollider);
//				
//				if (collisionSurface != null)
//				{
//					a.handleCollision(collisionSurface, b);
//					b.handleCollision(collisionSurface.scale(-1), a);
//				}
//			}
//		}
//	}
	
	private static void draw()
	{
		Application.window.clear();		
		Application.currentScene.getRootObject().drawGraph();
	}
	
	// =======================================================================
	// Helper Methods
	// =======================================================================
	private static boolean shouldExit()
	{
		return Input.keyPressed(Keyboard.KEY_ESCAPE) ||
				Application.window.isCloseRequested() ||
				quitWasCalled;
	}
	
	private static void initTime()
	{
		thisFrameTime = lastFrameTime = SystemTime();
	}
	
	private static void stepTime() 
	{		
        thisFrameTime = SystemTime();
        deltaTime = thisFrameTime - lastFrameTime;
		lastFrameTime = thisFrameTime;
		timeSinceStart += deltaTime;
	}
	
	private static long SystemTime()
	{
		return System.nanoTime() / 1000000;
	}
}
