#version 330

// VERTEX SHADER
// Sets clip position of vertex.
// Calculates camera position of vertex and passes it to frag shader.
// Calculates camera space normal vector and passes it to frag shader.

layout (location = 0) in vec4 position;
layout (location = 1) in vec4 normal;

out vec4 cameraSpacePosition;
out vec4 vertexNormal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 normalViewMatrix;

void main()
{
	vec4 camPosition = viewMatrix * position;
	gl_Position = projectionMatrix * camPosition;

	cameraSpacePosition = camPosition;
	vertexNormal = normalViewMatrix * normal;
}
