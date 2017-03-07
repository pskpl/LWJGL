#version 330

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 normal;

smooth out vec4 interpColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 diffuseColor;

uniform vec4 modelSpaceLightPos;
uniform vec4 lightIntensity;
uniform vec4 ambientIntensity;

void main()
{
	gl_Position = projectionMatrix * (viewMatrix * position);

	vec4 dirToLight = normalize(modelSpaceLightPos - position);
	float cosAngIncidence = dot(normal, dirToLight);
	cosAngIncidence = clamp(cosAngIncidence, 0, 1);
	
	interpColor = lightIntensity * diffuseColor * cosAngIncidence +
					diffuseColor * ambientIntensity;
}
