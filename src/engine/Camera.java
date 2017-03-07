package engine;

import math.*;

public class Camera
{		
	public float moveSpeed = 0.5f; // world units per second
	public float rotationSpeed = 30; // degrees per second
	
	private Vec3 targetPosition;
	private Vec3 relativePositionSpherical;	// <r, theta, phi>
	private Mat4 projectionMatrix;
	private SphericalControlScheme controlScheme;
	
	// ========================================================================
	// Property Accessors
	// ========================================================================
	public Mat4 getProjectionMatrix()
	{
		return projectionMatrix;
	}
	
	public Mat4 getWorldToCameraMatrix()
	{
		Vec3 camPos = Utility.sphericalToEuclidean(targetPosition, 
												   relativePositionSpherical);
		return calculateWorldToCamera(camPos, targetPosition, 
									  new Vec3(0, 1, 0));
	}
	
	// ========================================================================
	// Initialization
	// ========================================================================
	public Camera(float FOV, float zNear, float zFar, Vec3 targetPosition, 
			  Vec3 relativePositionSpherical, SphericalControlScheme controlScheme)
	{
		initProjectionMatrix(FOV, zNear, zFar);
		this.targetPosition = targetPosition;
		this.relativePositionSpherical = relativePositionSpherical;
		this.controlScheme = controlScheme;
	}
	
	// ========================================================================
	// Camera Positioning
	// ========================================================================
	public void update()
	{
		float dt = RunLoop.getDeltaTime();
		
		if (controlScheme.isControlDown(SphericalControlScheme.Control.IN))
			relativePositionSpherical.x += moveSpeed * dt;
		if (controlScheme.isControlDown(SphericalControlScheme.Control.OUT))
			relativePositionSpherical.x -= moveSpeed * dt;
		
		if (controlScheme.isControlDown(SphericalControlScheme.Control.ROT_THETA))
			relativePositionSpherical.y += rotationSpeed * dt;
		if (controlScheme.isControlDown(SphericalControlScheme.Control.UNROT_THETA))
			relativePositionSpherical.y -= rotationSpeed * dt;
		
		if (controlScheme.isControlDown(SphericalControlScheme.Control.ROT_PHI))
			relativePositionSpherical.z += rotationSpeed * dt;
		if (controlScheme.isControlDown(SphericalControlScheme.Control.UNROT_PHI))
			relativePositionSpherical.z -= rotationSpeed * dt;
		
		relativePositionSpherical.z = 
				Utility.clamp(relativePositionSpherical.z, 1, 179);
	}
	
	// ========================================================================
	// Helper Methods
	// ========================================================================
	private Mat4 calculateWorldToCamera(Vec3 camPos, Vec3 targetPos, 
									    Vec3 upDirection)
	{
		Vec3 lookDir = Glm.normalize(Vec3.sub(targetPos, camPos));
        Vec3 upDir = Glm.normalize(upDirection);

        Vec3 rightDir = Glm.normalize(Glm.cross(lookDir, upDir));
        Vec3 perpUpDir = Glm.cross(rightDir, lookDir);

        
        Mat4 rotMat = new Mat4(1.0f);
        rotMat.setColumn(0, new Vec4(rightDir, 0.0f));
        rotMat.setColumn(1, new Vec4(perpUpDir, 0.0f));
        rotMat.setColumn(2, new Vec4(Vec3.negate(lookDir), 0.0f));
        rotMat = Glm.transpose(rotMat);

        // translation component is simply the negation of the camera's position
        // in world space
        Mat4 transMat = new Mat4(1.0f);
        transMat.setColumn(3, new Vec4(Vec3.negate(camPos), 1.0f));

        return rotMat.mul(transMat);
	}
	
	private void initProjectionMatrix(float FOV, float zNear, float zFar)
	{
		float frustumTopExtent = frustumTopExtentForFOV(FOV);
		int windowWidth = Application.window.getWidth();
		int windowHeight = Application.window.getHeight();
		float aspectRatio = windowWidth / (float) windowHeight;
		
		projectionMatrix = new Mat4();
		projectionMatrix.set(0, 0, 1 / (frustumTopExtent * aspectRatio));
		projectionMatrix.set(1, 1, 1 / frustumTopExtent);
		projectionMatrix.set(2, 2, (zFar + zNear) / (zNear - zFar));
		projectionMatrix.set(3, 2, (2 * zFar * zNear) / (zNear - zFar));
		projectionMatrix.set(2, 3, -1.0f);
		projectionMatrix.set(3, 3, 0);
//		Debug.log(this, "Projection Matrix:\n" + projectionMatrix.toString());
	}
	
	private float frustumTopExtentForFOV(float FOV) {
		float FOVrad = (float) Math.toRadians(FOV);
		return (float) Math.tan(FOVrad / 2);
	}
}
