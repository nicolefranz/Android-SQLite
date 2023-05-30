package com.sqllite;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.IntRange;

import java.util.ArrayList;

public class UpdateActivity extends Activity {

    public EditText Fname, Mname, Lname;

    public String FNAME = null;
    public String MNAME = null;
    public String LNAME = null;

    public Intent DispForm;
    public SQLiteDatabase Conn;
    public Cursor rs;
    public static Integer ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Fname = (EditText) findViewById(R.id.Fname);
        Mname = (EditText) findViewById(R.id.Mname);
        Lname = (EditText) findViewById(R.id.Lname);
        Conn = new SQLiteDatabase(this);

        try {
            rs = Conn.getData(ID);
            rs.moveToFirst();
            Fname.setText(rs.getString(rs.getColumnIndex(Conn.PROFILE_FNAME)));
            Mname.setText(rs.getString(rs.getColumnIndex(Conn.PROFILE_MNAME)));
            Lname.setText(rs.getString(rs.getColumnIndex(Conn.PROFILE_LNAME)));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void UpdateRecord(View view){
        FNAME = Fname.getText().toString().trim();
        MNAME = Mname.getText().toString().trim();
        LNAME = Lname.getText().toString().trim();

        if (FNAME.isEmpty()){
            Fname.setError("Enter your First Name");
            Fname.requestFocus();
        } else if (MNAME.isEmpty()) {
            Mname.setError("Enter your Middle Name");
            Mname.requestFocus();
        } else if (LNAME.isEmpty()) {
            Lname.setError("Enter your Last Name");
            Lname.requestFocus();
        } else {
            if (isDuplicateRecord(FNAME, MNAME, LNAME)){
                Fname.setText(null);
                Mname.setText(null);
                Lname.setText(null);
                Toast.makeText(this, "DUPLICATED RECORD!", Toast.LENGTH_SHORT).show();
                hidekeyboard(view);
            }else{
                try{
                    Conn.UpdateRecords(ID, FNAME, MNAME, LNAME);
                    Fname.setText(null);
                    Mname.setText(null);
                    Lname.setText(null);
                    Toast.makeText(this, "RECORD UPDATED!", Toast.LENGTH_SHORT).show();

                    DispForm = new Intent(UpdateActivity.this, RecordsActivity.class);
                    startActivity(DispForm);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void DeleteRecord(View view){
        try{
            Conn.DeleteRecord(ID);
            Toast.makeText(this, "RECORDED DELETED!", Toast.LENGTH_SHORT).show();
            DispForm = new Intent(UpdateActivity.this, RecordsActivity.class);
            startActivity(DispForm);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Back(View view){
        DispForm = new Intent(UpdateActivity.this, RecordsActivity.class);;
        startActivity(DispForm);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void hidekeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}