package com.patrickz.cocktailboss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Map;

public class SearchMenuActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleLayout.createContentLayout(this, R.string.search);
        LinearLayout contentLayout = (LinearLayout) layout.get("scrollLayout");

        LinearLayout searchByText = MainActivity.createMenuEntry(this, R.drawable.search, R.string.search, contentLayout);
        searchByText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

//        LinearLayout findByIngredient = MainActivity.createMenuEntry(this, R.drawable.bottle, R.string.find, contentLayout);
        LinearLayout findByIngredient = MainActivity.createMenuEntry(this, R.drawable.bottle, R.string.search_by_ingredient, contentLayout);
        findByIngredient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout randomCocktail = MainActivity.createMenuEntry(this, R.drawable.random, R.string.random, contentLayout);
        randomCocktail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), CocktailDetails.class);
                intent.putExtra("cocktail", CocktailDetails.getRandomCocktail());
                intent.putExtra("random", true);

                startActivity(intent);
            }
        });
    }
}
