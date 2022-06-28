package com.jollycoder.calc;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String exp, result;
        while (!(exp = getInput()).equals("")) {
            try {
                result = calc(exp);
            } catch (IOException e) {
                System.out.println("Exception: " + e.getMessage());
                return;
            }
            System.out.println(result);
        }
    }
    public static String calc(String input) throws IOException {
        Map<String, String> items = new HashMap<>(3);
        parseExpression(input, items);
        return calculateExpression(items);
    }
    private static String getInput() {
        final Scanner in = new Scanner(System.in);
        System.out.println("Input an expression to calculate or an empty line to exit:");
        return in.nextLine();
    }
    private static void parseExpression(String exp, Map <String, String> items) throws IOException {
        class RegExParser {
            private void getItems(String regex, String exp, Map <String, String> items) {
                Pattern p = Pattern.compile(regex + "\\s*([-+*/])\\s*" + regex);
                Matcher m = p.matcher(exp);
                if (m.matches()) {
                    items.put("operand1", m.group(1));
                    items.put("operator", m.group(2));
                    items.put("operand2", m.group(3));
                }
            }
        }
        final RegExParser parser = new RegExParser();
        parser.getItems("(\\b(?:I|II|III|IV|V|VI|VII|VIII|IX|X)\\b)", exp, items);
        if (items.isEmpty())
            parser.getItems("(\\b(?:[1-9]|10)\\b)", exp, items);
        if (items.isEmpty())
            throw new IOException("wrong input");
    }
    private static String calculateExpression(Map <String, String> items) throws IOException {
        boolean isArabic = items.get("operand1").matches("\\d+");
        String op1 = items.get("operand1"), op2 = items.get("operand2");
        byte operand1 = Byte.parseByte( isArabic ? op1 : RomanArabicConverter.convert(op1) ),
             operand2 = Byte.parseByte( isArabic ? op2 : RomanArabicConverter.convert(op2) );
        int result = 0;
        switch (items.get("operator")) {
            case "+": result = operand1 + operand2;
                break;
            case "-": result = operand1 - operand2;
                break;
            case "*": result = operand1 * operand2;
                break;
            case "/": result = operand1 / operand2;
        }
        if (isArabic) return String.valueOf(result);
        if (result < 1) throw new IOException("wrong output");
        return RomanArabicConverter.convert(result);
    }
}