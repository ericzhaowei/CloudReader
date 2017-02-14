package com.ider.cloudreader.common;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by ider-eric on 2017/2/14.
 */

public class SpellUtil {


    /* 获取汉子字符串拼音，例如：你好>nihao */
    public static String getPinyinHeadChar(String hanzi) {
        StringBuilder sb = new StringBuilder();
        char[] chars = hanzi.toCharArray();
        HanyuPinyinOutputFormat pinyinOutputFormat = new HanyuPinyinOutputFormat();
        pinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        pinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char aChar : chars) {
            if(aChar > 128) {
                try {
                    String stringPinyin = PinyinHelper.toHanyuPinyinStringArray(aChar, pinyinOutputFormat)[0];
                    sb.append(stringPinyin);

                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
            } else {
                sb.append(aChar);
            }
        }
        return sb.toString();
    }

    public static String getFirstLetter(String hanzi) {
        char[] chars = hanzi.toCharArray();
        char first = chars[0];
        if(first > 128) {
            HanyuPinyinOutputFormat pinyinOutputFormat = new HanyuPinyinOutputFormat();
            pinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            pinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            try {
                String strPinyin = PinyinHelper.toHanyuPinyinStringArray(chars[0], pinyinOutputFormat)[0];
                return strPinyin.substring(0, 1);
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        } else {
            return String.valueOf(first);
        }

        return null;
    }

}
