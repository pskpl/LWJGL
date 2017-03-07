package engine;

// Encapsulates an OpenGL program object. NOT IN USE.
public abstract class Shader
{		
	// OpenGL objects
	private int programObject;
	
	// =======================================================================
	// Properties
	// =======================================================================
	int getProgramObject()
	{
		return programObject;
	}

	// =======================================================================
	// Initialization
	// =======================================================================
	Shader(int programObject)
	{
		this.programObject = programObject;
		initializeUniforms();
	}
	
	// =======================================================================
	// Setting Uniforms
	// =======================================================================
	public abstract void setUniforms(GameObject gameObject);
	
	// =======================================================================
	// Helper Methods
	// =======================================================================
	protected abstract void initializeUniforms();
}
