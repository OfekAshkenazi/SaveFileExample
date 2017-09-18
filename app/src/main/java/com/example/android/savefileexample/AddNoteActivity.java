package com.example.android.savefileexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class AddNoteActivity extends AppCompatActivity {
    SharedPreferences preferences;
    int currentIndex;
    EditText contentET;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        preferences=getSharedPreferences("Settings",MODE_PRIVATE);
        currentIndex=preferences.getInt("index",0);
        contentET= (EditText) findViewById(R.id.contentET);
        btn= (Button) findViewById(R.id.saveBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentET.getText().toString().equals(""))return;
                if (saveToFile(contentET.getText().toString())){
                    Toast.makeText(AddNoteActivity.this, "Note Added", Toast.LENGTH_SHORT).show();
                    preferences.edit().putInt("index",++currentIndex).commit();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                    return;
                }
                Toast.makeText(AddNoteActivity.this, "there was a problem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean saveToFile(String s) {
        OutputStream stream = null;
        try {
            stream=this.openFileOutput(""+currentIndex,MODE_PRIVATE);
            stream.write(s.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (stream!=null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


}
