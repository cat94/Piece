package nju.com.piece.first_intros;

import android.app.Application;
import android.content.Context;

/**
 * Created by shen on 15/6/22.
 */
public class PieceAppContext extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        PieceAppContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return PieceAppContext.context;
    }
}
