package application;

import org.lwjgl.input.Keyboard;

import math.*;
import engine.*;

public class Sphere extends DrawnObject
{
	private static final String MESH_PATH = 
			"build/classes/data/meshes/sphere_smooth.obj";

	public Sphere(Vec3 initialPosition, float initialScale,
			String vertexShaderPath, String fragmentShaderPath)
	{
		super(initialPosition, MeshLoader.loadMesh(MESH_PATH), Color.green(),
				1.5f, vertexShaderPath, fragmentShaderPath);
		localScale(initialScale);
	}
	
	public void update()
	{
		if (Input.keyPressed(Keyboard.KEY_T))
		{
			shininess += 0.5f;
			Debug.log(this, "Shininess now " + shininess + ".");
		}
		
		if (Input.keyPressed(Keyboard.KEY_G))
		{
			shininess -= 0.5f;	
			Debug.log(this, "Shininess now " + shininess + ".");
		}
	}
}
