package love.com.voiceanimation;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by love on 2018/7/5.
 */

public class MorAudioManager {

    private AudioManager mAudioManager;
    private static MorAudioManager morAudioManager;

    public MorAudioManager(Context mContext) {
        //创建audioManger
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public static MorAudioManager getInstance(Context mContext) {
        if (morAudioManager == null) {
            synchronized (MorAudioManager.class) {
                if (morAudioManager == null) {
                    morAudioManager = new MorAudioManager(mContext);
                }
            }
        }
        return morAudioManager;
    }

    public boolean requestFocus() {
        if (mFocusChangeListener != null) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                    mAudioManager.requestAudioFocus(mFocusChangeListener,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN);
        }
        return false;
    }


    public boolean abandonFocus() {
        if (mFocusChangeListener != null) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                    mAudioManager.abandonAudioFocus(mFocusChangeListener);
        }
        return false;
    }


    AudioManager.OnAudioFocusChangeListener mFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        /**---------------音频焦点处理相关的方法---------------**/
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN://你已经得到了音频焦点。
                    System.out.println("-------------AUDIOFOCUS_GAIN---------------");
                    // resume playback
//                        mPlayer.start();
//                        mPlayer.setVolume(1.0f, 1.0f);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS://你已经失去了音频焦点很长时间了。你必须停止所有的音频播放
                    System.out.println("-------------AUDIOFOCUS_LOSS---------------");
                    // Lost focus for an unbounded amount of time: stop playback and release media player
//                        if (mPlayer.isPlaying())
//                            mPlayer.stop();
//                        mPlayer.release();
//                        mPlayer = null;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://你暂时失去了音频焦点
                    System.out.println("-------------AUDIOFOCUS_LOSS_TRANSIENT---------------");
                    // Lost focus for a short time, but we have to stop
                    // playback. We don't release the media player because playback
                    // is likely to resume
//                        if (mPlayer.isPlaying())
//                            mPlayer.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://你暂时失去了音频焦点，但你可以小声地继续播放音频（低音量）而不是完全扼杀音频。
                    System.out.println("-------------AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK---------------");
                    // Lost focus for a short time, but it's ok to keep playing
//                        // at an attenuated level
//                        if (mPlayer.isPlaying())
//                            mPlayer.setVolume(0.1f, 0.1f);
                    break;
            }

        }

    };

}
