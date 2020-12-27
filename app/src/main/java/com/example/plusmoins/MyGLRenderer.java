package com.example.plusmoins;

/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.plusmoins.modele.Card;
import com.example.plusmoins.modele.Diamond;
import com.example.plusmoins.modele.Sablier;
import com.example.plusmoins.modele.ParallelXceed;
import com.example.plusmoins.modele.Rectangle;
import com.example.plusmoins.modele.Square;
import com.example.plusmoins.modele.Star;
import com.example.plusmoins.modele.Triangle;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "MyGLRenderer";
    private Card deck1;
    private Card deck2;
    private Card pills;
    private Diamond diamond;
    private Diamond diamond2;
    private Diamond diamondScale;
    private Sablier sablier;
    private Sablier sablier2;
    private Sablier sablierScale;
    private Rectangle rectangle;
    private Rectangle rectangle2;
    private Rectangle rectScale;
    private ParallelXceed parallelXceed;
    private ParallelXceed parallelXceed2;
    private ParallelXceed parallelXceedScale;
    private Star star;
    private Star star2;
    private Star starScale;
    private Square square;
    private Square square2;
    private Square squareScale;
    private Triangle triangle;
    private Triangle triangle2;
    private Triangle triangleScale;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float mAngle;

    private float[] mPosition = {0.0f, 0.0f};
    private float[] mPosition2 = {0.5f, 0.0f};
    private float[] mPosition3 = {-0.5f, 0.0f};

    private int topPillCard;
    private int topDeckCard;
    private boolean player;
    private boolean guess;


    public void onGuess(float[] mMVPMatrix){
        float[] scratch = new float[16];
        pills.draw(mMVPMatrix);
        switch(topPillCard){
            case 0:
                square.draw(mMVPMatrix);
                break;
            case 1:
                rectangle.draw(mMVPMatrix);
                break;
            case 2:
                parallelXceed.draw(mMVPMatrix);
                break;
            case 3:
                triangle.draw(mMVPMatrix);
                break;
            case 4:
                diamond.draw(mMVPMatrix);
                break;
            case 5:
                sablier.draw(mMVPMatrix);
                break;
            case 6:
                star.draw(mMVPMatrix);
                break;
        }
        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,mPosition2[0],mPosition2[1],0);
        Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
        deck1.draw(scratch);
        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,mPosition3[0],mPosition3[1],0);
        Matrix.multiplyMM(scratch,0,mMVPMatrix,0,mModelMatrix,0);
        deck2.draw(scratch);
    }

    public void onPlay(float[] mMVPMatrix){
        float[] scratch = new float[16];
        pills.draw(mMVPMatrix);
        switch(topPillCard){
            case 0:
                square.draw(mMVPMatrix);
                break;
            case 1:
                rectangle.draw(mMVPMatrix);
                break;
            case 2:
                parallelXceed.draw(mMVPMatrix);
                break;
            case 3:
                triangle.draw(mMVPMatrix);
                break;
            case 4:
                diamond.draw(mMVPMatrix);
                break;
            case 5:
                sablier.draw(mMVPMatrix);
                break;
            case 6:
                star.draw(mMVPMatrix);
                break;
        }
        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,mPosition2[0],mPosition2[1],0);
        Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
        deck1.draw(scratch);
        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,mPosition3[0],mPosition3[1],0);
        Matrix.multiplyMM(scratch,0,mMVPMatrix,0,mModelMatrix,0);
        deck2.draw(scratch);
        if(player == true){
            switch(topDeckCard){
                case 0:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition2[0],mPosition2[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    square2.draw(scratch);
                    break;
                case 1:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition2[0],mPosition2[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    rectangle2.draw(scratch);
                    break;
                case 2:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition2[0],mPosition2[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    parallelXceed2.draw(scratch);
                    break;
                case 3:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition2[0],mPosition2[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    triangle2.draw(scratch);
                    break;
                case 4:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition2[0],mPosition2[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    diamond2.draw(scratch);
                    break;
                case 5:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition2[0],mPosition2[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    sablier2.draw(scratch);
                    break;
                case 6:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition2[0],mPosition2[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    star2.draw(scratch);
                    break;
            }
        }else{
            switch(topDeckCard){
                case 0:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition3[0],mPosition3[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    square2.draw(scratch);
                    break;
                case 1:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition3[0],mPosition3[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    rectangle2.draw(scratch);
                    break;
                case 2:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition3[0],mPosition3[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    parallelXceed2.draw(scratch);
                    break;
                case 3:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition3[0],mPosition3[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    triangle2.draw(scratch);
                    break;
                case 4:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition3[0],mPosition3[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    diamond2.draw(scratch);
                    break;
                case 5:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition3[0],mPosition3[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    sablier2.draw(scratch);
                    break;
                case 6:
                    Matrix.setIdentityM(mModelMatrix,0);
                    Matrix.translateM(mModelMatrix,0,mPosition3[0],mPosition3[1],0);
                    Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
                    star2.draw(scratch);
                    break;
            }
        }
    }


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        deck1 = new Card();
        deck2 = new Card();
        pills = new Card();
        rectangle = new Rectangle();
        rectangle2 = new Rectangle();
        rectScale = new Rectangle();
        parallelXceed = new ParallelXceed();
        parallelXceed2 = new ParallelXceed();
        parallelXceedScale = new ParallelXceed();
        square = new Square();
        square2 = new Square();
        squareScale = new Square();
        triangle = new Triangle();
        triangle2 = new Triangle();
        triangleScale = new Triangle();
        diamond = new Diamond();
        diamond2 = new Diamond();
        diamondScale = new Diamond();
        sablier = new Sablier();
        sablier2 = new Sablier();
        sablierScale = new Sablier();
        star = new Star();
        star2 = new Star();
        starScale = new Star();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Draw background color
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        // Set the camera position (View matrix)
        //Matrix.setIdentityM(mViewMatrix,0);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        float[] scratch = new float[16];
        pills.draw(mMVPMatrix);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,-0.60f,0.8f,0);
        Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
        starScale.draw(scratch);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,-0.37f,0.8f,0);
        Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
        sablierScale.draw(scratch);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,-0.20f,0.8f,0);
        Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
        diamondScale.draw(scratch);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,0.07f,0.8f,0);
        Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
        triangleScale.draw(scratch);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,0.35f,0.8f,0);
        Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
        parallelXceedScale.draw(scratch);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,0.57f,0.8f,0);
        Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
        rectScale.draw(scratch);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,0.7f,0.8f,0);
        Matrix.multiplyMM(scratch,0, mMVPMatrix, 0, mModelMatrix, 0);
        squareScale.draw(scratch);

        if(guess == true)
            onGuess(mMVPMatrix);
        else
            onPlay(mMVPMatrix);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES30.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }
    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }


    public void setGuess(boolean guess) {
        this.guess = guess;
    }

    public void setTopDeckCard(int topDeckCard) {
        this.topDeckCard = topDeckCard;
    }


    public void setTopPillCard(int topPillCard) {
        this.topPillCard = topPillCard;
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }
    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }
}