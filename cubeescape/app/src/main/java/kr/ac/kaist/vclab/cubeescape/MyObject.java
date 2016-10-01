package kr.ac.kaist.vclab.cubeescape;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;

import java.util.Vector;
import java.util.Arrays;

import android.opengl.GLES20;
import android.opengl.Matrix;


import org.apache.commons.io.IOUtils;


/**
 * Created by NamjoAhn on 2016. 9. 28..
 */

public class MyObject {

    private static FloatBuffer texBuffer;
    private static FloatBuffer verBuffer;
    private static FloatBuffer colBuffer;
    private static FloatBuffer norBuffer;

    private final int program;

    private int verID, colID, norID, texID;
    private int mvpID, textypeID;

    private int[] bufferIDs = new int[10];

    public float[] eyeAT;
    public float[] modelAT;
    public float[] projectionAT;
    public float[] mvp;
    private float[] reset;

    static int textureCount;
    static int textureID;
    static Vector<Integer> textures;
    static Vector<Float> vertices;
    static Vector<Float> colors;
    static Vector<Float> normals;
    static Vector<Float> texcoords;

    private int vertex_changed;

    static final int COORD_PER_VERTEX = 3;
    static final int TEXCOORD_PER_VERTEX = 2;

    public MyObject(){
        textures = new Vector<Integer>();
        vertices = new Vector<Float>();
        colors = new Vector<Float>();
        normals = new Vector<Float>();
        texcoords = new Vector<Float>();

        vertex_changed = 0;

        GLES20.glGenBuffers(10, bufferIDs, 0);

        modelAT = new float[16];
        eyeAT = new float[16];
        projectionAT = new float[16];
        mvp = new float[16];
        reset = new float[16];

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(i == j){
                    reset[i * 4 + j] = 1.0f;
                } else{
                    reset[i * 4 + j] = 0.0f;
                }
            }
        }

        resetAT();


        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, "my.vshader");
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, "my.fshader");

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);

        GLES20.glLinkProgram(program);

        GLES20.glUseProgram(program);

        mvpID = GLES20.glGetUniformLocation(program, "MVP");
        textypeID = GLES20.glGetUniformLocation(program, "texID");
        verID = GLES20.glGetAttribLocation(program, "aPosition");
        colID = GLES20.glGetAttribLocation(program, "aColor");
        norID = GLES20.glGetAttribLocation(program, "aNormal");
        texID = GLES20.glGetAttribLocation(program, "aTex");
    }



    public void resetAT(){
        modelAT = Arrays.copyOf(reset, 16);
        eyeAT = Arrays.copyOf(reset, 16);
        projectionAT = Arrays.copyOf(reset, 16);
        mvp = Arrays.copyOf(reset, 16);
    }

    public int loadShader(int type, String file){
        InputStream fileStream;
        try{
            fileStream = MainActivity.context.getAssets().open(file);
        } catch (IOException e){
            e.printStackTrace();
            return -1;
        }

        String code;
        try{
            code = IOUtils.toString(fileStream, Charset.defaultCharset());
        } catch (IOException e){
            e.printStackTrace();
            return -1;
        }

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        return shader;
    }

    private void updateVertex(){
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.size() * 4);
        bb.order(ByteOrder.nativeOrder());
        verBuffer = bb.asFloatBuffer();
        verBuffer.position(0);
        Float temp[] = vertices.toArray(new Float[0]);
        for(int i = 0; i < vertices.size(); i++){
            verBuffer.put(temp[i]);
        }
        verBuffer.position(0);

        GLES20.glEnableVertexAttribArray(verID);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIDs[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.size() * 4, verBuffer, GLES20.GL_STATIC_DRAW);
    }

    private void updateColor(){
        ByteBuffer bb = ByteBuffer.allocateDirect(colors.size() * 4);
        bb.order(ByteOrder.nativeOrder());
        colBuffer = bb.asFloatBuffer();
        colBuffer.position(0);
        Float temp[] = colors.toArray(new Float[0]);
        for(int i = 0; i < colors.size(); i++){
            colBuffer.put(temp[i]);
        }
        colBuffer.position(0);

        GLES20.glEnableVertexAttribArray(colID);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIDs[2]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colors.size() * 4, colBuffer, GLES20.GL_STATIC_DRAW);
    }


    private void updateNormal(){
        ByteBuffer bb = ByteBuffer.allocateDirect(normals.size() * 4);
        bb.order(ByteOrder.nativeOrder());
        norBuffer = bb.asFloatBuffer();
        norBuffer.position(0);
        Float temp[] = normals.toArray(new Float[0]);
        for(int i = 0; i < normals.size(); i++){
            norBuffer.put(temp[i]);
        }
        norBuffer.position(0);

        GLES20.glEnableVertexAttribArray(norID);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIDs[1]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, normals.size() * 4, norBuffer, GLES20.GL_STATIC_DRAW);
    }

    private void updateTex(){
        ByteBuffer bb = ByteBuffer.allocateDirect(texcoords.size() * 4);
        bb.order(ByteOrder.nativeOrder());
        texBuffer = bb.asFloatBuffer();
        texBuffer.position(0);
        Float temp[] = texcoords.toArray(new Float[0]);
        for(int i = 0; i < texcoords.size(); i++){
            texBuffer.put(temp[i]);
        }
        texBuffer.position(0);

        GLES20.glEnableVertexAttribArray(texID);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIDs[3]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, texcoords.size() * 4, texBuffer, GLES20.GL_STATIC_DRAW);
    }

    private void computeMVP(){
        float[] temp = new float[16];
        float[] invTemp = new float[16];
        Matrix.invertM(invTemp, 0, eyeAT, 0);
        Matrix.multiplyMM(temp, 0, invTemp, 0, modelAT, 0);
        Matrix.multiplyMM(mvp, 0, projectionAT, 0, temp, 0 );
    }

    private void updateAT(){
        computeMVP();
        GLES20.glUniformMatrix4fv(mvpID, 1, false, mvp, 0);
    }

    public void update(){
        updateAT();
        updateVertex();
        updateNormal();
        updateColor();
        updateTex();
        GLES20.glUniform1i(textypeID, textureCount);
    }

    public void setTexture(int count, int ID){
        textureCount = count;
        textureID = ID;
    }


    public void addTexCoord(float u, float v){
        texcoords.add(u);
        texcoords.add(v);
        vertex_changed = 1;
    }

    public void addNormal(float x, float y, float z){
        normals.add(x);
        normals.add(y);
        normals.add(z);
        vertex_changed = 1;
    }

    public void addColor(float x, float y, float z){
        colors.add(x);
        colors.add(y);
        colors.add(z);
        vertex_changed = 1;
    }

    public void addVertex(float x, float y, float z){
        vertices.add(x);
        vertices.add(y);
        vertices.add(z);
        vertex_changed = 1;
    }

    public void draw(){
        GLES20.glUseProgram(program);
        update();

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureCount);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);

        GLES20.glEnableVertexAttribArray(verID);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIDs[0]);
        GLES20.glVertexAttribPointer(verID, COORD_PER_VERTEX, GLES20.GL_FLOAT, false, 0, 0);

        GLES20.glEnableVertexAttribArray(colID);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIDs[2]);
        GLES20.glVertexAttribPointer(colID, COORD_PER_VERTEX, GLES20.GL_FLOAT, false, 0, 0);

        GLES20.glEnableVertexAttribArray(norID);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIDs[1]);
        GLES20.glVertexAttribPointer(norID, COORD_PER_VERTEX, GLES20.GL_FLOAT, false, 0, 0);


        GLES20.glEnableVertexAttribArray(texID);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIDs[3]);
        GLES20.glVertexAttribPointer(texID, TEXCOORD_PER_VERTEX, GLES20.GL_FLOAT, false, 0, 0);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.size() / 3);
    }



}
