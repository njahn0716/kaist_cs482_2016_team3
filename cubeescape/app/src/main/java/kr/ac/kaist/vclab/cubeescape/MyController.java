package kr.ac.kaist.vclab.cubeescape;


import android.opengl.Matrix;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by NamjoAhn on 2016. 10. 1..
 */

public class MyController {

    private static float[] eyeAT;
    private static float[] worldAT;
    private static float[] projectionAT;

    public static Vector<MyObject> squares;
    public static Vector<MyObject> cubes;

    public enum objectType {CUBE, SQUARE};


    public MyTexture texture;

    private static final float[] squareVertex = {
            -0.5f, 0.5f, -1.0f,
            -0.5f, -0.5f, -1.0f,
            0.5f, -0.5f, -1.0f,
            -0.5f, 0.5f, -1.0f,
            0.5f, -0.5f, -1.0f,
            0.5f, 0.5f, -1.0f
    };

    private static final float[] cubeVertex = {
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
    };

    private static final float[] cubeTexCoord = {
            0.25f, 0.5f, 0.5f, 0.5f, 0.5f, 0.75f, 0.25f ,0.5f, 0.5f, 0.75f, 0.25f, 0.75f,
            0.25f, 0.5f, 0.25f, 0.25f, 0.5f, 0.25f, 0.25f, 0.5f, 0.5f, 0.25f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f, 0.25f, 0.75f, 0.25f, 0.5f, 0.5f, 0.75f, 0.25f, 0.75f, 0.5f,
            0.75f, 0.5f, 0.75f, 0.25f, 1.0f, 0.25f, 0.75f, 0.5f, 1.0f, 0.25f, 1.0f, 0.5f,
            0.0f, 0.5f, 0.0f, 0.25f, 0.25f, 0.25f, 0.0f, 0.5f, 0.25f, 0.25f, 0.25f, 0.5f,
            0.25f, 0.25f, 0.25f, 0.0f, 0.5f, 0.0f, 0.25f, 0.25f, 0.5f, 0.0f, 0.5f, 0.25f,
    };

    public MyController(){
        texture = new MyTexture();

        eyeAT = new float[16];
        worldAT = new float[16];
        projectionAT = new float[16];

        squares = new Vector<MyObject>();
        cubes = new Vector<MyObject>();
    }

    public void setEye(){
        float[] basicEye = {1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 10.0f, 1.0f};
        eyeAT = Arrays.copyOf(basicEye, 16);
    }

    public void setEye(float eye[]){
        eyeAT = Arrays.copyOf(eye, 16);
    }

    public void setWorld(){
        float[] basicWorld = {1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f};
        worldAT = Arrays.copyOf(basicWorld, 16);
    }
    // Should be called only once
    public void setWorld(float world[]){
        worldAT = Arrays.copyOf(world, 16);
    }

    // Should be called only once
    public void setProjection(float angle, float aspectRatio, float nearZ, float farZ){
        Matrix.perspectiveM(projectionAT, 0, angle, aspectRatio, nearZ, farZ);
    }

    public void setRBT(MyObject obj){
        obj.eyeAT = Arrays.copyOf(eyeAT, 16);
        obj.projectionAT = Arrays.copyOf(projectionAT, 16);
        obj.modelAT = Arrays.copyOf(worldAT, 16);
    }

    public void setRBT(objectType type, int count){
        if(type == objectType.CUBE){
            cubes.elementAt(count).eyeAT = Arrays.copyOf(eyeAT, 16);
            cubes.elementAt(count).projectionAT = Arrays.copyOf(projectionAT, 16);
            cubes.elementAt(count).modelAT = Arrays.copyOf(worldAT, 16);
        } else if(type == objectType.SQUARE){
            squares.elementAt(count).eyeAT = Arrays.copyOf(eyeAT, 16);
            squares.elementAt(count).projectionAT = Arrays.copyOf(projectionAT, 16);
            squares.elementAt(count).modelAT = Arrays.copyOf(worldAT, 16);
        }
    }

    public float[] normalOfPlane(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3){
        float vec1[] = {x1 - x2, y1 - y2, z1 - z2};
        float vec2[] = {x1 - x3, y1 - y3, z1 - z3};
        float crossP[] = new float[3];

        crossP[0] = vec1[0] * vec2[1] - vec1[1] * vec2[0];
        crossP[1] = vec1[2] * vec2[0] - vec1[0] * vec2[2];
        crossP[2] = vec1[1] * vec2[2] - vec1[2] * vec2[1];

        return crossP;
    }

    public void squareSet(MyObject obj, float r, float g, float b) {

        float[] normal1 = normalOfPlane(squareVertex[0], squareVertex[1], squareVertex[2],
                squareVertex[3], squareVertex[4], squareVertex[5],
                squareVertex[6], squareVertex[7], squareVertex[8]);
        float[] normal2 = normalOfPlane(squareVertex[9], squareVertex[10], squareVertex[11],
                squareVertex[12], squareVertex[13], squareVertex[14],
                squareVertex[15], squareVertex[16], squareVertex[17]);

        for (int i = 0; i < 6; i++) {
            obj.addVertex(squareVertex[3 * i], squareVertex[3 * i + 1], squareVertex[3 * i + 2]);
            obj.addTexCoord(squareVertex[3 * i] + 0.5f, squareVertex[3 * i + 1] + 0.5f);
            obj.addColor(r, g, b);
        }
        obj.addNormal(normal1[0], normal1[1], normal1[2]);
        obj.addNormal(normal1[0], normal1[1], normal1[2]);
        obj.addNormal(normal1[0], normal1[1], normal1[2]);
        obj.addNormal(normal2[0], normal2[1], normal2[2]);
        obj.addNormal(normal2[0], normal2[1], normal2[2]);
        obj.addNormal(normal2[0], normal2[1], normal2[2]);
    }

    public void addCube(String texturePath, float r, float g, float b){
        MyObject obj = new MyObject();

        int tempCount = texture.loadBMP(texturePath);
        obj.setTexture(tempCount, texture.getTextureID(tempCount));
        setRBT(obj);
        cubeSet(obj, r, g, b);
        cubes.add(obj);
    }


    public void cubeSideSet(MyObject obj, int v1, int v2, int v3, int v4, float r, float g, float b, int texN){

        // Texture Counting
        // [ - ] [ - ] [ - ] [ - ]
        // [ - ] [ 1 ] [ - ] [ - ]
        // [ 5 ] [ 2 ] [ 3 ] [ 4 ]
        // [ - ] [ 6 ] [ - ] [ - ]

        float[] normal1 = normalOfPlane(cubeVertex[v1*3], cubeVertex[v1*3+1], cubeVertex[v1*3+2],
                cubeVertex[v2*3], cubeVertex[v2*3+1], cubeVertex[v2*3+2],
                cubeVertex[v3*3], cubeVertex[v3*3+1], cubeVertex[v3*3+2]);
        float[] normal2 = normalOfPlane(cubeVertex[v1*3], cubeVertex[v1*3+1], cubeVertex[v1*3+2],
                cubeVertex[v3*3], cubeVertex[v3*3+1], cubeVertex[v3*3+2],
                cubeVertex[v4*3], cubeVertex[v4*3+1], cubeVertex[v4*3+2]);

        obj.addVertex(cubeVertex[v1*3], cubeVertex[v1*3+1], cubeVertex[v1*3+2]);
        obj.addVertex(cubeVertex[v2*3], cubeVertex[v2*3+1], cubeVertex[v2*3+2]);
        obj.addVertex(cubeVertex[v3*3], cubeVertex[v3*3+1], cubeVertex[v3*3+2]);
        obj.addVertex(cubeVertex[v1*3], cubeVertex[v1*3+1], cubeVertex[v1*3+2]);
        obj.addVertex(cubeVertex[v3*3], cubeVertex[v3*3+1], cubeVertex[v3*3+2]);
        obj.addVertex(cubeVertex[v4*3], cubeVertex[v4*3+1], cubeVertex[v4*3+2]);

        for (int i = 0; i < 6; i++) {
            obj.addTexCoord(cubeTexCoord[(texN - 1) * 12 + i * 2], cubeTexCoord[(texN - 1) * 12 + i * 2 + 1]);
            obj.addColor(r, g, b);
        }
        obj.addNormal(normal1[0], normal1[1], normal1[2]);
        obj.addNormal(normal1[0], normal1[1], normal1[2]);
        obj.addNormal(normal1[0], normal1[1], normal1[2]);
        obj.addNormal(normal2[0], normal2[1], normal2[2]);
        obj.addNormal(normal2[0], normal2[1], normal2[2]);
        obj.addNormal(normal2[0], normal2[1], normal2[2]);

    }

    public void cubeSet(MyObject obj, float r, float g, float b){
        cubeSideSet(obj, 0, 1, 2, 3, r, g, b, 1);
        cubeSideSet(obj, 0, 4, 5, 1, r, g, b, 2);
        cubeSideSet(obj, 1, 5, 6, 2, r, g, b, 3);
        cubeSideSet(obj, 2, 6, 7, 3, r, g, b, 4);
        cubeSideSet(obj, 3, 7, 4, 0, r, g, b, 5);
        cubeSideSet(obj, 4, 7, 6, 5, r, g, b, 6);
    }


    public void drawType(objectType type){
        if(type==objectType.CUBE){

            for(int i = 0; i < cubes.size(); i++){
                cubes.elementAt(i).draw();
            }
        }
    }

    public void drawAll(){
        drawType(objectType.CUBE);
    }

    public void eyeSet(float x1, float y1, float z1, float x2, float y2, float z2, float angle2){
        float[] tempEyeAT = Arrays.copyOf(eyeAT, 16);
        float[] temp = new float[16];

        float x, y, z;
        x = y1 * z2 - y2 * z1;
        y = z1 * x2 - z2 * x1;
        z = x1 * y2 - x2 * y1;

        float angle1;
        float sinAngle = (x * x + y * y + z * z) / (float)Math.sqrt((x1 * x1 + y1 * y1 + z1 * z1) * (x2 * x2 + y2 * y2 + z2* z2));
        angle1 = (float)Math.asin(Math.min(Math.max(sinAngle, -0.999f), 0.999f));
        Matrix.rotateM(tempEyeAT, 0, angle1, x, y, z);
        Matrix.rotateM(tempEyeAT, 0, angle2, 0.0f, 0.0f, -1.0f);

        for(int i = 0; i < 16; i++){
            eyeAT[i] = tempEyeAT[i];
        }
    }

}
