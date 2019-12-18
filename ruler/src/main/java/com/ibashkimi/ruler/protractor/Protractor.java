package com.ibashkimi.ruler.protractor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Protractor extends FrameLayout {

    private static final String PROTRACTOR_LINE1_SIN = "protractor_line1_sin";
    private static final String PROTRACTOR_LINE1_COS = "protractor_line1_cos";
    private static final String PROTRACTOR_LINE2_SIN = "protractor_line2_sin";
    private static final String PROTRACTOR_LINE2_COS = "protractor_line2_cos";

    private ProtractorOverlay overlay;

    private Line[] lines;

    public Protractor(@NonNull Context context) {
        this(context, null);
    }

    public Protractor(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Protractor(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Protractor(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.lines = new Line[]{new Line(), new Line()};
        ProtractorBoard board = new ProtractorBoard(context, attrs, defStyleAttr, defStyleRes);
        this.overlay = new ProtractorOverlay(context, attrs, defStyleAttr, defStyleRes);
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(board, params);
        this.addView(this.overlay, params);
    }

    public void onSave() {
        SharedPreferences prefs = getPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(PROTRACTOR_LINE1_SIN, lines[0].getSin());
        editor.putFloat(PROTRACTOR_LINE1_COS, lines[0].getCos());
        editor.putFloat(PROTRACTOR_LINE2_SIN, lines[1].getSin());
        editor.putFloat(PROTRACTOR_LINE2_COS, lines[1].getCos());
        editor.apply();
    }

    public void onRestore() {
        SharedPreferences prefs = getPreferences();
        lines[0].setSin(prefs.getFloat(PROTRACTOR_LINE1_SIN, 0.866025404f));
        lines[0].setCos(prefs.getFloat(PROTRACTOR_LINE1_COS, 0.5f));
        lines[1].setSin(prefs.getFloat(PROTRACTOR_LINE2_SIN, 1.0f));
        lines[1].setCos(prefs.getFloat(PROTRACTOR_LINE2_COS, 0.0f));
        this.overlay.setLines(lines);
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }
}
