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

import static com.chhabras.utilities.Regex.pattern_d;
import static com.chhabras.utilities.Regex.pattern_p;
import static com.chhabras.utilities.Regex.pattern_pz;

public abstract class AbstractParser implements Parser {

    @Override
    public int startPointer(List<String> mainList) {
        return 0;
    }

    @Override
    public int endPointer(List<String> mainList) {
        return 0;
    }

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
                map.put("price", matcher_p.group(1).trim());
                pricefound = true;
            } else if (matcher_pz.find() && (text.toLowerCase().contains("summe") || text.toLowerCase().contains("zahlen"))&& matcher_pz.group(1) != null) {
                map.put("price", matcher_pz.group(1).trim());
                pricefound = true;
            }
            if (pricefound && matcher_d.find() && matcher_d.group(1) != null) {
                map.put("description", matcher_d.group(1).trim());
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
        boolean finished = false;

        for (String str : mainList) {
            Item it;
            HashMap<String, String> map = segregate(str);
            if (map != null && !finished) {
                String description = map.get("description");
                String price = map.get("price");
                if (price != null && !price.trim().isEmpty()) {
                    it = PriceUtils.findFirstItemWithoutPrice(items);
                    if (it != null) {
                        PriceUtils.setPrice(it, price.trim());
                    } else {
                        it = new Item(null, price.trim());
                        items.add(it);
                    }
                }
                if (description != null && !description.trim().isEmpty()) {
                    it = PriceUtils.findFirstItemWithoutName(items);
                    if (it != null) {
                        PriceUtils.setName(it, description.trim());
                        if(description.toLowerCase().contains("summe") || description.toLowerCase().contains("zahlen")){
                            finished = true;
                        }
                    } else {
                        it = new Item(description.trim(), null);
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


    @Override
    public boolean excludeBasedOnString(String text) {
        return true;
    }

    @Override
    public boolean excludeBasedOnRegex(String text) {
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
            return text.replaceAll("(B|BW|A|AW|A)", "").trim(); // TODO get rid of complex logic here, eventually refine lidl_price should only be taking care of removing extras at the end A|B|AB and so on
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
