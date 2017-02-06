package com.ider.cloudreader.navigation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ider.cloudreader.R;
import com.ider.cloudreader.common.BitmapTools;

/**
 * Created by ider-eric on 2017/1/13.
 */

public class SwitchButton extends View {

    private static final String TAG = "SwitchButton";

    private boolean switchOn;

    private int trackWidth;
    private int trackHeight;
    private int ballRadius;

    private static final int DEFAULT_TRACK_WIDTH = 75;
    private static final int DEFAULT_TRACK_HEIGHT = 40;
    private static final int DEFAULT_BALL_RADIUS = 40;

    private ValueAnimator animator;

    private int ballLeft = 0;
    // 滑块距X=0处最大的位移距离，也就是移至最右边时的X位置
    private int ballLeftMax;
    // 记录touch_down时X的位置
    private float touchDownX;
    // 记录move过程中的X位置
    private float touchCurrentX;
    // 记录滑块移动方向
    private int ballDirection = 0;
    private static final int DIRECTION_LEFT = -1;
    private static final int DIRECTION_RIGHT = 1;
    private static final int DIRECTION_NULL = 0;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
            trackWidth = typedArray.getDimensionPixelSize(R.styleable.SwitchButton_trackWidth, DEFAULT_TRACK_WIDTH);
            trackHeight = typedArray.getDimensionPixelSize(R.styleable.SwitchButton_trackHeight, DEFAULT_TRACK_HEIGHT);
            ballRadius = typedArray.getDimensionPixelSize(R.styleable.SwitchButton_ballRadius, DEFAULT_BALL_RADIUS);
            switchOn = typedArray.getBoolean(R.styleable.SwitchButton_enabled, false);

            typedArray.recycle();
        } else {
            trackWidth = DEFAULT_TRACK_WIDTH;
            trackHeight = DEFAULT_TRACK_HEIGHT;
            ballRadius = DEFAULT_BALL_RADIUS;
            switchOn = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int measureWidth = 0;
        int measureHeight = 0;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                measureWidth = width + getPaddingLeft() + getPaddingRight();
                break;
            case MeasureSpec.AT_MOST:
                measureWidth = trackWidth + 2 * (ballRadius - trackHeight / 2) + getPaddingLeft() + getPaddingRight();
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                measureHeight = height + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.AT_MOST:
                measureHeight = ballRadius * 2 + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.UNSPECIFIED:

                break;
        }

        ballLeftMax = measureWidth - 2 * ballRadius;
        ballLeft = switchOn ? ballLeftMax : 0;
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawTrack(canvas);
        drawBall(canvas);

    }

    private void drawTrack(Canvas canvas) {
        int trackRes = R.drawable.switch_track;
        Bitmap trackBitmap = BitmapTools.drawable2Bitmap(getResources().getDrawable(trackRes));
        Bitmap bitmap = Bitmap.createScaledBitmap(trackBitmap, trackWidth, trackHeight, true);
        int x = ballRadius - trackHeight / 2;
        canvas.drawBitmap(bitmap, x, x, null);
    }

    private void drawBall(Canvas canvas) {
        int ballRes = R.drawable.switch_thmub_off;
        Bitmap ballBitmap = BitmapTools.drawable2Bitmap(getResources().getDrawable(ballRes));
        Bitmap bitmap = Bitmap.createScaledBitmap(ballBitmap, ballRadius * 2, ballRadius * 2, true);
        canvas.drawBitmap(bitmap, ballLeft, 0, null);
    }


    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchCurrentX = touchDownX = event.getX();
                break;

            case MotionEvent.ACTION_UP:
                // 确定up动作触发时，当前的touchCurrentX
                float upX = event.getX();
                float movedX = Math.abs(upX - touchDownX);
                if (movedX > ballLeftMax) {
                    movedX = ballLeftMax;
                }
                if (!switchOn) {
                    touchCurrentX = movedX;
                } else {
                    touchCurrentX = ballLeftMax - movedX;
                }
                moveToEnd(ballDirection);
                break;

            case MotionEvent.ACTION_MOVE:
                // 屏蔽掉action_move动作
//                float currentX = event.getX();
//                if (currentX > touchCurrentX) {
//                    ballDirection = DIRECTION_RIGHT;
//                } else if (currentX < touchCurrentX) {
//                    ballDirection = DIRECTION_LEFT;
//                } else {
//                    ballDirection = DIRECTION_NULL;
//                }
//                touchCurrentX = currentX;
//                float dertaX = currentX - touchDownX;
//                if (dertaX > 0 && !switchOn) {
//                    ballLeft = Math.min((int) dertaX, ballLeftMax);
//                } else if (dertaX < 0 && switchOn) {
//                    ballLeft = Math.max((int) (ballLeftMax + dertaX), 0);
//                }
//                invalidate();
                break;
        }

        return true;
    }


    private void moveToEnd(final int direction) {

        float targetX = 0;
        float currentX = touchCurrentX;

        if (direction == DIRECTION_NULL) {
            if (!switchOn) {
                targetX = ballLeftMax;
            } else {
                targetX = 0;
            }

        } else if (direction == DIRECTION_LEFT) {
            targetX = 0;

        } else if (direction == DIRECTION_RIGHT) {
            targetX = ballLeftMax;
        }

        if(animator != null && animator.isRunning()) {
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(currentX, targetX);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ballLeft = (int) value;
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (direction == DIRECTION_LEFT) {
                    switchOn = false;
                } else if (direction == DIRECTION_RIGHT) {
                    switchOn = true;
                } else {
                    switchOn = !switchOn;
                }
                ballDirection = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        float arg = Math.abs(targetX - currentX) / ballLeftMax;
        animator.setDuration((long) (200 * arg));
        animator.start();
    }


    public boolean isEnabled() {
        return switchOn;
    }

    public void setEnable(boolean enable) {
        if (enable) {
            if (switchOn) return;
            ballLeft = ballLeftMax;
            invalidate();
            switchOn = true;
        } else {
            if (!switchOn) return;
            ballLeft = 0;
            invalidate();
            switchOn = false;
        }
    }
}
