package com.patrickz.cocktailboss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.json.simple.JSONObject;

import java.util.Map;

public class MyIngredientsEditActivity extends Activity
{
    private static final String LOGTAG = "MyIngredientsEditActiv";

    private LinearLayout scrollLayout;
    private MyIngredientsManager manager;

    private EditText nameEditText;
    private SeekBar seekBar;

    private JSONObject ingredientJson;

    private TextView alcohol_content_view;

    private String originalIngredient;

    private CircleView colorCircle;

    private void showColorPicker()
    {
        ColorPickerDialogBuilder builder = ColorPickerDialogBuilder.with(this);

        builder.setTitle(R.string.choose_color);
        builder.density(12);
        builder.noSliders();
        builder.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER);
        builder.initialColor(Color.CYAN);
        // builder.showColorPreview(true);
        builder.showColorEdit(true);
        builder.setColorEditTextColor(Color.BLACK);

        builder.setPositiveButton("ok", new ColorPickerClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors)
            {
                String color = "#" + Integer.toHexString(selectedColor).substring(2);
                colorCircle.setCircleColor(color);
                ingredientJson.put("color", color);
            }
        });

        builder.build().show();
    }

    private void addName(String ingredient)
    {
        LinearLayout layout = SimpleLayout.getBox(this, "Ingredient");
        scrollLayout.addView(layout);

        nameEditText = new EditText(this);
        nameEditText.setHint("Ingredient");
        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        nameEditText.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));
        nameEditText.setSelected(false);

        if (ingredient != null)
        {
            nameEditText.setText(ingredient);
        }

        layout.addView(nameEditText);
    }

    private void alcohol_content_view(LinearLayout layout)
    {
        alcohol_content_view = new TextView(this);
        alcohol_content_view.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        layout.addView(alcohol_content_view);
    }

    private void updateAlcoholContent(int progress)
    {
        ingredientJson.put("alcohol_content", progress);
        alcohol_content_view.setText(progress + "%");

        if (progress <= 10) return;

        alcohol_content_view.setTextSize(progress);
    }

    private void alcohol_content(LinearLayout layout)
    {
        alcohol_content_view(layout);

        LinearLayout seekLayout = SimpleLayout.getLayout(this);
        seekLayout.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(seekLayout);

        TextView text = new TextView(this);
        text.setLayoutParams(SimpleLayout.getLayoutParams(-2, -2));
        text.setText(R.string.alcohol_percent);

        seekLayout.addView(text);

        boolean enabled = ingredientJson.optBoolean("alcoholic", false);

        seekBar = new SeekBar(this);
        seekBar.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));
        seekBar.setEnabled(enabled);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                updateAlcoholContent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        int proValue = ingredientJson.optInt("alcohol_content", 0);

        if (! enabled) proValue = 0;

        seekBar.setProgress(proValue);
        updateAlcoholContent(proValue);

        seekLayout.addView(seekBar);
    }

    private void alcoholic()
    {
        LinearLayout layout = SimpleLayout.getBox(this, R.string.alcoholic);
        scrollLayout.addView(layout);

        LinearLayout switchLayout = SimpleLayout.getLayout(this);
        switchLayout.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(switchLayout);

        TextView text = new TextView(this);
        text.setLayoutParams(SimpleLayout.getLayoutParams(-2, -2));
        text.setText(R.string.alcoholic);
        switchLayout.addView(text);

        LinearLayout tmpLayout = SimpleLayout.getLayout(this);
        tmpLayout.setGravity(Gravity.END);
        switchLayout.addView(tmpLayout);

        boolean preChecked = ingredientJson.optBoolean("alcoholic", false);

        Switch switchButton = new Switch(this);
        switchButton.setLayoutParams(SimpleLayout.getLayoutParams(-2, -2));
        switchButton.setChecked(preChecked);
        tmpLayout.addView(switchButton);

        alcohol_content(layout);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
            {
                ingredientJson.put("alcoholic", checked);
                seekBar.setEnabled(checked);

                if (checked)
                {
                    alcohol_content_view.setAlpha(1.0f);
                }
                else
                {
                    alcohol_content_view.setAlpha(0.5f);
                }
            }
        });

        ingredientJson.put("alcoholic", preChecked);
    }

    private void save()
    {
        String newName = nameEditText.getText().toString();

        if (newName.isEmpty())
        {
            Simple.toastL(this, R.string.no_ingredient_name);
            return;
        }

        if ((originalIngredient != null) && newName.equals(originalIngredient))
        {
            manager.update(originalIngredient, ingredientJson);
            finish();
            return;
        }

        manager.addIngredient(newName, ingredientJson);
        manager.replace(originalIngredient, newName);

        finish();
    }

    private void colorPicker()
    {
        LinearLayout colorBox = SimpleLayout.getBox(this, R.string.color);
        colorBox.setGravity(Gravity.CENTER);
        scrollLayout.addView(colorBox);

        colorCircle = new CircleView(this);
        colorCircle.setLayoutParams(SimpleLayout.getLayoutParams(80, 80));

        String preColor = ingredientJson.optString("color", "#00ff00");

        colorCircle.setCircleColor(preColor);
        ingredientJson.put("color", preColor);

        colorCircle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showColorPicker();
            }
        });

        colorBox.addView(colorCircle);
    }

    private void buttons()
    {
        Button save = SimpleLayout.createRoundButton(this, R.string.save);
        scrollLayout.addView(save);

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                save();
            }
        });

        Button cancel = SimpleLayout.createRoundButton(this, R.string.cancel);
        scrollLayout.addView(cancel);

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Simple.addMenuItem(menu, Simple.getText(this, R.string.delete), R.drawable.garbage);
        return true;
    }

    private void replaceWithIngredient()
    {
        IngredientsDialogCallback callback = new IngredientsDialogCallback()
        {
            @Override
            public void ingredientsSelected(String ingredient)
            {
                Simple.toastS(getApplicationContext(), "Old: " + originalIngredient + " New: " + ingredient);
                manager.replace(originalIngredient, ingredient);

                finish();
            }
        };

        IngredientsDialog ingredientsDialog = new IngredientsDialog(this, callback);
        ingredientsDialog.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        DialogInterface.OnClickListener deleteEvent = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Simple.toastS(getApplicationContext(), "Delete: " + originalIngredient);
                manager.remove(originalIngredient);
                finish();
            }
        };

        DialogInterface.OnClickListener replaceEvent = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                replaceWithIngredient();
            }
        };

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(Simple.getText(this, R.string.delete) + ": " + nameEditText.getText().toString() + "?");
//        dialog.setMessage("");
        dialog.setPositiveButton("Delete",  deleteEvent);
        dialog.setNegativeButton("Replace", replaceEvent);
        dialog.setNeutralButton(R.string.cancel, null);
        dialog.show();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleLayout.createContentLayout(this, R.string.my_ingredients_edit);
        scrollLayout = (LinearLayout) layout.get("scrollLayout");

        // Disable EditText auto focus
        scrollLayout.setFocusableInTouchMode(true);

        manager = new MyIngredientsManager(this);

        Intent intent = getIntent();
        originalIngredient = intent.getStringExtra("ingredient");
        String json = intent.getStringExtra("json");

        if (json == null)
        {
            json = "{}";
        }

        ingredientJson = new JSONObject(json);

        addName(originalIngredient);

        // ## AdMarkerBroke BANNER this --> scrollLayout --> com.google.android.gms.ads.AdSize.LARGE_BANNER ##

        alcoholic();
        colorPicker();
        buttons();
    }
}
