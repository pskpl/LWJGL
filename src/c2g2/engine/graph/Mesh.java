package c2g2.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

    private int vaoId;

    private List<Integer> vboIdList;

    private int vertexCount;

    private Material material;
    
    private float[] pos;
    private float[] textco;
    private float[] norms;
    private int[] inds;
    
    
    public Mesh(){
    	this(new float[]{-0.5f,-0.5f,-0.5f,-0.5f,-0.5f,0.5f,-0.5f,0.5f,-0.5f,-0.5f,0.5f,0.5f,0.5f,-0.5f,-0.5f,0.5f,-0.5f,0.5f,0.5f,0.5f,-0.5f,0.5f,0.5f,0.5f}, 
    			new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f}, 
    			new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f}, 
    			new int[]{0,6,4,0,2,6,0,3,2,0,1,3,2,7,6,2,3,7,4,6,7,4,7,5,0,4,5,0,5,1,1,5,7,1,7,3});
    }
    
    public void setMesh(float[] positions, float[] textCoords, float[] normals, int[] indices){
    	pos = positions;
    	textco = textCoords;
    	norms = normals;
    	inds = indices;
    	FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indicesBuffer = null;
        System.out.println("create mesh:");
        System.out.println("v: "+positions.length+" t: "+textCoords.length+" n: "+normals.length+" idx: "+indices.length);
        try {
            vertexCount = indices.length;
            vboIdList = new ArrayList<Integer>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
    	setMesh(positions, textCoords, normals, indices);        
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void render() {


        // Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
    
    public void scaleMesh(float sx, float sy, float sz){
    	cleanUp(); //clean up buffer
    	//Reset position of each point
    	//Do not change textco, norms, inds
    	//student code 
    	for (int i = 0; i < pos.length/3; i++) {
    		pos[i * 3] *= sx;
    		pos[i * 3 + 1] *= sy;
    		pos[i * 3 + 2] *= sz;
		}   	
    	setMesh(pos, textco, norms, inds);
    }
    
    public void translateMesh(Vector3f trans){
    	cleanUp();
    	//reset position of each point
    	//Do not change textco, norms, inds
    	//student code
    	for(int i=0; i< pos.length/3; i++){
    		pos[i * 3] += trans.x;
    		pos[i * 3 + 1] += trans.y;
    		pos[i * 3 + 2] += trans.z;
    	}
    	setMesh(pos, textco, norms, inds);
    }
    
    public void rotateMesh(Vector3f axis, float angle){
    	cleanUp();
    	//reset position of each point
    	//Do not change textco, norms, inds
    	//student code
    	for(int i=0; i< pos.length/3; i++){
    		/*
    		// build vector to be rotated
    		Vector3f v = new Vector3f(pos[i * 3], pos[i * 3 + 1], pos[i * 3 + 2]);
    		// compute cross product
    		Vector3f crossProduct = new Vector3f();
    		axis.cross(v, crossProduct);
    		// compute dot product
    		float dotProduct = axis.dot(v);
    		// compute cos(angle in randians)
    		float cosAngle = (float) Math.cos(Math.toRadians(angle));
    		// compute sin(angle in randians)
    		float sinAngle = (float) Math.sin(Math.toRadians(angle));
    		// use Rodrigues formula
    		pos[i * 3] = v.x * cosAngle + crossProduct.x * sinAngle + axis.x * dotProduct * (1 - cosAngle);
    		pos[i * 3 + 1] *= v.y * cosAngle + crossProduct.y * sinAngle + axis.y * dotProduct * (1 - cosAngle);
    		pos[i * 3 + 2] *= v.z * cosAngle + crossProduct.z * sinAngle + axis.z * dotProduct * (1 - cosAngle);
    		*/
    		angle = (float)Math.toRadians(angle);
    		float sin = (float)Math.sin(angle);
            float cos = (float)Math.cos(angle);
            float t = 1 - cos;
            
            Matrix3f R = new Matrix3f(cos,           -axis.z * sin, axis.y * sin, 
            						  axis.z * sin,  cos,           -axis.x * sin, 
            						  -axis.y * sin,                axis.x * sin, cos);
            Matrix3f temp = new Matrix3f(axis.x * axis.x * t, axis.x * axis.y * t, axis.x * axis.z * t, 
            							 axis.x * axis.y * t, axis.y * axis.y * t, axis.y * axis.z * t, 
            							 axis.x * axis.z * t, axis.y * axis.z * t, axis.z * axis.z * t);
            R.add(temp);
            
            Vector3f v = new Vector3f(pos[i * 3], pos[i * 3 + 1], pos[i * 3 + 2]);
            pos[i * 3] = R.m00 * v.x + R.m01 * v.y + R.m02 * v.z;
            pos[i * 3 + 1] = R.m10 * v.x + R.m11 * v.y + R.m12 * v.z;
            pos[i * 3 + 2] = R.m20 * v.x + R.m21 * v.y + R.m22 * v.z;
    		/*
    		Vector3f v = new Vector3f(pos[i * 3], pos[i * 3 + 1], pos[i * 3 + 2]);
    		angle = (float)Math.toRadians(angle);
    		float sin = (float)Math.sin(angle * 0.5);
            float cos = (float)Math.cos(angle * 0.5);
    		v.rotate(new Quaternionf(axis.x * sin, axis.y * sin, axis.z * sin, cos));
    		
    		pos[i * 3] = v.x;
            pos[i * 3 + 1] = v.y;
            pos[i * 3 + 2] = v.z;
            */
    		/*
    		angle = (float)Math.toRadians(angle);
    		
    		float q0x = v.x, q0y = v.y, q0z = v.z;
    		
            float sin = (float)Math.sin(angle * 0.5);
            float cos = (float)Math.cos(angle * 0.5);
            float q1x = axis.x * sin, q1y = axis.y * sin, q1z = axis.z * sin, q1w = cos;
            float scale = 1.0f / (q1x * q1x + q1y * q1y + q1z * q1z);
//            float scale = 1f;
            
            float q2x =  q1w * q0x + q1y * q0z - q1z * q0y;
            float q2y =  q1w * q0y - q1x * q0z + q1z * q0x;
            float q2z =  q1w * q0z + q1x * q0y - q1y * q0x;
            float q2w = -q1x * q0x - q1y * q0y - q1z * q0z;
            pos[i * 3] = (-q2w * q1x + q2x * q1w - q2y * q1z + q2z * q1y) * scale;
            pos[i * 3 + 1] = (-q2w * q1y + q2x * q1z + q2y * q1w - q2z * q1x) * scale;
            pos[i * 3 + 2] = (-q2w * q1z - q2x * q1y + q2y * q1x + q2z * q1w) * scale;
            */
    	}
    	setMesh(pos, textco, norms, inds);
    }
    
    public void reflectMesh(Vector3f p, Vector3f n){
    	cleanUp();
    	//reset position of each point
    	//Do not change textco, norms, inds
    	//student code
    	for(int i=0; i< pos.length/3; i++){
    		// compute how many "steps" do we need to move current point onto the plane along plane's normal direction
    		Vector3f v = new Vector3f(pos[i * 3], pos[i * 3 + 1], pos[i * 3 + 2]);
    		float t = (n.dot(p) - n.dot(v)) / (n.x * n.x + n.y * n.y + n.z * n.z);
    		// move current point twice the distance along normal direction of the plane
    		pos[i * 3] += 2 * t * n.x;
    		pos[i * 3 + 1] += 2 * t * n.y;
    		pos[i * 3 + 2] += 2 * t * n.z;
    	}
    	setMesh(pos, textco, norms, inds);
    }
}
