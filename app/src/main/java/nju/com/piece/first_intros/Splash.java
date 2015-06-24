package nju.com.piece.first_intros;

/**
 * Created by shen on 15/6/24.
 */
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nju.com.piece.R;
import nju.com.piece.activity.MainActivity;

public class Splash extends Fragment {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                Splash.this.startActivity(mainIntent);
                startActivity(mainIntent);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_fisrt_spash, container, false);

        return rootView;
    }
}