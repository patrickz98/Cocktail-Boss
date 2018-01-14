package com.patrickz.cocktailboss;

import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class CocktailSearchHelper
{
    private final static String LOGTAG = "CocktailSearchHelper";

    private CocktailHelper simpleCocktail;

    CocktailSearchHelper()
    {
        this.simpleCocktail = MainActivity.simpleCocktail;
    }

    private boolean checkIngredients(JSONObject cocktail, ArrayList<String> search)
    {
        JSONArray ingredients = cocktail.getJSONArray("ingredients");
        String name = cocktail.getString("name");

        JSONObject allIncluded = new JSONObject();

        for (String searchInd: search)
        {
            allIncluded.put(searchInd, false);
        }

        for (String searchInd: search)
        {
            if ((searchInd.length() == 1) && (name.startsWith(searchInd)))
            {
//                return true;
                allIncluded.put(searchInd, true);
                continue;
            }

            if (searchInd.equals("#") && name.matches("^(?![A-Z]).*$"))
            {
//                return true;
                allIncluded.put(searchInd, true);
                continue;
            }

            for (int inx = 0; inx < ingredients.length(); inx++)
            {
                String ingredient = ingredients.getJSONObject(inx).getString("ingredient");

//                if (ingredient.equals(searchInd)) return true;
                if (ingredient.equals(searchInd))
                {
                    allIncluded.put(searchInd, true);
                    break;
                }
            }
        }

        for (String searchInd: search)
        {
            if (! allIncluded.getBoolean(searchInd)) return false;
        }

        return true;
    }

    public ArrayList<String> getLeftIngredients(JSONArray found)
    {
        ArrayList<String> leftIngredients = new ArrayList<>();

        for (int inx = 0; inx < found.length(); inx++)
        {
            JSONObject cocktail = found.getJSONObject(inx);
            JSONArray ingredients = cocktail.getJSONArray("ingredients");

            for (int iny = 0; iny < ingredients.length(); iny++)
            {
                JSONObject indgredientsPart = ingredients.getJSONObject(iny);
                String ingredient = indgredientsPart.getString("ingredient");

                if (! leftIngredients.contains(ingredient)) leftIngredients.add(ingredient);
            }
        }

        return leftIngredients;
    }

    private void applyOptions(JSONObject cocktail, Map<Integer, Boolean> alcOptions, JSONArray found)
    {
        if (alcOptions.get(R.string.all_alcoholic))
        {
            found.put(cocktail);
            return;
        }

        if (alcOptions.get(R.string.alcoholic) && CocktailHelper.isAlcoholic(cocktail))
        {
            found.put(cocktail);
            return;
        }

        if (alcOptions.get(R.string.non_alcoholic) && (! CocktailHelper.isAlcoholic(cocktail)))
        {
            found.put(cocktail);
        }
    }

    public JSONArray searchForCocktails(ArrayList<String> search, Map<Integer, Boolean> alcOptions)
    {
        JSONObject allCocktails = simpleCocktail.cocktails;
        JSONArray found = new JSONArray();

        for (String key: allCocktails.keySet())
        {
            JSONObject cocktail = allCocktails.getJSONObject(key);

            if (! checkIngredients(cocktail, search)) continue;

            applyOptions(cocktail, alcOptions, found);
        }

        return found;
    }
}
