package kr.ac.kaist.vclab.cubeescape;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by NamjoAhn on 2016. 10. 1..
 */

public class MyTexture {

    private Vector<Integer> textureIDs;

    public int textureCount;

    public MyTexture(){
        textureIDs = new Vector<Integer>();
        textureCount = 0;
    }

    public int getTextureID(int count){
        return textureIDs.elementAt(count);
    }

    public int loadBMP(String path){

        if(textureCount == GLES20.GL_MAX_TEXTURE_IMAGE_UNITS - 1){
            System.out.printf("Max texture unit reached : %d\n", GLES20.GL_MAX_TEXTURE_IMAGE_UNITS);
            return 0;
        }

        int textureID;
        Bitmap bmp;
        int texID;
        int width, height;
        try{
            bmp = BitmapFactory.decodeStream(MainActivity.context.getAssets().open(path));
        } catch (IOException e){
            e.printStackTrace();
            return -1;
        }
        Matrix mat = new Matrix();
        mat.preScale(1.0f, -1.0f);
        width = bmp.getWidth();
        height = bmp.getHeight();
        Bitmap data = Bitmap.createBitmap(bmp, 0, 0, width, height, mat, true);
        bmp.recycle();

        int temp[] = new int[1];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureCount);
        GLES20.glGenTextures(1, temp, 0);
        textureID = temp[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
        // GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, bb);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, data, 0);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);


        textureIDs.add(textureID);
        textureCount = textureCount + 1;
        return textureCount - 1;
    }

}
