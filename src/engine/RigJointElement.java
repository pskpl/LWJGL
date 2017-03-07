package engine;

import java.util.HashSet;
import math.*;

/**
 * Class representing a joint in a rig. Private to engine.
 */
class RigJointElement extends RigElement
{
	private Joint joint;
	private HashSet<RigEffectorElement> descendantEndEffectors;
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public RigJointElement(Joint joint) 
	{
		super(joint);
		
		this.joint = joint;
		descendantEndEffectors = new HashSet<RigEffectorElement>();
	}
	
	// =======================================================================
	// Rotation
	// =======================================================================
	public void rotateBy(float rotationDegrees)
	{
		joint.rotateBy(rotationDegrees);
	}
	
	// =======================================================================
	// Jacobian Matrix Computation
	// =======================================================================
	public void notifyOfDescendantEffector(RigEffectorElement endEffector)
	{
		descendantEndEffectors.add(endEffector);
		
		if (parent != null)
			parent.notifyOfDescendantEffector(endEffector);
	}
	
	public double[] jacobianVector(RigEffectorElement endEffector)
	{
		if (!descendantEndEffectors.contains(endEffector))
			return new Vec3().toDoubleArray();
		
		Vec3 posDiff = Vec3.sub(endEffector.getTargetPosition(), 
				getWorldPosition());
		return Glm.cross(joint.getWorldRotationAxis(), posDiff).toDoubleArray();
	}
}
