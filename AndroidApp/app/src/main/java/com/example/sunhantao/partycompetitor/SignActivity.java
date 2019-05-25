package com.example.sunhantao.partycompetitor;


import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.angmarch.views.NiceSpinner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SignActivity extends AppCompatActivity {
    String name1;
    String name2;
    int numberOfQuestions;
    TextInputEditText tv1;
    TextInputEditText tv2;
    NiceSpinner ns;
    TextView wrong;
    Button start;
    BluetoothSocket mBlueToothSocket;
    OutputStream outStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_activity);
        tv1 = findViewById(R.id.name1);
        tv2 = findViewById(R.id.name2);
        ns = findViewById(R.id.nice_spinner);
        wrong = findViewById(R.id.wrong);
        start = findViewById(R.id.start);
        final List<String> dataList = new ArrayList<>();
        dataList.add("5");
        dataList.add("10");
        dataList.add("15");
        dataList.add("20");
        dataList.add("25");
        mBlueToothSocket = ((GlobalBlueSocket)getApplication()).getGlobalBlueSocket();
        ns.attachDataSource(dataList);
        Intent lastIntent = getIntent();
        numberOfQuestions = lastIntent.getIntExtra("number", 0);
        if (numberOfQuestions > 0) {
            name1 = lastIntent.getStringExtra("name1");
            name2 = lastIntent.getStringExtra("name2");
            tv1.setText(name1);
            tv2.setText(name2);
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).equals(numberOfQuestions + ""))
                    ns.setSelectedIndex(i);
            }
        } else {
            ns.setSelectedIndex(0);
            numberOfQuestions = 5;
        }
        ns.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                numberOfQuestions = Integer.parseInt(dataList.get(position));
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv1.getText().toString().length() > 0 && tv2.getText().toString().length() > 0){
                    Intent intent = new Intent(SignActivity.this, PlayActivity.class);
                    intent.putExtra("name1", tv1.getText().toString());
                    intent.putExtra("name2", tv2.getText().toString());
                    intent.putExtra("number", numberOfQuestions);
                    startActivity(intent);
                    finish();
                } else
                    wrong.setVisibility(View.VISIBLE);
            }
        });
    }

}
