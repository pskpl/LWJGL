package engine;

import static org.lwjgl.opengl.GL20.*;
import math.*;

/**
 * An object with a mesh that can be drawn.
 */
public abstract class DrawnObject extends GameObject
{
	protected static final String DEFAULT_VERTEX_SHADER_PATH = 
			"build/classes/data/shaders/Standard.vert";
	protected static final String DEFAULT_FRAGMENT_SHADER_PATH = 
			"build/classes/data/shaders/Phong.frag";
	
	protected Mesh mesh;
	protected Color color;
	protected float shininess;
	
	// uniforms
	protected int programObject;
	protected int projectionMatrixUnf;
	protected int viewMatrixUnf;
	protected int normalViewMatrixUnf;
	protected int diffuseColorUnf;
	protected int modelSpaceLightPosUnf;
	protected int cameraSpaceLightPosUnf;
	protected int lightIntensityUnf;
	protected int ambientIntensityUnf;
	protected int lightAttenUnf;
	protected int shininessUnf;
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public DrawnObject(Vec3 initialPosition, Mesh mesh, Color color,
			float shininess, String vertexShaderPath, String fragmentShaderPath)
	{
		super(initialPosition);
		
		this.mesh = mesh;
		this.color = color;	
		this.shininess = shininess;
		programObject = ShaderLoader.loadShader(vertexShaderPath, 
				  fragmentShaderPath);
		
		initializeUniforms();
	}
	
	public DrawnObject(Vec3 initialPosition, Mesh mesh, Color color, 
						float shininess)
	{
		this(initialPosition, mesh, color, shininess, DEFAULT_VERTEX_SHADER_PATH,
				DEFAULT_FRAGMENT_SHADER_PATH);
	}
	
	public DrawnObject(Vec3 initialPosition, Mesh mesh)
	{
		this(initialPosition, mesh, Color.white(), 1, DEFAULT_VERTEX_SHADER_PATH,
				DEFAULT_FRAGMENT_SHADER_PATH);
	}

	// =======================================================================
	// Game Object Methods
	// =======================================================================
	public void handleClick(int mouseButton)
	{
		// do nothing
	}
	
	public void update()
	{
		// do nothing
	}
	
	public void handleCollision(Vec3 collisionSurface, 
			GameObject otherObject)
	{
		// do nothing
	}
	
	public void draw(Mat4 transform)
	{
		glUseProgram(programObject);
		        
		Mat4 projectionMatrix = Application.mainCamera.getProjectionMatrix();
		glUniformMatrix4(projectionMatrixUnf, false,
				Utility.matrixToBuffer(projectionMatrix));
		
        Mat4 worldToCameraMatrix = Application.mainCamera.getWorldToCameraMatrix();
        Mat4 viewMatrix = Mat4.mul(worldToCameraMatrix, transform);
		glUniformMatrix4(viewMatrixUnf, false,
				Utility.matrixToBuffer(viewMatrix));
		
		// take transposed inverse of view matrix to transform normals
		Mat3 normalViewMatrix3 = new Mat3(viewMatrix);
		normalViewMatrix3 = Glm.transpose(Glm.inverse(normalViewMatrix3));
		Mat4 normalViewMatrix = new Mat4(normalViewMatrix3);
		glUniformMatrix4(normalViewMatrixUnf, false,
				Utility.matrixToBuffer(normalViewMatrix));
		
		glUniform4f(diffuseColorUnf, color.r, color.g, color.b, color.a);
		
		PointLight light = Application.currentScene.getPointLight();
		if (light != null)
		{
			Vec4 worldSpaceLightPos = light.getWorldPosition();
			Vec4 camSpaceLightPos = Mat4.mul(worldToCameraMatrix, worldSpaceLightPos);
			Vec4 modelSpaceLightPos = Mat4.mul(Glm.inverse(viewMatrix), camSpaceLightPos);
			
			glUniform4f(cameraSpaceLightPosUnf, camSpaceLightPos.x, camSpaceLightPos.y,
					camSpaceLightPos.z, camSpaceLightPos.w);
			glUniform4f(modelSpaceLightPosUnf, modelSpaceLightPos.x, modelSpaceLightPos.y, 
					modelSpaceLightPos.z, modelSpaceLightPos.w);
			
			Color lightIntensity = light.getColor();
			glUniform4f(lightIntensityUnf, lightIntensity.r, lightIntensity.g,
							lightIntensity.b, lightIntensity.a);
		}
		
		Color ambientLightIntensity = 
				Application.currentScene.getAmbientLightColor();
		glUniform4f(ambientIntensityUnf, ambientLightIntensity.r,
				ambientLightIntensity.g, ambientLightIntensity.b,
				ambientLightIntensity.a);
		
		glUniform1f(lightAttenUnf, 1);
		glUniform1f(shininessUnf, shininess);
        
        mesh.draw();
        
        glUseProgram(0);
	}
	
	// =======================================================================
	// Helper Methods
	// =======================================================================
	private void initializeUniforms()
	{
		projectionMatrixUnf = glGetUniformLocation(programObject,
									"projectionMatrix");
		viewMatrixUnf = glGetUniformLocation(programObject, "viewMatrix");
		normalViewMatrixUnf = glGetUniformLocation(programObject,
									"normalViewMatrix");
		diffuseColorUnf = glGetUniformLocation(programObject, "diffuseColor");
		modelSpaceLightPosUnf = glGetUniformLocation(programObject, 
				"modelSpaceLightPos");
		cameraSpaceLightPosUnf = glGetUniformLocation(programObject,
				"cameraSpaceLightPos");
		lightIntensityUnf = glGetUniformLocation(programObject, 
											"lightIntensity");
		ambientIntensityUnf = glGetUniformLocation(programObject,
											"ambientIntensity");
		lightAttenUnf = glGetUniformLocation(programObject,
				"lightAttenuation");
		shininessUnf = glGetUniformLocation(programObject,
				"shininess");
	}
}
