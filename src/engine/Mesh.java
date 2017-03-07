package engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Encapsulates vertex data defining a number of triangles.
 */
public class Mesh
{
	private final float[] vertexData;
	private final short[] indexData;
	
	// openGL objects
	private int vertexBufferObject;
	private int indexBufferObject;
    private int vertexArrayObject;
	
	// =======================================================================
	// Properties
	// =======================================================================
	public int getNumberOfVertices()
	{
		return indexData.length;
	}
	
	public int getNumberOfTrianges()
	{
		return getNumberOfVertices() / 3;
	}
	
	// =======================================================================
	// Initialization
	// =======================================================================
	Mesh(float[] vertexData, short[] indexData)
	{
		this.vertexData = vertexData;
		this.indexData = indexData;
		
		initializeBuffers();
		initializeVertexArrayObject();
	}
	
	// =======================================================================
	// Drawing
	// =======================================================================
	public void draw()
	{
        glBindVertexArray(vertexArrayObject);

        glDrawElements(GL_TRIANGLES, getNumberOfVertices(), 
        		GL_UNSIGNED_SHORT, 0);
        
        glBindVertexArray(0);
	}
	
	// =======================================================================
	// Helper Methods
	// =======================================================================
	private void initializeBuffers()
	{
		// format vertex data so it can be passed to openGL
		FloatBuffer vertexBuffer = 
				Utility.floatArrayToBuffer(vertexData);
		    
	    // create a buffer object and store the vertex data there
		vertexBufferObject = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		// format index data so it can be passed to openGL
		ShortBuffer indexDataBuffer = 
				Utility.shortArrayToBuffer(indexData);
		
		// create a buffer object and store the index data there
		indexBufferObject = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexDataBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	private void initializeVertexArrayObject()
	{
        vertexArrayObject = glGenVertexArrays();
        glBindVertexArray(vertexArrayObject);
        
        int normalDataOffset = Application.FLOAT_SIZE * 
        		vertexData.length / 3;
        int baryDataOffset = normalDataOffset * 2;
        
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, normalDataOffset);
        glVertexAttribPointer(2, 4, GL_FLOAT, false, 0, baryDataOffset);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);

        glBindVertexArray(0);
	}
}
