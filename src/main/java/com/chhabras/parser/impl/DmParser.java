package com.chhabras.parser.impl;

import com.chhabras.parser.AbstractParser;

import java.util.ArrayList;
import java.util.List;

public class DmParser extends AbstractParser {

    @Override
    public int endPointer(List<String> mainList) {
        int i = 0;
        for (i = 0; i < mainList.size(); i++) {
            if (mainList.get(i).contains("*********") | mainList.get(i).contains("MwSt-Satz")| mainList.get(i).contains("Brutto")) {
                break;
            }
        }
        return i;
    }

    @Override
    public boolean excludeBasedOnRegex(String text) {
        String regex = "\\d{5}.*|\\d{5}/\\d{7}|\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2} .*";  // 85570 Markt Schwaben
                                                                                        //08121/2235917
                                                                                        // 12.12.2020 18:28 2338/2 250954/2 9626
        if(text.matches(regex)) {
            System.out.println("###REGREX### " + text);
            return false;
        }
        return true;
    }

    @Override
    public boolean excludeBasedOnString(String text) {
        List<String> excludeList = new ArrayList<>();
        excludeList.add("dm-drogerle markt");
        excludeList.add("Burgerfeld 12");
        excludeList.add("ZEILENSTORNO");
        excludeList.add("HIER BIN ICH MENSCH");
        excludeList.add("HIER KAUF ICH EIN");
        excludeList.add("dm-Rabatte auf rabattfähige Artikel");
        excludeList.add("Schwaben");
        for (String str : excludeList) {
            if (text.contains(str)) {
                System.out.println("###STRING### " + text);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isPrice(String text) {
        return text.matches("^(-)?(.)?\\d{0,3}(\\,\\s?\\d{1,2})?\\s?(1|2)?\\s?");
    }

    @Override
    public boolean hasPrice(String text) {
        return text.matches("(.*?)(\\d{0,3},\\s?\\d{0,2})(.*?)(1|2)\\s?");

    }

    @Override
    public String[] segregate(String text) {
        return new String[0];
    }

    @Override
    public String getWeight(String text) {
        return text;
    }

    @Override
    public String getQuantity(String text) {
        return text;
    }

    @Override
    public String removeExtras(String text) {
        return text;
    }

    @Override
    public String refinePrice(String text) {
        return text;
    }
}
