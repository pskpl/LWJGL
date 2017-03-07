#version 330

in vec4 modelSpacePosition;
in vec4 vertexNormal;

out vec4 outputColor;

uniform vec4 modelSpaceLightPos;
uniform vec4 lightIntensity;
uniform vec4 ambientIntensity;
uniform vec4 diffuseColor;

void main()
{
	vec4 lightDir = normalize(modelSpaceLightPos - modelSpacePosition);

	float cosAngIncidence = dot(normalize(vertexNormal), lightDir);
	cosAngIncidence = clamp(cosAngIncidence, 0, 1);

	outputColor = (diffuseColor * lightIntensity * cosAngIncidence) +
				(diffuseColor * ambientIntensity);
}
