#version 330

// VERTEX SHADER
// Sets clip position of vertex.
// Calculates camera position of vertex and passes it to frag shader.
// Calculates camera space normal vector and passes it to frag shader.
// Passes barycentric coordinate to frag shader.

layout (location = 0) in vec4 position;
layout (location = 1) in vec4 normal;
layout (location = 2) in vec4 barycentric;

out vec4 cameraSpacePosition;
out vec4 vertexNormal;
out vec4 barycentricCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 normalViewMatrix;

void main()
{
	vec4 camPosition = viewMatrix * position;
	gl_Position = projectionMatrix * camPosition;

	cameraSpacePosition = camPosition;
	vertexNormal = normalViewMatrix * normal;
	barycentricCoord = barycentric;
}
