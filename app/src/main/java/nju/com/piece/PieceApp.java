package nju.com.piece;

import android.app.Application;
import android.content.Context;

/**
 * Created by shen on 15/6/22.
 */
public class PieceApp extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        PieceApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return PieceApp.context;
    }
}
