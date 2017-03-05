package c2g2.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
        
        // compute aspect ratio
        float aspectRatio = width / height;
        // compute cot(fov / 2)
        float cotHalfFov = 1 / (float)Math.tan(fov / 2);
        Vector4f col0 = new Vector4f(cotHalfFov / aspectRatio, 0f, 0f, 0f);
        Vector4f col1 = new Vector4f(0f, cotHalfFov, 0f, 0f);
        Vector4f col2 = new Vector4f(0f, 0f, (zFar + zNear) / (zNear - zFar), -1f);
        Vector4f col3 = new Vector4f(0f, 0f, 2 * zFar * zNear / (zNear - zFar), 0f);
        
        projectionMatrix.set(new Matrix4f(col0, col1, col2, col3));
        return projectionMatrix;
    }
    
    public Matrix4f getViewMatrix(Camera camera) {
    	Vector3f cameraPos = camera.getPosition();
    	Vector3f cameraTarget = camera.getTarget();
    	Vector3f up = camera.getUp();
        viewMatrix.identity();
        
        // compute coordinates of z axis of camera coordinate frame
        float zx = cameraPos.x - cameraTarget.x, zy = cameraPos.y - cameraTarget.y, zz = cameraPos.z - cameraTarget.z;
        // normalize z vector
        float znorm = (float)Math.sqrt(zx * zx + zy * zy + zz * zz);
        zx /= znorm;
        zy /= znorm;
        zz /= znorm;
        
        // compute coordinates of x axis of camera coordinate frame, i.e., the cross product of up and z
        float xx = up.y * zz - up.z * zy, xy = up.z * zx - up.x * zz, xz = up.x * zy - up.y * zx;
        // normalize x vector
        float xnorm = (float) Math.sqrt(xx * xx + xy * xy + xz * xz);
        xx /= xnorm;
        xy /= xnorm;
        xz /= xnorm;
        
        // compute coordinates of y axis of camera coordinate frame, i.e., the cross product of z and x
        float yx = zy * xz - zz * xy, yy = zz * xx - zx * xz, yz = zx * xy - zy * xx;

        // build model matrix and compute inverse
        Vector4f col0 = new Vector4f(xx, xy, xz, -(xx * cameraPos.x + xy * cameraPos.y + xz * cameraPos.z));
        Vector4f col1 = new Vector4f(yx, yy, yz, -(yx * cameraPos.x + yy * cameraPos.y + yz * cameraPos.z));
        Vector4f col2 = new Vector4f(zx, zy, zz, -(zx * cameraPos.x + zy * cameraPos.y + zz * cameraPos.z));
        Vector4f col3 = new Vector4f(0f, 0f, 0f, 1f);
        
        viewMatrix.set(new Matrix4f(col0, col1, col2, col3));
        return viewMatrix;
    }
    
    public Matrix4f getModelMatrix(GameItem gameItem){
        Vector3f rotation = gameItem.getRotation();
        Vector3f position = gameItem.getPosition();
        float scaling = gameItem.getScale();
        modelMatrix.identity();
        
        modelMatrix.translate(position);
        modelMatrix.rotateX((float)Math.toRadians(rotation.x));
        modelMatrix.rotateY((float)Math.toRadians(rotation.y));
        modelMatrix.rotateZ((float)Math.toRadians(rotation.z));
        modelMatrix.scale(scaling);
     
        return modelMatrix;
    }

    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(getModelMatrix(gameItem));
    }
}