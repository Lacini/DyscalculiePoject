package be.thomasmore.dyscalculie;

import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static be.thomasmore.dyscalculie.R.id.nieuwePrijs;
import static be.thomasmore.dyscalculie.R.id.oorspronkelijkePrijs;


public class PercentenFragment extends Fragment {

    private TextToSpeech textToSpeech;
    private Timer timer;
    private HistoryFragment historyFragment = new HistoryFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_percenten, container, false);
        final Button button = view.findViewById(R.id.buttonCalculate);
        final EditText oPrijs = view.findViewById(oorspronkelijkePrijs);
        final EditText percentage = view.findViewById(R.id.percentage);
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status){
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(new Locale("nl_NL"));
                }
            }
        });

        oPrijs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        textToSpeech.speak(oPrijs.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }, 600);
            }
        });

        percentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        textToSpeech.speak(percentage.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }, 600);
            }
        });
        button.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v){
                calculate(v);
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void calculate(View v) {
        TextView tOorspronkelijkePrijs = getView().findViewById(oorspronkelijkePrijs);
        TextView tPercentage = getView().findViewById(R.id.percentage);
        TextView tNieuwePrijs = getView().findViewById(nieuwePrijs);

        if (!(tOorspronkelijkePrijs.getText().toString().isEmpty() || tPercentage.getText().toString().isEmpty())) {
            if (Integer.parseInt(tPercentage.getText().toString()) <= 100){
                float oorspronkelijkePrijs = Float.parseFloat(tOorspronkelijkePrijs.getText().toString());
                float percentage = Float.parseFloat(tPercentage.getText().toString());
                float nieuwePrijs = oorspronkelijkePrijs * (1 - (percentage / 100));
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");


                tNieuwePrijs.setText(decimalFormat.format(nieuwePrijs));
                textToSpeech.speak("De nieuwe prijs is " + decimalFormat.format(nieuwePrijs) + " euro", TextToSpeech.QUEUE_FLUSH, null);


                SharedPreferences pref = getContext().getSharedPreferences("dyscalculie", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("procent", decimalFormat.format(nieuwePrijs) + " euro");
                editor.commit();
            } else {
                String nieuwePrijs = "Een percentage kan niet meer zijn dan 100";
                tNieuwePrijs.setText(nieuwePrijs);
                textToSpeech.speak(nieuwePrijs, TextToSpeech.QUEUE_FLUSH, null);
            }
        } else {
            if (tOorspronkelijkePrijs.getText().toString().isEmpty()) {
                String nieuwePrijs = "Oorspronkelijke prijs is niet ingevuld";
                tNieuwePrijs.setText(nieuwePrijs);
                textToSpeech.speak(nieuwePrijs, TextToSpeech.QUEUE_FLUSH, null);
            }
            if (tPercentage.getText().toString().isEmpty()){
                String nieuwePrijs = "Percentage is niet ingevuld";
                tNieuwePrijs.setText(nieuwePrijs);
                textToSpeech.speak(nieuwePrijs, TextToSpeech.QUEUE_FLUSH, null);
            }
            if (tOorspronkelijkePrijs.getText().toString().isEmpty() && tPercentage.getText().toString().isEmpty()) {
                String nieuwePrijs = "De velden zijn niet ingevuld";
                tNieuwePrijs.setText(nieuwePrijs);
                textToSpeech.speak(nieuwePrijs, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }
}
