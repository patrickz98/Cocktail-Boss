package com.patrickz.cocktailbossfree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public class CocktailBunch extends Activity
{
    private final static String LOGTAG = "MainActivity";

    private FavoritesManager favoritesManager;

    private LinearLayout scrollLayout;

    public static void createText(Context context, LinearLayout layout, JSONObject cocktail)
    {
        LinearLayout mainLayout = SimpleLayout.getBox(context);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setGravity(Gravity.CENTER_VERTICAL);

        layout.addView(mainLayout);

        JSONObject layoutJson = new JSONObject();
        layoutJson.put("padding",     SimpleLayout.getSize(20));
        layoutJson.put("glassWidth",  SimpleLayout.getSize(55));
        layoutJson.put("glassHeight", SimpleLayout.getSize(110));

        JSONObject transformedCocktail = MainActivity.simpleCocktail.transformCocktail(cocktail);
        CocktailView view = new CocktailView(context, transformedCocktail, layoutJson);
        mainLayout.addView(view);

        LinearLayout textLayout = SimpleLayout.getLayout(context);
        mainLayout.addView(textLayout);

        TextView headline = new TextView(context);
        headline.setText(cocktail.getString("name"));
        headline.setTextSize(25f);
        headline.setTextColor(Color.parseColor("#141414"));
        headline.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(headline, 10, 5, 10, 5);

//        headline.setText(Html.fromHtml("<u>" + cocktail.getString("name") + "</u>"));
//        headline.setText(Html.fromHtml("<u>Text</u>"));

        textLayout.addView(headline);

        TextView textView = new TextView(context);

        textView.setText(CocktailHelper.getAllIngredientString(cocktail));

        textView.setTextColor(Color.parseColor("#787878"));
        textView.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(textView, 10, 5, 10, 5);

        textLayout.addView(textView);
    }

    public static void createEntries(
        final Activity context,
        final LinearLayout scrollLayout,
        final JSONArray cocktails,
        final boolean ownCocktail)
    {
        for (int inx = 0; inx < cocktails.length(); inx++)
        {
            final JSONObject cocktail = cocktails.getJSONObject(inx);

            final LinearLayout layout = SimpleLayout.getLayout(context);
            layout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context, CocktailDetails.class);
                    intent.putExtra("cocktail", cocktail.toString());
                    intent.putExtra("ownCocktail", ownCocktail);
                    context.startActivity(intent);
                }
            });

            createText(context, layout, cocktail);
            scrollLayout.addView(layout);
        }
    }

    public static void createEntries(
        final Activity context,
        final LinearLayout scrollLayout,
        final JSONArray cocktails)
    {
        createEntries(
            context,
            scrollLayout,
            cocktails,
            false);
    }

    public static void createOwnCocktailsEntries(
        final Activity context,
        final LinearLayout scrollLayout,
        final JSONArray cocktails)
    {
        createEntries(
            context,
            scrollLayout,
            cocktails,
            true);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        scrollLayout.removeAllViewsInLayout();

        favoritesManager = new FavoritesManager(this);
        JSONArray cocktails = favoritesManager.getAllCocktails();

        if (cocktails.length() <= 0)
        {
            SimpleLayout.createNoneNote(this, scrollLayout, R.string.no_items);
            return;
        }

        cocktails = Simple.sortJSONArray(cocktails);

        createEntries(this, scrollLayout, cocktails);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String title  = intent.getStringExtra("title");

        Map<String, Object> layout = SimpleAd.createContentLayout(this, title);
        scrollLayout = (LinearLayout) layout.get("scrollLayout");
    }
}
