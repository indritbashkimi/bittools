package com.ibashkimi.ruler.ruler2;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ibashkimi.ruler.R;

public class Ruler2Fragment extends Fragment {
    private static final String PREF_RULER2_CIRCLE1_X = "pref_ruler2_circle1_x";
    private static final String PREF_RULER2_CIRCLE1_Y = "pref_ruler2_circle1_y";
    private static final String PREF_RULER2_CIRCLE2_X = "pref_ruler2_circle2_x";
    private static final String PREF_RULER2_CIRCLE2_Y = "pref_ruler2_circle2_y";

    private Ruler2Overlay overlay;
    private CircleArea[] circles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        circles = new CircleArea[2];
        circles[0] = new CircleArea();
        circles[1] = new CircleArea();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ruler2, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.ruler_title);
        toolbar.setNavigationIcon(R.drawable.ic_back_nav);
        toolbar.setNavigationOnClickListener(view -> findNavController(this).navigateUp());

        overlay = rootView.findViewById(R.id.ruler2overlay);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        circles[0].setCenterX(prefs.getFloat(PREF_RULER2_CIRCLE1_X, 0));
        circles[0].setCenterY(prefs.getFloat(PREF_RULER2_CIRCLE1_Y, 0));
        circles[1].setCenterX(prefs.getFloat(PREF_RULER2_CIRCLE2_X, 0));
        circles[1].setCenterY(prefs.getFloat(PREF_RULER2_CIRCLE2_Y, 0));
    }

    @Override
    public void onPause() {
        super.onPause();
        circles = overlay.getCircles();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit()
                .putFloat(PREF_RULER2_CIRCLE1_X, circles[0].getCenterX())
                .putFloat(PREF_RULER2_CIRCLE1_Y, circles[0].getCenterY())
                .putFloat(PREF_RULER2_CIRCLE2_X, circles[1].getCenterX())
                .putFloat(PREF_RULER2_CIRCLE2_Y, circles[1].getCenterY())
                .apply();
    }
}
