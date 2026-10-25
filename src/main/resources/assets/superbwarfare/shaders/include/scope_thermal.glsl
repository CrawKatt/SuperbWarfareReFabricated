// Alpha distinguishes an omitted data colour from an explicitly configured black colour.
uniform vec4 ThermalBackgroundColor;
uniform vec4 ThermalEntityColor;

// Palette ids mirror ThermalPalette's ordinals: 0 = red-blue, 1 = black-white, 2 = black-green.
vec3 scopeThermalCold(float luminance, int palette) {
    if (palette == 1) {
        return vec3(0.004 + 0.18 * luminance);
    }
    if (palette == 2) {
        return vec3(0.0, 0.006 + 0.15 * luminance, 0.003 + 0.018 * luminance);
    }
    return mix(vec3(0.006, 0.014, 0.055), vec3(0.035, 0.11, 0.38), luminance);
}

vec3 scopeThermalHot(float luminance, int palette) {
    if (palette == 1) {
        return vec3(mix(0.62, 1.0, luminance));
    }
    if (palette == 2) {
        return mix(vec3(0.06, 0.42, 0.10), vec3(0.62, 1.0, 0.68), luminance);
    }
    return mix(vec3(0.6, 0.012, 0.005), vec3(1.0, 0.23, 0.1), luminance);
}

vec3 scopeThermalEdge(int palette) {
    return vec3(1.0);
}

vec3 scopeThermalColour(sampler2D scene, sampler2D heat, vec2 uv, vec2 texel, int palette) {
    vec3 source = texture(scene, uv).rgb;
    float luminance = dot(source, vec3(0.2126, 0.7152, 0.0722));
    vec3 cold = scopeThermalCold(luminance, palette);
    if (ThermalBackgroundColor.a > 0.5) {
        cold = ThermalBackgroundColor.rgb * mix(0.15, 1.0, luminance);
    }
    float center = texture(heat, uv).r;
    float outer = center;
    float inner = center;
    for (int y = -1; y <= 1; ++y) {
        for (int x = -1; x <= 1; ++x) {
            vec2 offset = vec2(x, y) * texel * 1.5;
            float value = texture(heat, clamp(uv + offset, texel * 0.5, vec2(1.0) - texel * 0.5)).r;
            outer = max(outer, value);
            inner = min(inner, value);
        }
    }
    vec3 hot = scopeThermalHot(luminance, palette);
    if (ThermalEntityColor.a > 0.5) {
        hot = ThermalEntityColor.rgb * mix(0.6, 1.0, luminance);
    }
    vec3 colour = mix(cold, hot, center);
    return mix(colour, scopeThermalEdge(palette), clamp(outer - inner, 0.0, 1.0));
}
