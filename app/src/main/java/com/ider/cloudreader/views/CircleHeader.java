package com.ider.cloudreader.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import com.ider.cloudreader.R;
import com.ider.cloudreader.common.BitmapTools;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ider-eric on 2017/2/7.
 */

public class CircleHeader extends CircleImageView {

    private boolean verified;

    public CircleHeader(Context context) {
        super(context);
    }

    public CircleHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(verified) {
            drawVerifiedIcon(canvas);
        }
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void drawVerifiedIcon(Canvas canvas) {
        Bitmap verifiedBitmap = BitmapTools.drawable2Bitmap(getContext().getDrawable(R.drawable.user_verified_icon));
        int left = getMeasuredWidth() - verifiedBitmap.getWidth();
        int top = getMeasuredHeight() - verifiedBitmap.getHeight();
        canvas.drawBitmap(verifiedBitmap, left, top, null);
    }
}
