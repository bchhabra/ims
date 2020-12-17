package com.chhabras.utilities;

public final class Regex {

    public static final String postalcode = "^\\d{5}(.*)?";
    public static final String phone1 = "Tel(\\.|:|\\s)?(\\s)?089(.*)";
    public static final String phone2 = "\\d{5}/\\d{7}"; //08121/2235917
    public static final String multiplier = "\\d{1,2} x";

    public static final String x = "(\\s+)?x(\\s+)?"; // x
    public static final String price = "\\d{1,2}(,|.)(\\s)?(\\d{2}+)"; //3, 99
    public static final String quantity1 = "\\d{1,2}(\\s)Stk"; //2 Stk
    public static final String weight_kg = "\\d{1,3}(,|.)(\\s)?(\\d{2,3}+) kg"; //0, 372 kg
    public static final String weight = "\\d{0,3}(\\s)?(KG|Kg|kg|g)"; //100g

    public static final String REWE_regex1 = quantity1 + x + price;//2 Stk x 3, 99
    public static final String REWE_regex2 = weight_kg + x + price;//+"(\\s)?EUR/kg";//0, 372 kg x 24, 40 EUR/kg

    public static final String LIDL_regex1 = price+x+"(\\s\\d+)?";
    public static final String LIDL_regex2 = weight_kg+x+price+" EUR/kg";
    public static final String LIDL_regex3 = "(.*?)("+price+")(.*?)\\s?\\*?(A|B|BW|A,)\\s?";
    public static final String LIDL_regex4 = "(.*?)("+price+")(\\s?(A|B|BW))";
    public static final String LIDL_regex5 = "(.*?)("+weight+")(.*?)";
    public static final String LIDL_regex6 = "(.*?)("+price+x+"\\d{0,2})(.*?)";

    public static final String refine_price1 = "(-)?(\\.)?"+ price +"(\\s)(B|BW|A|AW|A,|1|2)";
    public static final String refine_price2 = "^"+refine_price1;
    public static final String refine_price3 = "(-)?(\\.)?"+ price +"(\\*)(B|BW|A|AW|A,)";

    public static final String DM_regex1 = "\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2} .*";

}
