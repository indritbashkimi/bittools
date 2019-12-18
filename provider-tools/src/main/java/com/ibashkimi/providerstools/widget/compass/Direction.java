package com.ibashkimi.providerstools.widget.compass;

import android.content.Context;

import com.ibashkimi.providerstools.R;
import com.ibashkimi.theme.utils.MathUtils;


public class Direction {
    private float angleInDegree;
    private String unknown;
    private String north;
    private String northEast;
    private String east;
    private String southEast;
    private String south;
    private String southWest;
    private String west;
    private String northWest;

    Direction(Context context, float angleInDegree) {
        this.angleInDegree = angleInDegree;
        this.unknown = "";
        this.north = "°" + context.getString(R.string.compass_direction_north);
        this.northEast = "°" + context.getString(R.string.compass_direction_north_east);
        this.east = "°" + context.getString(R.string.compass_direction_est);
        this.southEast = "°" + context.getString(R.string.compass_direction_south_east);
        this.south = "°" + context.getString(R.string.compass_direction_south);
        this.southWest = "°" + context.getString(R.string.compass_direction_south_west);
        this.west = "°" + context.getString(R.string.compass_direction_west);
        this.northWest = "°" + context.getString(R.string.compass_direction_north_west);
    }

    public void update(float angle) {
        this.angleInDegree = angle % 360;
    }

    private String getDirection(float angleInDegree) {
        // Note: assuming it's never < 0
        if (angleInDegree < 0)
            return this.unknown;
        if (angleInDegree <= 22)
            return this.north;
        if (angleInDegree <= 67)
            return this.northEast;
        if (angleInDegree <= 112)
            return this.east;
        if (angleInDegree <= 157)
            return this.southEast;
        if (angleInDegree <= 202)
            return this.south;
        if (angleInDegree <= 247)
            return this.southWest;
        if (angleInDegree <= 292)
            return this.west;
        if (angleInDegree <= 337)
            return this.northWest;
        else
            return this.north;
    }

    @Override
    public String toString() {
        return (int) MathUtils.round(angleInDegree, 0) + getDirection(angleInDegree);
    }
}
