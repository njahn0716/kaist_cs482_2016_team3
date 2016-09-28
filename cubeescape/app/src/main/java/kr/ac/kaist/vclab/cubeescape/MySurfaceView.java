package kr.ac.kaist.vclab.cubeescape;

import android.opengl.GLSurfaceView;
import android.content.Context;
/**
 * Created by NamjoAhn on 2016. 9. 28..
 */

public class MySurfaceView extends GLSurfaceView {

    private final MyRenderer myRenderer;

    public MySurfaceView(Context context){
        super(context);

        setEGLContextClientVersion(2);

        myRenderer = new MyRenderer();
        setRenderer(myRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


}
