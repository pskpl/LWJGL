package c2g2.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

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
    	// compute cos(angle in randians)
    	float cos = (float)Math.cos(Math.toRadians(angle));
    	float sin = (float)Math.sin(Math.toRadians(angle));
    	for(int i=0; i< pos.length/3; i++){
    		// build vector to be rotated
    		Vector3f v = new Vector3f(pos[i * 3], pos[i * 3 + 1], pos[i * 3 + 2]);
    		// compute cross product
    		Vector3f crossProduct = new Vector3f();
    		axis.cross(v, crossProduct);
    		// compute dot product
    		float dotProduct = axis.dot(v);
    		// use Rodrigues formula
    		pos[i * 3] = v.x * cos + crossProduct.x * sin + axis.x * dotProduct * (1 - cos);
    		pos[i * 3 + 1] = v.y * cos + crossProduct.y * sin + axis.y * dotProduct * (1 - cos);
    		pos[i * 3 + 2] = v.z * cos + crossProduct.z * sin + axis.z * dotProduct * (1 - cos);
    	}
    	setMesh(pos, textco, norms, inds);
    }
    
    public void reflectMesh(Vector3f p, Vector3f n){
    	cleanUp();
    	//reset position of each point
    	//Do not change textco, norms, inds
    	for(int i=0; i< pos.length/3; i++){
    		// compute how many "steps" we need to move current point onto the plane along plane's normal direction
    		Vector3f v = new Vector3f(pos[i * 3], pos[i * 3 + 1], pos[i * 3 + 2]);
    		Vector3f v_p = new Vector3f(v.x - p.x, v.y - p.y, v.z - p.z);
    		float t = v_p.dot(n);
//    		float t = (n.dot(p) - n.dot(v)) / (n.dot(n));
//    		float t = ((n.x * p.x + n.y * p.y + n.z * p.z) - (n.x * v.x + n.y * v.y + n.z * v.z)) / (n.x * n.x + n.y * n.y + n.z * n.z);
    		// move current point twice the distance along normal direction of the plane
    		pos[i * 3] -= 2 * t * n.x;
    		pos[i * 3 + 1] -= 2 * t * n.y;
    		pos[i * 3 + 2] -= 2 * t * n.z;
    	}
    	setMesh(pos, textco, norms, inds);
    }
}
