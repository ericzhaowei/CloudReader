package com.ider.cloudreader.common;

import android.util.Log;

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

}
