package com.patrickz.cocktailbossfree;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FavoritesManager
{
    private static final String LOGTAG = "FavoritesManager";

    private String PREFERENCE = "favorites";

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public FavoritesManager(Context context)
    {
        settings = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public JSONArray getUuids()
    {
        String set = settings.getString(PREFERENCE, "[]");
        return new JSONArray(set);
    }

    public void add(String uuid)
    {
        JSONArray array = getUuids();

        if (Simple.include(array, uuid) == -1) array.put(uuid);

        editor.putString(PREFERENCE, array.toString());
        editor.apply();
        editor.commit();
    }

    public void remove(String uuid)
    {
        JSONArray array = getUuids();

        int index = Simple.include(array, uuid);
        if (index > -1) array.remove(index);

        editor.putString(PREFERENCE, array.toString());
        editor.apply();
        editor.commit();
    }

    public boolean includes(String uuid)
    {
        JSONArray array = getUuids();

        int index = Simple.include(array, uuid);
        if (index > -1) return true;

        return false;
    }

    public JSONArray getAllCocktails()
    {
        JSONArray uuids = getUuids();

        JSONObject cocktails = MainActivity.simpleCocktail.cocktails;

        JSONArray savedCocktails = new JSONArray();

        for (int inx = 0; inx < uuids.length(); inx++)
        {
            String uuid = uuids.getString(inx);
            savedCocktails.put(cocktails.getJSONObject(uuid));
        }

        return savedCocktails;
    }
}