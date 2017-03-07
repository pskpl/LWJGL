package engine;

import math.Vec3;

/**
 * Node object that sits at root of scene graph. Private class.
 */
class RootNode extends Node
{
	public RootNode(Vec3 initialPosition)
	{
		super(initialPosition);
	}
	
	// =======================================================================
	// Game Object Methods
	// =======================================================================
	public void drawGraph()
	{
		MatrixStack transformStack = new MatrixStack();
		transformStack.getTop().mul(getWorldTransform());
		drawSelfAndChildren(transformStack);
	}
	
	public void updateGraph()
	{
		updateSelfAndChildren();
	}
}
