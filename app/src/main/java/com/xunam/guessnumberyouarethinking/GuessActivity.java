package com.xunam.guessnumberyouarethinking;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.IntegerRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

enum NumberTip {
    MORE, LESS
}

public class GuessActivity extends AppCompatActivity {
    private int current_number = 0;
    private Random random;
    private Button button_less, button_more, button_correct;
    private TextView textview_random_number;
    private TreeMap<Integer, NumberTip> numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guess);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        random = new Random();
        numbers = new TreeMap<Integer, NumberTip>();

        button_correct = findViewById(R.id.button3);
        button_more = findViewById(R.id.button_more);
        button_less = findViewById(R.id.button_less);
        textview_random_number = findViewById(R.id.random_number);

        generateNumber();
        updateLabel();

        button_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers.put(current_number, NumberTip.LESS);
                generateNumber();
                updateLabel();
            }
        });
        button_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers.put(current_number, NumberTip.MORE);
                generateNumber();
                updateLabel();
            }
        });
        button_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers.clear();
                generateNumber();
                updateLabel();
            }
        });
    }
    private void generateNumber() {
        if(numbers.size() >= 99) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GuessActivity.this);

            builder.setTitle("Seu número é");
            builder.setMessage(String.valueOf(current_number));
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.create().show();
        } else {
            int min = 0;
            int max = 101; // Intervalo de números permitidos [min, max)

            // Remove os números já chutados incorretamente do intervalo permitido
            for (Map.Entry<Integer, NumberTip> entry : numbers.entrySet()) {
                int num = entry.getKey();
                NumberTip tip = entry.getValue();

                if (tip == NumberTip.MORE) {
                    min = Math.max(min, num + 1);
                } else if (tip == NumberTip.LESS) {
                    max = Math.min(max, num);
                }
            }

            // Atualiza o número atual para um número aleatório dentro do intervalo ajustado
            current_number = random.nextInt(max - min) + min;
        }
    }
    protected void updateLabel() {
        textview_random_number.setText(String.valueOf(current_number));
    }
}