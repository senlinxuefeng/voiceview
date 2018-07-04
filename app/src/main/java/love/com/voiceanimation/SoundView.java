package love.com.voiceanimation;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by xinhuashi on 2018/5/21.
 */

public class SoundView extends View {

    //第一个圆的画笔
    private Paint firstPaint;
    //第二个画笔
    private Paint secondPaint;
    //第三个画笔
    private Paint thirdPaint;
    //第四个画笔
    private Paint fourPaint;
    //第五个画笔
    private Paint fivePaint;
    //第一个圆点
    private Paint firstDotPaint;
    //第二个圆点
    private Paint secondDotPaint;
    //第三个圆点
    private Paint thirdDotPaint;
    //圆的半径大小
    private int mWidth;
    //第一个圆的颜色
    private int firstcolor;
    //第二个圆的颜色
    private int secondColor;
    //第三个圆的颜色
    private int thirdColor;
    //第四个圆的颜色
    private int fourColor;
    //第五个圆的颜色
    private int fiveColor;
    //圆的变化幅度
    private int changeWidth;
    private final int DRAW_FIRST_CIRCLE = 0;
    private final int DRAW_SECOND_CIRCLE = 1;
    private final int DRAW_THIRD_CIRCLE = 2;
    private final int DRAW_FOUR_CIRCLE = 3;
    private final int DRAW_FIVE_CIRCLE = 4;
    private int draw_count;
    private int i;

    public SoundView(Context context) {
        this(context, null);
    }

    public SoundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SoundView);
        firstcolor = typedArray.getColor(R.styleable.SoundView_firstColors, Color.BLUE);
        secondColor = typedArray.getColor(R.styleable.SoundView_secondColors, Color.BLUE);
        thirdColor = typedArray.getColor(R.styleable.SoundView_thirdColors, Color.BLUE);
        fourColor = typedArray.getColor(R.styleable.SoundView_fourColors, Color.BLUE);
        fiveColor = typedArray.getColor(R.styleable.SoundView_fiveColors, Color.WHITE);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        firstPaint = new Paint();
        firstPaint.setAntiAlias(true);
        firstPaint.setColor(firstcolor);
        secondPaint = new Paint();
        secondPaint.setAntiAlias(true);
        secondPaint.setColor(secondColor);
        thirdPaint = new Paint();
        thirdPaint.setAntiAlias(true);
        thirdPaint.setColor(thirdColor);
        fourPaint = new Paint();
        fourPaint.setAntiAlias(true);
        fourPaint.setColor(fourColor);
        fivePaint = new Paint();
        fivePaint.setAntiAlias(true);
        fivePaint.setColor(fiveColor);
        firstDotPaint = fivePaint;
        secondDotPaint = fivePaint;
        thirdDotPaint = fivePaint;
        //setDeal();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1:
                    startAnimator4();
                    Message msg1 = Message.obtain();
                    msg1.arg1 = 2;
                    handler.sendMessageDelayed(msg1, 80);
                    break;
                case 2:
                    startAnimator3();
                    Message msg2 = Message.obtain();
                    msg2.arg1 = 3;
                    handler.sendMessageDelayed(msg2, 60);
                    break;
                case 3:
                    startAnimator2();
                    Message msg3 = Message.obtain();
                    msg3.arg1 = 4;
                    handler.sendMessageDelayed(msg3, 50);
                    break;
                case 4:
                    startAnimator1();
                    break;
                case 5:
                    switch (i) {
                        case 0:
                            firstDotPaint = fourPaint;
                            secondDotPaint = fivePaint;
                            thirdDotPaint = fivePaint;
                            i = 1;
                            break;
                        case 1:
                            firstDotPaint = fivePaint;
                            secondDotPaint = fourPaint;
                            thirdDotPaint = fivePaint;
                            i = 2;
                            break;
                        case 2:
                            firstDotPaint = fivePaint;
                            secondDotPaint = fivePaint;
                            thirdDotPaint = fourPaint;
                            i = 0;
                            break;

                    }
                    invalidate();
                    Message msg5 = Message.obtain();
                    msg5.arg1 = 5;
                    handler.sendMessageDelayed(msg5, 500);
                    break;
                case 6:
                    startAnimator6();
                    Message msg6 = Message.obtain();
                    msg6.arg1 = 7;
                    handler.sendMessageDelayed(msg6, 80);
                    break;
                case 7:
                    startAnimator7();
                    Message msg7 = Message.obtain();
                    msg7.arg1 = 8;
                    handler.sendMessageDelayed(msg7, 60);
                    break;
                case 8:
                    startAnimator8();
                    Message msg8 = Message.obtain();
                    msg8.arg1 = 9;
                    handler.sendMessageDelayed(msg8, 50);
                    break;
                case 9:
                    startAnimator9();
                    break;
            }
        }
    };

    public void setSound() {
        firstAnimation = false;
        sendAnimation = true;
        thirdAnimation = false;
        //startAnimator5();
        Message msg = Message.obtain();
        msg.arg1 = 1;
        handler.sendMessageDelayed(msg, 100);

    }

    public void setDeal() {
        firstAnimation = true;
        sendAnimation = false;
        thirdAnimation = false;
        Message msg = Message.obtain();
        msg.arg1 = 5;
        handler.sendMessageDelayed(msg, 100);
    }

    private ValueAnimator animator1;
    private int value1;

    private void startAnimator1() {
        animator1 = ValueAnimator.ofInt(0, 30);
        animator1.setDuration(1000);
        animator1.setInterpolator(new AccelerateDecelerateInterpolator());
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                value1 = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator1.setRepeatCount(ValueAnimator.INFINITE);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator1.start();
    }

    private ValueAnimator animator2;
    private int value2;

    private void startAnimator2() {
        animator2 = ValueAnimator.ofInt(0, 26);
        animator2.setDuration(1000);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                value2 = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator2.setRepeatCount(ValueAnimator.INFINITE);
        animator2.setRepeatMode(ValueAnimator.REVERSE);
        animator2.start();
    }

    private ValueAnimator animator3;
    private int value3;

    private void startAnimator3() {
        animator3 = ValueAnimator.ofInt(0, 20);
        animator3.setDuration(1000);
        animator3.setInterpolator(new AccelerateDecelerateInterpolator());
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                value3 = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator3.setRepeatCount(ValueAnimator.INFINITE);
        animator3.setRepeatMode(ValueAnimator.REVERSE);
        animator3.start();
    }

    public void startThird() {
        firstAnimation = false;
        sendAnimation = false;
        thirdAnimation = true;
        Message msg = Message.obtain();
        msg.arg1 = 6;
        handler.sendMessageDelayed(msg, 100);
    }

    //停止循环
    public void stopFirst() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void stopThird() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (animator6 != null) {
            animator6.cancel();
            animator6 = null;
        }
        if (animator7 != null) {
            animator7.cancel();
            animator7 = null;
        }
        if (animator8 != null) {
            animator8.cancel();
            animator8 = null;
        }
        if (animator9 != null) {
            animator9.cancel();
            animator9 = null;
        }
    }

    public void stopSecond() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (animator1 != null) {
            animator1.cancel();
            animator1 = null;
        }
        if (animator2 != null) {
            animator2.cancel();
            animator2 = null;
        }
        if (animator3 != null) {
            animator3.cancel();
            animator3 = null;
        }
        if (animator4 != null) {
            animator4.cancel();
            animator4 = null;
        }
        if (animator5 != null) {
            animator5.cancel();
            animator5 = null;
        }
    }

    private ValueAnimator animator4;
    private int value4;

    private void startAnimator4() {
        animator4 = ValueAnimator.ofInt(0, 15);
        animator4.setDuration(1000);
        animator4.setInterpolator(new AccelerateDecelerateInterpolator());
        animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                value4 = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator4.setRepeatCount(ValueAnimator.INFINITE);
        animator4.setRepeatMode(ValueAnimator.REVERSE);
        animator4.start();
    }

    private ValueAnimator animator6;
    private int value6;

    private void startAnimator6() {
        animator6 = ValueAnimator.ofInt(0, 5);
        animator6.setDuration(1000);
        animator6.setInterpolator(new AccelerateDecelerateInterpolator());
        animator6.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                value6 = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator6.setRepeatCount(ValueAnimator.INFINITE);
        animator6.setRepeatMode(ValueAnimator.REVERSE);
        animator6.start();
    }

    private ValueAnimator animator7;
    private int value7;

    private void startAnimator7() {
        animator7 = ValueAnimator.ofInt(0, 5);
        animator7.setDuration(1000);
        animator7.setInterpolator(new AccelerateDecelerateInterpolator());
        animator7.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                value7 = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator7.setRepeatCount(ValueAnimator.INFINITE);
        animator7.setRepeatMode(ValueAnimator.REVERSE);
        animator7.start();
    }

    private ValueAnimator animator8;
    private int value8;

    private void startAnimator8() {
        animator8 = ValueAnimator.ofInt(0, 5);
        animator8.setDuration(1000);
        animator8.setInterpolator(new AccelerateDecelerateInterpolator());
        animator8.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                value8 = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator8.setRepeatCount(ValueAnimator.INFINITE);
        animator8.setRepeatMode(ValueAnimator.REVERSE);
        animator8.start();
    }

    private ValueAnimator animator9;
    private int value9;

    private void startAnimator9() {
        animator9 = ValueAnimator.ofInt(0, 5);
        animator9.setDuration(1000);
        animator9.setInterpolator(new AccelerateDecelerateInterpolator());
        animator9.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                value9 = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator9.setRepeatCount(ValueAnimator.INFINITE);
        animator9.setRepeatMode(ValueAnimator.REVERSE);
        animator9.start();
    }

    private ValueAnimator animator5;
    private int value5;

    private void startAnimator5() {
        animator5 = ValueAnimator.ofInt(0, 30);
        animator5.setDuration(1000);
        animator5.setInterpolator(new AccelerateDecelerateInterpolator());
        animator5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                value5 = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator5.setRepeatCount(ValueAnimator.INFINITE);
        animator5.setRepeatMode(ValueAnimator.REVERSE);
        animator5.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        //firstPaint.setShader(new RadialGradient(mWidth/2,mWidth/2,mWidth/2,Color.GRAY,Color.WHITE, Shader.TileMode.CLAMP));
    }

    boolean sendAnimation;
    boolean firstAnimation;
    boolean thirdAnimation;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (firstAnimation) {
            canvas.drawCircle(mWidth / 2, mWidth / 2 - mWidth / 6, 28 * mWidth / 100, fivePaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2 - mWidth / 6, 26 * mWidth / 100, fourPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2 - mWidth / 6, 24 * mWidth / 100, thirdPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2 - mWidth / 6, 22 * mWidth / 100, secondPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2 - mWidth / 6, 20 * mWidth / 100, firstPaint);
            canvas.drawCircle(mWidth / 2 - mWidth / 8, mWidth / 2 + mWidth / 5, 5 * mWidth / 100, firstDotPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2 + mWidth / 5, 5 * mWidth / 100, secondDotPaint);
            canvas.drawCircle(mWidth / 2 + mWidth / 8, mWidth / 2 + mWidth / 5, 5 * mWidth / 100, thirdDotPaint);
        } else if (sendAnimation) {
            canvas.drawCircle(mWidth / 2, mWidth / 2, 40 * mWidth / 100 - value1, fivePaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2, 35 * mWidth / 100 - value2, fourPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2, 30 * mWidth / 100 - value3, thirdPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2, 25 * mWidth / 100 - value4, secondPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2, 20 * mWidth / 100, firstPaint);
        } else if (thirdAnimation) {
            canvas.drawCircle(mWidth / 2, mWidth / 2, 40 * mWidth / 100 - value6, fivePaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2, 35 * mWidth / 100 - value7, fourPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2, 30 * mWidth / 100 - value8, thirdPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2, 25 * mWidth / 100 - value9, secondPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2, 20 * mWidth / 100, firstPaint);
        } else {
            canvas.drawCircle(mWidth / 2, mWidth / 2 - mWidth / 6, 28 * mWidth / 100, fivePaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2 - mWidth / 6, 26 * mWidth / 100, fourPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2 - mWidth / 6, 24 * mWidth / 100, thirdPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2 - mWidth / 6, 22 * mWidth / 100, secondPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2 - mWidth / 6, 20 * mWidth / 100, firstPaint);
            canvas.drawCircle(mWidth / 2 - mWidth / 8, mWidth / 2 + mWidth / 5, 5 * mWidth / 100, firstDotPaint);
            canvas.drawCircle(mWidth / 2, mWidth / 2 + mWidth / 5, 5 * mWidth / 100, secondDotPaint);
            canvas.drawCircle(mWidth / 2 + mWidth / 8, mWidth / 2 + mWidth / 5, 5 * mWidth / 100, thirdDotPaint);
        }
    }
}
