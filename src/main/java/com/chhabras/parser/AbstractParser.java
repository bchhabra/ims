package com.chhabras.parser;

import com.chhabras.entities.Item;
import com.chhabras.utilities.PriceUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import static com.chhabras.utilities.Regex.pattern_d;
import static com.chhabras.utilities.Regex.pattern_p;
import static com.chhabras.utilities.Regex.pattern_pz;

public abstract class AbstractParser implements Parser {

    @Override
    public HashMap<String, String> segregate(String text) {

        Matcher matcher_d = pattern_d.matcher(text);
        Matcher matcher_p = pattern_p.matcher(text);
        Matcher matcher_pz = pattern_pz.matcher(text);

        if (matcher_p.find() || matcher_pz.find() || matcher_d.find()) {
            matcher_p.reset();
            matcher_pz.reset();
            matcher_d.reset();
            HashMap<String, String> map = new HashMap<>();
            boolean pricefound = false;
            if (matcher_p.find() && matcher_p.group(1) != null) {
                map.put("price", matcher_p.group(1));
                pricefound = true;
            } else if (matcher_pz.find() && (text.toLowerCase().contains("summe") || text.toLowerCase().contains("zahlen"))&& matcher_pz.group(1) != null) {
                map.put("price", matcher_pz.group(1));
                pricefound = true;
            }
            if (pricefound && matcher_d.find() && matcher_d.group(1) != null) {
                map.put("description", matcher_d.group(1));
            }
            return map;
        } else {
            return null;
        }
    }

    @Override
    public List<Item> parse(List<String> mainList) {
        printList(mainList);
        List<Item> items = new ArrayList<>();
        System.out.println("########### Items ###########");

        //TODO get rid of map returning from segregate
        Iterator<String> lineIterator = mainList.iterator();
        while(lineIterator.hasNext()) {
            Item it;
            HashMap<String, String> map = segregate(lineIterator.next());

            if (map != null) {
                String description = map.get("description");
                String price = map.get("price");
                if (price != null && !price.isEmpty()) {
                    it = PriceUtils.findFirstItemWithoutPrice(items);
                    if (it != null) {
                        it.setPrice(price);
                    } else {
                        it = new Item(null, price);
                        items.add(it);
                    }
                }
                if (description != null && !description.isEmpty()) {
                    it = PriceUtils.findFirstItemWithoutName(items);
                    if (it != null) {
                        it.setName(description);
                        if(description.toLowerCase().contains("summe") || description.toLowerCase().contains("zahlen")){
                            break;
                        }
                    } else {
                        it = new Item(description, null);
                        items.add(it);
                    }
                }
            }
        }
        for (Item i : items) {
            System.out.println(i.getName() + " : " + i.getPrice());
        }
        return items;
    }

    public boolean standardValidationIsOk(List<Item> items) {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal zuZahlen = BigDecimal.ZERO;
        for (Item item : items) {
            BigDecimal price = new BigDecimal(item.getPrice().replaceAll(",", "."));
            if (item.getName().toLowerCase().contains("zahlen") || item.getName().toLowerCase().startsWith("summe")) {
                zuZahlen = zuZahlen.add(price);
            } else {
                sum = sum.add(price);
            }
            if (price.compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }
        }
        if (sum.subtract(zuZahlen).compareTo(BigDecimal.ZERO) == 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean validate(List<Item> items) {
        return standardValidationIsOk(items);
    }

    protected void printList(List<String> list) {
        for (String l : list) {
            System.out.println(l);
        }
    }
}
