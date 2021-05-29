package com.example.doinhietdo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnCtoF, btnFtoC, btnClear;
    private EditText etF, etC;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etC =      (EditText) findViewById(R.id.etC);
        etF =      (EditText) findViewById(R.id.etF);
        tvResult = (TextView) findViewById(R.id.tvkq);
        btnFtoC =  (Button) findViewById(R.id.btnFtoC);
        btnCtoF =  (Button) findViewById(R.id.btnCtoF);
        btnClear = (Button) findViewById(R.id.btnclear);

        btnCtoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double c = Double.parseDouble(etC.getText().toString());
                    convert convert = new convert();
                    convert.setC(c);
                    convert.convertCtoF();
                    double doF = convert.getF();
                    tvResult.setText("C to F: "+ String.valueOf(doF));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,"Bạn chưa nhập nhiệt độ!", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnFtoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double f = Double.parseDouble(etF.getText().toString());
                    convert convert = new convert();
                    convert.setF(f);
                    convert.convertFtoC();
                    tvResult.setText("F to C: "+String.valueOf(convert.getC()));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Bạn chưa nhập nhiệt độ", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etC.setText("");
                etF.setText("");
                tvResult.setText("");
            }
        });

    }
}