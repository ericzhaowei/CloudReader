package com.ider.cloudreader.common;

import android.util.Log;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/14.
 */

public class JsonUtil {

    public static ArrayList<User> parseContacts(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            ArrayList<User> contactList = new ArrayList<>();
            JSONArray users = jsonObject.getJSONArray("users");
            Log.i("ContactsActivity", "parseContacts: users.length = " + s);
            for(int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                User contact = new User();
                contact.id = user.get("id").toString();
                contact.name = user.getString("name");
                contact.avatar_large = user.getString("avatar_large");
                contact.verified = user.getBoolean("verified");
                contactList.add(contact);
            }
            return contactList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Comment> parseComments(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            ArrayList<Comment> comments = new ArrayList<>();
            JSONArray array = jsonObject.getJSONArray("comments");
            for(int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Comment comment = Comment.parse(object);
                comments.add(comment);
            }
            return comments;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int[] parseCommentPageInfo(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            int[] info = new int[3];
            info[0] = jsonObject.getInt("previous_cursor");
            info[1] = jsonObject.getInt("next_cursor");
            info[2] = jsonObject.getInt("total_number");
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

}
