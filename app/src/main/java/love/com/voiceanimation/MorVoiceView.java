package love.com.voiceanimation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by love on 2018/6/26.
 */

public class MorVoiceView extends View {

    private int firstColor;
    private int secondColor;
    private int thirdColor;
    private int fourColor;
    private Paint firstPaint;
    private Paint secondPaint;
    private Paint thirdPaint;
    private Paint fourPaint;
    private int mWidth;
    private boolean isRecording = true;
    private Thread thread;

    public MorVoiceView(Context context) {
        this(context, null);
    }

    public MorVoiceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MorVoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initType(context, attrs);
        initView();
        initHandler();
    }

    private void initHandler() {

    }

    boolean isIncreasing = true;

    public synchronized void startRecording() {
        if (thread != null) {
            thread.interrupted();
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isIncreasing) {
                        for (int i = 0; i < 40; i++) {
                            multiNum = i;
                            if (i == 39) {
                                isIncreasing = false;
                            }
                        }
                    } else {
                        for (int i = 40; i > 0; i--) {
                            multiNum = i;
                            if (i == 1) {
                                isIncreasing = true;
                            }
                        }
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void initType(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MorVoiceView);
        firstColor = typedArray.getColor(R.styleable.MorVoiceView_firstColor, context.getResources().getColor(R.color.firstColor));
        secondColor = typedArray.getColor(R.styleable.MorVoiceView_secondColor, context.getResources().getColor(R.color.secondColor));
        thirdColor = typedArray.getColor(R.styleable.MorVoiceView_thirdColor, context.getResources().getColor(R.color.thirdColor));
        fourColor = typedArray.getColor(R.styleable.MorVoiceView_fourColor, context.getResources().getColor(R.color.fourColor));
    }

    private void initView() {
        firstPaint = new Paint();
        firstPaint.setColor(firstColor);
        firstPaint.setAntiAlias(true);


        secondPaint = new Paint();
        secondPaint.setColor(secondColor);
        secondPaint.setAntiAlias(true);

        thirdPaint = new Paint();
        thirdPaint.setColor(thirdColor);
        thirdPaint.setAntiAlias(true);

        fourPaint = new Paint();
        fourPaint.setColor(fourColor);
        fourPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
    }

    private float initRadius = 45;
    private float radiusSpacing = 10;
    private float multiNum = 0;//倍数

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (MorVoiceView.class) {
            if (isRecording) {
                canvas.drawCircle(mWidth / 2, mWidth / 2, getRadius(1), firstPaint);
                canvas.drawCircle(mWidth / 2, mWidth / 2, getRadius(2), secondPaint);
                canvas.drawCircle(mWidth / 2, mWidth / 2, getRadius(3), thirdPaint);
                canvas.drawCircle(mWidth / 2, mWidth / 2, getRadius(4), fourPaint);
            } else {

            }
        }
    }

    private float getRadius(float paint) {
        if (paint == 1) {
            return initRadius + radiusSpacing * (3 + multiNum / 40);
        } else if (paint == 2) {
            return initRadius + radiusSpacing * (2 + multiNum / 40);
        } else if (paint == 3) {
            return initRadius + radiusSpacing * (1 + multiNum / 40);
        } else if (paint == 4) {
            return initRadius;
        } else {
            return initRadius;
        }
    }

}
