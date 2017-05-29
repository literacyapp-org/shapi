package fr.tvbarthel.apps.shapi.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * A rectangle {@link Shape}
 */
public class Rectangle implements Shape {

    @Override
    public void draw(Canvas canvas, RectF rect, Paint paint) {
        if (rect.width() == rect.height()) {
            canvas.drawRect(rect.left, (rect.top + rect.centerY()) / 2f, rect.right,
                    (rect.bottom + rect.centerY()) / 2f, paint);
        } else {
            canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint);
        }
    }
}