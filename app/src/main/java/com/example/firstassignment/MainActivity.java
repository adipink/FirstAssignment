package com.example.firstassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS_PERMISSION = 0;

    private EditText main_EDT_Password;
    private MaterialButton main_BTN_enter;

    private BatteryManager batteryManage ;

    private boolean isPercentageOK, isContactOK, isWifi, isDayOK, isHourOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initServices();
        createListeners();
    }

    private void createListeners() {
        main_BTN_enter.setOnClickListener(v -> {
            //get password from user
            String password = main_EDT_Password.getText().toString();

            //check if password is ""
            if(password.isEmpty()){
                Toast.makeText(MainActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
            }else{
                //check if phone is connected to WIFI
                checkWifi();
                //check Percentage of the phone
                checkPercentage(password);
                //check the time of the phone
                checkTime();
                //contactExists();
                if(isPercentageOK/* && isContactOK*/ && isWifi && isDayOK && isHourOK){
                    Toast.makeText(MainActivity.this, "You are connected to WIFI", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "Login Succeeded", Toast.LENGTH_SHORT).show();
                    loginComplete();
                }
            }
        });
    }

    private void loginComplete(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("SUCCESS");
        alert.setPositiveButton("Ok", (dialog, whichButton) -> {
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // Canceled.
        });
        alert.show();
    }


    private void initServices() {
        batteryManage = (BatteryManager) getSystemService(BATTERY_SERVICE);
       // requestContactsPermission();
    }

    private void findViews() {
        main_EDT_Password = findViewById(R.id.main_EDT_Password);
        main_BTN_enter = findViewById(R.id.main_BTN_enter);
    }

    private void checkPercentage(String password) {
        int battery = batteryManage.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        int pass = Integer.parseInt(password);

        //is password is bigger than battery user can enter
        if(pass > battery){
            isPercentageOK = true;
            Toast.makeText(MainActivity.this, "Password is correct", Toast.LENGTH_SHORT).show();
        }else{
            isPercentageOK = false;
        }
    }

    private void checkWifi(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private void checkTime(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        //if the day is sunday|friday|saturday you are allowed entrance
        if(dayOfWeek >= 6 || dayOfWeek == 1){
            isDayOK = true;
            Toast.makeText(MainActivity.this, "Today is part of the weekend", Toast.LENGTH_SHORT).show();
        }else{
            isDayOK = false;
        }
        //if the hour is past 12:00AM you are allowed entrance
        if(hourOfDay >= 12){
            isHourOK = true;
            Toast.makeText(MainActivity.this, "The Hour is past 12AM", Toast.LENGTH_SHORT).show();
        }else{
            isHourOK = false;
        }
    }
/*
    private boolean hasContactsPermission()
    {
        return ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }

    // Request contact permission if it
    // has not been granted already
    private void requestContactsPermission()
    {
        if (!hasContactsPermission())
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
        }
    }

    public void contactExists() {
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        String contactId = null;
        String contactName = null;
        int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        int idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        if(nameColumnIndex>=0){
            contactName = cursor.getString(nameColumnIndex);
        }
        while (cursor.moveToNext()) {

            if (contactName.equals("Police")  && idColumnIndex>=0 ) { // Replace with the name of the contact you want to check
                contactId = cursor.getString(idColumnIndex);
                break;
            }
        }
        cursor.close();

        if (contactId != null) {
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);
            if (phoneCursor.moveToNext()) {
                // Allow entrance to the application
                isContactOK = true;
            } else {
                // Show an error message to the user
                isContactOK = false;
                Toast.makeText(this, "Contact does not have a phone number", Toast.LENGTH_SHORT).show();
            }
            phoneCursor.close();
        } else {
            // Show an error message to the user
            isContactOK = false;
            Toast.makeText(this, "Contact not found", Toast.LENGTH_SHORT).show();
        }
    }// contactExists*/

}