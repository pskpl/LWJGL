package engine;

import java.util.HashMap;
import org.lwjgl.input.Keyboard;

/**
 * Encapsulates a particular set of control-key bindings for positioning a
 * camera.
 */
public class SphericalControlScheme
{	
	public enum Control { ROT_THETA, UNROT_THETA, ROT_PHI, UNROT_PHI,
							IN, OUT }
	
	private HashMap<Control, Integer> controlMap;
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public SphericalControlScheme(int rotTheta, int unrotTheta, int rotPhi,
			int unrotPhi, int in, int out)
	{
		controlMap = new HashMap<Control, Integer>();
		controlMap.put(Control.ROT_THETA, rotTheta);
		controlMap.put(Control.UNROT_THETA, unrotTheta);
		controlMap.put(Control.ROT_PHI, rotPhi);
		controlMap.put(Control.UNROT_PHI, unrotPhi);
		controlMap.put(Control.IN, in);
		controlMap.put(Control.OUT, out);
	}
	
	// =======================================================================
	// Querying
	// =======================================================================
	public boolean isControlDown(Control control)
	{
		int key = controlMap.get(control);
		return key != Keyboard.CHAR_NONE && 
				Input.isKeyDown(controlMap.get(control));
	}
}