package com.patrickz.cocktailbossfree;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.simple.JSONObject;

public class ShoppingListManager
{
    private static final String LOGTAG = "ShoppingListManager";

    public static final String PREFERENCE = "SHOPPING";

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public ShoppingListManager(Context context)
    {
        settings = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public JSONObject getShoppingList()
    {
        String set = settings.getString(PREFERENCE, "{}");
        return new JSONObject(set);
    }

    public void addToList(String item)
    {
        JSONObject array = getShoppingList();

        if (! array.has(item)) array.put(item, false);

        editor.putString(PREFERENCE, array.toString());
        editor.apply();
        editor.commit();
    }

    public void setValue(String item, boolean value)
    {
        JSONObject array = getShoppingList();

        if (array.has(item)) array.put(item, value);

        editor.putString(PREFERENCE, array.toString());
        editor.apply();
        editor.commit();
    }

    public void removeFromList(String item)
    {
        JSONObject array = getShoppingList();

        if (array.has(item)) array.remove(item);

        editor.putString(PREFERENCE, array.toString());
        editor.apply();
        editor.commit();
    }
}
