package engine;

import java.util.ArrayList;

import math.*;

// Encapsulates data about a particular scene and holds the root object.
public class Scene
{
	private RootNode rootGameObject;
	private Vec4 directionalLightDirection;
	private Color directionalLightColor;
	private Color ambientLightColor;
	private PointLight pointLight;

	// =======================================================================
	// Properties
	// =======================================================================
	public RootNode getRootObject()
	{
		if (rootGameObject == null)
			rootGameObject = new RootNode(new Vec3());
		return rootGameObject;
	}
	
	public Vec4 getDirectionalLightDirection()
	{
		return directionalLightDirection;
	}
	
	public Color getDirectionalLightColor()
	{
		return directionalLightColor;
	}
	
	public Color getAmbientLightColor()
	{
		return ambientLightColor;
	}
	
	public PointLight getPointLight()
	{
		return pointLight;
	}
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public Scene(Vec4 directionalLightDirection, Color directionalLightColor,
			Color ambientLightColor)
	{
		this.directionalLightDirection = directionalLightDirection;
		this.directionalLightColor = directionalLightColor;
		this.ambientLightColor = ambientLightColor;
	}
	
	public Scene()
	{
		this(new Vec4(0.866f, 0.5f, 0.0f, 0.0f), Color.none(),
				new Color(0.15f, 0.15f, 0.15f, 1));
	}
	
	// =======================================================================
	// Scene Graph Management
	// =======================================================================
	public ArrayList<GameObject> allObjects()
	{
		ArrayList<GameObject> allObjects = new ArrayList<GameObject>();
		rootGameObject.addSelfAndChildren(allObjects);
		return allObjects;
	}
	
	public void loadGameObject(GameObject gameObject)
	{
		getRootObject().addChild(gameObject);
	}
	
	public void unloadGameObject(GameObject gameObject)
	{
		getRootObject().removeChild(gameObject);
	}
	
	public void loadPointLight(PointLight light)
	{
		getRootObject().addChild(light);
		pointLight = light;
	}
}
