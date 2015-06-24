package nju.com.piece.first_intros;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import nju.com.piece.R;

/**
 * Created by shen on 15/6/24.
 */
public class FirstSlidePageFragment extends Fragment {

    private static final int[] imageResourses = {
            R.drawable.slide_test_pic,
            R.drawable.slide_test_pic,
            R.drawable.slide_test_pic,
            R.drawable.slide_test_pic
    };

    private int mPageNumber;

    public static final String ARG_PAGE = "page";

    public static FirstSlidePageFragment create(int pageNumber) {
        FirstSlidePageFragment fragment = new FirstSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FirstSlidePageFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_first_slide_page, container, false);

        ImageView imageView = (ImageView)rootView.findViewById(R.id.first_page_image);

        imageView.setImageResource(imageResourses[mPageNumber]);

        return rootView;
    }

}
