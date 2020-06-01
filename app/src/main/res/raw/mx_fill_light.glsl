precision mediump float;
uniform sampler2D iChannel0;
uniform sampler2D iChannel1;
varying vec2 texCoord;
vec4 calNewSaturation(vec4 color,float saturation) {
	float gray = dot(color.rgb, vec3(0.299,0.587,0.114));
	return vec4(gray + (saturation / 100.0 + 1.0) * (color.r - gray), gray + (saturation / 100.0 + 1.0) * (color.g - gray), gray + (saturation / 100.0 + 1.0) * (color.b - gray), color.a);
}
vec4 calColorTemperature(vec4 color, float scale) {
	vec3 new_color = color.rgb;
	new_color.r = color.r + color.r * ( 1.0 - color.r) * scale;
	new_color.b = color.b - color.b * ( 1.0 - color.b) * scale;
	if (scale > 0.0) {
		new_color.g = color.g + color.g * ( 1.0 - color.g) * scale * 0.25;
	}
	float max_value = max(new_color.r, max(new_color.g, new_color.b));
	if (max_value > 1.0) {
		new_color /= max_value;
	}
	return vec4(new_color.rgb, color.a);
}
void main() {
	vec4 color = texture2D(iChannel0, texCoord);
    color.r = texture2D(iChannel1, vec2(color.r, 1.0)).a;
    color.g = texture2D(iChannel1, vec2(color.g, 1.0)).a;
    color.b = texture2D(iChannel1, vec2(color.b, 1.0)).a;
	color = calNewSaturation(color, 39.0);
	color = calColorTemperature(color, 0.2);
	gl_FragColor = color;
}

