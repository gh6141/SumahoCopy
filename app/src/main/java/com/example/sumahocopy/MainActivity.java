package com.example.sumahocopy;


import android.content.Context;

import android.os.Bundle;

import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    private AccessTask task;
    private AccessTask2 task2;
    private Button accessbtn;
    private Button accessbtn2;
    private Button btn_save;
    private TextInputEditText toNas;
    private TextInputEditText fromNas;
    private TextInputEditText toSumaho;
    private TextInputEditText fromSumaho;
    private TextInputEditText ipaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtView;
        txtView = (TextView) findViewById(R.id.textView);
         toNas = (TextInputEditText) findViewById(R.id.textInputEditText);
         fromNas = (TextInputEditText) findViewById(R.id.textInputEditTextB);
         fromSumaho = (TextInputEditText) findViewById(R.id.textInputEditText2);
         toSumaho = (TextInputEditText) findViewById(R.id.textInputEditText2B);
         ipaddress = (TextInputEditText) findViewById(R.id.ipaddress);

        toNas.setText(readFile("toNas.txt"));
        toSumaho.setText(readFile("toSumaho.txt"));
        fromNas.setText(readFile("fromNas.txt"));
        fromSumaho.setText(readFile("fromSumaho.txt"));
        ipaddress.setText(readFile("ipaddress.txt"));

        ProgressBar pBar;
        pBar = (ProgressBar)findViewById(R.id.progressBar);
        task = new AccessTask(txtView,pBar,readFile("toSumaho.txt"),readFile("fromNas.txt"),readFile("ipaddress.txt"));
        task2 = new AccessTask2(txtView,pBar,readFile("toNas.txt"),readFile("fromSumaho.txt"),readFile("ipaddress.txt"));

        accessbtn = (Button)findViewById(R.id.button5);
        accessbtn.setOnClickListener(AccessListener);

        accessbtn2 = (Button)findViewById(R.id.button);
        accessbtn2.setOnClickListener(AccessListener2);

        btn_save = (Button)findViewById(R.id.button_save);
        btn_save.setOnClickListener(saveListener);

        Samba samba = new Samba();
        try {
            if (samba.connect(readFile("ipaddress.txt"))) {
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
            boolean flg=true;
            try {
                String tmp=toNas.getText().toString();
                saveF("toNas.txt", tmp);
            } catch (IOException e) {
                toNas.setText("");
                flg=flg && false;
            }

            try {
                saveF("fromSumaho.txt",fromSumaho.getText().toString() );
            } catch (IOException e) {
                toSumaho.setText("");
                flg=false&&flg;
            }

            try {
                saveF("fromNas.txt",fromNas.getText().toString() );
            } catch (IOException e) {
                fromNas.setText("");
                flg=false&&flg;
            }

            try {
                saveF("toSumaho.txt",toSumaho.getText().toString());
            } catch (IOException e) {
                toSumaho.setText("");
                flg=false&&flg;}

            try {
                saveF("ipaddress.txt",ipaddress.getText().toString());
            } catch (IOException e) {
                toSumaho.setText("");
                flg=false&&flg;
            }
                TextView txtView = (TextView) findViewById(R.id.textView);
            if (flg) {
                txtView.setText("設定を保存しました");
            }else{
                txtView.setText("Error 設定の保存できないものがありました");
            }

        }


    };




    // ファイルを保存

    public void saveF(String file, String str) throws IOException {

            FileOutputStream  fileOutputstream = openFileOutput(file,Context.MODE_PRIVATE);
            fileOutputstream.write(str.getBytes());

    }

    // ファイルを読み出し

    public String readFile(String file)  {
        String text = null;
        BufferedReader in = null;
        final Context context = this;
        try {
        FileInputStream filex = context.openFileInput(file);
        in = new BufferedReader(new InputStreamReader(filex));
        text=in.readLine();
        in.close();
        } catch (IOException e) {
        text="" ;
        }
        return text;
    }

}

