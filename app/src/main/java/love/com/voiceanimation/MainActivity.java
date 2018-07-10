package love.com.voiceanimation;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MorVoiceView morVoiceView;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        morVoiceView = findViewById(R.id.morVoiceView2);

        findViewById(R.id.zeo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morVoiceView.startRecording();
            }
        });


        findViewById(R.id.one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morVoiceView.startAsr();
            }
        });


        findViewById(R.id.two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morVoiceView.stopAsr();
            }
        });

        findViewById(R.id.three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int volume = new Random().nextInt(100);
                Log.i("sdfsdf", "volume:" + volume);
                morVoiceView.updateVolumes(volume);
            }
        });

        findViewById(R.id.four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morVoiceView.stopAsr(0);
            }
        });

//
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MorAudioManager.getInstance(mContext).requestFocus();
//            }
//        });
//
//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MorAudioManager.getInstance(mContext).abandonFocus();
//            }
//        });

    }
}
