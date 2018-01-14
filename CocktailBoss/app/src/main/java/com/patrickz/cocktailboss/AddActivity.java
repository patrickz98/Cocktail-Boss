package com.patrickz.cocktailboss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class AddActivity extends Activity
{
    private final static String LOGTAG = "AddActivity";

    private LinearLayout indLayout;

    private EditText nameEditText;
    private EditText descriptionEditText;
    private ArrayList<JSONObject> ingredients;

    private boolean editMode;

    private JSONObject cocktail;

    public void addRow(final JSONObject ingredient, int index)
    {
        LinearLayout row = SimpleLayout.getIngredientRow(this, ingredient);
        indLayout.addView(row, index);

        row.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                indLayout.removeView(view);
                ingredients.remove(ingredient);

                return true;
            }
        });

        final AddDialog addDialog = new AddDialog(this);

        row.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addDialog.editIngredient(ingredient, view);
            }
        });

        // ingredients.put(ingredient);
        ingredients.add(ingredient);
    }

    public void addRow(JSONObject ingredient)
    {
        addRow(ingredient, 0);
    }

    public void updateRow(View view, JSONObject ingredient)
    {
        int index = ((ViewGroup) view.getParent()).indexOfChild(view);

        indLayout.removeView(view);
        ingredients.remove(ingredient);

        addRow(ingredient, index);
    }

    private void addIngredientsList(LinearLayout layout)
    {
        indLayout = SimpleLayout.getLayoutPadding(this, 10, 10, 10, 10);
        layout.addView(indLayout);

        LinearLayout row = SimpleLayout.getLayoutPadding(this, 0, 5, 0, 0);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setOrientation(LinearLayout.HORIZONTAL);

        indLayout.addView(row);

        RelativeLayout imgCricle = SimpleLayout.getImgCircle(
            this,
            R.drawable.add,
            SimpleLayout.INGREDIENT_CIRCLE_SIZE,
            20,
            "#696969",
            1,
            "#696969");

        row.addView(imgCricle);

        TextView text = new TextView(this);
        SimpleLayout.setPadding(text, 20, 0, 0, 0);
        text.setTextSize(20f);
        text.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));
        text.setText(Simple.getText(this, R.string.add_ingredient));

        final AddActivity self = this;

        row.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AddDialog addDialog = new AddDialog(self);
                addDialog.addIngredient();
            }
        });

        row.addView(text);
    }

    private void createIngredientsBox(LinearLayout scrollLayout)
    {
        LinearLayout layout = SimpleLayout.getBox(this, R.string.ingredients);
        scrollLayout.addView(layout);

        addIngredientsList(layout);

        TextView note = new TextView(this);
        note.setText(Simple.getText(this, R.string.long_touch_note));
        note.setGravity(Gravity.END);
        note.setTextColor(Color.GRAY);
        note.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        layout.addView(note);
    }

    private void addCocktailsName(LinearLayout scrollLayout)
    {
        LinearLayout layout = SimpleLayout.getBox(this, R.string.cocktail_name);
        scrollLayout.addView(layout);

        nameEditText = new EditText(this);
        nameEditText.setHint(Simple.getText(this, R.string.name));
//        editText.setTextSize(25f);
//        editText.setMaxHeight(1);
//        editText.setTextColor(Color.BLACK);
        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        nameEditText.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));
        nameEditText.setSelected(false);

        layout.addView(nameEditText);
    }

    private void summit()
    {
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        if (name.equals(""))
        {
            Simple.toastS(this, R.string.no_name);

            nameEditText.requestFocus();

            return;
        }

        if (ingredients.size() <= 0)
        {
            Simple.toastS(this, R.string.no_ingredients);
            return;
        }

        if (description.equals("")) description = Simple.getText(this, R.string.mix_all_ingredients);

        cocktail.put("name",        name);
        cocktail.put("description", description);
        cocktail.put("ingredients", new JSONArray(ingredients));
        cocktail.put("glass",       "Highball glass");

        OwnCreationManager manager = new OwnCreationManager(this);

        Intent intent = new Intent(getApplicationContext(), CocktailDetails.class);

        if (editMode)
        {
            boolean updated = manager.updateCocktail(cocktail);

            if (! updated)
            {
                Simple.toastL(this, R.string.name_exist_already);
                return;
            }

            Simple.toastS(this, Simple.getText(this, R.string.update) + ": " + name);

            intent.putExtra("cocktail", cocktail.toString());
            startActivity(intent);

            finish();
            return;
        }

        boolean added = manager.addCocktail(cocktail);

        if (added)
        {
            Simple.toastS(this, Simple.getText(this, R.string.added) + ": " + name);

            intent.putExtra("cocktail", cocktail.toString());
            startActivity(intent);

            finish();
            return;
        }

        Simple.toastL(this, R.string.name_exist_already);
    }

    private void exitDialog()
    {
        if ((nameEditText.getText().toString().equals("")) && (ingredients.size() <= 0))
        {
            finish();
            return;
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.really_exit);
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });
        dialog.setNegativeButton(R.string.no,  null);
        dialog.show();
    }

    private void createCancelButton(LinearLayout scrollLayout)
    {
        View.OnClickListener event = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                exitDialog();
            }
        };

        Button button = SimpleLayout.createRoundButton(this, R.string.cancel);
        button.setOnClickListener(event);

        scrollLayout.addView(button);
    }

    private void createOkButton(LinearLayout scrollLayout)
    {
        View.OnClickListener event = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                summit();
            }
        };

//        Button button = SimpleLayout.createRoundButton(this, R.string.ok, "#6bffa0");
        Button button = SimpleLayout.createRoundButton(this, R.string.ok);
        button.setOnClickListener(event);

        scrollLayout.addView(button);
    }

    private void createDescription(LinearLayout scrollLayout)
    {
        LinearLayout layout = SimpleLayout.getBox(this, R.string.description);
        scrollLayout.addView(layout);

        descriptionEditText = new EditText(this);
        descriptionEditText.setHint(R.string.mix_all_ingredients);
        descriptionEditText.setSelected(false);
        descriptionEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        descriptionEditText.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        layout.addView(descriptionEditText);
    }

    private void editMode()
    {
        Intent intent = getIntent();
        String dataString = intent.getStringExtra("cocktail");

        editMode = (dataString != null);

        if (! editMode) return;

        cocktail = new JSONObject(dataString);

        JSONArray ingredients = cocktail.getJSONArray("ingredients");

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            JSONObject ingredientPart = ingredients.getJSONObject(inx);
            addRow(ingredientPart);
        }

        nameEditText.setText(cocktail.getString("name"));
        descriptionEditText.setText(cocktail.getString("description"));
    }

    @Override
    public void onBackPressed()
    {
        exitDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleLayout.createContentLayoutNoAds(this, R.string.i_have_label);
        LinearLayout scrollLayout = (LinearLayout) layout.get("scrollLayout");

        // Disable EditText auto focus
        scrollLayout.setFocusableInTouchMode(true);

        addCocktailsName(scrollLayout);

        // ## AdMarkerBroke BANNER this --> scrollLayout --> com.google.android.gms.ads.AdSize.LARGE_BANNER ##

        ingredients = new ArrayList<>();
        cocktail    = new JSONObject();

        createIngredientsBox(scrollLayout);

        // ## AdMarkerBroke BANNER this --> scrollLayout --> com.google.android.gms.ads.AdSize.LARGE_BANNER ##

        createDescription(scrollLayout);

        LinearLayout buttonLayout = SimpleLayout.getLayoutPadding(this, 0, 8, 0, 0);

        createOkButton(buttonLayout);
        createCancelButton(buttonLayout);

        scrollLayout.addView(buttonLayout);

        editMode();
    }
}
