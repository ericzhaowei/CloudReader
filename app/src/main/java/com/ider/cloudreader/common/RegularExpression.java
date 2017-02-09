package com.ider.cloudreader.common;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ider-eric on 2017/2/8.
 */

public class RegularExpression {
    private static final String TAG = "regex";
    
    private static String AT = "@[\\u4e00-\\u9fa5\\w]+";
    private static String TOPIC = "#[\\u4e00-\\u9fa5\\w]+#";
    private static String EMOJI = "\\[[\\u4e00-\\u9fa5\\w]+\\]";
    private static String URL = "http://([\\w-]+\\.)+([\\w./-?%&=]*)?";


    private static int highlightColor = 0xFF2A86E2;

    private static String REGEX = String.format("(%s)|(%s)|(%s)|(%s)", AT, TOPIC, EMOJI, URL);

    public static SpannableString checkText(Context context, String text) {
        SpannableString sp = new SpannableString(text);
        Log.i(TAG, "checkText: " + REGEX);
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()) {

            for(int i = 1; i <= 4; i++) {
                if(i == 3) {
                    continue;
                }
                String group = matcher.group(i);
                if(group != null) {
                    sp.setSpan(new ClickSpan(i), matcher.start(i), matcher.end(i), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            String emojiStr = matcher.group(3);
            if(emojiStr != null) {
                int emoji = EmojiHolder.getEmojiIcon(emojiStr);
                if (emoji != 0) {
                    sp.setSpan(new ImageSpan(context, emoji), matcher.start(3), matcher.end(3), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

        }
        return sp;
    }

    static class ClickSpan extends ClickableSpan {

        private int type;

        public ClickSpan(int type) {
            super();
            this.type = type;
        }

        @Override
        public void onClick(View view) {

            Toast.makeText(view.getContext(), "点击" + type, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(highlightColor);
            ds.setUnderlineText(false);
        }
    }



    public static String getStatusSource(String string) {
        String regex = ">[\\w\\W]+<";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        if(matcher.find()) {
            String group = matcher.group();
            return group.substring(1, group.length() -1);
        }
        return string;
    }

}
