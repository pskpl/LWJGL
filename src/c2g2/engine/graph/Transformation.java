package c2g2.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import c2g2.engine.GameItem;

public class Transformation {

    private final Matrix4f projectionMatrix;
    
    private final Matrix4f viewMatrix;
    
    private final Matrix4f modelMatrix;

    public Transformation() {
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        projectionMatrix.identity();
    	//// --- student code ---
    	
        return projectionMatrix;
    }
    
    public Matrix4f getViewMatrix(Camera camera) {
    	Vector3f cameraPos = camera.getPosition();
    	Vector3f cameraTarget = camera.getTarget();
    	Vector3f up = camera.getUp();
        viewMatrix.identity();
    	//// --- student code ---

        return viewMatrix;
    }
    
    public Matrix4f getModelMatrix(GameItem gameItem){
        Vector3f rotation = gameItem.getRotation();
        Vector3f position = gameItem.getPosition();
        modelMatrix.identity();
    	//// --- student code ---

        return modelMatrix;
    }

    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(getModelMatrix(gameItem));
    }
}