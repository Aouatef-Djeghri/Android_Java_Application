package com.example.servicebypro.Activities.HelperClasses;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Application;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class Util extends AppCompatActivity {

    private Context context;
    MyToast myToast;

    public Util(Context context) {

        this.context = context;
        myToast = new MyToast(context);
    }

    public String getNameFromString(String currentString) {

        if (currentString.contains("-")) {
            String[] separated = currentString.split("-");
            return separated[1].trim();
        } else {
            return currentString;
        }
    }

    public String getRealPathFromUri(Uri uri, Activity activity) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(activity.getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_idx);
        cursor.close();
        return result;
    }


    @NonNull
    public MultipartBody.Part prepareFilePart(String path, String partName) {
        // create RequestBody instance from file
        if (path != null) {
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
        } else {
            return null;
        }

    }

    public ArrayList<String> jsonStringToArray(String arrayString){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(arrayString, type);
    }
    public int getRandomInt(){
        Random rand = new Random();
        int randomNum = rand.nextInt((1000 - 500 ) + 1) + 500;
        return randomNum;
    }

    public void setAvatar(String avatarLink, ImageView imageView){

        if(!(avatarLink == null) && !avatarLink.trim().equals("")){
            Glide.with(context)
                    .load(avatarLink)
                    .error(R.drawable.ic_user)
                    .into(imageView);
        }
    }

    public void setWorkImage(String imageLink, ImageView imageView){

        if(!(imageLink == null) && !imageLink.trim().equals("")){
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(imageLink)
                    .error(R.drawable.field_id_image_icone)
                    .into(imageView);
        }else{
            imageView.setVisibility(View.GONE);
        }
    }

    public void setPhotoView(String imageLink, PhotoView photoView){

        if(!(imageLink == null) && !imageLink.trim().equals("")){
            photoView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(imageLink)
                    .error(R.drawable.field_id_image_icone)
                    .into(photoView);
        }else{
            photoView.setVisibility(View.GONE);
        }
    }

    public int calculateAverageRating(ArrayList<Review> reviews){

        int average = 0;
        for(int i=0;i<reviews.size();i++){
            average = average + reviews.get(i).getRating();
        }
        if( reviews.size() > 0 ){
            average = average/reviews.size();
            return average;
        }
        return average;
    }

    public static void dismissAllDialogs(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();

        if (fragments == null)
            return;

        for (android.app.Fragment fragment : fragments) {
            if (fragment instanceof DialogFragment) {
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismissAllowingStateLoss();
            }

            FragmentManager childFragmentManager = fragment.getChildFragmentManager();
            if (childFragmentManager != null)
                dismissAllDialogs(childFragmentManager);
        }
    }



    public  boolean isMyApplicationAccepted(Work work , User user){
        ArrayList<Application> applications = work.getApplications();

        for (int i=0;i<applications.size();i++){
            if (applications.get(i).getStatus().equals("accepted") && (applications.get(i).getUser().getIdUser().equals(user.getIdUser()))) {
                return true;
            }
        }
        return false;
    }

    public  Application getAcceptedApplication(Work work){

        ArrayList<Application> applications = work.getApplications();
        for (int i=0;i<applications.size();i++){
            if (applications.get(i).getStatus().equals("accepted")) {
                return applications.get(i);
            }
        }
        return null;
    }

    public List<String> getServiceNamesList(ArrayList<Service> services) {
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < services.size(); i++) {
            names.add(services.get(i).getName());
        }
        return names;
    }

    public boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        return sameDay;
    }

    public void hideKeyBoardWhenButtonClicked(View layout) {
        try {
            // Then just use the following:
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public String formationJoinDate(String joinDate) {
        MyToast myToast = new MyToast(context);
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat myFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            String reformattedStr = myFormat.format(fromUser.parse(joinDate));
            return reformattedStr;
        } catch (ParseException e) {
           // myToast.showError(e.getMessage());
        }
        return "";
    }

    public String formationJoinDateTime(String joinDate) {
        MyToast myToast = new MyToast(context);
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat myFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        try {
            String reformattedStr = myFormat.format(fromUser.parse(joinDate));
            return reformattedStr;
        } catch (ParseException e) {
           // myToast.showError(e.getMessage());
        }
        return "";
    }

    // Check internet connection
    public boolean isInternetConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }

    }

    //Show Custom Dialog 01
    public void ShowCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please connect to the internet to proceed further").setTitle("Connection Problem !")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Show Custom Dialog 02
    public void ShowCustomDialog(Activity activityToFinish, Class toRedirectTo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please connect to the internet to proceed further").setTitle("Connection Problem !")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(context, toRedirectTo));
                        activityToFinish.finish(); //if  you use this ,first pass activity in param
                    }
                });
        //Creating dialog box
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Fields Validation functions

    public Boolean validateXInput(@NotNull TextInputLayout xNameTextInputLayout) {

        String name = xNameTextInputLayout.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            xNameTextInputLayout.setError("Field cannot be empty");
            return false;
        } else {
            xNameTextInputLayout.setError(null);
            xNameTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }


    public Boolean validateEmail(@NotNull TextInputLayout emailTextInputLayout) {

        String email = emailTextInputLayout.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            emailTextInputLayout.setError("Field cannot be empty");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextInputLayout.setError("Invalid email address");
            return false;
        } else {
            emailTextInputLayout.setError(null);
            emailTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }


    public Boolean validatePhoneNo(@NotNull TextInputLayout phoneTextInputLayout) {
        String phoneNo = phoneTextInputLayout.getEditText().getText().toString().trim();

        if (phoneNo.isEmpty()) {
            phoneTextInputLayout.setError("Field cannot be empty");
            return false;
        } else if (!Patterns.PHONE.matcher(phoneNo).matches()) {
            phoneTextInputLayout.setError("Invalid phone number address");
            return false;
        } else {
            phoneTextInputLayout.setError(null);
            phoneTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }


    public Boolean validatePassword(@NotNull TextInputLayout passwordTextInputLayout) {
        String password = passwordTextInputLayout.getEditText().getText().toString().trim();

        //RegEx : Regular expression tester with syntax highlighting
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                //"(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (password.isEmpty()) {
            passwordTextInputLayout.setError("Field cannot be empty");
            return false;
        } else if (!password.matches(passwordVal)) {
            passwordTextInputLayout.setError("Password is too weak");
            return false;
        } else {
            passwordTextInputLayout.setError(null);
            passwordTextInputLayout.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean validateZipCode(@NotNull TextInputLayout zipCodeTextInputLayout) {
        String zipCode = zipCodeTextInputLayout.getEditText().getText().toString().trim();

        if (zipCode.isEmpty()) {
            zipCodeTextInputLayout.setError("Field cannot be empty");
            return false;
        } else {
            zipCodeTextInputLayout.setError(null);
            zipCodeTextInputLayout.setErrorEnabled(false);
            return true;
        }

    }


    public Boolean validateServicesList(@NotNull TextInputLayout servicesListTextInputLayout) {

        String name = servicesListTextInputLayout.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            servicesListTextInputLayout.setError("Field cannot be empty");
            return false;
        } else {
            servicesListTextInputLayout.setError(null);
            servicesListTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCurrentDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateAsString = simpleDateFormat.format(Calendar.getInstance().getTime());
        return dateAsString;
    }


    public Boolean validateWorkTitle(@NotNull TextInputLayout xNameTextInputLayout) {

        String name = xNameTextInputLayout.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            xNameTextInputLayout.setError("Field cannot be empty");
            return false;
        } else {
            xNameTextInputLayout.setError(null);
            xNameTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public Boolean validateWorkTitleCharacterNumber(@NotNull TextInputLayout textInputLayout, TextView number, int maxLength, int minLength, int length) {
        if (length > maxLength) {
            textInputLayout.setError("Please enter at most " + maxLength + " characters");
            number.setTextColor(ContextCompat.getColor(context,R.color.my_red));
            return false;
        }
        if (length < minLength) {
            textInputLayout.setError("Please enter at least " + minLength + " characters");
            number.setTextColor(ContextCompat.getColor(context,R.color.my_red));
            return false;
        } else if (length == minLength || length == maxLength) {
            number.setTextColor(ContextCompat.getColor(context,R.color.mygray));
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return true;

    }

    public Boolean validateWorkDate(@NotNull TextInputLayout textInputLayout) {

        String name = textInputLayout.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            textInputLayout.setError("Field cannot be empty");
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
