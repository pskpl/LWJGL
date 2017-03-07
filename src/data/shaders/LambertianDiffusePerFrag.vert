#version 330

layout (location = 0) in vec4 position;
layout (location = 1) in vec4 normal;

out vec4 modelSpacePosition;
out vec4 vertexNormal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main()
{
	gl_Position = projectionMatrix * (viewMatrix * position);

	modelSpacePosition = position;
	vertexNormal = normal;
}
