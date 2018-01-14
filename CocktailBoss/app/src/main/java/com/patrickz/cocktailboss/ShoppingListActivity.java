package com.patrickz.cocktailboss;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.Map;

public class ShoppingListActivity extends Activity
{
    private final static String LOGTAG = "ShoppingListActivity";
    private ShoppingListManager shoppingListManager;

    private LinearLayout scrollLayout;

    private void createListItem(final LinearLayout scrollLayout, final String title, boolean bought)
    {
        final LinearLayout layout = SimpleLayout.getLayoutPadding(this, 20, 20, 20, 0);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        final CheckBox box = new CheckBox(this);
        box.setLayoutParams(SimpleLayout.getLayoutParams(-2, -2));
        layout.addView(box);

        TextView text = new TextView(this);
        text.setText(title);
        text.setGravity(Gravity.CENTER);
        text.setTextSize(30f);
        text.setTextColor(Color.BLACK);

        SimpleLayout.setPadding(text, 15, 0, 0, 0);

        layout.addView(text);

        scrollLayout.addView(layout);

        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (box.isChecked())
                {
                    box.setChecked(false);
                    view.setAlpha(1f);
                    shoppingListManager.setValue(title, false);
                }
                else
                {
                    box.setChecked(true);
                    view.setAlpha(0.5f);
                    shoppingListManager.setValue(title, true);
                }
            }
        });

        box.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (! box.isChecked())
                {
                    layout.setAlpha(1f);
                    shoppingListManager.setValue(title, false);
                }
                else
                {
                    layout.setAlpha(0.5f);
                    shoppingListManager.setValue(title, true);
                }
            }
        });

        layout.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                Toast.makeText(getApplicationContext(),
                    "remove: " + title, Toast.LENGTH_SHORT).show();
                shoppingListManager.removeFromList(title);

                scrollLayout.removeView(layout);

                return true;
            }
        });

        if (bought)
        {
            layout.callOnClick();
        }
    }

    private void createInfoField(LinearLayout scrollLayout)
    {
        TextView textView = new TextView(this);
        textView.setText(R.string.long_touch_note);
        textView.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(textView, 20, 20, 20, 20);

        scrollLayout.addView(textView);
    }

    private String listToShare()
    {
        StringBuilder message = new StringBuilder();

        message.append("*Buy:*\n");

        JSONObject list = shoppingListManager.getShoppingList();
        String[] sortList = Simple.sortJsonKeys(list);

        for (String key: sortList)
        {
            boolean bought = list.getBoolean(key);

            message.append("-  ");

            if (bought) message.append("~");
            message.append(key);
            if (bought) message.append("~");

            message.append("\n");
        }

        return message.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Simple.addMenuItem(menu, "Add",   R.drawable.add);
        Simple.addMenuItem(menu, "Share", R.drawable.share);

        return true;
    }

    private void searchAction()
    {
        IngredientsDialogCallback callback = new IngredientsDialogCallback()
        {
            @Override
            public void ingredientsSelected(String ingredient)
            {
                shoppingListManager.addToList(ingredient);
                update();
            }
        };

        IngredientsDialog ingredientDialog = new IngredientsDialog(this, callback);
        ingredientDialog.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getTitle().toString().equals("Share"))
        {
            Simple.shareIntent(this, listToShare());
        }

        if (item.getTitle().toString().equals("Add"))
        {
            searchAction();
        }

        return super.onOptionsItemSelected(item);
    }

    private void update()
    {
        scrollLayout.removeAllViewsInLayout();

        JSONObject list = shoppingListManager.getShoppingList();
        String[] sortList = Simple.sortJsonKeys(list);

        if (sortList.length <= 0)
        {
            SimpleLayout.createNoneNote(this, scrollLayout, R.string.no_shopping_items);
            return;
        }

        for (String key: sortList)
        {
            createListItem(scrollLayout, key, list.getBoolean(key));
        }

        createInfoField(scrollLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleLayout.createContentLayout(this, R.string.shopping_list);
        scrollLayout = (LinearLayout) layout.get("scrollLayout");

        shoppingListManager = new ShoppingListManager(this);

        update();
    }
}
