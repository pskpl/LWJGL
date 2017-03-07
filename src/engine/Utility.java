package engine;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import math.*;

public class Utility
{
	public static FloatBuffer matrixToBuffer(Mat4 matrix)
	{
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
		matrix.fillBuffer(floatBuffer);
		floatBuffer.flip();
		return floatBuffer;
	}
	
	public static FloatBuffer floatArrayToBuffer(float[] floatArray)
	{
		FloatBuffer buffer = 
				BufferUtils.createFloatBuffer(floatArray.length);
	    buffer.put(floatArray);
	    buffer.flip();
	    return buffer;
	}
	
	public static ShortBuffer shortArrayToBuffer(short[] shortArray)
	{
		ShortBuffer buffer =
				BufferUtils.createShortBuffer(shortArray.length);
		buffer.put(shortArray);
		buffer.flip();
		return buffer;
	}
	
	public static float clamp(float num, float min, float max)
	{
		float newNum = num;
		
		if (newNum > max)
			newNum = max;
		if (newNum < min)
			newNum = min;
		
		return newNum;
	}
	
	public static Vec3 reflect(Vec3 vector, Vec3 surfaceNormal)
	{
		Vec3 projection = surfaceNormal.scale(Glm.dot(vector, surfaceNormal));
		return vector.sub(projection.scale(2));
	}
	
	// expects <r, theta, phi>, where theta is the azimuth (from X) and phi is
	// the polar angle (from Z), expressed in degrees
	public static Vec3 sphericalToEuclidean(Vec3 origin, Vec3 sphericalCoords)
	{
		float r = sphericalCoords.x;
		float theta = (float) Math.toRadians(sphericalCoords.y);
		float phi = (float) Math.toRadians(sphericalCoords.z);
		
		float sinTheta = (float) Math.sin(theta);
		float cosTheta = (float) Math.cos(theta);
		float sinPhi = (float) Math.sin(phi);
		float cosPhi = (float) Math.cos(phi);
		
		Vec3 direction = new Vec3(sinPhi * cosTheta, cosPhi, sinPhi * sinTheta);
		return direction.scale(r).add(origin);
	}
}
