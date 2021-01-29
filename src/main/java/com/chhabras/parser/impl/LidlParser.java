package com.chhabras.parser.impl;

import com.chhabras.entities.Item;
import com.chhabras.parser.AbstractParser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chhabras.utilities.Regex.eurperkg;
import static com.chhabras.utilities.Regex.weight;
import static com.chhabras.utilities.Regex.x;

public class LidlParser extends AbstractParser {

    public static final String description = "^(([a-zA-ZäöüÄÖÜß0-9]*(\\.|\\s)*)+ )";
    public static final String price_only = "\\d{1,2}\\s*(,|\\.)\\s*\\d{2}";
    public static final String price_x_quantity = "("+price_only+x+"\\d{1,2} )";
    public static final String price = "(-?\\s?"+price_only+"\\s*(B|BW|A|AW|A))";

    public static final String LIDL_regex1 = description+price_x_quantity+"?"+price;
    public static final String LIDL_regex2 = description+price_x_quantity+"?"+"(-?\\s?"+price_only+")";
    public static final String LIDL_regex3 = weight + x + price_only + eurperkg;


    public int startPointer(List<String> mainList) {
        for (int i = 0; i < mainList.size(); i++) { // reference lidl04.jpg
            if (mainList.get(i).contains("EUR")) {
                if (mainList.get(i).startsWith("EUR")) {
                    return i + 1;
                }
                return i;
            }
        }
        return 0;
    }

    @Override
    public int endPointer(List<String> mainList) {
        for (int i = 0; i < mainList.size(); i++) {
            if (mainList.get(i).contains("zu zahlen") | mainList.get(i).contains("MwStd")) {
                return i + 1;
            }
        }
        return 0;
    }

    @Override
    public boolean excludeBasedOnRegex(String text) {
        if (text.matches(LIDL_regex3)) {
            System.out.println(" ###REGREX### " + text);
            return false;
        }
        return true;
    }


    @Override
    public boolean validate(List<Item> items) {
        if(standardValidationIsOk(items)){
            String regex = "^pfandrückgabe(.*)|^RABATT(.*)";
            for (Item item : items) {
                BigDecimal price = new BigDecimal(item.getPrice().replaceAll(",","."));
                if (item.getName().matches(regex)) {
                    if(price.compareTo(BigDecimal.ZERO) > 0){
                        return false;
                    }
                }
                if (price.compareTo(BigDecimal.ZERO) == 0) {
                        return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }

    public HashMap<String, String> segregate(String text) {
        HashMap<String, String> map = new HashMap<>();

        if(!text.contains("zu zahlen")) {
            final String regex = LIDL_regex1;
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                map.put("description", matcher.group(1).trim());
                map.put("price", matcher.group(6).trim());
            }
        }else {
            final String regex = LIDL_regex2;
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                map.put("description", matcher.group(1).trim());
                map.put("price", matcher.group(6).trim());
            }
        }

        return map;
    }

}
