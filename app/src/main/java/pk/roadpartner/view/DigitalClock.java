package pk.roadpartner.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by User on 3/27/2016.
 */
public class DigitalClock extends Button {

    public DigitalClock(Context context) {
        super(context);
        inti();
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        inti();
    }

    public DigitalClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inti();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DigitalClock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inti();
    }

    private void inti() {
        Typeface tf_normal = Typeface.createFromAsset(getContext().getAssets(),
                "DSEG7Modern_Bold.ttf");
        setTypeface(tf_normal);
    }
}
