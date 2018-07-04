package love.com.voiceanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MorVoiceView2 morVoiceView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        morVoiceView2 = findViewById(R.id.morVoiceView2);

        findViewById(R.id.zeo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morVoiceView2.startRecording();
            }
        });


        findViewById(R.id.one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morVoiceView2.startAsr();
            }
        });


        findViewById(R.id.two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morVoiceView2.stopAsr();
            }
        });


    }
}
