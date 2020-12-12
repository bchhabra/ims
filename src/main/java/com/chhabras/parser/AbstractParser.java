package com.chhabras.parser;

import com.chhabras.entities.Item;
import com.chhabras.utilities.ListUtils;
import com.chhabras.utilities.PriceUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParser implements Parser{


    @Override
    public boolean validate(List<Item> items) {
        return false;
    }

    @Override
    public List<Item> parse(String input){
        List<String> mainList = ListUtils.convert2ListOfString(input);
        List<String> operationalList = ListUtils.filterList(mainList);
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
}
