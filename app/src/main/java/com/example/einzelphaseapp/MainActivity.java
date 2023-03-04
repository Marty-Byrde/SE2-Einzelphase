package com.example.einzelphaseapp;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Socket socket = null;
    private PrintWriter _out = null;
    private BufferedReader _in = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button btnSend = findViewById(R.id.btnSend);
        EditText txtMatri = findViewById(R.id.txtMatrikelNumber);
        TextView txtResult = findViewById(R.id.txtResult);

        createSocket();

        btnSend.setOnClickListener(view -> {
            String response = transmit(txtMatri.getText().toString());
            txtResult.setText(response);

            Log.d("SE2", "Server responded: "+response);
        });

    }

    boolean hasSocket(){
        if(this.socket != null) return true;

        return false;
    }

    void createSocket(){
        if(!hasSocket()){
            try{
                this.socket = new Socket("se2-isys.aau.at", 53212);

                this._out = new PrintWriter(socket.getOutputStream(), true);
                this._in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            }catch (Exception e){
                Log.d("SE2", e.toString());
                e.printStackTrace();
            }
        }
    }

    String transmit(String message){
        try {

            _out.println(message);

            return this._in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}