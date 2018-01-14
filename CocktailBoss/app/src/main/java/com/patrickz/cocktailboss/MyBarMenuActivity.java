package com.patrickz.cocktailboss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Map;

public class MyBarMenuActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleLayout.createContentLayout(this, R.string.i_have_label);
        LinearLayout contentLayout = (LinearLayout) layout.get("scrollLayout");

//        LinearLayout iHaveIngredients = MainActivity.createMenuEntry(this, R.drawable.bottle, R.string.my_bar_offers, contentLayout);
        LinearLayout iHaveIngredients = MainActivity.createMenuEntry(this, R.drawable.shaker, R.string.my_bar_offers, contentLayout);
        iHaveIngredients.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), IHaveActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout extraIngredients = MainActivity.createMenuEntry(this, R.drawable.lemon, R.string.my_ingredients, contentLayout);
        extraIngredients.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), MyIngredientsActivity.class);
                startActivity(intent);
            }
        });
    }
}
