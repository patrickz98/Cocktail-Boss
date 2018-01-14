package com.patrickz.cocktailbossfree;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

public class AboutActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleAd.createContentLayout(this, R.string.about);
        LinearLayout contentLayout = (LinearLayout) layout.get("scrollLayout");

        //
        // QuadFlask/colorpicker
        //

        LinearLayout colorPicker = SimpleLayout.getBox(this, "Colorpicker Libary");
        contentLayout.addView(colorPicker);

        TextView colorPickerText = new TextView(this);
        colorPickerText.setLayoutParams(SimpleLayout.getLayoutParams(-1, -1));
        colorPickerText.setText(
            "Source:  https://github.com/QuadFlask/colorpicker\n" +
            "License: Apache License, Version 2.0");

        colorPicker.addView(colorPickerText);

        //
        // dm77/barcodescanner
        //

        LinearLayout qrCodeScanner = SimpleLayout.getBox(this, "Qr-Code Scanner Libary");
        contentLayout.addView(qrCodeScanner);

        TextView qrCodeScannerText = new TextView(this);
        qrCodeScannerText.setLayoutParams(SimpleLayout.getLayoutParams(-1, -1));
        qrCodeScannerText.setText(
            "Source:  https://github.com/dm77/barcodescanner\n" +
            "License: Apache License, Version 2.0");

        qrCodeScanner.addView(qrCodeScannerText);
    }
}
