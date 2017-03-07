package engine.physics;

import engine.GameObject;

public class RaycastHit implements Comparable<RaycastHit>
{
	public GameObject gameObject;
	public float distanceFromOrigin;
	
	public RaycastHit(GameObject gameObject, float distanceFromOrigin)
	{
		this.gameObject = gameObject;
		this.distanceFromOrigin = distanceFromOrigin;
	}

	@Override
	public int compareTo(RaycastHit otherHit)
	{	
		if (distanceFromOrigin < otherHit.distanceFromOrigin)
			return -1;
		else if (distanceFromOrigin > otherHit.distanceFromOrigin)
			return 1;
		else
			return 0;
	}
}