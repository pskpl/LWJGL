package engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Static utility class for loading and compiling shaders.
 */
public class ShaderLoader
{
	private static HashMap<String, Integer> loadedProgramObjects =
			new HashMap<String, Integer>();
	
	// =======================================================================
	// Loading Shaders
	// =======================================================================
	public static int loadShader(String vertexShaderPath, 
			String fragmentShaderPath)
	{
		String key = vertexShaderPath.concat(fragmentShaderPath);
		if (loadedProgramObjects.containsKey(key))
			return loadedProgramObjects.get(key);
		
		ArrayList<Integer> shaderList = new ArrayList<Integer>();
		
		int vertexShader = createShader(GL_VERTEX_SHADER, vertexShaderPath);
		int fragShader = createShader(GL_FRAGMENT_SHADER, fragmentShaderPath);
		
		shaderList.add(vertexShader);
		shaderList.add(fragShader);
		
		int programObject = createProgram(shaderList);
		
		loadedProgramObjects.put(key, programObject);
		return programObject;
	}
	
	// reads a shader from a file and compiles it
	private static int createShader(int shaderType, String filename)
	{
		String shaderText = loadShaderText(filename);
		return compileShader(shaderType, shaderText);
	}
	
	// creates a program by linking the passed shaders
	private static int createProgram(ArrayList<Integer> shaders)
	{
		int programObject = glCreateProgram();
		
		linkProgram(programObject, shaders);
		
		return programObject;
	}
	
	// =======================================================================
	// File IO
	// =======================================================================
	private static String loadShaderText(String filename)
	{
		StringBuilder text = new StringBuilder();
		Path path = Paths.get(filename);
		
		try
		{
			BufferedReader reader = Files.newBufferedReader(path, 
					Charset.defaultCharset());
			
			String line;
			while ((line = reader.readLine()) != null)
				text.append(line).append("\n");
		}
		catch (IOException exception)
		{
			Debug.logError(ShaderLoader.class, 
							"Exception encountered: " + exception);
		}
		
		return text.toString();
	}
	
	// =======================================================================
	// Shader Compilation
	// =======================================================================
	// compiles a shader
	private static int compileShader(int shaderType, String shaderText)
	{
		int shaderObject = glCreateShader(shaderType);
		glShaderSource(shaderObject, shaderText); // set shader source
		
		glCompileShader(shaderObject);
		
		// error checking
		int status = glGetShaderi(shaderObject, GL_COMPILE_STATUS);
        if (status == GL_FALSE) 
        	printCompileError(shaderObject, shaderType);
		
		return shaderObject;
	}
	
	// links shaders into a program
	private static int linkProgram(int programObject, 
								   ArrayList<Integer> shaderList)
	{		
		// attach shaders
		for (int shaderObject : shaderList)
			glAttachShader(programObject, shaderObject);
		
		glLinkProgram(programObject);
		
		// error checking
		int status = glGetProgrami(programObject, GL_LINK_STATUS);
		if (status == GL_FALSE)
			printLinkError(programObject);

		// detach shaders which we no longer need
		for (int shaderObject : shaderList)
			glDetachShader(programObject, shaderObject);
		
		return programObject;
	}
	
	// =======================================================================
	// Printing Errors
	// =======================================================================
	private static void printCompileError(int shaderObject, int shaderType)
	{
		int infoLogLength = glGetShaderi(shaderObject, GL_INFO_LOG_LENGTH );

        String infoLog = glGetShaderInfoLog(shaderObject, infoLogLength);

        String shaderTypeStr = null;
        switch (shaderType) {
            case GL_VERTEX_SHADER:
                shaderTypeStr = "vertex";
                break;
            case GL_GEOMETRY_SHADER:
                shaderTypeStr = "geometry";
                break;
            case GL_FRAGMENT_SHADER:
                shaderTypeStr = "fragment";
                break;
        }

        Debug.logError(ShaderLoader.class, 
        		"Compile failure in " + shaderTypeStr + "s shader:"
        		+ "\n" + infoLog);
	}
	
	private static void printLinkError(int programObject)
	{
		int infoLogLength = glGetProgrami(programObject, GL_INFO_LOG_LENGTH);
        String infoLog = glGetProgramInfoLog(programObject, infoLogLength);
        Debug.logError(ShaderLoader.class, "Linker failure: " + infoLog);
	}
}
