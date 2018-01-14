package com.patrickz.cocktailboss;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CircleView extends View
{
    private static final int STROCKEWIDTH = 5;

    private int circleColor = Color.BLACK;
    private int strokColor = Color.BLACK;

    private Paint paint;
    private Paint paintBorder;

    public CircleView(Context context)
    {
        super(context);

        paint = new Paint();
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setStrokeWidth(STROCKEWIDTH);
        paintBorder.setStyle(Paint.Style.STROKE);
    }

    public void setCircleColor(int circleColor, int strokColor)
    {
        this.circleColor = circleColor;
        this.strokColor = strokColor;

        invalidate();
    }

    public void setCircleColor(String circleColor, String strokColor)
    {
        this.circleColor = Color.parseColor(circleColor);
        this.strokColor = Color.parseColor(strokColor);

        invalidate();
    }

    public void setCircleColor(String circleColor)
    {
        setCircleColor(circleColor, circleColor);
    }

    public void setCircleColor(int circleColor)
    {
        setCircleColor(circleColor, circleColor);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        int usableWidth  = w - (pl + pr);
        int usableHeight = h - (pt + pb);

        int radius = (Math.min(usableWidth, usableHeight) - STROCKEWIDTH * 2) / 2;
        int cx = pl + (usableWidth  / 2);
        int cy = pt + (usableHeight / 2);

        paint.setColor(circleColor);
        paintBorder.setColor(strokColor);

        canvas.drawCircle(cx, cy, radius, paint);
        canvas.drawCircle(cx, cy, radius, paintBorder);
    }
}
