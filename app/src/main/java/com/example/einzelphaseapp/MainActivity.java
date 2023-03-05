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
import java.util.Arrays;

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

        TextView txtTask = findViewById(R.id.txtTask);


        createSocket();

        btnSend.setOnClickListener(view -> {
            Log.d("SE2", "Button has b een clicked!");
            String response = transmit(txtMatri.getText().toString());
            txtResult.setText(response);

            Log.d("SE2", "Server responded: "+response);

            txtTask.setText(Even_Odd(txtMatri.getText().toString()));
            txtTask.setVisibility(View.VISIBLE);
        });


    }

    void createSocket(){
        try{
            this.socket = new Socket("se2-isys.aau.at", 53212);

            this._out = new PrintWriter(socket.getOutputStream(), true);
            this._in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        }catch (Exception e){
            Log.d("SE2", e.toString());
            e.printStackTrace();
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


    int[] generateSelection(String matrikel, int type){
        int[] _selection = new int[matrikel.length()];

        int index = 0;
        for(char _number : matrikel.toCharArray()){
            int number = Integer.parseInt((_number+""));

            if(number >= 10) {
                Log.d("SE2", "The given matrikelnumber is invalid!");
                continue;
            }

            if(number % 2 == type) _selection[index++] = number;
        }

        int[] trimmedSelected = new int[index];
        for(int i = 0; i < trimmedSelected.length; i++){
            trimmedSelected[i] = _selection[i];
        }
        Log.d("SE2", "Sorting elements for: "+type+" result => ");
        Log.d("SE2", Arrays.toString(trimmedSelected));

        return trimmedSelected;
    }





    String Even_Odd(String matrikelNumber){
        int[] even = generateSelection(matrikelNumber, 0);
        int[] odd = generateSelection(matrikelNumber, 1);

        Arrays.sort(even);
        Arrays.sort(odd);

        int[] total = new int[even.length + odd.length];
        for(int i = 0; i < even.length; i++) total[i] = even[i];
        for(int i = 0; i < odd.length; i++) total[even.length + i] = odd[i];

        String output = "";
        for(int number : total) output += number+"";
        return output;
    }

}