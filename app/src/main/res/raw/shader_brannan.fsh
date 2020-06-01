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

float overlay(in float s, in float d )
{
    return (d < 0.5) ? 2.0 * s * d : 1.0 - 2.0 * (1.0 - s) * (1.0 - d);
}

vec3 overlay(in vec3 s, in vec3 d )
{
    vec3 c;
    c.x = overlay(s.x,d.x);
    c.y = overlay(s.y,d.y);
    c.z = overlay(s.z,d.z);
    return c;
}

float greyScale(in vec3 col)
{
    return dot(col, vec3(0.3, 0.59, 0.11));
}

mat3 saturationMatrix( float saturation ) {
    vec3 luminance = vec3( 0.3086, 0.6094, 0.0820 );
    float oneMinusSat = 1.0 - saturation;
    vec3 red = vec3( luminance.x * oneMinusSat );
    red.r += saturation;

    vec3 green = vec3( luminance.y * oneMinusSat );
    green.g += saturation;

    vec3 blue = vec3( luminance.z * oneMinusSat );
    blue.b += saturation;

    return mat3(red, green, blue);
}

void levels(inout vec3 col, in vec3 inleft, in vec3 inright, in vec3 outleft, in vec3 outright) {
    col = clamp(col, inleft, inright);
    col = (col - inleft) / (inright - inleft);
    col = outleft + col * (outright - outleft);
}

void brightnessAdjust( inout vec3 color, in float b) {
    color += b;
}

void contrastAdjust( inout vec3 color, in float c) {
    float t = 0.5 - c * 0.5;
    color = color * c + t;
}

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord.xy;
    vec3 col = texture2D(iChannel0, uv).rgb;
    if (iMouse.z > 0.5) {
        fragColor = vec4(col, 1.0);
        return;
    }
    vec3 grey = vec3(greyScale(col));
    col = saturationMatrix(0.7) * col;
    grey = overlay(grey, col);
    col = mix(grey, col, 0.63);
    levels(col, vec3(0., 0., 0.) / 255., vec3(228., 255., 239.) / 255.,
    vec3(23., 3., 12.) / 255., vec3(255.) / 255.);
    brightnessAdjust(col, -0.1);
    contrastAdjust(col, 1.05);
    vec3 tint = vec3(255., 248., 242.) / 255.;
    levels(col, vec3(0., 0., 0.) / 255., vec3(255., 224., 255.) / 255.,
    vec3(9., 20., 18.) / 255., vec3(255.) / 255.);
    col = pow(col, vec3(0.91, 0.91, 0.91*0.94));
    brightnessAdjust(col, -0.04);
    contrastAdjust(col, 1.14);
    col = tint * col;
    fragColor = vec4(col, 1.0);
}

void main() {
    mainImage(gl_FragColor, texCoord);
}