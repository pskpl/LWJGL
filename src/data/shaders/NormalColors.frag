#version 330

uniform vec4 color;

in vec4 normal;
out vec4 outputColor;

void main()
{
	vec3 temp = normal.xyz * color.xyz;

	if (temp.x < 0)
		temp.x = 0.5f;
	else if (temp.y < 0)
		temp.y = 0.5f;
	else if (temp.z < 0)
		temp.z = 0.5f;

	outputColor = vec4(temp, 1.0);
}
