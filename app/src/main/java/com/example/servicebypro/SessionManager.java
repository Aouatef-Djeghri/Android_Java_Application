package com.example.servicebypro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.servicebypro.Activities.Common.StartUpScreen;
import com.example.servicebypro.Remote.Models.Address;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {

    private static final String TAG = "SessionManager";
    private SharedPreferences sharedPreferences;
    private Context context;
    SharedPreferences.Editor editor;
    User user = new User();

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveSession(User userData) {
        //save session of user whenever user is logged in
        user = userData;
        editor.putInt(context.getString(R.string.pref_user_id), user.getIdUser());
        editor.putBoolean(context.getString(R.string.pref_login_status), true);
        editor.putString(context.getString(R.string.pref_user_first_name), user.getFirstName());
        editor.putString(context.getString(R.string.pref_user_last_name), user.getLastName());
        editor.putString(context.getString(R.string.pref_user_role), user.getRole());
        editor.putString(context.getString(R.string.pref_user_phone), user.getPhone());
        editor.putString(context.getString(R.string.pref_user_email), user.getEmail());
        editor.putString(context.getString(R.string.pref_user_about), user.getAboutMe());
        editor.putString(context.getString(R.string.pref_user_commune), user.getAddress().getCommune());
        editor.putString(context.getString(R.string.pref_user_wilaya), user.getAddress().getWilaya());
        editor.putString(context.getString(R.string.pref_user_join_date), user.getJoinDate());
        editor.putString(context.getString(R.string.pref_user_avatar), user.getAvatar());
        editor.putInt(context.getString(R.string.pref_user_is_active), user.getIsActive());
        editor.apply();

        saveUserServicesList(user.getServices());
    }

    public void updateUserVisibility(int visibility){
        editor.putInt(context.getString(R.string.pref_user_is_active), visibility);
        editor.apply();
    }

    public int getUserVisibility(){
        return  sharedPreferences.getInt(context.getString(R.string.pref_user_is_active),0);
    }

    public void saveUserServicesList(ArrayList<Service> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("services", json);
        editor.apply();
    }

    public ArrayList<Service> getUserServicesList(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("services", null);
        Type type = new TypeToken<ArrayList<Service>>() {}.getType();
        return gson.fromJson(json, type);
    }


    public void updateUserData(User user) {
        editor.putString(context.getString(R.string.pref_user_phone), user.getPhone());
        editor.putString(context.getString(R.string.pref_user_email), user.getEmail());
        editor.putString(context.getString(R.string.pref_user_about), user.getAboutMe());
        editor.putString(context.getString(R.string.pref_user_commune), user.getAddress().getCommune());
        editor.putString(context.getString(R.string.pref_user_wilaya), user.getAddress().getWilaya());
        editor.putString(context.getString(R.string.pref_user_avatar), user.getAvatar());
        editor.apply();
    }

    public User getUser() {
        user.setIdUser(sharedPreferences.getInt(context.getString(R.string.pref_user_id), -1));
        user.setFirstName(sharedPreferences.getString(context.getString(R.string.pref_user_first_name), null));
        user.setLastName(sharedPreferences.getString(context.getString(R.string.pref_user_last_name), null));
        user.setPhone(sharedPreferences.getString(context.getString(R.string.pref_user_phone), null));
        user.setEmail(sharedPreferences.getString(context.getString(R.string.pref_user_email), null));
        user.setRole(sharedPreferences.getString(context.getString(R.string.pref_user_role), null));
        user.setAboutMe(sharedPreferences.getString(context.getString(R.string.pref_user_about), null));
        user.setJoinDate(sharedPreferences.getString(context.getString(R.string.pref_user_join_date), null));
        user.setAvatar(sharedPreferences.getString(context.getString(R.string.pref_user_avatar), null));
        user.setIsActive(sharedPreferences.getInt(context.getString(R.string.pref_user_is_active), 0));
        Address address = new Address();
        address.setCommune(sharedPreferences.getString(context.getString(R.string.pref_user_commune), null));
        address.setWilaya(sharedPreferences.getString(context.getString(R.string.pref_user_wilaya), null));
        user.setAddress(address);
        user.setServices(getUserServicesList());
        return user;
    }

    public boolean checkLogin() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status), false);
    }

    public void logout() {
        editor.clear();
        editor.commit();

        //Redirection to login page and clearing all the tasks
        Intent intent = new Intent(context, StartUpScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
