package com.chhabras.parser.impl;

import com.chhabras.entities.Item;
import com.chhabras.parser.AbstractParser;
import com.chhabras.utilities.Regex;

import java.util.ArrayList;
import java.util.List;

import static com.chhabras.utilities.Regex.eurperkg;
import static com.chhabras.utilities.Regex.price;
import static com.chhabras.utilities.Regex.weight_kg;
import static com.chhabras.utilities.Regex.x;

public class BonusParser extends AbstractParser {

    public static final String Bonus_regex1 = weight_kg + x + price + eurperkg;
    public static final String Bonus_regex2 = weight_kg + x;
    public static final String Bonus_regex3  = price + eurperkg;

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
        String regex1 = Regex.multiplier;
        String regex2 = Regex.phone1;
        String regex3 = Regex.postalcode;
        String regex4 = Bonus_regex1;
        String regex5 = Bonus_regex2;
        String regex6 = Bonus_regex3;

        if (text.matches(regex1)|| text.matches(regex2)|| text.matches(regex3)|| text.matches(regex4) || text.matches(regex5) || text.matches(regex6)) {
            System.out.println(" ###REGREX### " + text);
            return false;
        }
        return true;
    }

    @Override
    public boolean excludeBasedOnString(String text) {
        List<String> excludeList = new ArrayList<>();
        excludeList.add("Bonus gGmbh");
        excludeList.add("Am Brunnen 17");
        excludeList.add("Fraunhofer");
        excludeList.add("Kirchheim");
        excludeList.add("kirchheim");
        for (String str : excludeList) {
            if (text.contains(str)) {
                System.out.println(" ###STRING### " + text);
                return false;
            }
        }
        /**
         * Only EUR
         */
        if(text.equalsIgnoreCase("EUR")){
            return false;
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
}
