package com.hand.plm.spec_product_detail.util.controllers;

import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mary
 * @version 1.0
 * @name FillForegroundColorUtil
 * @date 2019/12/10
 */
public class FillForegroundColorUtil {

    public static Short getBackGround(String key){
        Map<String,Short> map=new HashMap<String,Short>();

        map.put("X1", IndexedColors.AUTOMATIC.getIndex());
        map.put("X2", IndexedColors.AUTOMATIC.getIndex());
        map.put("X3", IndexedColors.BLUE.getIndex());
        map.put("X4", IndexedColors.BLUE_GREY.getIndex());
        map.put("X5", IndexedColors.BRIGHT_GREEN.getIndex());
        map.put("X6", IndexedColors.BROWN.getIndex());
        map.put("X7", IndexedColors.CORAL.getIndex());
        map.put("X8", IndexedColors.CORNFLOWER_BLUE.getIndex());
        map.put("X9", IndexedColors.DARK_BLUE.getIndex());
        map.put("X10", IndexedColors.DARK_GREEN.getIndex());
        map.put("X11", IndexedColors.DARK_RED.getIndex());
        map.put("X12", IndexedColors.DARK_TEAL.getIndex());
        map.put("X13", IndexedColors.DARK_YELLOW.getIndex());
        map.put("X14", IndexedColors.GOLD.getIndex());
        map.put("X15", IndexedColors.GREEN.getIndex());
        map.put("X16", IndexedColors.GREY_25_PERCENT.getIndex());
        map.put("X17", IndexedColors.GREY_40_PERCENT.getIndex());
        map.put("X18", IndexedColors.GREY_50_PERCENT.getIndex());
        map.put("X19", IndexedColors.GREY_80_PERCENT.getIndex());
        map.put("X20", IndexedColors.INDIGO.getIndex());
        map.put("X21", IndexedColors.LAVENDER.getIndex());
        map.put("X22", IndexedColors.LEMON_CHIFFON.getIndex());
        map.put("X23", IndexedColors.LIGHT_BLUE.getIndex());
        map.put("X24", IndexedColors.LEMON_CHIFFON.getIndex());
        map.put("X25", IndexedColors.LIGHT_BLUE.getIndex());
        map.put("X26", IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        map.put("X27", IndexedColors.LIGHT_GREEN.getIndex());
        map.put("X28", IndexedColors.LIGHT_ORANGE.getIndex());
        map.put("X29", IndexedColors.LIGHT_TURQUOISE.getIndex());
        map.put("X30", IndexedColors.LIGHT_YELLOW.getIndex());
        map.put("X31", IndexedColors.LIME.getIndex());
        map.put("X32", IndexedColors.MAROON.getIndex());
        map.put("X33", IndexedColors.OLIVE_GREEN.getIndex());
        map.put("X34", IndexedColors.ORANGE.getIndex());
        map.put("X35", IndexedColors.ORCHID.getIndex());
        map.put("X36", IndexedColors.PALE_BLUE.getIndex());
        map.put("X37", IndexedColors.PINK.getIndex());
        map.put("X38", IndexedColors.PLUM.getIndex());
        map.put("X39", IndexedColors.RED.getIndex());
        map.put("X40", IndexedColors.ROSE.getIndex());
        map.put("X41", IndexedColors.ROYAL_BLUE.getIndex());
        map.put("X42", IndexedColors.SEA_GREEN.getIndex());
        map.put("X43", IndexedColors.SKY_BLUE.getIndex());
        map.put("X44", IndexedColors.TAN.getIndex());
        map.put("X45", IndexedColors.TEAL.getIndex());
        map.put("X46", IndexedColors.TURQUOISE.getIndex());
        map.put("X47", IndexedColors.VIOLET.getIndex());
        map.put("X48", IndexedColors.WHITE.getIndex());
        map.put("X49", IndexedColors.YELLOW.getIndex());
        map.put("X50", IndexedColors.WHITE.getIndex());
        if(map.containsKey(key)){
            return map.get(key);
        }else{
            return null;
        }
    }

}
