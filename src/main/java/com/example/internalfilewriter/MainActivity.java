package com.example.internalfilewriter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    Button Read;
    Button Write;

    EditText Data;

    private int REQUEST_CODE = 6162;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Read = findViewById(R.id.button_read);
        Write = findViewById(R.id.button_write);
        Data = findViewById(R.id.editText_data);

        Data.setText(loadData());



        Read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.setText(loadData());
            }
        });

        Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeData(Data.getText().toString());
            }
        });



    }

    private void writeData(String Data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                // Permission is Already is Granted
                try{
                    writeDataHelper(Data);
                }catch (Exception E){
                    Log.e(getApplication().toString(),E.getMessage());
                }
            }else{

                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(getApplicationContext(),"The Permission is necessary",Toast.LENGTH_LONG).show();
                }

                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }

        else{
            try{
                writeDataHelper(Data);
            }catch (Exception E){
                Log.e(getApplication().toString(),E.getMessage());
            }
        }
    }

    private void writeDataHelper(String Data) throws IOException {

        File path = this.getFilesDir();
        File file = new File(path,"Data.txt");

        if(!file.exists()){
            file.createNewFile();
        }

        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(Data.getBytes());
            System.out.println("Data Writtten");
        } finally {
            stream.close();
        }

    }


    private String loadData() {

        String Result = "Enter the Data Here";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                // Permission is Already is Granted
                try{
                    Result = loadDataHelper();
                }catch (Exception E){
                    Log.e(getApplication().toString(),E.getMessage());
                }

            }else{
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }

        try{
            Result = loadDataHelper();
        }catch (Exception E){
            Log.e(getApplication().toString(),E.getMessage());
        }

        return Result;
    }


    private String loadDataHelper() throws IOException{
        File path = this.getFilesDir();

        File file = new File(path,"Data.txt");

        if(!file.exists()) {
            file.createNewFile();
        }

        int length = (int) file.length();

        byte[] bytes = new byte[length];

        FileInputStream in = new FileInputStream(file);
        try {
            in.read(bytes);
        } finally {
            in.close();
        }
        String contents = new String(bytes);
        System.out.println("The C"+contents);

        return contents;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if( permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Cant DO I/0 .. ",Toast.LENGTH_LONG);
            }
        }



    }
}
