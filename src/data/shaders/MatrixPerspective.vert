#version 330

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 normalIn;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec4 normal;

void main()
{
	vec4 cameraPos = viewMatrix * position;

	gl_Position = projectionMatrix * cameraPos;
	normal = normalIn;
}
