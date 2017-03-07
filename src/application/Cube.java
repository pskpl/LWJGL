package application;

import math.*;
import engine.*;

public class Cube extends DrawnObject
{
	private static final String MESH_PATH = 
			"build/classes/data/meshes/cube.obj";

	public Cube(Vec3 initialPosition, float initialScale)
	{
		super(initialPosition, MeshLoader.loadMesh(MESH_PATH), Color.red(), 1);
		localScale(initialScale);
	}
}
