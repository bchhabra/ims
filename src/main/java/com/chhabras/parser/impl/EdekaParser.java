package com.chhabras.parser.impl;

import com.chhabras.entities.Item;
import com.chhabras.parser.AbstractParser;
import java.util.ArrayList;
import java.util.List;

public class EdekaParser extends AbstractParser {

    @Override
    public int endPointer(List<String> mainList) {
        int i = 0;
        for (i = 0; i < mainList.size(); i++) {
            if (mainList.get(i).contains("SUMME") | mainList.get(i).contains("MwSt")) {
                break;
            }
        }
        return i;
    }

    @Override
    public boolean excludeBasedOnRegex(String text) {
        String regex1 = "\\d{1,2} x";
        String regex2 = "\\d{1,2},\\d{1,2}";
        String regex3 = "Tel(\\.|:|\\s)?(\\s)?089(.*)";
        String regex4 = "\\d{4} (.*)";
        if (text.matches(regex1)|| text.matches(regex2)|| text.matches(regex3)|| text.matches(regex4)) {
            System.out.println(" ###REGREX### " + text);
            return false;
        }
        return true;
    }

    @Override
    public boolean excludeBasedOnString(String text) {
        List<String> excludeList = new ArrayList<>();
        excludeList.add("Fraunhoferstraße 1");
        excludeList.add("Fraunhofer");
        excludeList.add("Kirchheim");
        excludeList.add("kirchheim");
        excludeList.add("85551");
        excludeList.add("DEKA");
        excludeList.add("EBEKA");
        excludeList.add("EDRKA");
        excludeList.add("Lebensmitelspeciailstent");
        excludeList.add("Tel. 089");
        excludeList.add("Tel: 089");
        excludeList.add("edeka");
        excludeList.add("EUR");
        excludeList.add("Posten:");
        for (String str : excludeList) {
            if (text.contains(str)) {
                System.out.println(" ###STRING### " + text);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validate(List<Item> items) {
        List<Boolean> results = new ArrayList<>();
        for (Item item : items) {
            if(item.getPrice() == null){
                results.add(false);
            }
        }
        if (results.contains(false)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String refinePrice(String text) {
        if (text.matches("^(-)?(\\.)?\\d{0,3},\\d{0,2}(\\s)(B|BW|A|AW|A,)")) {
            return text.split("\\s")[0];
        }
        if (text.matches("^(-)?(\\.)?\\d{0,3},\\d{0,2}(\\*)(B|BW|A|AW|A,)")) {
            return text.split("\\*")[0];
        }
        return text;
    }
}
