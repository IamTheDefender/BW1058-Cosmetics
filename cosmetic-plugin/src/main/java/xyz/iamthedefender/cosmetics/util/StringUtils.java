package xyz.iamthedefender.cosmetics.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {


    public static List<String> formatLore(List<String> lores, String name, int price, String status, String rarity){
        for(int i = 0; i < lores.size(); i++){
            if (lores.get(i).contains("{status}")){
                lores.set(i, lores.get(i).replace("{status}", status));
            }
            if (lores.get(i).contains("{name}")){
                lores.set(i, lores.get(i).replace("{name}", name.replace("-", " ")));
            }
            if (lores.get(i).contains("{cost}")){
                lores.set(i, lores.get(i).replace("{cost}", new DecimalFormat().format(price)));
            }
            if (lores.get(i).contains("{rarity}")){
                lores.set(i, lores.get(i).replace("{rarity}", rarity));
            }
        }
        return new ArrayList<>(lores);
    }
    public static String replaceHyphensAndCaptalizeFirstLetter(String str) {
        if (str == null){
            return "&cDISABLED";
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        if (str == null){
            return str;
        }
        for (char c : str.toCharArray()) {
            if (c == '-') {
                result.append(' ');
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }
}
