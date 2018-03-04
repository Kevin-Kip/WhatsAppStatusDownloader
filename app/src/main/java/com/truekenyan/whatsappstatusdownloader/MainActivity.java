package com.truekenyan.whatsappstatusdownloader;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.truekenyan.whatsappstatusdownloader.adapters.RecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String STATUS_LOCATION = "/WhatsApp/Media/.Statuses";
        RecyclerView statusList = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getStatuses(new File(Environment.getExternalStorageDirectory().toString() + STATUS_LOCATION)), MainActivity.this);
        statusList.setHasFixedSize(true);
        statusList.setLayoutManager(layoutManager);
        statusList.setAdapter(adapter);
    }

    private ArrayList<File> getStatuses(File parentDirectory){
        ArrayList<File> statuses = new ArrayList<>();
        File[] files;
        files = parentDirectory.listFiles();
        if (files != null){
            for (File file : files){
                if (file.getName().endsWith(".mp4") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")){
                    if (!statuses.contains(file)){
                        statuses.add(file);
                    }
                }
            }
            Toast.makeText(getApplicationContext(), String.valueOf(statuses.size()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Oops! No statuses found", Toast.LENGTH_SHORT).show();
        }
        return statuses;
    }
}
