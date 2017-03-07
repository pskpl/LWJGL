#version 330

in vec4 vertexNormal;

out vec4 outputColor;

uniform vec4 diffuseColor;
uniform vec4 modelSpaceLightPos;
uniform vec4 cameraSpaceLightPos;
uniform vec4 lightIntensity;
uniform vec4 ambientIntensity;
uniform float lightAttenuation;
uniform mat4 clipToCameraMatrix;
uniform ivec2 windowSize;

vec4 calcCameraSpacePosition()
{
	vec4 ndcPos;
	ndcPos.xy = ((gl_FragCoord.xy / windowSize.xy) * 2.0) - 1.0;
	ndcPos.z = (2.0 * gl_FragCoord.z - gl_DepthRange.near - gl_DepthRange.far)
				/ (gl_DepthRange.far - gl_DepthRange.near);
	ndcPos.w = 1.0;

	vec4 clipPos = ndcPos / gl_FragCoord.w;

	return clipToCameraMatrix * clipPos;
}

vec4 applyLightIntensity(in vec4 cameraSpacePosition, out vec4 lightDirection)
{
	vec4 lightDifference = cameraSpaceLightPos - cameraSpacePosition;
	float lightDistanceSqr = dot(lightDifference, lightDifference);
	lightDirection = lightDifference * inversesqrt(lightDistanceSqr);

	float distFactor = sqrt(lightDistanceSqr);

	return lightIntensity * (1 / (1.0 + lightAttenuation * distFactor));
}

void main()
{
	vec4 cameraSpacePosition = calcCameraSpacePosition();

	vec4 lightDir = vec4(0.0);
	vec4 attenIntensity = applyLightIntensity(cameraSpacePosition, lightDir);

	float cosAngIncidence = dot(normalize(vertexNormal), lightDir);
	cosAngIncidence = clamp(cosAngIncidence, 0, 1);

	outputColor = (diffuseColor * attenIntensity * cosAngIncidence) +
				(diffuseColor * ambientIntensity);
}
