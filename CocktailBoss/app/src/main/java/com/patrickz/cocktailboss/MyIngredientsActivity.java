package com.patrickz.cocktailboss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MyIngredientsActivity extends Activity
{
    private static final String LOGTAG = "MyIngredientsActivity";

    private LinearLayout scrollLayout;
    private MyIngredientsManager manager;

    private String infoBoxText(String name)
    {
        JSONObject info = MainActivity.simpleCocktail.ingredients_db.getJSONObject(name);

//        "alcoholic": true,
//        "alcohol_content": 40,
//        "color": "#1eefed"

        boolean alcoholic = info.getBoolean("alcoholic");

        String text = "";

        text += Simple.getText(this, R.string.alcoholic) + ": ";
        text += alcoholic;

        if (! alcoholic) return text;

        text += "\n";
        text += Simple.getText(this, R.string.alcohol_percent) + ": ";
        text += info.get("alcohol_content") + "%";

        return text;
    }

    private void addInfoBox(LinearLayout box, String name)
    {
        LinearLayout layout = SimpleLayout.getLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        box.addView(layout);

        CircleView circleView = new CircleView(this);
        circleView.setCircleColor(MainActivity.simpleCocktail.getColor(name));
        circleView.setLayoutParams(SimpleLayout.getLayoutParams(40, 40));

        layout.addView(circleView);

        TextView text = new TextView(this);
        text.setLayoutParams(SimpleLayout.getLayoutParamsMargin(-1, -2, 20, 0, 0, 0));
        text.setText(infoBoxText(name));

        layout.addView(text);
    }

    private void addIngredient(final String name)
    {
        LinearLayout box = SimpleLayout.getBox(this, name);
        scrollLayout.addView(box);

        box.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), MyIngredientsEditActivity.class);
                intent.putExtra("ingredient", name);
                intent.putExtra("json", MainActivity.simpleCocktail.ingredients_db.getJSONObject(name).toString());

                startActivity(intent);
            }
        });

        addInfoBox(box, name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Simple.addMenuItem(menu, Simple.getText(this, R.string.add_ingredient), R.drawable.add);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent = new Intent(getApplicationContext(), MyIngredientsEditActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        scrollLayout.removeAllViewsInLayout();

        JSONObject myIngredients = manager.getIngredients();

        List<String> sortList = new ArrayList<>(myIngredients.keySet());
        Collections.sort(sortList);

        for (String name: sortList)
        {
            addIngredient(name);
        }

        if (sortList.isEmpty())
        {
            SimpleLayout.createNoneNote(this, scrollLayout, R.string.no_ingredients);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleLayout.createContentLayout(this, R.string.my_ingredients);
        scrollLayout = (LinearLayout) layout.get("scrollLayout");

        manager = new MyIngredientsManager(this);

//        JSONObject test = new JSONObject();
//        test.put("alcoholic", true);
//        test.put("alcohol_content", 40);
//        test.put("color", "#ff00ee");
//
//        manager.addIngredient("TestIngredient 3", test);
//
//        JSONObject test1 = new JSONObject();
//        test1.put("alcoholic", false);
//        test1.put("alcohol_content", 0);
//        test1.put("color", "#ff0033");
//
//        manager.addIngredient("TestIngredient 2", test1);
    }
}
