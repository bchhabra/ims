package com.chhabras.parser.impl;

import com.chhabras.entities.Item;
import com.chhabras.parser.AbstractParser;
import com.chhabras.utilities.Regex;

import java.util.ArrayList;
import java.util.List;

public class DmParser extends AbstractParser {

    public static final String DM_regex1 = "\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2} .*";

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
        String regex1 = DM_regex1;
        String regex2 = Regex.phone2;
        String regex3 = Regex.postalcode;
        if (text.matches(regex1)|| text.matches(regex2)|| text.matches(regex3)) {
            System.out.println(" ###REGREX### " + text);
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
    public boolean validate(List<Item> items) {
        String regex = "(.*)Vorteil Satz(.*)";
        List<Boolean> results = new ArrayList<>();
        for (Item item : items) {
            if (item.getName().matches(regex)) {
                results.add(item.getPrice().startsWith("-"));
            }
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
        if (text.startsWith(".") && text.matches(Regex.refine_price1)) {
            text = text.substring(1);
            return text.split("\\s")[0];
        }
        if (text.matches(Regex.refine_price2)) {
            return text.split("\\s")[0];
        }
        return text;
    }
}
