
precision highp float;

uniform float               iGlobalTime;
uniform sampler2D           iChannel0;
varying vec2                texCoord;

uniform float     iTime;// shader playback time (in seconds)
uniform float     iTimeDelta;// render time (in seconds)
uniform int       iFrame;// shader playback frame
uniform float     iChannelTime[4];// channel playback time (in seconds)
uniform vec3      iChannelResolution[4];// channel resolution (in pixels)
uniform vec4      iMouse;// mouse pixel coords. xy: current (if MLB down), zw: click
uniform vec4      iDate;// (year, month, day, time in seconds)
uniform float     iSampleRate;// sound sample rate (i.e., 44100)

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord.xy;

    vec3 orig = texture2D(iChannel0, uv).xyz;	// ...this is the video

    vec3 col = orig * orig * 1.4; 				// ...some contrast
    float bri = (col.x+col.y+col.z)/3.0;		// ...it's ok it's black and white!

    float v = smoothstep(.0, .7, bri);			// ...tint the dark values into cyan/teal.
    col = mix(vec3(0., 1., 1.2) * bri, col, v);

    v = smoothstep(.2, 1.1, bri);				// ...tint the light values into orange.
    col = mix(col, min(vec3(1.0, .55, 0.) * col * 1.2, 1.0), v);

    float x = (iMouse.x);
    col = mix(orig, col, step(x, uv.x));		// ...mouse X to slide filter
    fragColor = vec4(min(col, 1.0),1.0);
}

void main() {
    mainImage(gl_FragColor, texCoord);
}