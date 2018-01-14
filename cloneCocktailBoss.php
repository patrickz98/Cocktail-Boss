<?php

function endsWith($haystack, $needle)
{
    $length = strlen($needle);

    if ($length == 0)
    {
        return true;
    }

    return (substr($haystack, -$length) === $needle);
}

function copyStrings()
{
    $file = "./CocktailBoss/app/src/main/res/values/strings.xml";
    $newfile = "./CocktailBossFree/app/src/main/res/values/strings.xml";

    if (! copy($file, $newfile))
    {
        echo "copy $file schlug fehl...\n";
    }
    // else
    // {
    //     printf("%-100s\r", $file);
    // }

    $file = "./CocktailBoss/app/src/main/res/values-de/strings.xml";
    $newfile = "./CocktailBossFree/app/src/main/res/values-de/strings.xml";

    if (! copy($file, $newfile))
    {
        echo "copy $file schlug fehl...\n";
    }
    // else
    // {
    //     printf("%-100s\r", $file);
    // }
}

function main()
{
    copyStrings();

    $sourceDir = "./CocktailBoss/app/src/main/java/com/patrickz/cocktailboss/";

    $javaFiles = scandir($sourceDir);

    $countLines = 0;
    $countWords = 0;

    foreach ($javaFiles as $key => $value)
    {
        if (! endsWith($value, ".java")) continue;

        // echo "--> " . $fileName . "\n";
        // printf("%-100s\r", $sourceDir . $value);

        echo "--> " . $value . "\n";

        $content = file_get_contents($sourceDir . $value);

        $content = str_replace(
            "package com.patrickz.cocktailboss;",
            "package com.patrickz.cocktailbossfree;",
            $content);

        $content = preg_replace(
            "/\/\/ ## AdMarker MEDIUM_RECTANGLE (.*?) --> (.*?) ##/",
            "SimpleAd.addAdvertisementMedium($1, $2);",
            $content);

        $content = preg_replace(
            "/\/\/ ## AdMarker INTERSTITIAL (.*?) ##/",
            "SimpleAd.createInterstitialAd($1);",
            $content);

        $content = preg_replace(
            "/SimpleLayout.createContentLayout\((.*?), (.*?)\);/",
            "SimpleAd.createContentLayout($1, $2);",
            $content);

        $content = preg_replace(
            "/\/\/ ## AdMarker BANNER (.*?) --> (.*?) --> (.*?) ##/",
            "SimpleAd.addAdvertisement($1, $2, $3);",
            $content);

        $countLines += substr_count($content, "\n");
        $countWords += sizeof(explode(" ", $content));

        $fileName = "./CocktailBossFree/app/src/main/java/com/patrickz/cocktailbossfree/$value";

        $myfile = @fopen($fileName, "w");
        @fwrite($myfile, $content);
        @fclose($myfile);
    }

    echo "Lines: " . $countLines . "\n";
    echo "Words: " . $countWords . "\n";
    echo "Pages: " . ($countWords / 450) . "\n";
    // printf("%-100s\r", " ");
    // printf("%-100d\n", $count);
}

main();

?>
