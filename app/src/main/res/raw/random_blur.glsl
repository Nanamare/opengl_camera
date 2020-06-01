precision mediump float;

uniform vec3 iResolution;

varying vec2                texCoord;
uniform sampler2D           iChannel0;

#define ITER 32
#define SIZE 16.0

#define DR 0.5

void srand(vec2 a, out float r)
{
	r=sin(dot(a,vec2(1233.224,1743.335)));
}

float rand(inout float r)
{
	r=fract(3712.65*r+0.61432);
	return (r-0.5)*2.0;
}

void mainImage( out vec4 fragColor, in vec2 fragCoord ){
	vec2 uv = fragCoord.xy;
	float p=SIZE/iResolution.y*(sin(DR/2.0)+1.0);
	vec4 c=vec4(0.0);
	float r;
	srand(uv, r);
	vec2 rv;

	for(int i=0;i<ITER;i++){
		rv.x=rand(r);
		rv.y=rand(r);
		c+=texture2D(iChannel0,uv+rv*p)/float(ITER);
	}
	fragColor=c;
}

void main() {
    mainImage(gl_FragColor, texCoord);
}