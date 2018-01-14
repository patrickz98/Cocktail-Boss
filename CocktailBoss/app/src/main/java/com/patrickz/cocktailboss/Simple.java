package com.patrickz.cocktailboss;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Simple
{
    public static String getRaw(Context context, int id)
    {
        InputStream is = context.getResources().openRawResource(id);
        Writer writer = new StringWriter();
        char[] buffer = new char[ 1024 ];

        try
        {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1)
            {
                writer.write(buffer, 0, n);
            }

            is.close();
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }

        return dezify(writer.toString());
    }

    public static int[] jsonToColorArray(JSONArray json)
    {
        int array[] = new int[ json.length() ];

        for (int inx = 0; inx < json.length(); inx++)
        {
            array[ inx ] = Color.parseColor(json.getString(inx));
        }

        return array;
    }

    public static JSONArray reverse(JSONArray json)
    {
        JSONArray rjson = new JSONArray();

        for (int inx = json.length() - 1; inx > -1; inx--)
        {
            rjson.put(json.get(inx));
        }

        return rjson;
    }

    public static int include(JSONArray jsonArray, String entry)
    {
        for (int inx = 0; inx < jsonArray.length(); inx++)
        {
            String tmp = jsonArray.getString(inx);
            if (tmp.equals(entry)) return inx;
        }

        return -1;
    }

    public static String[] sortJsonKeys(JSONObject json)
    {
        String[] array = new String[ json.length() ];

        int inx = 0;
        for (String key: json.keySet())
        {
            array[ inx ] = key;
            inx++;
        }

        Arrays.sort(array);

        return array;
    }

    public static JSONArray sortJSONArray(JSONArray array)
    {
        Map<String, JSONObject> map = new HashMap<>();
        String keys[] = new String[ array.length() ];

        for (int inx = 0; inx < array.length(); inx++)
        {
            JSONObject obj = array.getJSONObject(inx);

            map.put(obj.getString("name"), obj);

            keys[ inx ] = obj.getString("name");
        }

        Arrays.sort(keys);

        JSONArray newArray = new JSONArray();

        for (String key: keys)
        {
            newArray.put(map.get(key));
        }

        return newArray;
    }

    public static String md5(String text)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            return number.toString(16);
        }
        catch (Exception exc)
        {

        }

        return text;
    }

    public static void shareIntent(Context context, String text)
    {
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
//        sendIntent.setType("text/plain");
//        context.startActivity(sendIntent);

        Intent sendIntent = new Intent();
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");

        context.startActivity(sendIntent);
    }

    public static void toastS(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toastS(Context context, int textId)
    {
        Toast.makeText(context, getText(context, textId), Toast.LENGTH_SHORT).show();
    }

    public static void toastL(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void toastL(Context context, int textId)
    {
        Toast.makeText(context, getText(context, textId), Toast.LENGTH_LONG).show();
    }

    public static void addMenuItem(Menu menu, String title, int icon)
    {
        MenuItem menuItem = menu.add(0, 0, 0, title);
        menuItem.setIcon(icon);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    public static MenuItem addMenuItemCheck(Menu menu, String title)
    {
        MenuItem menuItem = menu.add(0, 0, 0, title);
        menuItem.setCheckable(true);

        return menuItem;
    }

    public static MenuItem addMenuItemCheck(Menu menu, int titleId)
    {
        MenuItem menuItem = menu.add(titleId);
        menuItem.setCheckable(true);

        return menuItem;
    }

    public static String getText(Context context, int idRes)
    {
        return context.getResources().getText(idRes).toString();
    }

    public static long dezify(long number)
    {
        return number ^ 0x2905196228051998L;
    }

    @Nullable
    public static byte[] dezify(byte[] bytes)
    {
        if (bytes == null) return null;

        byte[] dezi = {0x29, 0x05, 0x19, 0x62};

        for (int inx = 0; inx < bytes.length; inx++)
        {
            bytes[ inx ] = (byte) (bytes[ inx ] ^ (0x0f & dezi[ inx % 4 ]));
        }

        return bytes;
    }

    @Nullable
    public static String dezify(String string)
    {
        if (string == null) return null;
        byte[] bytes = dezify(string.getBytes());
        if (bytes == null) return null;
        return new String(bytes);
    }

    public static int getDarkColor(int color)
    {
        // 0.7 0.77 0.87

        return Color.rgb(
            (int) (Color.red(color)   * 0.7),
            (int) (Color.green(color) * 0.77),
            (int) (Color.blue(color)  * 0.87));
    }

    public static int getDarkColor(String color)
    {
        return getDarkColor(Color.parseColor(color));
    }
}
