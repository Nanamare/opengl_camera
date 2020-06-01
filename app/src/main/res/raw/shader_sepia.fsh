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

mat4 rgba2sepia =
mat4
(
0.393, 0.349, 0.272, 0,
0.769, 0.686, 0.534, 0,
0.189, 0.168, 0.131, 0,
0,     0,     0,     1
);

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    float timeFactor = ( 1.0 + sin( iTime ) ) * 0.5;
    vec4 color = texture2D( iChannel0, fragCoord );
    mat4 rgba2sepiaDiff = mat4( 1.0 ) + timeFactor * ( rgba2sepia - mat4( 1.0 ) );

    fragColor = rgba2sepiaDiff * color;
}


void main() {
    mainImage(gl_FragColor, texCoord);
}