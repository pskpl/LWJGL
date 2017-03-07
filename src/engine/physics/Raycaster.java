package engine.physics;

import java.util.ArrayList;
import java.util.Collections;

import engine.*;
import math.*;

/**
 * Static class that can cast a ray against all colliders in the scene.
 */
public class Raycaster
{	
	// screen coordinates should be with Y == 0 at bottom of screen
	public static ArrayList<RaycastHit> castThroughScreen(Vec2 screenPosition)
	{
		Vec3 ray = createRay(screenPosition, true);
		
		ArrayList<RaycastHit> hits = new ArrayList<RaycastHit>();
		ArrayList<GameObject> allObjects = Application.currentScene.allObjects();
		
		for (GameObject gameObject : allObjects)
		{
			if (gameObject.enabled && gameObject.getCollider() != null)
			{
				RaycastHit hit = gameObject.getCollider().rayHit(ray);
				if (hit != null)
					hits.add(hit);
			}
		}
		
		Collections.sort(hits);
		return hits;
	}
	
	public static Vec3 mouseDeltaRay(Vec2 mouseDelta)
	{
		float x = (Application.window.getWidth() / 2) + mouseDelta.x;
		float y = (Application.window.getHeight() / 2) + mouseDelta.y;
		
		Vec3 ray = createRay(new Vec2(x, y), false);
		return new Vec3(ray.x, ray.y, 0);
	}
	
	private static Vec3 createRay(Vec2 screenPosition, boolean normalized)
	{
		float x = (2 * screenPosition.x) / Application.window.getWidth() - 1;
		float y = (2 * screenPosition.y) / Application.window.getHeight() - 1; 
		float z = 1;
		
		Vec4 ray_clip = new Vec4(x, y, -z, 1);
		Mat4 inverseProjectionMatrix = 
				Glm.inverse(Application.mainCamera.getProjectionMatrix());
		
		Vec4 ray = Mat4.mul(inverseProjectionMatrix, ray_clip);
		ray.z = -1;
		ray.w = 0;
		
		Vec4 normalizedRay = Glm.normalize(ray);
		
		if (normalized)
			return normalizedRay.xyz();
		else
			return ray.xyz();
	}
}