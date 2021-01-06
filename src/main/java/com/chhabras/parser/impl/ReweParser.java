package com.chhabras.parser.impl;

import com.chhabras.parser.AbstractParser;
import com.chhabras.utilities.Regex;

import java.util.ArrayList;
import java.util.List;

public class ReweParser extends AbstractParser {
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

        if (text.matches(Regex.REWE_regex1)) {
            System.out.println("###REGREX1### " + text);
            return false;
        }
        if (text.matches(Regex.REWE_regex2)) {
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

    @Override
    public String refinePrice(String text) {
        if(spaceCount(text)==2){ // to handle the case where price is 1, 37 A
            text = text.replaceFirst(" ", "");
        }
        if (text.startsWith(".") && text.matches(Regex.refine_price1)) {
            text = text.substring(1);
            return text.split("\\s")[0];
        }
        if (text.matches(Regex.refine_price2)) {
            return text.split("\\s")[0];
        }
        return text;
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
