package application;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import engine.*;
import math.*;

/**
 * Delegate class that specifies initial parameters of the application.
 */
public class ApplicationInitializer implements IApplicationInitializer
{
	private final int WINDOW_WIDTH = 960;
	private final int WINDOW_HEIGHT = 540;
	private final float FOV = 90;
	private final String TITLE = "PA 3";
	private final Vec4 CLEAR_COLOR = new Vec4(0, 0, 0, 1);
	
	private static final String STANDARD_VERTEX_SHADER_PATH = 
			"build/classes/data/shaders/Standard.vert";
	private static final String WIRE_VERTEX_SHADER_PATH =
			"build/classes/data/shaders/Wire.vert";
	private static final String PHONG_FRAG_SHADER_PATH = 
			"build/classes/data/shaders/Phong.frag";
	private static final String BLINN_FRAG_SHADER_PATH =
			"build/classes/data/shaders/Blinn.frag";
	private static final String WIRE_FRAG_SHADER_PATH =
			"build/classes/data/shaders/Wire.frag";
	
	public Window initWindow() throws LWJGLException
	{
		return new Window(WINDOW_WIDTH, WINDOW_HEIGHT, TITLE, CLEAR_COLOR);
	}
	
	public Camera initCamera()
	{
		SphericalControlScheme controlScheme = 
				new SphericalControlScheme(Keyboard.KEY_A, Keyboard.KEY_D,
						Keyboard.KEY_S, Keyboard.KEY_W, Keyboard.KEY_X,
						Keyboard.KEY_Z);
		return new Camera(FOV, 1.0f, 5.0f, new Vec3(0, 0, -2.5f), 
							new Vec3(2.5f, 90, 90), controlScheme);
	}
	
	public Scene initScene()
	{
		Scene scene = new Scene();
				
		Sphere phongSphere = new Sphere(new Vec3(0, 0, -2.5f), 1, 
				STANDARD_VERTEX_SHADER_PATH, PHONG_FRAG_SHADER_PATH);
		Sphere blinnSphere = new Sphere(new Vec3(0, 0, -2.5f), 1,
				STANDARD_VERTEX_SHADER_PATH, BLINN_FRAG_SHADER_PATH);
		Sphere wireSphere = new Sphere(new Vec3(0, 0, -2.5f), 1,
				WIRE_VERTEX_SHADER_PATH, WIRE_FRAG_SHADER_PATH);
		scene.loadGameObject(phongSphere);
		scene.loadGameObject(blinnSphere);
		scene.loadGameObject(wireSphere);
		
		ModelToggler toggler = new ModelToggler(phongSphere, blinnSphere, 
												wireSphere);
		scene.loadGameObject(toggler);
		
		Cube cube_left = new Cube(new Vec3(-2f, 0, -2.5f), 0.5f);
		Cube cube_right = new Cube(new Vec3(2f, 0, -2.5f), 0.5f);

		scene.loadGameObject(cube_left);
		scene.loadGameObject(cube_right);
		
		SphericalControlScheme controlScheme =
				new SphericalControlScheme(Keyboard.KEY_J, Keyboard.KEY_L,
						Keyboard.KEY_K, Keyboard.KEY_I, Keyboard.KEY_M,
						Keyboard.KEY_N);
		PointLight light = new PointLight(new Vec3(0, 0, -2.5f),
				new Vec3(2.5f, 45, 45), new Color(0.8f, 0.8f, 0.8f, 1),
				controlScheme);
		scene.loadPointLight(light);
		
		return scene;
	}
}
