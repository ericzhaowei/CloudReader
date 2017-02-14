package com.ider.cloudreader.common;

import com.sina.weibo.sdk.openapi.models.User;

import java.util.Comparator;

/**
 * Created by ider-eric on 2017/2/14.
 */

public class LetterComparator implements Comparator {
    @Override
    public int compare(Object o, Object o1) {

        if(o instanceof User && o1 instanceof User) {
            String pinyin = SpellUtil.getPinyinHeadChar(((User) o).name);
            String pinyin1 = SpellUtil.getPinyinHeadChar(((User) o1).name);
            char[] chars = pinyin.toCharArray();
            char[] chars1 = pinyin1.toCharArray();
            int minLength = Math.min(chars.length, chars1.length);
            for(int i = 0; i < minLength; i++) {
                int result = compareLetter(chars[i], chars1[i]);
                if(result != 0) {
                    return result;
                }
            }
            if(chars.length < chars1.length) {
                return -1;
            } else if(chars.length > chars1.length) {
                return 1;
            } else {
                return 0;
            }
        }



        return 0;
    }


    private int compareLetter(char letter1, char letter2) {
        if(letter1 < letter2) {
            return -1;
        } else if(letter1 == letter2) {
            return 0;
        } else {
            return 1;
        }
    }
}
