precision highp float;

uniform vec3                iResolution; // TODO 삭제하기
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

float hash(float n) { return fract(sin(n)*43758.5453123); }

void mainImage(out vec4 fragColor, in vec2 fragCoord)
{
    vec2 p = fragCoord.xy;

    vec2 u = p * 2. - 1.;
    vec2 n = u * vec2(iResolution.x / iResolution.y, 1.0);
    vec3 c = texture2D(iChannel0, p).xyz;


    // flicker, grain, vignette, fade in
    c += sin(hash(iTime)) * 0.01;
    c += hash((hash(n.x) + n.y) * iTime) * 0.5;
    c *= smoothstep(length(n * n * n * vec2(0.075, 0.4)), 1.0, 0.4);
    c *= smoothstep(0.001, 3.5, iTime) * 1.5;

    c = dot(c, vec3(0.2126, 0.7152, 0.0722))
    * vec3(0.8, 1.5 - hash(iTime) * 0.1, 0.6);

    fragColor = vec4(c, 1.0);
}

void main() {
    mainImage(gl_FragColor, texCoord);
}