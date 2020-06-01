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

/*
	Andor Salga
	March 2014

	Simple demo showing mirror effects.
*/

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{

    // int choice = int( mod(iTime/3., 6.) );
    int choice = int(5);

    vec2 p = fragCoord.xy;

    // No Mirror
    // choice == 0

    // Left Mirror
    if(choice == 1){
        p.x -= step(0.5, p.x) * (p.x-0.5) * 2.0;
    }

    // Right Mirror
    if(choice == 2){
        p.x += step(p.x, 0.5) * (0.5-p.x) * 2.0;
    }

    // Top Mirror
    if(choice == 3){
        p.y -= step(p.y, 0.5) * (p.y-.5) * 2.0;
    }

    // Bottom Mirror
    if(choice == 4){
        p.y += step(0.5, p.y) * (0.5-p.y) * 2.0;
    }

    // Quad Mirror
    if(choice == 5){
        p.x += step(p.x, 0.5) * (0.5-p.x) * 2.0;
        p.y += step(0.5, p.y) * (0.5-p.y) * 2.0;
    }

    fragColor = texture2D(iChannel0, p);
}

void main() {
    mainImage(gl_FragColor, texCoord);
}