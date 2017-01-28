import org.lwjgl.glfw.*;

public class Main
{

	public static void main(String[] args)
	{
		if(!GLFW.glfwInit())
		{
			throw new IllegalStateException("Fail to init glfw");
		}

		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		long window = GLFW.glfwCreateWindow(640, 480, "fuck", 0, 0);
		if(window == 0)
		{
			throw new IllegalStateException("fail");
		}
		
		GLFWVidMode vm = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, 0, 0);
		GLFW.glfwShowWindow(window);
		while(!GLFW.glfwWindowShouldClose(window))
		{
			GLFW.glfwPollEvents();
		}
	}

}
