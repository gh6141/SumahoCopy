package com.example.sumahocopy;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {



    private AccessTask task;
    private AccessTask2 task2;

    private TextView txtView;
    private Button accessbtn;
    private Button accessbtn2;
    private ProgressBar pBar;
    private Button btn_save;

    private TextInputEditText toNas;
    private TextInputEditText fromNas;
    private TextInputEditText toSumaho;
    private TextInputEditText fromSumaho;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtView = (TextView)findViewById(R.id.textView);
        // txtView.setText("0");

        TextInputEditText toNas = (TextInputEditText) findViewById(R.id.textInputEditText);
        TextInputEditText fromNas = (TextInputEditText) findViewById(R.id.textInputEditTextB);
        TextInputEditText fromSumaho = (TextInputEditText) findViewById(R.id.textInputEditText2);
        TextInputEditText toSumaho = (TextInputEditText) findViewById(R.id.textInputEditText2B);


        toNas.setText(readFile("toNas.txt"));
        toSumaho.setText(readFile("toSumaho.txt"));
        fromNas.setText(readFile("fromNas.txt"));
        fromSumaho.setText(readFile("fromSumahos.txt"));


        task = new AccessTask(txtView,pBar);
        pBar = findViewById(R.id.progressBar);
        task2 = new AccessTask2(txtView,pBar);

        accessbtn = findViewById(R.id.button5);
        accessbtn.setOnClickListener(AccessListener);

        accessbtn2 = findViewById(R.id.button);
        accessbtn2.setOnClickListener(AccessListener2);

        btn_save = findViewById(R.id.button_save);
        btn_save.setOnClickListener(saveListener);

        Samba samba = new Samba();
        try {
            if (samba.connect("192.168.1.200")) {
              //  System.out.println("Sambaサーバの接続に成功");
                txtView.setText("NASに接続できました");
            } else {
              //  System.out.println("Sambaサーバの接続に失敗");
                txtView.setText("NASに接続できません");
            }
        } catch (InterruptedException e) {
           // e.printStackTrace();
            txtView.setText("WIFIに接続できません.Error");
        }


    }




    private android.view.View.OnClickListener AccessListener = new android.view.View.OnClickListener() {
        public void onClick(android.view.View v)
        {
            task.execute(1);
        }
    };

    private android.view.View.OnClickListener AccessListener2 = new android.view.View.OnClickListener() {
        public void onClick(android.view.View v)
        {
            task2.execute(1);
        }
    };

    private android.view.View.OnClickListener saveListener = new android.view.View.OnClickListener() {
        public void onClick(android.view.View v)
        {
       
            saveFile("toNas", toNas.getText().toString());
            saveFile("toSumaho", toSumaho.getText().toString());
            saveFile("fromNas", fromNas.getText().toString());
            saveFile("toSumaho", toSumaho.getText().toString());
        }
    };




    // ファイルを保存
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveFile(String file, String str) {

        // try-with-resources
        try (FileOutputStream  fileOutputstream = openFileOutput(file,
                Context.MODE_PRIVATE);){

            fileOutputstream.write(str.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ファイルを読み出し
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readFile(String file) {
        String text = null;

        // try-with-resources
        try (FileInputStream fileInputStream = openFileInput(file);
             BufferedReader reader= new BufferedReader(
                     new InputStreamReader(fileInputStream, StandardCharsets.UTF_8))) {

            String lineBuffer;
            while( (lineBuffer = reader.readLine()) != null ) {
                text = lineBuffer ;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

}

