package engine;

import java.util.ArrayList;
import java.util.HashMap;

import math.*;
import org.ejml.simple.*;

// A rig that supports inverse kinematics.
public class Rig extends Node
{
	private static final double DAMPING_CONSTANT = 0.5;

	private ArrayList<RigJointElement> joints;
	private ArrayList<RigEffectorElement> endEffectors;
	
	private HashMap<GameObject, RigElement> elementMap;
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public Rig(Vec3 initialPosition)
	{
		super(initialPosition);
		
		joints = new ArrayList<RigJointElement>();
		endEffectors = new ArrayList<RigEffectorElement>();
		
		elementMap = new HashMap<GameObject, RigElement>();
		elementMap.put(this, new RigElement(this));
	}
	
	// =======================================================================
	// Extending the Rig
	// =======================================================================
	public void appendJointTo(GameObject rigObject, Joint newJoint)
	{
		RigJointElement newRigJointElement = new RigJointElement(newJoint);
		append(rigObject, newJoint, newRigJointElement);
		
		joints.add(newRigJointElement);
	}
	
	public void appendEndEffectorTo(GameObject rigObject, 
			EndEffector newEndEffector)
	{
		RigEffectorElement newRigElement = 
				new RigEffectorElement(newEndEffector);
		append(rigObject, newEndEffector, newRigElement);
		
		endEffectors.add(newRigElement);
		
		// notify all ancestors that effector has been added
		newRigElement.notifyOfDescendantEffector(newRigElement);
	}
	
	public void appendSegmentTo(GameObject rigObject, GameObject newRigObject)
	{
		append(rigObject, newRigObject, new RigElement(newRigObject));
	}
	
	private void append(GameObject rigObject, GameObject newRigObject,
			RigElement newRigElement)
	{
		RigElement rigElement = elementMap.get(rigObject);
		if (rigElement == null) {
			Debug.log(this, "Game object provided was not in the rig.");
			return;
		}
		
		rigElement.addChild(newRigElement);
		rigObject.addChild(newRigObject);
		
		elementMap.put(newRigObject, newRigElement);
	}
	
	// =======================================================================
	// Game Object Methods
	// =======================================================================
	public void update()
	{		
		if (joints.isEmpty() || endEffectors.isEmpty())
			return;
		
		SimpleMatrix jacobian = calculateJacobian();
		
		SimpleMatrix s = calculateEffectorPositions();
		SimpleMatrix t = calculateTargetPositions();
		SimpleMatrix e = t.minus(s); // vector difference
		
		SimpleMatrix deltaAngles = leastSquares(jacobian, e);
//		Debug.log(this, "Delta Angles:");
//		deltaAngles.print("%10.2f");
		
		updateJointAngles(deltaAngles);
	}
	
	// =======================================================================
	// Inverse Kinematics
	// =======================================================================
	public SimpleMatrix leastSquares(SimpleMatrix jacobian, SimpleMatrix e)
	{
		SimpleMatrix jacobianT = jacobian.transpose();
		SimpleMatrix jjT = jacobian.mult(jacobianT);
		double lambdaSquared = Math.pow(DAMPING_CONSTANT, 2);
		SimpleMatrix lambdaMatrix = 
				SimpleMatrix.identity(jjT.numCols()).scale(lambdaSquared);
		
		SimpleMatrix A = jacobianT.mult(jjT.plus(lambdaMatrix).invert());
		return A.mult(e);
	}
	
	private SimpleMatrix calculateJacobian()
	{
		int m = endEffectors.size() * 3;
		int n = joints.size();
		SimpleMatrix jacobian = new SimpleMatrix(m, n);
		
		double[] currentJacobianVector = new double[0];
		for (int col = 0; col < n; col++)
		{
			for (int row = 0; row < m; row++)
			{
				if (row % 3 == 0) 
				{
					RigEffectorElement endEffector = endEffectors.get(row / 3);
					currentJacobianVector = 
							joints.get(col).jacobianVector(endEffector);
				}
				
				jacobian.set(row, col, currentJacobianVector[row % 3]);
			}
		}
		
		return jacobian;
	}
	
	private SimpleMatrix calculateEffectorPositions()
	{
		int m = endEffectors.size() * 3;
		int n = 1;
		SimpleMatrix effectorPositions = new SimpleMatrix(m, n);
		
		double[] position = new double[0];
		for (int row = 0; row < m; row++)
		{
			if (row % 3 == 0)
				position = 
					endEffectors.get(row / 3).getWorldPosition().toDoubleArray();
			
			effectorPositions.set(row, 0, position[row % 3]);
		}
		
		return effectorPositions;
	}
	
	private SimpleMatrix calculateTargetPositions()
	{
		int m = endEffectors.size() * 3;
		int n = 1;
		SimpleMatrix targetPositions = new SimpleMatrix(m, n);
		
		double[] position = new double[0];
		for (int row = 0; row < m; row++)
		{
			if (row % 3 == 0)
				position = 
					endEffectors.get(row / 3).getTargetPosition().toDoubleArray();
			
			targetPositions.set(row, 0, position[row % 3]);
		}
		
		return targetPositions;
	}
	
	// =======================================================================
	// Helper Methods
	// =======================================================================
	private void updateJointAngles(SimpleMatrix anglesMatrix)
	{
		for (int row = 0; row < joints.size(); row++)
		{
			float degrees = (float) Math.toDegrees(anglesMatrix.get(row, 0));
			joints.get(row).rotateBy(degrees);
		}
	}
}
