package com.patrickz.cocktailbossfree;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SimpleLayout
{
//    public static int dpToPx(int dp)
//    {
//        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
//    }
//
//    public static int pxToDp(int px)
//    {
//        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
//    }

    public static int getSize(int size)
    {
        return (int) (size * Resources.getSystem().getDisplayMetrics().density);
    }

    public static RelativeLayout getImgCircle(
        Context context,
        int imageResource,
        int size,
        int imgSize,
        String color,
        int strokeSize,
        String strokeColor)
    {
        size    = getSize(size);
        imgSize = getSize(imgSize);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(params);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.setBackground(
            SimpleLayout.roundedCorners(1000, color, strokeSize, strokeColor));

        RelativeLayout.LayoutParams imgParms = new RelativeLayout.LayoutParams(imgSize, imgSize);
        imgParms.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView imgView = new ImageView(context);
        imgView.setLayoutParams(imgParms);
        imgView.setImageResource(imageResource);

        relativeLayout.addView(imgView);

        return relativeLayout;
    }

    public static GradientDrawable roundedCorners(int radius, int color, int strokeSize, int strokeColor)
    {
        GradientDrawable gdrawable = new GradientDrawable();
        gdrawable.setCornerRadius(getSize(radius));
        gdrawable.setColor(color);
        gdrawable.setStroke(getSize(strokeSize), strokeColor);

        return gdrawable;
    }

    public static GradientDrawable roundedCorners(int radius, String color, int strokeSize, String strokeColor)
    {
        return roundedCorners(radius, Color.parseColor(color), strokeSize, Color.parseColor(strokeColor));
    }

    public static GradientDrawable roundedCorners(int radius, String color)
    {
        GradientDrawable gdrawable = new GradientDrawable();
        gdrawable.setCornerRadius(radius);
        gdrawable.setColor(Color.parseColor(color));

        return gdrawable;
    }

    public static final int scrollPadding = 20;

    public static Map<String, Object> createContentLayout(Activity context, String title)
    {
        Map<String, Object> map = new HashMap<>();

        //
        // main Layout
        //

        LinearLayout mainLayout = getLayout(context, -1, -1);
        context.setContentView(mainLayout);

        map.put("mainLayout", mainLayout);

        //
        // Toolbar
        //

        // -1 => MATCH_PARENT, -2 => WRAP_CONTENT
        Toolbar.LayoutParams toolBar = new Toolbar.LayoutParams(-1, -2);

        Toolbar toolbar = new Toolbar(context);
        toolbar.setTitle(title);
        toolbar.setLayoutParams(toolBar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        mainLayout.addView(toolbar);

        map.put("toolbar", toolbar);

        context.setActionBar(toolbar);

        //
        // content Layout
        //

        LinearLayout contentLayout = getLayout(context, -1, -1);
        mainLayout.addView(contentLayout);

        map.put("contentLayout", contentLayout);

        //
        // Scroll
        //

        LinearLayout scroll = createScrollView(context, contentLayout);

        map.put("scrollLayout", scroll);

        return map;
    }

    public static Map<String, Object> createContentLayout(Activity context, int titleId)
    {
        return createContentLayout(context, Simple.getText(context, titleId));
    }

    public static Map<String, Object> createContentLayoutNoAds(Activity context, int titleId)
    {
        return createContentLayout(context, Simple.getText(context, titleId));
    }

    public static LinearLayout createScrollView(Activity context, ViewGroup layout)
    {
        //
        // Scroll
        //

        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(-1, -1));

        layout.addView(scrollView);

        //
        // Layout for scrolling
        //

        LinearLayout scrollLayout = getLayout(context, -1, -1);
        scrollLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        scrollView.addView(scrollLayout);

        return scrollLayout;
    }

    public static LinearLayout.LayoutParams getLayoutParams(int width, int height)
    {
        if (width  > 0) width  = getSize(width);
        if (height > 0) height = getSize(height);

        return new LinearLayout.LayoutParams(width, height);
    }

    public static LinearLayout.LayoutParams getLayoutParamsMargin(int width, int height, int left, int top, int right, int bottom)
    {
        LinearLayout.LayoutParams layoutParams = getLayoutParams(width, height);
        layoutParams.setMargins(
            getSize(left),
            getSize(top),
            getSize(right),
            getSize(bottom)
        );

        return layoutParams;
    }

    public static LinearLayout getLayout(Context context, int width, int height)
    {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(getLayoutParams(width, height));

        return layout;
    }

    public static LinearLayout getLayout(Context context)
    {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(getLayoutParams(-1, -2));

        return layout;
    }

    public static void setPadding(View view, int left, int top, int right, int bottom)
    {
        view.setPadding(
            getSize(left),
            getSize(top),
            getSize(right),
            getSize(bottom));
    }

    public static LinearLayout getLayoutPadding(Context context, int width, int height, int left, int top, int right, int bottom)
    {
        LinearLayout layout = getLayout(context, width, height);
        setPadding(layout, left, top, right, bottom);

        return layout;
    }

    public static LinearLayout getLayoutPadding(Context context, int left, int top, int right, int bottom)
    {
        LinearLayout layout = getLayout(context);
        setPadding(layout, left, top, right, bottom);

        return layout;
    }

    public static LinearLayout getLayoutMargins(
        Context context, int width, int height, int left, int top, int right, int bottom)
    {
        LinearLayout.LayoutParams params = getLayoutParams(width, height);
        params.setMargins(
            getSize(left),
            getSize(top),
            getSize(right),
            getSize(bottom));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);

        return layout;
    }

    public static LinearLayout getLayoutMargins(Context context, int left, int top, int right, int bottom)
    {
        return getLayoutMargins(context, -1, -2, left, top, right, bottom);
    }

    public final static String boxBorderColor = "#d0d0d0";

    public static LinearLayout getBox(Context context)
    {
        // LinearLayout layout = getLayoutMargins(context, 15, 15, 15, 0);
        // LinearLayout layout = getLayoutMargins(context, 15, 15, 15, 15);
        LinearLayout layout = getLayoutMargins(context, 15, 8, 15, 8);

        setPadding(layout, 10, 10, 10, 10);

        layout.setBackground(SimpleLayout.roundedCorners(15, "#ffffff", 1, boxBorderColor));

        return layout;
    }

    public static LinearLayout getBox(Context context, String title)
    {
        LinearLayout layout = getBox(context);
        setPadding(layout, 20, 20, 20, 20);

        TextView headline = new TextView(context);
        headline.setText(title);
        headline.setTextSize(25f);
        headline.setTextColor(Color.BLACK);
        headline.setLayoutParams(getLayoutParams(-1, -2));

        setPadding(headline, 0, 0, 0, 20);

        layout.addView(headline);

        return layout;
    }

    public static LinearLayout getBox(Context context, int titleId)
    {
        return getBox(context, Simple.getText(context, titleId));
    }

    public final static int INGREDIENT_CIRCLE_SIZE = 40;

    public static LinearLayout getIngredientRow(Context context, JSONObject json)
    {
        String ingredient = json.getString("ingredient");

        LinearLayout row = SimpleLayout.getLayoutPadding(context, 0, 10, 0, 0);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setOrientation(LinearLayout.HORIZONTAL);

        // String color = MainActivity.simpleCocktail.colors.getString(ingredient);
        String color = MainActivity.simpleCocktail.ingredients_db.getJSONObject(ingredient).getString("color");

        CircleView circleView = new CircleView(context);
        circleView.setLayoutParams(SimpleLayout.getLayoutParams(INGREDIENT_CIRCLE_SIZE, INGREDIENT_CIRCLE_SIZE));
        circleView.setCircleColor(color, color);

        row.addView(circleView);

        TextView text = new TextView(context);
        text.setTextSize(20f);
        text.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(text, 20, 0, 0, 0);

//        "unit": "ml",
//        "amount": 120,
//        "ingredient": "Ginger beer",

        text.setText(CocktailHelper.getIngredientString(json));

        row.addView(text);

        return row;
    }

    public static LinearLayout getIngredientRow(Context context, String ingredient)
    {
        LinearLayout row = SimpleLayout.getLayoutPadding(context, 0, 10, 0, 0);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setOrientation(LinearLayout.HORIZONTAL);

        // String color = MainActivity.simpleCocktail.colors.getString(ingredient);
        String color = MainActivity.simpleCocktail.getColor(ingredient);

        CircleView circleView = new CircleView(context);
        circleView.setLayoutParams(SimpleLayout.getLayoutParams(INGREDIENT_CIRCLE_SIZE, INGREDIENT_CIRCLE_SIZE));
        circleView.setCircleColor(color);

        row.addView(circleView);

        TextView text = new TextView(context);
        text.setTextSize(20f);
        text.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(text, 20, 0, 0, 0);

        text.setText(ingredient);

        row.addView(text);

        return row;
    }

    public static void createNote(Context context, LinearLayout layout, String title)
    {
        TextView textView = new TextView(context);
        textView.setText(title);
        textView.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(textView, scrollPadding, 20, scrollPadding, 20);

        layout.addView(textView);
    }

    public static void createNote(Context context, LinearLayout layout, int titleId)
    {
        createNote(context, layout, Simple.getText(context, titleId));
    }

    public static void createNoneNote(Context context, LinearLayout scrollLayout, String text)
    {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20f);
        textView.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(textView, scrollPadding, 20, scrollPadding, 20);

        scrollLayout.addView(textView);
    }

    public static void createNoneNote(Context context, LinearLayout scrollLayout, int textId)
    {
        createNoneNote(context, scrollLayout, Simple.getText(context, textId));
    }

    public static Button createRoundButton(Context context, String title, String color)
    {
        LinearLayout.LayoutParams params = SimpleLayout.getLayoutParamsMargin(-1, 50, 10, 0, 10, 10);

        int hexColor = Color.parseColor(color);
//        int darkColor = Simple.getDarkColor(hexColor);
//        int darkColor = Color.parseColor("#ff4d4d");
        int darkColor = Color.parseColor(boxBorderColor);

        Button button = new Button(context);
        button.setText(title);
        button.setAllCaps(false);
        button.setTextSize(20f);
        button.setTextColor(Color.WHITE);
        button.setBackground(SimpleLayout.roundedCorners(1000, hexColor, 2, darkColor));
        button.setLayoutParams(params);

        return button;
    }

    public static Button createRoundButton(Context context, int titleId, String color)
    {
        return createRoundButton(context, Simple.getText(context, titleId), color);
    }

    private final static String BUTTON_COLOR = "#3d3d3d";

    public static Button createRoundButton(Context context, String title)
    {
        return createRoundButton(context, title, BUTTON_COLOR);
    }

    public static Button createRoundButton(Context context, int titleId)
    {
        return createRoundButton(context, Simple.getText(context, titleId), BUTTON_COLOR);
    }
}
