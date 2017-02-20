package c2g2.engine.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class OBJLoader {
    public static Mesh loadMesh(String fileName) throws Exception {
    	
    	BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
    	
    	List<Vector3f> verticesList = new ArrayList<>();
    	List<Vector2f> texturesList = new ArrayList<>();
    	List<Vector3f> normalsList = new ArrayList<>();
    	List<Integer> indicesList = new ArrayList<>();
    	float[] positions = null; // vertex positions
        float[] textCoords = null; // texture coordinates of vertices
        float[] norms = null; // use for lighting
        int[] indices = null; // array of indices that decide which vertices are connected to which
        
        // do 2 scan of the file, in case that face declarations are not at the end of the file
        // first scan, store all vertex, texture, and normal 
    	for(String line = br.readLine(); line != null; line = br.readLine())
    	{
    		String[] items = line.split("\\s+");
    		if(items[0].equals("v"))
    			verticesList.add(new Vector3f(Float.parseFloat(items[1]), Float.parseFloat(items[2]), Float.parseFloat(items[3])));
    		else if(items[0].equals("vt"))
    			texturesList.add(new Vector2f(Float.parseFloat(items[1]), Float.parseFloat(items[2])));
    		else if(items[0].equals("vn"))
    			normalsList.add(new Vector3f(Float.parseFloat(items[1]), Float.parseFloat(items[2]), Float.parseFloat(items[3])));
    	}
    	
    	// build position array
    	positions = new float[verticesList.size() * 3];
		for(int i = 0; i < verticesList.size(); i++)
		{
			Vector3f v = verticesList.get(i);
			positions[i * 3] = v.x;
			positions[i * 3 + 1] = v.y;
			positions[i * 3 + 2] = v.z;
		}
		
		// second scan, only scan face declarations, and build texture and normal arrays
		br = new BufferedReader(new FileReader(new File(fileName)));
    	textCoords = new float[verticesList.size() * 2];
		norms = new float[verticesList.size() * 3];
		for(String line = br.readLine(); line != null; line = br.readLine())
        {
        	if(!line.startsWith("f ")) continue;
    		String[] items = line.split("\\s+");
    		for(int i = 1; i <= 3; i++)
    		{
    			if(items[i].contains("/"))
    			{
    				String[] vertexInfo = items[i].split("/");
        			
        			int vertexIdx = Integer.parseInt(vertexInfo[0]) - 1;
        			indicesList.add(vertexIdx);
        			
        			if(!vertexInfo[1].equals(""))
        			{
        				Vector2f texture = texturesList.get(Integer.parseInt(vertexInfo[1]) - 1);
            			textCoords[vertexIdx * 2] = texture.x;
            			textCoords[vertexIdx * 2 + 1] = texture.y;
        			}
        			
        			Vector3f normal = normalsList.get(Integer.parseInt(vertexInfo[2]) - 1);
        			norms[vertexIdx * 3] = normal.x;
        			norms[vertexIdx * 3 + 1] = normal.y;
        			norms[vertexIdx * 3 + 2] = normal.z;
    			}
    			else indicesList.add(Integer.parseInt(items[i]) - 1);
    		}
        }
		
		br.close();
		
		// build indices array
		indices = new int[indicesList.size()];
		for(int i = 0; i < indices.length; i++)
			indices[i] = indicesList.get(i);
		
		/*
    	BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
    	
    	List<Vector3f> verticesList = new ArrayList<>();
    	List<Vector2f> texturesList = new ArrayList<>();
    	List<Vector3f> normalsList = new ArrayList<>();
    	List<Integer> indicesList = new ArrayList<>();
    	float[] positions = null; // vertex positions
        float[] textCoords = null; // texture coordinates of vertices
        float[] norms = null; // use for lighting
        int[] indices = null; // array of indices that decide which vertices are connected to which
        
        String line;
        while(true)
        {
        	line = br.readLine();
        	String[] currentLine = line.split(" ");
        	if(line.startsWith("v "))
        	{
        		Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
        		verticesList.add(vertex);
        	}
        	else if(line.startsWith("vt "))
        	{
        		Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
        		texturesList.add(texture);
        	}
        	else if(line.startsWith("vn "))
        	{
        		Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
        		normalsList.add(normal);
        	}
        	else if(line.startsWith("f "))
        	{
        		textCoords = new float[verticesList.size() * 2];
        		norms = new float[verticesList.size() * 3];
        		break;
        	}
        }
        
        while(line != null)
        {
        	if(!line.startsWith("f "))
        	{
        		line = br.readLine();
        		continue;
        	}
        	String[] currentLine = line.split(" ");
        	String[] vertex1 = currentLine[1].split("/");
        	String[] vertex2 = currentLine[2].split("/");
        	String[] vertex3 = currentLine[3].split("/");
        	
        	processVertex(vertex1, indicesList, texturesList, normalsList, textCoords, norms);
        	processVertex(vertex2, indicesList, texturesList, normalsList, textCoords, norms);
        	processVertex(vertex3, indicesList, texturesList, normalsList, textCoords, norms);
        	line = br.readLine();
        }
        br.close();
    	
        positions = new float[verticesList.size() * 3];
        indices = new int[indicesList.size()];
        
        int vertexPointer = 0;
        for(Vector3f vertex : verticesList)
        {
        	positions[vertexPointer++] = vertex.x;
        	positions[vertexPointer++] = vertex.y;
        	positions[vertexPointer++] = vertex.z;
        }
        
        for(int i = 0; i < indicesList.size(); i++)
        	indices[i] = indicesList.get(i);
        */
        
        return new Mesh(positions, textCoords, norms, indices);
    }
    /*
    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray)
    {
    	int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
    	indices.add(currentVertexPointer);
    	if(!vertexData[1].equals(""))
    	{
    		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        	textureArray[currentVertexPointer * 2] = currentTex.x;
        	textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
    	}
    	Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
    	normalsArray[currentVertexPointer * 3] = currentNorm.x;
    	normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
    	normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }
	*/
}
