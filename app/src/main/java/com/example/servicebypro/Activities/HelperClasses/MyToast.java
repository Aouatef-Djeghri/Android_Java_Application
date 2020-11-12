package com.example.servicebypro.Activities.HelperClasses;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.dmoral.toasty.Toasty;

public class MyToast extends AppCompatActivity {


    private Context context;


    public MyToast(Context context) {
        this.context = context;
    }

    // To display an error Toast:
    public  void showError(String message)  {
        Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
    }

    // To display a success Toast
    public  void showSuccess(String message)  {
        Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    //To display an info Toast:
    public  void showInfo(String message)  {
        Toasty.info(context, message, Toast.LENGTH_SHORT, true).show();
    }

    //To display a warning Toast:
    public  void showWarning(String message)  {
        Toasty.warning(context, message, Toast.LENGTH_SHORT, true).show();
    }

    //To display the usual Toast:
    public  void showUsual(String message)  {
        Toasty.normal(context, message).show();
    }

}

