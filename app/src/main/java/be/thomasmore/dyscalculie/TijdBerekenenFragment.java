package be.thomasmore.dyscalculie;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;


@RequiresApi(api = Build.VERSION_CODES.M)
public class TijdBerekenenFragment extends Fragment {

    private TextToSpeech textToSpeech;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tijd_berekenen, container, false);
        final Button button = view.findViewById(R.id.buttonCalculate);

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status){
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v){
                calculate(v);
            }
        });

        TimePicker beginTimePicker = view.findViewById(R.id.beginTimePicker);
        TimePicker endTimePicker = view.findViewById(R.id.endTimePicker);
        beginTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);


        return view;
    }

    public void calculate(View v) {
        TimePicker beginTimePicker = getView().findViewById(R.id.beginTimePicker);
        TimePicker endTimePicker = getView().findViewById(R.id.endTimePicker);
        int hour = 0;
        int minutes = 0;
        int beginHour = beginTimePicker.getHour();
        int beginMinutes = beginTimePicker.getMinute();

        int endHour = endTimePicker.getHour();
        int endMinutes = endTimePicker.getMinute();

        TextView tijdverschil = getView().findViewById(R.id.tijdverschil);

            if (beginHour < endHour){
                hour = endHour - beginHour;
            }
            if (beginHour == endHour && beginMinutes > endMinutes){
                hour = 24;
            }
            if (beginMinutes <= endMinutes){
                if (hour == 24){
                    hour--;
                }
                minutes = endMinutes - beginMinutes;
            }
            if (beginHour > endHour) {
                hour = 24 - beginHour + endHour;
            }
            if (beginMinutes > endMinutes) {
                hour--;
                minutes = 60 - beginMinutes + endMinutes;
            }
            if (beginHour == endHour && endMinutes == beginMinutes){
                hour = 0;
                minutes = 0;
            }
        tijdverschil.setText("Verstreken tijd: " + hour + " uur " + minutes + " minuten");
        textToSpeech.speak("Passed time: " + hour + " hour " + minutes + " minutes", TextToSpeech.QUEUE_FLUSH, null);
    }

}
