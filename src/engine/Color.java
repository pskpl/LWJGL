package engine;

import math.Vec4;

/**
 * Encapsulates a color and provides convenience methods for getting colors.
 */
public class Color
{
	public float r;
	public float g;
	public float b;
	public float a;
	
	public Color(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Vec4 vector()
	{
		return new Vec4(r, g, b, a);
	}
	
	// =======================================================================
	// Common Colors
	// =======================================================================
	public static Color white()
	{
		return new Color(1, 1, 1, 1);
	}
	
	public static Color black()
	{
		return new Color(0, 0, 0, 1);
	}
	
	public static Color red()
	{
		return new Color(1, 0, 0, 1);
	}
	
	public static Color green()
	{
		return new Color(0, 1, 0, 1);
	}
	
	public static Color blue()
	{
		return new Color(0, 0, 1, 1);
	}
	
	public static Color none()
	{
		return new Color(0, 0, 0, 0);
	}
}
