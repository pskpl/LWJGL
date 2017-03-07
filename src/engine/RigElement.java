package engine;

import math.*;
import java.util.ArrayList;

/**
 * Class representing a rig element. Private to engine.
 * 
 * RigElement and its subclasses wrap GameObjects so that different kinds of 
 * GameObject subclasses can be used with a rig without having to include a
 * bunch of Rig-related functionality in the GameObject base class.
 */
class RigElement 
{	
	protected RigElement parent;
	protected ArrayList<RigElement> children;
	
	private GameObject gameObject;
	
	// =======================================================================
	// Properties
	// =======================================================================
	public Vec3 getWorldPosition() 
	{
		return gameObject.getWorldPosition().xyz();
	}
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public RigElement(GameObject gameObject) 
	{
		this.gameObject = gameObject;
		children = new ArrayList<RigElement>();
	}
	
	// =======================================================================
	// Rig Graph Management
	// =======================================================================
	public void addChild(RigElement child)
	{
		child.parent = this;
		children.add(child);
	}
	
	// =======================================================================
	// Jacobian Matrix Computation
	// =======================================================================
	public void notifyOfDescendantEffector(RigEffectorElement endEffector)
	{
		if (parent != null)
			parent.notifyOfDescendantEffector(endEffector);
	}
}
