package com.chhabras.parser;

import com.chhabras.entities.Item;
import com.chhabras.utilities.PriceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractParser implements Parser{


    @Override
    public boolean validate(List<Item> items) {
        return false;
    }

    @Override
    public List<Item> parse(String input){
        List<String> mainList = convert2ListOfString(input);
        List<String> operationalList = filterList(mainList);
       // printList(mainList);
        System.out.println("##################################");
        //printList(operationalList);
        List<Item> items = new ArrayList<>();

        for (String str : operationalList) {
            Item it;
            if (isPrice(str)) {
                it = PriceUtils.findFirstItemWithoutPrice(items);
                String pr = refinePrice(str);
                if (it != null) {
                    PriceUtils.setPrice(it, pr);
                } else {
                    it = new Item(null, pr);
                    items.add(it);
                }
            } else {
                str = removeExtras(str);
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
        if(end > 0){
            operationalList = mainList.subList(1, end);
        }
        System.out.println("OperationalList Start");
        printList(operationalList);
        System.out.println("Filter ###########");

        operationalList = operationalList.stream()
                                        .filter(this::excludeBasedOnRegex)
                                        .filter(this::excludeBasedOnString)
                                        .collect(Collectors.toList());
        return operationalList;
    }

    private void printList(List<String> list) {
        for(String l : list){
            System.out.println(l);
        }
    }
}
