#version 150

#moj_import <superbwarfare:scope_thermal.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform vec2 ThermalTexelSize;
uniform int ThermalPalette;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    fragColor = vec4(scopeThermalColour(Sampler0, Sampler1, texCoord, ThermalTexelSize, ThermalPalette), 1.0);
}
