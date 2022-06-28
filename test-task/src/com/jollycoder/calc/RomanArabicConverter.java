// I tried to implement a complete Roman-Arabic number converter that accepts any possible values

package com.jollycoder.calc;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum RomanArabicConverter {
    M(1000), CM(900), D(500), CD(400),
    C( 100), XC( 90), L( 50), XL( 40),
    X(  10), IX(  9), V(  5), IV(  4),
    I(   1);

    private Integer numericValue;

    RomanArabicConverter(Integer value) {
        this.numericValue = value;
    }

    public static boolean enumHasObject(String name) {
        return Arrays.stream(RomanArabicConverter.values()).anyMatch(obj -> obj.name().equals(name));
    }

    public static void validator(String romanOrArabic) throws IOException {
        Pattern p = Pattern.compile("^\\d+$");
        Matcher m = p.matcher(romanOrArabic);
        if (m.matches()) validator(Integer.parseInt(romanOrArabic));
        else {
            p = Pattern.compile("^M{0,3}(CM|CD|D?C{0,3})?(XC|XL|L?X{0,3})?(IX|IV|V?I{0,3})?$");
            m = p.matcher(romanOrArabic);
            if (!m.matches()) throw new IOException("wrong input");
        }
    }

    public static void validator(int arabic) throws IOException {
        if (arabic < 1 || arabic > 3999) throw new IOException("wrong input");
    }

    public static String convert(String romanOrArabic) throws IOException {
        return romanOrArabic.matches("\\d+") ? arabicToRoman(romanOrArabic) : romanToArabic(romanOrArabic);
    }

    public static String convert(int arabic) throws IOException {
        return arabicToRoman(arabic);
    }

    public static String arabicToRoman(String arabic) throws IOException {
        return arabicToRoman(Integer.parseInt(arabic));
    }

    public static String arabicToRoman(int arabic) throws IOException {
        validator(arabic);
        String roman = "";
        for (RomanArabicConverter obj : RomanArabicConverter.values()) {
            while (arabic >= obj.numericValue) {
                roman += obj.name();
                arabic -= obj.numericValue;
            }
        }
        return roman;
    }

    public static String romanToArabic(String roman) throws IOException {
        validator(roman);
        String sym = "", prevSym = "";
        int arabic = 0;
        boolean flag = false;
        roman += " ";
        for (int i = 0; i < roman.length(); i++) {
            sym = Character.toString(roman.charAt(i));
            if (flag) flag = false;
            else if (i > 0) {
                flag = enumHasObject(prevSym + sym);
                arabic += RomanArabicConverter.valueOf(prevSym + (flag ? sym : "")).numericValue;
            }
            prevSym = sym;
        }
        return String.valueOf(arabic);
        /* since in our case roman numbers are within I - X it could be like this:
            String[] array = "|I|II|III|IV|V|VI|VII|VIII|IX|X".split("\\|");
            return String.valueOf(List.of(array).indexOf(roman));
        */
    }
}
