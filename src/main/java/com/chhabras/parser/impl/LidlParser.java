package com.chhabras.parser.impl;

import com.chhabras.entities.Item;
import com.chhabras.parser.AbstractParser;
import com.chhabras.utilities.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LidlParser extends AbstractParser {

    @Override
    public int endPointer(List<String> mainList) {
        int i = 0;
        for (i = 0; i < mainList.size(); i++) {
            if (mainList.get(i).contains("zu zahlen") | mainList.get(i).contains("MwStd")) {
                break;
            }
        }
        return i;
    }

    @Override
    public boolean excludeBasedOnRegex(String text) {
        String regex1 = Regex.LIDL_regex1;
        String regex2 = Regex.LIDL_regex2;
        if (text.matches(regex1)) {
            System.out.println("###REGREX1### " + text);
            return false;
        }
        if (text.matches(regex2)) {
            System.out.println("###REGREX2### " + text);
            return false;
        }
        return true;
    }

    @Override
    public boolean excludeBasedOnString(String text) {
        List<String> excludeList = new ArrayList<>();
        excludeList.add("EUR/kg");
        excludeList.add("Tel: 089");
        excludeList.add("EUR");
        excludeList.add("Kirchheim");
        excludeList.add("85551");
        excludeList.add("Frauenhoferstr");
        excludeList.add("LIDL");
        excludeList.add("Lebensmittelspezialisten");
        excludeList.add("Lebensmitelspeciailstent");
        excludeList.add("Fraunhoferstraße");
        excludeList.add("90129219");
        excludeList.add("edeka");
        excludeList.add("EDEKA");
        excludeList.add("Posten");
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
        String regex = "^pfandrückgabe(.*)|^RABATT(.*)";
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
    public boolean hasPrice(String text) {
        return text.matches(Regex.LIDL_regex3);
    }

    @Override
    public String[] segregate(String text) {
        /*
            Bio Rindergulasch 4,36 A
            Rindersuppenfleisch 3,97A
         */
        String[] arr = new String[2];
        Pattern pattern = Pattern.compile(Regex.LIDL_regex4);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            arr[0] = matcher.group(1).trim();
            arr[1] = matcher.group(2).trim();
        }
        return arr;
    }

    @Override
    public String getWeight(String text) {
        Pattern pattern = Pattern.compile(Regex.LIDL_regex5);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    @Override
    public String getQuantity(String text) {
        Pattern pattern = Pattern.compile(Regex.LIDL_regex6);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    @Override
    public String removeWeightandQuantity(String text) {
        String weight = getWeight(text);
        if (weight != null) {
            text = text.replaceAll(weight, "");
        }
        String quantity = getQuantity(text);
        if (quantity != null) {
            text = text.replaceAll(quantity, "");
        }
        return text.trim();
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
