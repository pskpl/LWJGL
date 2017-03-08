#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 vertexNormal;
layout (location=2) in vec4 barycentric;

out vec3 mvVertexNormal;
out vec3 mvVertexPos;
out vec4 barycentricCoord;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPos;
    mvVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
    barycentricCoord = barycentric;
}