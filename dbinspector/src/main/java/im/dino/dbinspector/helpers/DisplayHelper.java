package im.dino.dbinspector.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by dino on 27/02/14.
 */
public class DisplayHelper {

    public static int dpToPx(Context context, int dp) {

        Resources r = context.getResources();
        return (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());

    }

    public static int spToPx(Context context, int sp) {

        Resources r = context.getResources();
        return (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());

    }

}
