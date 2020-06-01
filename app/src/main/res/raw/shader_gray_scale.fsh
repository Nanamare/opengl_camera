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

/*
  Written by Alan Wolfe
  http://demofox.org/
  http://blog.demofox.org/

  more info on this shader:
  http://blog.demofox.org/2014/02/03/converting-rgb-to-grayscale/
*/
void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 percent = (fragCoord.xy);
    // percent.y = 1.0 - percent.y; 상하반전

    vec3 pixelColor = texture2D(iChannel0, percent).xyz;

    // naive grey scale conversion - average R,G and B
    float pixelGrey = dot(pixelColor, vec3(1.0/3.0));
    pixelColor = vec3(pixelGrey);

    fragColor = vec4(pixelColor, 1.0);
}

void main() {
    mainImage(gl_FragColor, texCoord);
}