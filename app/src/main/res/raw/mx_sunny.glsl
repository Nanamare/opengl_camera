precision mediump float;
uniform sampler2D iChannel0;
uniform sampler2D iChannel1;
varying vec2 texCoord;
vec4 calNewSaturation(vec4 color,float saturation) {
	float gray = dot(color.rgb, vec3(0.299,0.587,0.114));
	return vec4(gray + (saturation / 100.0 + 1.0) * (color.r - gray), gray + (saturation / 100.0 + 1.0) * (color.g - gray), gray + (saturation / 100.0 + 1.0) * (color.b - gray), color.a);
}
void main() {
	vec4 color = texture2D(iChannel0, texCoord);
	color = calNewSaturation(color, 23.0);
    color.r = texture2D(iChannel1, vec2(color.r, 1.0)).a;
    color.g = texture2D(iChannel1, vec2(color.g, 1.0)).a;
    color.b = texture2D(iChannel1, vec2(color.b, 1.0)).a;
	gl_FragColor = color;
}

