package com.example.sumahocopy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AccessTask task;
    private AccessTask2 task2;

    private TextView txtView;
    private Button accessbtn;
    private Button accessbtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtView = (TextView)findViewById(R.id.textView);
        // txtView.setText("0");

        task = new AccessTask(txtView);
        task2 = new AccessTask2(txtView);

        accessbtn = findViewById(R.id.button5);
        accessbtn.setOnClickListener(AccessListener);

        accessbtn2 = findViewById(R.id.button);
        accessbtn2.setOnClickListener(AccessListener2);

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
}

