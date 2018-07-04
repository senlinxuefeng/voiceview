package love.com.voiceanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by love on 2018/6/26.
 */

public class MorVoiceView2 extends RelativeLayout {
    private Context mContext;
    private ImageView firstView;
    private ImageView secondView;
    private ImageView threeView;
    private ImageView fourView;
    private List<AnimatorSet> animaList = new ArrayList();
    private float scale = 1.0f;

    private boolean isAsr = false;
    private Handler mHandler;
    private long delayMillis = 200;

    public MorVoiceView2(Context context) {
        this(context, null);
    }

    public MorVoiceView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MorVoiceView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initHandlerThread();
        initView();
        initHandler();
    }

    private void initHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("eee");
        handlerThread.start();
        //运行在工作线程(子线程)中，用于实现自己的消息处理
        mHandler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //运行在工作线程(子线程)中，用于实现自己的消息处理
                switch (msg.what) {
                    case 0:
                        if (isAsr) {
                            setSmallRes(R.drawable.shape_voice_four, R.drawable.shape_voice_three, R.drawable.shape_voice_second);
                            sendMessage(1, delayMillis);
                        }
                        break;
                    case 1:
                        if (isAsr) {
                            setSmallRes(R.drawable.shape_voice_second, R.drawable.shape_voice_four, R.drawable.shape_voice_three);
                            sendMessage(2, delayMillis);
                        }
                        break;
                    case 2:
                        if (isAsr) {
                            setSmallRes(R.drawable.shape_voice_three, R.drawable.shape_voice_second, R.drawable.shape_voice_four);
                            sendMessage(0, delayMillis);
                        }
                        break;
                    case 3:
                        if (!isAsr) {
                            setSmallRes(R.drawable.shape_voice_three, R.drawable.shape_voice_second, R.drawable.shape_voice_one);
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    toBig();
                                }
                            });
                        }
                        break;
                }
                return false;
            }
        });


    }

    private void initHandler() {

    }

    public synchronized void startRecording() {
        if (isSmall()) {
            stopAsr(0);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    transViewToBig();
                }
            }, 50);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isEmptyAnimation()) {
                        startScaleBreathAnimation(firstView, 1.15f);
                        startScaleBreathAnimation(secondView, 1.1f);
                        startScaleBreathAnimation(threeView, 1.05f);
                    }
                    if (!isAnimationing()) {
                        startRecordingAnimation();
                    }
                    scale = 1.0f;
                }
            }, 350);
        } else {
            if (isEmptyAnimation()) {
                startScaleBreathAnimation(firstView, 1.15f);
                startScaleBreathAnimation(secondView, 1.1f);
                startScaleBreathAnimation(threeView, 1.05f);
            }
            if (!isAnimationing()) {
                startRecordingAnimation();
            }
            scale = 1.0f;
        }
    }

    private boolean isAnimationing() {
        if (animaList == null || animaList.size() == 0) return false;
        for (int i = 0; i < animaList.size(); i++) {
            if (animaList.get(i).isStarted()) {
                return true;
            }
        }
        return false;
    }

    private boolean isSmall() {
        return (scale < 1.0f);
    }

    private boolean isBig() {
        return (scale >= 1.0f);
    }

    /**
     * 开始ASR识别
     */
    public synchronized void startAsr() {
        if (!isAsr) {
            isAsr = true;
            toSmall();
        } else {
//            isAsr = false;
//            stopAsr();
        }
    }

    /**
     * 停止ASR识别
     */
    public synchronized void stopAsr() {
        if (isAsr) {
            isAsr = false;
            sendMessage(3, delayMillis);
        }
    }

    /**
     * 停止ASR识别
     */
    public synchronized void stopAsr(long delayMillis) {
        if (isAsr) {
            isAsr = false;
            sendMessage(3, delayMillis);
        }
    }

    /**
     * 变小
     */
    private void toSmall() {
        stopRecordingAnimation();
        if (isBig()) {
            transViewToSmall();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendMessage(0, 0);
                }
            }, 300);
        }
    }

    private void sendMessage(int what, long delayMillis) {
        mHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    /**
     * 还原
     */
    public void toBig() {
        stopRecordingAnimation();
        transViewToBig();
    }

    private boolean isEmptyAnimation() {
        return !(animaList != null && animaList.size() != 0);
    }

    private synchronized void stopRecordingAnimation() {
        if (animaList != null && animaList.size() > 0) {
            for (int i = 0; i < animaList.size(); i++) {
                animaList.get(i).cancel();
            }
        }
    }

    private synchronized void startRecordingAnimation() {
        if (animaList != null && animaList.size() > 0) {
            for (int i = 0; i < animaList.size(); i++) {
                animaList.get(i).start();
            }
        }
    }

    private void setSmallRes(final int threeViewDrawable, final int secondViewColor, final int firstViewColor) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                threeView.setImageResource(threeViewDrawable);
                secondView.setImageResource(secondViewColor);
                firstView.setImageResource(firstViewColor);
            }
        }, 0);
    }


    private synchronized void transViewToSmall() {
        startTranslationAnimation(firstView, 35, 80, 1f, 0.20f);
        startTranslationAnimation(secondView, 0, 80, 1f, 0.20f);
        startTranslationAnimation(threeView, -35, 80, 1f, 0.20f);
        scale = 0.25f;
    }

    public synchronized void transViewToBig() {
        startTranslationAnimation(firstView, 0, 0, 0.20f, 1f);
        startTranslationAnimation(secondView, 0, 0, 0.20f, 1f);
        startTranslationAnimation(threeView, 0, 0, 0.20f, 1f);
        scale = 1f;
    }

    private synchronized void startTranslationAnimation(View view, float x, float y, float... scale) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", x);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", y);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationX, translationY, scaleX, scaleY);
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mor_voice_view_2, null);
        addView(view);
        firstView = view.findViewById(R.id.firstView);
        secondView = view.findViewById(R.id.secondView);
        threeView = view.findViewById(R.id.threeView);
        fourView = view.findViewById(R.id.fourView);
    }

    /**
     * 开启缩放渐变呼吸动画
     */
    private void startScaleBreathAnimation(View view, float scale) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scale);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        AnimatorSet breatheAnima = new AnimatorSet();
        breatheAnima.playTogether(scaleX, scaleY);
        breatheAnima.setDuration(2500);
        breatheAnima.setInterpolator(new BraetheInterpolator());
//        breatheAnima.start();
        animaList.add(breatheAnima);
    }

    /**
     * 定义拟合呼吸变化的插值器
     */
    public class BraetheInterpolator implements TimeInterpolator {
        @Override
        public float getInterpolation(float input) {

            float x = 6 * input;
            float k = 1.0f / 3;
            int t = 6;
            int n = 1;//控制函数周期，这里取此函数的第一个周期
            float PI = 3.1416f;
            float output = 0;

            if (x >= ((n - 1) * t) && x < ((n - (1 - k)) * t)) {
                output = (float) (0.5 * Math.sin((PI / (k * t)) * ((x - k * t / 2) - (n - 1) * t)) + 0.5);

            } else if (x >= (n - (1 - k)) * t && x < n * t) {
                output = (float) Math.pow((0.5 * Math.sin((PI / ((1 - k) * t)) * ((x - (3 - k) * t / 2) - (n - 1) * t)) + 0.5), 2);
            }
            return output;
        }
    }
}
