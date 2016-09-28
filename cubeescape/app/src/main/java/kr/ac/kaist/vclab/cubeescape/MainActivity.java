package kr.ac.kaist.vclab.cubeescape;


import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity {

    public static Context context;
    private GLSurfaceView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();

        myView = new MySurfaceView(this);
        setContentView(myView);

    }

    @Override
    protected void onPause(){
        super.onPause();
        myView.onPause();

    }

    @Override
    protected void onResume(){
        super.onResume();
        myView.onResume();


    }
}
