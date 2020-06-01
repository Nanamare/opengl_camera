# opengl_camera

This project is a sample camera project using OpenGL2.

There are about 50 filters, and you can add more.

Adding a filter is very simple.

![example1](https://user-images.githubusercontent.com/17498974/83423192-9bc12500-a465-11ea-9f8d-deab020f4d89.gif)
![example2](https://user-images.githubusercontent.com/17498974/83423199-9e237f00-a465-11ea-815f-6b153b63bfa5.gif)


How to add camera filter?
1. implementation filter class
```
class EdgeDetectionFilter(context: Context, filterName: String) :
    CameraFilter(context, filterName) {
    private val program: Int =
        MyGLUtils.buildProgram(context, R.raw.shader_vertext, R.raw.shader_edge_detection)

    public override fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        setupShaderInputs(
            program,
            intArrayOf(canvasWidth, canvasHeight),
            intArrayOf(cameraTexId),
            arrayOf()
        )
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }
}

```

2. implementation shader class
```
#extension GL_OES_standard_derivatives : enable
precision mediump float;

uniform vec3                iResolution;
uniform sampler2D           iChannel0;
varying vec2                texCoord;

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord.xy;
    vec4 color =  texture2D(iChannel0, fragCoord);
    float gray = length(color.rgb);
    fragColor = vec4(vec3(step(0.06, length(vec2(dFdx(gray), dFdy(gray))))), 1.0);
}

void main() {
    mainImage(gl_FragColor, texCoord);
}
```

3. Just append
```
liveCameraFilters.value?.append(1, EdgeDetectionFilter(context, "EdgeDetection"))
```

#### Done
Image save

Change filter

Camera on/off relative to lifecycle

About 50 number filter

#### TODO
Recording

Portrait filter
