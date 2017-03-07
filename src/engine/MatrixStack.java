package engine;

import java.util.Stack;

import math.*;

/**
 * Implements a stack data structure holding 4X4 matrices.
 */
class MatrixStack
{
	private Stack<Mat4> stack;
	private Mat4 currentMatrix;
	
	// =======================================================================
	// Property Accessors
	// =======================================================================
	public Mat4 getTop()
	{
		return currentMatrix;
	}
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public MatrixStack() 
	{
		stack = new Stack<Mat4>();
		currentMatrix = new Mat4(1); // i.e. identity matrix
	}
	
	// =======================================================================
	// Stack Manipulation
	// =======================================================================
	public void push()
	{
		// push a copy of currentMatrix, leaving it unchanged
		stack.push(new Mat4(currentMatrix));
	}
	
	public void pop()
	{
		currentMatrix = stack.pop();
	}
	
	// =======================================================================
	// Matrix Operations
	// =======================================================================
	public void translate(Vec3 offsetVector)
	{
		Mat4 translationMatrix = new Mat4(1);
		translationMatrix.setColumn(3, new Vec4(offsetVector, 1));
		
		currentMatrix.mul(translationMatrix);
	}
	
    public void scale(Vec3 scaleVector) {
        Mat4 scaleMatrix = new Mat4(1);
        scaleMatrix.set(0, 0, scaleVector.x);
        scaleMatrix.set(1, 1, scaleVector.y);
        scaleMatrix.set(2, 2, scaleVector.z);

        currentMatrix.mul(scaleMatrix);
    }
    
    public void rotateAroundX(float angleDegrees)
    {
    	currentMatrix.mul(new Mat4(xAxisRotationMatrix(angleDegrees)));
    }
    
    public void rotateAroundY(float angleDegrees)
    {
    	currentMatrix.mul(new Mat4(yAxisRotationMatrix(angleDegrees)));
    }
    
    public void rotateAroundZ(float angleDegrees)
    {
    	currentMatrix.mul(new Mat4(zAxisRotationMatrix(angleDegrees)));
    }
    
    // =======================================================================
 	// Helper Methods
 	// =======================================================================
    private Mat3 xAxisRotationMatrix(float angleDegrees) {
        float angleRadians = (float) Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);

        Mat3 matrix = new Mat3(1);
        matrix.set(1, 1, cos);
        matrix.set(2, 1, -sin);
        matrix.set(1, 2, sin);
        matrix.set(2, 2, cos);
        return matrix;
    }

    private Mat3 yAxisRotationMatrix(float angleDegrees) {
        float angleRadians = (float) Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);

        Mat3 matrix = new Mat3(1);
        matrix.set(0, 0, cos);
        matrix.set(2, 0, sin);
        matrix.set(0, 2, -sin);
        matrix.set(2, 2, cos);
        return matrix;
    }

    private Mat3 zAxisRotationMatrix(float angleDegrees) {
        float angleRadians = (float) Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);

        Mat3 matrix = new Mat3(1);
        matrix.set(0, 0, cos);
        matrix.set(1, 0, -sin);
        matrix.set(0, 1, sin);
        matrix.set(1, 1, cos);
        return matrix;
    }
}
