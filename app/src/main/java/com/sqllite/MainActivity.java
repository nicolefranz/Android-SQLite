package com.sqllite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public Intent DisplayForm;
    public SQLiteDatabase Conn;

    public EditText Fname;
    public EditText Mname;
    public EditText Lname;

    public String FNAME = null;
    public String MNAME = null;
    public String LNAME = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fname = (EditText) findViewById(R.id.Fname);
        Mname = (EditText) findViewById(R.id.Mname);
        Lname = (EditText) findViewById(R.id.Lname);
        Conn = new SQLiteDatabase(this);
    }

    public void AddRecord(View view){
        FNAME = Fname.getText().toString().trim();
        MNAME = Mname.getText().toString().trim();
        LNAME = Lname.getText().toString().trim();

        if(FNAME.isEmpty()){
            Fname.setError("Enter Your First Name");
            Fname.requestFocus();
        } else if (MNAME.isEmpty()) {
            Mname.setError("Enter Your Middle Name");
            Mname.requestFocus();
        } else if (LNAME.isEmpty()) {
            Lname.setError("Enter Your Last Name");
            Lname.requestFocus();
        }else {
            if (isDuplicateRecord(FNAME, MNAME, LNAME)){
                Fname.setText(null);
                Mname.setText(null);
                Lname.setText(null);
                Toast.makeText(this, "DUPLICATED RECORD!", Toast.LENGTH_SHORT).show();
                hidekeyboard(view);
            }else {
                if (Conn.AddRecords(FNAME, MNAME, LNAME)){
                    Fname.setText(null);
                    Mname.setText(null);
                    Lname.setText(null);
                    Toast.makeText(getApplicationContext(), "RECORD SAVED!", Toast.LENGTH_SHORT).show();
                    hidekeyboard(view);
                }else {
                    Fname.setText(null);
                    Mname.setText(null);
                    Lname.setText(null);
                    Toast.makeText(getApplicationContext(), "SAVING INFORMATION FAILED!", Toast.LENGTH_SHORT).show();
                    hidekeyboard(view);
                }
            }
        }
    }

    private boolean isDuplicateRecord(String fname, String mname, String lname) {
        ArrayList<String> records = Conn.GetAllData();
        for (String record : records) {
            String[] parts = record.split(", ");
            if (parts.length == 3) {
                String existingFname = parts[1].trim();
                String existingMname = parts[2].trim();
                String existingLname = parts[0].trim();
                if (existingFname.equalsIgnoreCase(fname) &&
                        existingMname.equalsIgnoreCase(mname) &&
                        existingLname.equalsIgnoreCase(lname)) {
                    return true;
                }
            }
        }
        return false; // No duplicate record found
    }

    public void ViewRecords(View view){
        DisplayForm = new Intent(MainActivity.this, RecordsActivity.class);
        startActivity(DisplayForm);
    }

    public void ClearRecords(View view){
        try{
            Conn.ClearRecord();
            Toast.makeText(getApplicationContext(), "RECORDS CLEAR", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void hidekeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}