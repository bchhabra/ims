package com.chhabras.parser;

import com.chhabras.entities.Item;
import com.chhabras.utilities.PriceUtils;
import com.chhabras.utilities.Regex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractParser implements Parser {

    @Override
    public int startPointer(List<String> mainList) {
        return 0;
    }

    @Override
    public HashMap<String, String> segregate(String text) {
        return null;
    }

    @Override
    public List<Item> parse(List<String> mainList) {
        List<String> operationalList = filterList(mainList);
        System.out.println("########### OperationalList After###########");
        printList(operationalList);
        List<Item> items = new ArrayList<>();

        for (String str : operationalList) {
            Item it;
            HashMap<String, String> map = segregate(str);
            String description = map.get("description");
            String price = map.get("price");
            if (price != "" && price != null) {
                price = refinePrice(price);
                it = PriceUtils.findFirstItemWithoutPrice(items);
                if (it != null) {
                    PriceUtils.setPrice(it, price.trim());
                } else {
                    it = new Item(null, price.trim());
                    items.add(it);
                }
            }
            if (description != "") {
                it = PriceUtils.findFirstItemWithoutName(items);
                if (it != null) {
                    PriceUtils.setName(it, description.trim());
                } else {
                    it = new Item(description.trim(), null);
                    items.add(it);
                }
            }
        }
        return items;
    }

    public boolean standardValidationIsOk(List<Item> items) {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal zuZahlen = BigDecimal.ZERO;
        for (Item item : items) {
            BigDecimal price = new BigDecimal(item.getPrice().replaceAll(",","."));
            if(item.getName().contains("zu zahlen")) {
                zuZahlen = zuZahlen.add(price);
            }else{
                sum = sum.add(price);
            }
        }
        if (sum.subtract(zuZahlen).compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }else {
            return false;
        }

    }

    public List<String> filterList(List<String> mainList) {
        int start = startPointer(mainList);
        int end = endPointer(mainList);
        List<String> operationalList = mainList.subList(start, end);
        System.out.println("########### OperationalList Before###########");
        printList(operationalList);
        System.out.println("###########Filter ###########");

        operationalList = operationalList.stream()
                .filter(this::excludeBasedOnRegex)
                .filter(this::excludeBasedOnString)
                .collect(Collectors.toList());
        return operationalList;
    }

    public boolean isPrice(String text) { // Changing implementation to check only price(Extras like A, B are not considered as price anymore and will be filtered before it reaches to operational List
        boolean flag = false;
        flag = text.matches(Regex.onlyprice);
        return flag;
    }

    @Override
    public boolean validate(List<Item> items) {
        System.out.println("Abstract Implementation :: validate()");
        return false;
    }


    @Override
    public boolean excludeBasedOnString(String text) {
        return true;
    }

    public String refinePrice(String text) {
        /**
         *
         */
        Pattern r = Pattern.compile(Regex.refine_price0);
        Matcher m = r.matcher(text);
        if (m.find( )) {
            /*
            System.out.println("Found value: " + m.group(2) );
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            */
            return text.replaceAll("(B|BW|A|AW|A)", "").trim(); // TODO get rid of complex logic here, eventually refine price should only be taking care of removing extras at the end A|B|AB and so on
        }
        r = Pattern.compile(Regex.refine_price1);
        m = r.matcher(text);
        if (m.find( )) {
            return m.group(1).replaceAll(" ","").replaceAll("(B|BW|A|AW|A)", "").trim();
        }
        else {
            System.out.println("NO MATCH");
        }
        return text.replaceAll("(B|BW|A|AW|A)", "").trim();
    }

    private void printList(List<String> list) {
        for (String l : list) {
            System.out.println(l);
        }
    }
}
