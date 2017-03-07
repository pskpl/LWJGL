#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;

out vec4 outColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 colour;
    // Light position is assumed to be in view coordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

struct DirectionalLight
{
    vec3 colour;
    vec3 direction;
    float intensity;
};

struct Material
{
    vec3 colour;
    int useColour;
    float reflectance;
};

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform sampler2D texture_sampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;
uniform DirectionalLight directionalLight;

vec4 calcLightColor(vec3 lightColor, float lightIntensity, vec3 verPos, vec3 verNormal, vec3 toLightDir)
{
	vec4(0, 0, 0, 0);
	vec4 specularColor = vec4(0, 0, 0, 0);
	
	float diffuseFactor = max(dot(verNormal, toLightDir), 0.0);
	vec4 diffuseColor = vec4(lightColor, 1) * lightIntensity * diffuseFactor;
	
	vec3 cameraDir = normalize(-verPos);
	vec3 inLightDir = -toLightDir;
	vec3 outLightDir = normalize(reflect(inLightDir, verNormal));
	float specularFactor = max(dot(cameraDir, outLightDir), 0.0);
	specularFactor = pow(specularFactor, specularPower);
	specularColor = vec4(lightColor, 1) * lightIntensity * specularFactor * material.reflectance;

	return diffuseColor + specularColor;
}

vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 verPos, vec3 verNormal)
{
	return calcLightColor(directionalLight.colour, directionalLight.intensity, verPos, verNormal, normalize(directionalLight.direction));
}

vec4 calcPointLight(PointLight pointLight, vec3 verPos, vec3 verNormal)
{
	vec3 toLightVec = pointLight.position - verPos;
	vec3 toLightDir = normalize(toLightVec);
	vec4 lightColor = calcLightColor(pointLight.colour, pointLight.intensity, verPos, verNormal, toLightDir);

	// attenuation
	float distance = length(toLightVec);
	float attenuationFactor = pointLight.att.constant + pointLight.att.linear * distance + pointLight.att.exponent * distance * distance;
	return lightColor / attenuationFactor;
}

void main()
{
	vec3 mvVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;

	vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
	vec3 mvVertexPos = mvPos.xyz;
	gl_Position = projectionMatrix * mvPos;
	
	vec4 baseColor;
	if(material.useColour == 1)
	{
		baseColor = vec4(material.colour, 1);
	}
	else
	{
		baseColor = texture(texture_sampler, texCoord);
	}
	vec4 totalLight = vec4(ambientLight, 1);
	totalLight += calcDirectionalLight(directionalLight, mvVertexPos, mvVertexNormal);
	totalLight += calcPointLight(pointLight, mvVertexPos, mvVertexNormal);
	
	outColor = baseColor * totalLight;
}