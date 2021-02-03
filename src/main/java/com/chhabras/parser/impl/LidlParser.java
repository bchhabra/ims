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




public class LidlParser extends AbstractParser {


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

}
