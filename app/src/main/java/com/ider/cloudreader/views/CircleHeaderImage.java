package com.ider.cloudreader.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import com.ider.cloudreader.R;
import com.ider.cloudreader.common.BitmapTools;
import java.util.Timer;
import java.util.TimerTask;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ider-eric on 2017/2/7.
 */

public class CircleHeaderImage extends CircleImageView {

    private static final String TAG = "CircleHeader";

    private Handler mHandler = new Handler();
    
    private boolean verified;

    // SDK暂未支持
    private int VIP_TYPE;

    private int[] vipGoldDrawable = {
            R.drawable.avatar_vip_golden,
            R.drawable.avatar_vip_golden_anim_0,
            R.drawable.avatar_vip_golden_anim_1,
            R.drawable.avatar_vip_golden_anim_2,
            R.drawable.avatar_vip_golden_anim_3,
            R.drawable.avatar_vip_golden_anim_4,
            R.drawable.avatar_vip_golden_anim_5,
            R.drawable.avatar_vip_golden_anim_6,
            R.drawable.avatar_vip_golden_anim_7,
            R.drawable.avatar_vip_golden_anim_8,
            R.drawable.avatar_vip_golden_anim_9,
            R.drawable.avatar_vip_golden_anim_10,
            R.drawable.avatar_vip_golden_anim_11,
            R.drawable.avatar_vip_golden_anim_12,
            R.drawable.avatar_vip_golden_anim_13,
    };
    private int goldIndex;
    private Timer timer;
    private TimerTask task;

    public CircleHeaderImage(Context context) {
        this(context, null);
    }

    public CircleHeaderImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(verified) {
            drawVerifiedIcon(canvas, vipGoldDrawable[goldIndex]);
        }
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void drawVerifiedIcon(Canvas canvas, int drawable) {
        Bitmap verifiedBitmap = BitmapTools.drawable2Bitmap(getContext().getDrawable(drawable));
        int left = getMeasuredWidth() - verifiedBitmap.getWidth();
        int top = getMeasuredHeight() - verifiedBitmap.getHeight();
        canvas.drawBitmap(verifiedBitmap, left, top, null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        timer = new Timer();
        task = new GoldTimerTask();
        timer.schedule(task, 0, 120);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        task.cancel();
        timer.cancel();
        timer.purge();
        timer = null;
    }

    class GoldTimerTask extends TimerTask {
        @Override
        public void run() {
            goldIndex ++;
            if(goldIndex == vipGoldDrawable.length) {
                goldIndex = 0;
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            });
        }
    };
}
