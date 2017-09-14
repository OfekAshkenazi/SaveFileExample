package com.example.android.savefileexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Button addBtn;
    String[] files;
    ArrayList<String> notes=new ArrayList<>();
    ArrayAdapter adapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        files=getAllFiles();
        initializeNotes();
        listView= (ListView) findViewById(R.id.listView);
        addBtn= (Button) findViewById(R.id.addBtn);
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(adapter);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeNotes() {
        InputStreamReader stream = null;
        for (String fileName:files){
            try {
                stream=new InputStreamReader(this.openFileInput(fileName));
                BufferedReader reader=new BufferedReader(stream);
                StringBuilder sb=new StringBuilder();
                String line;
                while((line=reader.readLine())!=null){
                    sb.append(line);
                }
                notes.add(sb.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (stream!=null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences=getSharedPreferences("Settings",MODE_PRIVATE);
        int index=preferences.getInt("index",0);
        InputStreamReader stream = null;
        try {
            stream = new InputStreamReader(this.openFileInput(""+(index-1)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader=new BufferedReader(stream);
        StringBuilder sb=new StringBuilder();
        String line;
        try {
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        notes.add(sb.toString());
    }
    private String[] getAllFiles(){
        return fileList();
    }

}
