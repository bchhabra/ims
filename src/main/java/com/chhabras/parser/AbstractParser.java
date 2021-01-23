package com.chhabras.parser;

import com.chhabras.entities.Item;
import com.chhabras.utilities.PriceUtils;
import com.chhabras.utilities.Regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractParser implements Parser {

    @Override
    public List<Item> parse(String input) {
        List<String> mainList = convert2ListOfString(input);
        List<String> operationalList = filterList(mainList);
        // printList(mainList);
        System.out.println("########### OperationalList After###########");
        printList(operationalList);
        List<Item> items = new ArrayList<>();

        for (String str : operationalList) {
            Item it;
            if (isPrice(str)) {
                it = PriceUtils.findFirstItemWithoutPrice(items);
                if (it != null) {
                    PriceUtils.setPrice(it, str);
                } else {
                    it = new Item(null, str);
                    items.add(it);
                }
            } else {
                if (hasPrice(str)) {
                    String[] ar = segregate(str);
                        items.add(new Item(ar[0], ar[1]));
                } else {
                    it = PriceUtils.findFirstItemWithoutName(items);
                    if (it != null) {
                        PriceUtils.setName(it, str);
                    } else {
                        it = new Item(str, null);
                        items.add(it);
                    }
                }

            }
        }
        return items;
    }

    public List<String> convert2ListOfString(String string) {
        return Arrays.stream(string.split("\\r?\\n")).collect(Collectors.toList());
    }

    public List<String> filterList(List<String> mainList) {
        int end = endPointer(mainList);
        List<String> operationalList = mainList;
        if (end > 0) {
            operationalList = mainList.subList(1, end);
        }
        System.out.println("########### OperationalList Before###########");
        printList(operationalList);
        System.out.println("###########Filter ###########");

        operationalList = operationalList.stream()
                .filter(this::excludeBasedOnRegex)
                .filter(this::excludeBasedOnString)
                .map(x -> removeWeightandQuantity(x)) // it also trims at the end.
                .map(x -> refinePrice(x))
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
    public boolean hasPrice(String text) {
        System.out.println("Abstract Implementation :: hasPrice()");
        return false;
    }

    @Override
    public String[] segregate(String text) {
        System.out.println("Abstract Implementation :: segregate()");
        return null;
    }

    @Override
    public String getWeight(String text) {
        System.out.println("Abstract Implementation :: getWeight()");
        return text;
    }

    @Override
    public String getQuantity(String text) {
        System.out.println("Abstract Implementation :: getQuantity()");
        return text;
    }

    @Override
    public String removeWeightandQuantity(String text) {
        System.out.println("Abstract Implementation :: removeWeightandQuantity()");
        return text;
    }

    @Override
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
            return text; // and let segregater handle this.
        }
        r = Pattern.compile(Regex.refine_price1);
        m = r.matcher(text);
        if (m.find( )) {
            return m.group(1).replaceAll(" ","");
        }
        else {
            System.out.println("NO MATCH");
        }
        return text;
    }

    private void printList(List<String> list) {
        for (String l : list) {
            System.out.println(l);
        }
    }
}
