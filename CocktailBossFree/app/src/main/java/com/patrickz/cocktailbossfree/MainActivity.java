package com.patrickz.cocktailbossfree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

public class MainActivity extends Activity
{
    private final static String LOGTAG = "MainActivity";

    public static CocktailHelper simpleCocktail;

    public static LinearLayout createMenuEntry(Context context, int imgId, int titleId, LinearLayout contentLayout)
    {
        LinearLayout layout = SimpleLayout.getLayoutPadding(context, 20, 10, 20, 10);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setClickable(true);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        contentLayout.addView(layout);

        RelativeLayout imgView = SimpleLayout.getImgCircle(
            context,
            imgId,
            60,
            30,
            "#3d3d3d",
            2,
            "#ff4d4d");

        layout.addView(imgView);

        TextView text = new TextView(context);
        text.setText(titleId);
        text.setTextSize(30f);
        text.setTextColor(Color.parseColor("#1f1f1f"));
        text.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(text, 20, 0, 0, 0);

        layout.addView(text);

        return layout;
    }

    private void createIntent(int titleId, Class intentClass)
    {
        Intent intent = new Intent(getApplicationContext(), intentClass);
        intent.putExtra("title", Simple.getText(this, titleId));

        if (titleId == R.string.random)
        {
            intent.putExtra("cocktail", CocktailDetails.getRandomCocktail());
            intent.putExtra("random", true);
        }

        startActivity(intent);
    }

    private void createMenu(LinearLayout contentLayout)
    {
        int imgIds[] = {
            R.drawable.star_filled,
            R.drawable.cocktail,
            R.drawable.search,
            R.drawable.bar_stool,
            R.drawable.shopping,
            R.drawable.scan
        };

        int titles[] = {
            R.string.favorites,
            R.string.own_creations,
            R.string.search,
            R.string.i_have_label,
            R.string.shopping_list,
            R.string.scan
        };

        Class classes[] = {
            CocktailBunch.class,
            OwnCreationActivity.class,
            SearchMenuActivity.class,
            MyBarMenuActivity.class,
            ShoppingListActivity.class,
            QrCodeScanner.class
        };

        for (int inx = 0; inx < titles.length; inx++)
        {
            final int titleId = titles[ inx ];
            final Class intentClass = classes[ inx ];

            LinearLayout menuEntry = createMenuEntry(this, imgIds[ inx ], titleId, contentLayout);

            menuEntry.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    createIntent(titleId, intentClass);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Simple.addMenuItem(menu, Simple.getText(this, R.string.about), R.drawable.about);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SimpleAd.createInterstitialAd(this);

        simpleCocktail = new CocktailHelper(this);

        Map<String, Object> layout = SimpleAd.createContentLayout(this, R.string.app_name);
        LinearLayout contentLayout = (LinearLayout) layout.get("scrollLayout");

        //
        // create menu
        //

        createMenu(contentLayout);
    }
}
