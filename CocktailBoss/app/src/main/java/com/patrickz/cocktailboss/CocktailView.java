package com.patrickz.cocktailboss;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CocktailView extends View
{
    private final static String LOGTAG = "CustomView";

    private final static String STROCKE_COLOR = "#e2e2e2";
    private final static String FILL_COLOR    = "#e2e2e2";

    private Paint paint;
    private Paint paintLine;
    private Path  glass;

    private int ingredients;
    private JSONArray color;
    private JSONArray sizes;

    private Paint liquids[];
    private Path  liquidPaths[];

    private int width;
    private int height;

    private int standWidth;
    private int standHeight;

    private int glassHeight;

    private int PAD;

    public CocktailView(Context context, JSONObject json, JSONObject layout)
    {
        super(context);

        PAD         = layout.getInt("padding");
        width       = layout.getInt("glassWidth");
        height      = layout.getInt("glassHeight");
        standWidth  = (int) Math.round(width * 0.65);
        standHeight = (int) Math.round(height * 0.15);

        setLayoutParams(new ViewGroup.LayoutParams(width + PAD * 2, height + PAD * 2));

        glassHeight = height - standHeight;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setStrokeWidth(6f);
//        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(FILL_COLOR));

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paintLine.setAntiAlias(true);
//        paintLine.setStrokeWidth(30f);
        paintLine.setStrokeWidth(10f);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setColor(Color.parseColor(STROCKE_COLOR));

        paintLine.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        paintLine.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        paintLine.setDither(true);                    // set the dither to true

//        paintLine.setStrokeCap(Paint.Cap.ROUND);
//        paintLine.setPathEffect(new CornerPathEffect(25f));
//        paint.setPathEffect(new CornerPathEffect(25f));

        glass = new Path();

        ingredients = json.getInt("ingredients");
//        color       = json.getJSONArray("color");
//        sizes       = json.getJSONArray("sizes");
        color       = json.getJSONArray("color");
        sizes       = json.getJSONArray("sizes");

        liquids     = new Paint[ ingredients ];
        liquidPaths = new Path[  ingredients ];

        for (int inx = 0; inx < ingredients; inx++)
        {
//            createliquidEntry(inx, color[ inx ]);

            createliquidEntry(inx, color.getString(inx));
        }
    }

    private void createliquidEntry(int index, String color)
    {
        Paint liquid = new Paint(Paint.ANTI_ALIAS_FLAG);
        liquid.setStrokeWidth(6f);
        liquid.setStyle(Paint.Style.FILL);
        liquid.setColor(Color.parseColor(color));

        liquids[ index ] = liquid;
        liquidPaths[ index ] = new Path();
    }

    private void drawGlass(Canvas canvas)
    {
        //
        // A -------> B
        //
        glass.moveTo(PAD,         PAD);
        glass.lineTo(width + PAD, PAD);

        //    / B
        //   /
        //  / C
        glass.lineTo(width - (width - standWidth) / 2 + PAD, height - standHeight + PAD);

        //
        // D <------> C
        //
        glass.lineTo(        (width - standWidth) / 2 + PAD, height - standHeight + PAD);
        glass.lineTo(width - (width - standWidth) / 2 + PAD, height - standHeight + PAD);

        //
        //  D |         | C
        //    |         |
        //  F |_________| E
//        glass.lineTo(width - (width - standWidth) / 2 + PAD, height + PAD);
//        glass.lineTo(        (width - standWidth) / 2 + PAD, height + PAD);

        float bottomWidth = downLine(height);

        glass.lineTo(width - bottomWidth + PAD, height + PAD);
        glass.lineTo(        bottomWidth + PAD, height + PAD);

        glass.lineTo(        (width - standWidth) / 2 + PAD, height - standHeight + PAD);

        //  \
        //   \
        //    \
        glass.lineTo(PAD, PAD);

        //
        // finish
        //
        glass.close();

        canvas.drawPath(glass, paint);
        canvas.drawPath(glass, paintLine);
    }

    private float downLine(float fx)
    {
        float maxY = (width - standWidth) / 2;

        // maxY / maxX (glassHeight)
        float gradient = maxY / glassHeight;

        // g(x) = -gradient * fx + width
        // G(x) = -gradient / 2 * x^2 + width * x
        // f(x) = gradient * fx
        // F(x) = gradient / 2 * x^2 = (width - standWidth) * (height - standHeight) * 0.5
        //      =          x^2 = ((width - standWidth) * (height - standHeight) * 0.5) / (gradient / 2)
        //      =  x = Math.sqrt(((width - standWidth) * (height - standHeight) * 0.5) / (gradient / 2))
        // --> f(x) = posi

        return gradient * fx;
    }

    private void drawLiquid(Canvas canvas, Path liquidPath, Paint paint, float top, float bottom)
    {
        int topPadding = (int) (glassHeight * top);
        float downLine = downLine(topPadding);

        int bottomHeight = (int) (glassHeight * (1 - bottom));
        float bottomWidth = downLine(bottomHeight);

        //
        // A -------> B
        //
        liquidPath.moveTo(downLine         + PAD, topPadding + PAD);
        liquidPath.lineTo(width - downLine + PAD, topPadding + PAD);

        //    / B
        //   /
        //  / C
        liquidPath.lineTo(width - bottomWidth + PAD, bottomHeight + PAD);

        //
        // D _______ C
        //
        liquidPath.lineTo(bottomWidth + PAD, bottomHeight + PAD);

        //  \
        //   \
        //    \
        liquidPath.lineTo(downLine + PAD, topPadding + PAD);

        //
        // finish
        //
        liquidPath.close();

        canvas.drawPath(liquidPath, paint);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        drawGlass(canvas);

        float last = 0.0f;

        for (int inx = 0; inx < ingredients; inx++)
        {
//            float size = sizes[ inx ];
            float size = Float.valueOf(sizes.getString(inx));

            drawLiquid(canvas, liquidPaths[ inx ], liquids[ inx ], last, 1 - (last + size));

            last += size;
        }
    }
}