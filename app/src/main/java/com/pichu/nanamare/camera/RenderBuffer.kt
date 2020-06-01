package com.pichu.nanamare.camera

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.opengles.GL10

class RenderBuffer(val width: Int, val height: Int, activeTexUnit: Int) {

    var texId = 0
    var activeTexUnit = 0
    private var renderBufferId = 0
    private var frameBufferId = 0

    init {
        this.activeTexUnit = activeTexUnit
        val genbuf = IntArray(1)

        // Generate and bind 2d texture
        GLES20.glActiveTexture(activeTexUnit)
        texId = MyGLUtils.genTexture()
        val texBuffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder())
            .asIntBuffer()
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            width,
            height,
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            texBuffer
        )

        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GL10.GL_TEXTURE_MIN_FILTER,
            GL10.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GL10.GL_TEXTURE_MAG_FILTER,
            GL10.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE)

        // Generate frame buffer
        GLES20.glGenFramebuffers(1, genbuf, 0)
        frameBufferId = genbuf[0]
        // Bind frame buffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId)

        // Generate render buffer
        GLES20.glGenRenderbuffers(1, genbuf, 0)
        renderBufferId = genbuf[0]
        // Bind render buffer
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBufferId)
        GLES20.glRenderbufferStorage(
            GLES20.GL_RENDERBUFFER,
            GLES20.GL_DEPTH_COMPONENT16,
            width,
            height
        )

        unbind()
    }

    fun bind() {
        GLES20.glViewport(0, 0, width, height)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId)
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D, texId, 0
        )
        GLES20.glFramebufferRenderbuffer(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
            GLES20.GL_RENDERBUFFER, renderBufferId
        )
    }

    fun unbind() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }
}
