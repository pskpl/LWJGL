package engine;

import java.util.ArrayList;

import engine.physics.*;
import math.*;

/**
 * Parent abstract class for all game objects.
 */
public abstract class GameObject
{
	public boolean enabled = true;
	
	protected GameObject parent;
	protected ArrayList<GameObject> children;
	
	protected Vec3 velocity;
	protected Collider collider; // may be null
	
	private Mat4 transform;

	// =======================================================================
	// Property Accessors
	// =======================================================================	
	public ArrayList<GameObject> getChildren()
	{
		if (children == null)
			children = new ArrayList<GameObject>();
		return children;
	}
	
	public Vec4 getLocalPosition()
	{
		return Mat4.mul(transform, new Vec4(new Vec3(), 1));
	}
	
	public Vec4 getWorldPosition()
	{
		return Mat4.mul(getWorldTransform(), new Vec4(new Vec3(), 1));
	}
	
	public void setLocalPosition(Vec3 position)
	{
		transform.set(3, 0, position.x);
		transform.set(3, 1, position.y);
		transform.set(3, 2, position.z);
	}
	
	public Vec3 getVelocity()
	{
		return velocity;
	}
	
	public Collider getCollider()
	{
		return collider;
	}
	
	// objects are drawn with a matrix stack that gets passed down the graph
	// but this is for finding world position at an arbitrary object
	// so we have to work backwards
	// TODO: SOME CACHING MIGHT BE APPROPRIATE HERE
	public Mat4 getWorldTransform()
	{
		// base case
		if (parent == null)
			return new Mat4(1);
		
		// general case
		return Mat4.mul(parent.getWorldTransform(), transform);
	}
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public GameObject(Vec3 initialPosition)
	{
		transform = new Mat4(1);
		setLocalPosition(initialPosition);		
	}
	
	// =======================================================================
	// Scaling
	// =======================================================================
	public void localScale(float scalar)
	{
		transform.mul(Mat4.getScale(scalar, scalar, scalar));
	}
	
	// =======================================================================
	// Rotation
	// =======================================================================
	public void localRotateX(float angleDegrees) 
	{
		transform.mul(Mat4.getRotateX(angleDegrees));
	}
	
	public void localRotateY(float angleDegrees)
	{
		transform.mul(Mat4.getRotateY(angleDegrees));
	}
	
	public void localRotateZ(float angleDegrees)
	{
		transform.mul(Mat4.getRotateZ(angleDegrees));
	}
	
	public void localRotate(float angleDegrees, Vec3 rotationAxis)
	{
		transform.mul(Mat4.getRotate(angleDegrees, rotationAxis));
	}
	
	// =======================================================================
	// Run Loop Interface
	// =======================================================================	
	// no access specifier here means this method can be called from subclasses
	// but only within the "engine" package, which is exactly what we want
	void updateSelfAndChildren()
	{
		if (!enabled)
			return;
		
		update();
		
		for (GameObject gameObject : getChildren())
			gameObject.updateSelfAndChildren();
	}

	void drawSelfAndChildren(MatrixStack transformStack)
	{	
		if (!enabled)
			return;
		
		transformStack.push();
		transformStack.getTop().mul(transform);
		
		draw(transformStack.getTop());
		
		for (GameObject gameObject : getChildren())
			gameObject.drawSelfAndChildren(transformStack);
		
		transformStack.pop();
	}
	
	void addSelfAndChildren(ArrayList<GameObject> list)
	{
		list.add(this);
		
		for (GameObject gameObject : getChildren())
			gameObject.addSelfAndChildren(list);
	}
	
	public abstract void handleClick(int mouseButton);
		
	public abstract void update();
	
	public abstract void handleCollision(Vec3 collisionSurface, 
			GameObject otherObject);
	
	public abstract void draw(Mat4 transform);
	
	// =======================================================================
	// Scene Graph Management
	// =======================================================================
	public void addChild(GameObject gameObject)
	{
		getChildren().add(gameObject);
		gameObject.parent = this;
	}
	
	public void removeChild(GameObject gameObject)
	{
		getChildren().remove(gameObject);
		gameObject.parent = null;
	}
}
