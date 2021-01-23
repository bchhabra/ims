package com.chhabras.parser.impl;

import com.chhabras.parser.AbstractParser;
import com.chhabras.utilities.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chhabras.utilities.Regex.price;
import static com.chhabras.utilities.Regex.quantity1;
import static com.chhabras.utilities.Regex.weight_kg;
import static com.chhabras.utilities.Regex.x;

public class ReweParser extends AbstractParser {

    public static final String REWE_regex1 = quantity1 + x + price;//2 Stk x 3, 99
    public static final String REWE_regex2 = weight_kg + x + price;//+"(\\s)?EUR/kg";//0, 372 kg x 24, 40 EUR/kg

    @Override
    public int endPointer(List<String> mainList) {
        int i = 0;
        for (i = 0; i < mainList.size(); i++) {
            if (mainList.get(i).contains("SUMME")) {
                break;
            }
        }
        return i;
    }

    @Override
    public boolean excludeBasedOnRegex(String text) {

        if (text.matches(REWE_regex1)) {
            System.out.println("###REGREX1### " + text);
            return false;
        }
        if (text.matches(REWE_regex2)) {
            System.out.println("###REGREX2### " + text);
            return false;
        }
        if (text.matches(Regex.postalcode)) {
            System.out.println("###REGREX3### " + text);
            return false;
        }
        return true;
    }

    @Override
    public boolean excludeBasedOnString(String text) {
        List<String> excludeList = new ArrayList<>();
        excludeList.add("REWE");
        excludeList.add("EUR");
        excludeList.add("UID Nr.");
        excludeList.add("Werner-Schlierf-Str. 5");
        for (String str : excludeList) {
            if (text.contains(str)) {
                System.out.println(" ###STRING### " + text);
                return false;
            }
        }
        return true;
    }

    private int spaceCount(String text) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == ' ') {
                count++;
            }
        }
        return count;
    }
}
