package c2g2.game;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

import java.awt.HeadlessException;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import ar.com.hjg.pngj.*;

import c2g2.engine.GameItem;
import c2g2.engine.Window;
import c2g2.engine.graph.Camera;
import c2g2.engine.graph.DirectionalLight;
import c2g2.engine.graph.Mesh;
import c2g2.engine.graph.PointLight;
import c2g2.engine.graph.ShaderProgram;
import c2g2.engine.graph.Transformation;

public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Transformation transformation;

    private ShaderProgram shaderProgram;

    private final float specularPower;

    public Renderer() {
        transformation = new Transformation();
        specularPower = 10f;
    }

    public void init(Window window) throws Exception {
        // Create shader
        shaderProgram = new ShaderProgram();
//        shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/fragment.fs"))));
        shaderProgram.link();
        
        // Create uniforms for modelView and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        // Create uniform for material
        shaderProgram.createMaterialUniform("material");
        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, GameItem[] gameItems, Vector3f ambientLight,
        PointLight pointLight, DirectionalLight directionalLight) {
        
        clear();

        if ( window.isResized() ) {
        	System.out.println("isresized");
//            glViewport(0, 0, window.getWidth(), window.getHeight());
//        	int side = window.getWidth();
//        	if(window.getHeight()>side){
//        		side = window.getHeight();
//        	}
//        	glViewport((window.getWidth()-side)/2, (window.getHeight()-side)/2, side, side);
            window.setResized(false);
        }
        glViewport(0, 0, window.getWidth()*2, window.getHeight()*2);
//        glViewport(0, 0, window.getWidth(), window.getHeight());
        shaderProgram.bind();
        
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // Update Light Uniforms
        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);
        // Get a copy of the point light object and transform its position to view coordinates
        PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        shaderProgram.setUniform("pointLight", currPointLight);
        
        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        shaderProgram.setUniform("directionalLight", currDirLight);
        
        shaderProgram.setUniform("texture_sampler", 0);
        // Render each gameItem
        for(GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);            
            // Render the mesh for this game item
            shaderProgram.setUniform("material", mesh.getMaterial());
            mesh.render();
        }

        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
    
    private static int imgcount=0;
    

    public void writePNG(Window window) throws HeadlessException{
    	glPixelStorei(GL_PACK_ALIGNMENT, 1);
    	glReadBuffer(GL_FRONT);
    	
		int width = window.getWidth();
		int height= window.getHeight();
    	//NOTE: if your display *is* a retina display, please uncomment the following two lines.
    	width = window.getWidth()*2;
    	height= window.getHeight()*2;
    	

		int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		ImageInfo imi = new ImageInfo(width, height, 8, false);
        PngWriter png = new PngWriter(new File("screenshot"+imgcount+".png"), imi , true);
        
        ImageLineInt iline = new ImageLineInt(imi);        
		for(int row = 0; row < imi.rows; row++){
	        for (int col = 0; col < imi.cols; col++) { // this line will be written to all rows
				int i = (col + (width * (imi.rows-row-1))) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
	            ImageLineHelper.setPixelRGB8(iline, col, r, g, b); 
	        }
	        png.writeRow(iline);
		}
        png.end();
    	imgcount=imgcount+1;
    }
}
