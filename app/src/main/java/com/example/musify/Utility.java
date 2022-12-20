package com.example.musify;

public class Utility {

    public static String convertDuration(long duration){
        long min=(duration/1000) / 60;
        long sec=(duration/1000)%60;
        String converted=String.format("%d:%02d",min,sec);
        return converted;
    }
}
