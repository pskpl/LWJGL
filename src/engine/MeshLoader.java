package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Static utility class for parsing .obj files into Mesh objects.
 * 
 * Only works for files containing triangle data.
 */
public class MeshLoader
{
	private static HashMap<String, Mesh> loadedMeshes = 
			new HashMap<String, Mesh>();
	
	// =======================================================================
	// Helper Class
	// =======================================================================
	private static class FileData
	{
		public ArrayList<Float> fileVertexDataList; // of Vec4
		public ArrayList<Float> fileNormalDataList; // of Vec4
		
		private ArrayList<Float> vertexDataList;	// of Vec4
		private ArrayList<Float> normalDataList;	// of Vec4
		private ArrayList<Float> baryDataList;		// of Vec4
		private ArrayList<Short> indexDataList;
		private HashMap<String, Short> indexMap;
		private short vertexCount;
		
		public FileData()
		{
			fileVertexDataList = new ArrayList<Float>();
			fileNormalDataList = new ArrayList<Float>();
			
			vertexDataList = new ArrayList<Float>();
			normalDataList = new ArrayList<Float>();
			baryDataList = new ArrayList<Float>();
			indexDataList = new ArrayList<Short>();
			indexMap = new HashMap<String, Short>();
		}
		
		public void addVertex(String key, int vertexIndex, int normalIndex,
				int triangleVertexIndex)
		{
			if (indexMap.containsKey(key))
			{
				indexDataList.add(indexMap.get(key));
			}
			else
			{
				addVertexData(vertexIndex, normalIndex, triangleVertexIndex);
				indexMap.put(key, vertexCount);
				indexDataList.add(vertexCount);
				vertexCount++;
			}
		}
				
		public Mesh convertToMesh()
		{
			float[] vertexData = new float[vertexDataList.size() * 3];
			short[] indexData = new short[indexDataList.size()];
			
			for (int i = 0; i < vertexData.length; i++)
			{
				if (i < vertexDataList.size())
					vertexData[i] = vertexDataList.get(i);
				else if (i < vertexDataList.size() * 2)
					vertexData[i] = normalDataList.get(i % vertexDataList.size());
				else
					vertexData[i] = baryDataList.get(i % vertexDataList.size());
			}
			
			for (int i = 0; i < indexData.length; i++)
				indexData[i] = indexDataList.get(i);
			
			return new Mesh(vertexData, indexData);
		}
		
		private void addVertexData(int vertexIndex, int normalIndex,
				int triangleVertexIndex)
		{
			int adjVertexIndex = (vertexIndex - 1) * 4;
			int adjNormalIndex = (normalIndex - 1) * 4;
			
			for (int i = 0; i < 4; i++)
			{
				vertexDataList.add(fileVertexDataList.get(adjVertexIndex++));
				normalDataList.add(fileNormalDataList.get(adjNormalIndex++));
				
				if (i == triangleVertexIndex)
					baryDataList.add(1f);
				else
					baryDataList.add(0f);
			}			
		}
	}
	
	// =======================================================================
	// Public Interface
	// =======================================================================
	// loads the mesh or returns it if it has already been loaded
	public static Mesh loadMesh(String filename)
	{
		if (loadedMeshes.containsKey(filename))
			return loadedMeshes.get(filename);
		
		FileData data = new FileData();
		parseOBJFile(filename, data);
		
		Mesh newMesh = data.convertToMesh();
		
		loadedMeshes.put(filename, newMesh);
		return newMesh;
	}
	
	// =======================================================================
	// File IO and Parsing
	// =======================================================================
	private static void parseOBJFile(String filename, FileData data)
	{
		Path path = Paths.get(filename);
		
		try
		{
			BufferedReader reader = Files.newBufferedReader(path, 
					Charset.defaultCharset());
			
			String line;
			while ((line = reader.readLine()) != null)
				parseLine(line, data);
				
		}
		catch (IOException exception)
		{
			Debug.logError(MeshLoader.class, 
							"Exception encountered: " + exception);
		}
	}	
	
	private static void parseLine(String line, FileData data)
	{
		String[] tokens = line.split(" ");
		
		if (tokens.length == 0)
			return;
		
		if (tokens[0].equalsIgnoreCase("v"))
			parseVertexLine(tokens, data);
		
		if (tokens[0].equalsIgnoreCase("vn"))
			parseNormalLine(tokens, data);
		
		if (tokens[0].equalsIgnoreCase("f"))
			parseFaceLine(tokens, data);
	}
	
	private static void parseVertexLine(String[] tokens, FileData data)
	{
		for (int i = 1; i < 4; i++)
			data.fileVertexDataList.add(Float.valueOf(tokens[i]));
		
		data.fileVertexDataList.add(1f);
	}
	
	private static void parseNormalLine(String[] tokens, FileData data)
	{
		for (int i = 1; i < 4; i++)
			data.fileNormalDataList.add(Float.valueOf(tokens[i]));
		
		data.fileNormalDataList.add(0f);
	}
	
	private static void parseFaceLine(String[] tokens, FileData data)
	{
		if (tokens.length > 4)
		{
			Debug.logError(MeshLoader.class,
							"Only triangle primitives can be loaded.");
			return;
		}
		
		for (int i = 1; i < 4; i++)
		{
			String[] indices = tokens[i].split("//");
			data.addVertex(tokens[i].concat(Integer.toString((i - 1))), 
						   Integer.valueOf(indices[0]), 
						   Integer.valueOf(indices[1]),
						   i - 1);
		}
	}
}
