package com.chhabras.parser.impl;

import com.chhabras.entities.Item;
import com.chhabras.parser.AbstractParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chhabras.utilities.Regex.postalcode;
import static com.chhabras.utilities.Regex.price;
import static com.chhabras.utilities.Regex.weight_kg;
import static com.chhabras.utilities.Regex.weight;
import static com.chhabras.utilities.Regex.x;
import static com.chhabras.utilities.Regex.refine_price1;
import static com.chhabras.utilities.Regex.refine_price2;

public class LidlParser extends AbstractParser {

    public static final String LIDL_regex1 = price + x + "(\\s\\d+)?";
    public static final String LIDL_regex2 = weight_kg + x +price+" EUR/kg";
    public static final String LIDL_regex3 = "(.*?)(" + price + ")(.*?)\\s?\\*?(A|B|BW|A,)\\s?";
    public static final String LIDL_regex4 = "(.*?)(" + price + ")(\\s?(A|B|BW))";
    public static final String LIDL_regex5 = "(.*?)(" + weight + ")(.*?)";
    public static final String LIDL_regex6 = "(.*?)("+ price + x + "\\d{0,2})(.*?)";


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
        String regex1 = LIDL_regex1;
        String regex2 = LIDL_regex2;
        String regex3 = postalcode;
        if (text.matches(regex1)|| text.matches(regex2)|| text.matches(regex3)) {
            System.out.println(" ###REGREX### " + text);
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
        excludeList.add("Ohmstr");
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
        return text.matches(LIDL_regex3);
    }

    @Override
    public String[] segregate(String text) {
        /*
            Bio Rindergulasch 4,36 A
            Rindersuppenfleisch 3,97A
         */
        String[] arr = new String[2];
        Pattern pattern = Pattern.compile(LIDL_regex4);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            arr[0] = matcher.group(1).trim();
            arr[1] = matcher.group(2).trim();
        }
        return arr;
    }

    @Override
    public String getWeight(String text) {
        Pattern pattern = Pattern.compile(LIDL_regex5);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    @Override
    public String getQuantity(String text) {
        Pattern pattern = Pattern.compile(LIDL_regex6);
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
}
