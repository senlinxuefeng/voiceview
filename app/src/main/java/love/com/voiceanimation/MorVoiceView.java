package love.com.voiceanimation;

import android.animation.Animator;
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

public class MorVoiceView extends RelativeLayout {
    private Context mContext;
    private ImageView firstView;
    private ImageView secondView;
    private ImageView threeView;
    private ImageView fourView;
    private List<AnimatorSet> breatheAnimaList = new ArrayList();
    private List<AnimatorSet> transAnimaList = new ArrayList();
    private float scale = 1.0f;

    private boolean isAsr = false;
    private boolean isRecording = false;
    private Handler mHandler;
    private long delayMillis = 200;
    private List<View> viewList;

    public MorVoiceView(Context context) {
        this(context, null);
    }

    public MorVoiceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MorVoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initHandlerThread();
        initView();
        initHandler();
    }

    private void initHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("MorVoiceView");
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
                    case 4:
                        if (!isAsr) {
                            setSmallRes(R.drawable.shape_voice_three, R.drawable.shape_voice_second, R.drawable.shape_voice_one);
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    toBig(5);
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
            if (!isTransAnimationing()) {
                reRecording();
            } else {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reRecording();
                    }
                }, delayMillis);
            }
        } else {
            toBig(0);
            stopRecordingAnimation();
            if (isEmptyAnimation()) {
                startScaleBreathAnimation(firstView, 1.15f);
                startScaleBreathAnimation(secondView, 1.1f);
                startScaleBreathAnimation(threeView, 1.05f);
            }
            if (!isBreatheAnimationing()) {
                startRecordingAnimation();
            }
            scale = 1.0f;
            isRecording = true;
        }
    }

    /**
     * 重新开始录音
     */
    private void reRecording() {
        stopAsr(0);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isEmptyAnimation()) {
                    startScaleBreathAnimation(firstView, 1.15f);
                    startScaleBreathAnimation(secondView, 1.1f);
                    startScaleBreathAnimation(threeView, 1.05f);
                }
                if (!isBreatheAnimationing()) {
                    startRecordingAnimation();
                }
                scale = 1.0f;
                isRecording = true;
            }
        }, 30);
    }

    private boolean isTransAnimationing() {
        if (transAnimaList == null || transAnimaList.size() == 0) return false;
        for (int i = 0; i < transAnimaList.size(); i++) {
            if (transAnimaList.get(i).isStarted()) {
                return true;
            }
        }
        return false;
    }

    private boolean isBreatheAnimationing() {
        if (breatheAnimaList == null || breatheAnimaList.size() == 0) return false;
        for (int i = 0; i < breatheAnimaList.size(); i++) {
            if (breatheAnimaList.get(i).isStarted()) {
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
            isRecording = false;
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
            isRecording = false;
            isAsr = false;
            sendMessage(3, delayMillis);
        }
    }

    /**
     * 停止ASR识别
     */
    public synchronized void stopAsr(long delayMillis) {
        if (isAsr) {
            isRecording = false;
            isAsr = false;
            sendMessage(4, delayMillis);
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
    public void toBig(long duration) {
        stopRecordingAnimation();
        transViewToBig(duration);
    }


    /**
     * 还原
     */
    public void toBig() {
        stopRecordingAnimation();
        transViewToBig();
    }

    private boolean isEmptyAnimation() {
        return !(breatheAnimaList != null && breatheAnimaList.size() != 0);
    }

    private synchronized void stopRecordingAnimation() {
        if (breatheAnimaList != null && breatheAnimaList.size() > 0) {
            for (int i = 0; i < breatheAnimaList.size(); i++) {
                breatheAnimaList.get(i).cancel();
            }
        }
    }

    private synchronized void startRecordingAnimation() {
        updateVolumes(0);
        if (breatheAnimaList != null && breatheAnimaList.size() > 0) {
            for (int i = 0; i < breatheAnimaList.size(); i++) {
                breatheAnimaList.get(i).start();
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
        transAnimaList.clear();
        startTranslationAnimation(firstView, 300, 35, 80, 1f, 0.20f);
        startTranslationAnimation(secondView, 300, 0, 80, 1f, 0.20f);
        startTranslationAnimation(threeView, 300, -35, 80, 1f, 0.20f);
        scale = 0.25f;
    }

    public synchronized void transViewToBig(long duration) {
        transAnimaList.clear();
        startTranslationAnimation(firstView, duration, 0, 0, 0.20f, 1f);
        startTranslationAnimation(secondView, duration, 0, 0, 0.20f, 1f);
        startTranslationAnimation(threeView, duration, 0, 0, 0.20f, 1f);
        scale = 1f;
    }

    public synchronized void transViewToBig() {
        transAnimaList.clear();
        startTranslationAnimation(firstView, 300, 0, 0, 0.20f, 1f);
        startTranslationAnimation(secondView, 300, 0, 0, 0.20f, 1f);
        startTranslationAnimation(threeView, 300, 0, 0, 0.20f, 1f);
        scale = 1f;
    }

    private synchronized void startTranslationAnimation(View view, long duration, float x, float y, float... scale) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", x);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", y);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationX, translationY, scaleX, scaleY);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();

        transAnimaList.add(animatorSet);
    }


    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mor_voice_view, null);
        addView(view);
        firstView = view.findViewById(R.id.firstView);
        secondView = view.findViewById(R.id.secondView);
        threeView = view.findViewById(R.id.threeView);
        fourView = view.findViewById(R.id.fourView);
        viewList = new ArrayList<>();
        viewList.add(firstView);
        viewList.add(secondView);
        viewList.add(threeView);
    }

    /**
     * 开启缩放渐变呼吸动画
     */
    private void startScaleBreathAnimation(View view, float scale) {
        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scale);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        AnimatorSet breatheAnima = new AnimatorSet();
        breatheAnima.playTogether(scaleX, scaleY);
        breatheAnima.setDuration(1400);
        breatheAnima.setInterpolator(new BreatheInterpolator());

//        breatheAnima.start();
        breatheAnimaList.add(breatheAnima);
    }


    /**
     * 更新音量值
     */
    public synchronized void updateVolumes(int volume) {
        if (!isAsr && isRecording) {
            for (int i = 0; i < breatheAnimaList.size(); i++) {
                AnimatorSet animatorSet = breatheAnimaList.get(i);
                ArrayList<Animator> animators = animatorSet.getChildAnimations();
                for (int j = 0; j < animators.size(); j++) {
                    ObjectAnimator animator = (ObjectAnimator) animators.get(j);
                    animator.setFloatValues(1f, getScale(volume, i));
                }
                viewList.get(i).invalidate();
            }
        }
    }

    /**
     * @param volume       0-100
     * @param viewPosition
     * @return
     */
    private float getScale(int volume, int viewPosition) {
        if (volume <= 5) {
            return viewPosition == 0 ? 1.150f : viewPosition == 1 ? 1.100f : viewPosition == 2 ? 1.050f : 1.15f;
        } else if (volume > 5 && volume <= 10) {
            return viewPosition == 0 ? getFirstScale(1) : viewPosition == 1 ? getSecondScale(1) : viewPosition == 2 ? getThirdScale(1) : 1.15f;
        } else if (volume > 10 && volume <= 20) {
            return viewPosition == 0 ? getFirstScale(2) : viewPosition == 1 ? getSecondScale(2) : viewPosition == 2 ? getThirdScale(2) : 1.15f;
        } else if (volume > 20 && volume <= 30) {
            return viewPosition == 0 ? getFirstScale(3) : viewPosition == 1 ? getSecondScale(3) : viewPosition == 2 ? getThirdScale(3) : 1.15f;
        } else if (volume > 30 && volume <= 40) {
            return viewPosition == 0 ? getFirstScale(4) : viewPosition == 1 ? getSecondScale(4) : viewPosition == 2 ? getThirdScale(4) : 1.15f;
        } else if (volume > 50 && volume <= 60) {
            return viewPosition == 0 ? getFirstScale(5) : viewPosition == 1 ? getSecondScale(5) : viewPosition == 2 ? getThirdScale(5) : 1.15f;
        } else if (volume > 60 && volume <= 70) {
            return viewPosition == 0 ? getFirstScale(6) : viewPosition == 1 ? getSecondScale(6) : viewPosition == 2 ? getThirdScale(6) : 1.15f;
        } else if (volume > 70 && volume <= 80) {
            return viewPosition == 0 ? getFirstScale(7) : viewPosition == 1 ? getSecondScale(7) : viewPosition == 2 ? getThirdScale(7) : 1.15f;
        } else if (volume > 80 && volume <= 90) {
            return viewPosition == 0 ? getFirstScale(8) : viewPosition == 1 ? getSecondScale(8) : viewPosition == 2 ? getThirdScale(8) : 1.15f;
        } else if (volume > 90 && volume <= 100) {
            return viewPosition == 0 ? getFirstScale(9) : viewPosition == 1 ? getSecondScale(9) : viewPosition == 2 ? getThirdScale(9) : 1.15f;
        } else {
            return viewPosition == 0 ? 1.150f : viewPosition == 1 ? 1.100f : viewPosition == 2 ? 1.050f : 1.15f;
        }
    }

    private float getFirstScale(int multiple) {
        return 1.150f + multiple * 0.022f;
    }

    private float getSecondScale(int multiple) {
        return 1.100f + multiple * 0.012f;
    }

    private float getThirdScale(int multiple) {
        return 1.050f + multiple * 0.008f;
    }

    /**
     * 定义拟合呼吸变化的插值器
     */
    public class BreatheInterpolator implements TimeInterpolator {
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
