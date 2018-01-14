package com.patrickz.cocktailboss;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.simple.JSONArray;

public class IHaveManager
{
    private static final String LOGTAG = "IHaveManager";
    private static final String PREFERENCE = "OwnIngredients";

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public IHaveManager(Context context)
    {
        settings = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public JSONArray getAll()
    {
        String set = settings.getString(PREFERENCE, "[]");
        return new JSONArray(set);
    }

    public void add(String ingredient)
    {
        JSONArray array = getAll();

        if (Simple.include(array, ingredient) == -1) array.put(ingredient);

        editor.putString(PREFERENCE, array.toString());
        editor.apply();
        editor.commit();
    }

    public void remove(String ingredient)
    {
        JSONArray array = getAll();

        int index = Simple.include(array, ingredient);
        if (index > -1) array.remove(index);

        editor.putString(PREFERENCE, array.toString());
        editor.apply();
        editor.commit();
    }
}