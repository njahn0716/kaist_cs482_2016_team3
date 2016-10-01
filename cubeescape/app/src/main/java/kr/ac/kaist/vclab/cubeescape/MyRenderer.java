package kr.ac.kaist.vclab.cubeescape;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import android.opengl.Matrix.*;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;

/**
 * Created by NamjoAhn on 2016. 9. 28..
 */

public class MyRenderer implements GLSurfaceView.Renderer {

    MyObject testCube;
    MyTexture texture;
    MyController control;
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LESS);

        texture = new MyTexture();
        control = new MyController();
        control.setProjection(45.0f, 3.0f / 4.0f, 0.1f, 100.0f);
        control.setEye();
        control.setWorld();
        control.addCube("CubeMap_colorcube.bmp", 1.0f, 0.0f, 0.0f);
    }

    @Override
    public void onDrawFrame(GL10 unused){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        control.drawAll();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height){
        GLES20.glViewport(0, 0, width, height);
    }





}
