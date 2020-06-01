precision lowp float;
precision lowp int;

varying vec2 blurCoordinates[5];

varying vec2                texCoord;
uniform sampler2D           iChannel0;

void main()
{
    vec4 original = texture2D(iChannel0, texCoord);
    lowp vec4 sum = vec4(0.0);
    sum += texture2D(iChannel0, blurCoordinates[0]) * 0.204164;
    sum += texture2D(iChannel0, blurCoordinates[1]) * 0.304005;
    sum += texture2D(iChannel0, blurCoordinates[2]) * 0.304005;
    sum += texture2D(iChannel0, blurCoordinates[3]) * 0.093913;
    sum += texture2D(iChannel0, blurCoordinates[4]) * 0.093913;
    gl_FragColor = vec4(sum.xyz,  1.0);
    gl_FragColor=vec4(mix(gl_FragColor.rgb, vec3(0.0), 0.02), gl_FragColor.a);
}