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
import static com.chhabras.utilities.Regex.price_only;
import static com.chhabras.utilities.Regex.x;
import static com.chhabras.utilities.Regex.pattern_d;
import static com.chhabras.utilities.Regex.pattern_q;
import static com.chhabras.utilities.Regex.pattern_p;
import static com.chhabras.utilities.Regex.pattern_pz;



public class LidlParser extends AbstractParser {

    public static final String LIDL_regex3 = weight + x + price_only + eurperkg;

    @Override
    public int startPointer(List<String> mainList) {
        for (int i = 0; i < mainList.size(); i++) {
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
            }
            return true;
        }else{
            return false;
        }
    }

    public HashMap<String, String> segregate(String text) {
        HashMap<String, String> map = new HashMap<>();
        Matcher matcher_d = pattern_d.matcher(text);
        Matcher matcher_q = pattern_q.matcher(text);
        Matcher matcher_p = pattern_p.matcher(text);
        Matcher matcher_pz = pattern_pz.matcher(text);

        matcher_d.find();
        matcher_q.find();
        matcher_p.find();
        matcher_pz.find();

        if(matcher_d.group(1) != null) {
            map.put("description", matcher_d.group(1).trim());
        }

        if(!text.contains("zahlen")) {
            if(matcher_p.group(1) != null) {
                map.put("price", matcher_p.group(1).trim());
            }
        }else {
            if(matcher_pz.group(1) != null) {
                map.put("price", matcher_pz.group(1).trim());
            }
        }
        return map;
    }

}
