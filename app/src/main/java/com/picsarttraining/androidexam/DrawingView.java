package com.picsarttraining.androidexam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Arsen on 23.03.2016.
 */
public class DrawingView extends View{

    private static final float TOUCH_MOVE_THRESHOLD = 10f;
    private static final long CLICK_DURATION_LIMIT_MS = 200;
    private PointF actionDownPoint;
    private Paint paint;
    private ArrayList<MyCustomImage> myCustomImages;


    private Bitmap drawingImageBitmap;

    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        actionDownPoint = new PointF();
        myCustomImages = new ArrayList<>();
        paint = new Paint();
        this.setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(myCustomImages.size()!=0)
        {
            for (MyCustomImage customImage:myCustomImages)
                canvas.drawBitmap(customImage.bmp,customImage.x,customImage.y,paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        final int action = event.getActionMasked();

        boolean handled = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                actionDownPoint.set(event.getX(), event.getY());
                handled = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                handled = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float dist = (float) Math.hypot(x - actionDownPoint.x, y - actionDownPoint.y);
                if (dist > TOUCH_MOVE_THRESHOLD) {
                    handled = false;
                } else {
                    handled = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getEventTime() - event.getDownTime() <= CLICK_DURATION_LIMIT_MS) {
                    x = event.getX();
                    y = event.getY();

                    addImageTo(x, y);
                    handled = true;
                } else {
                    handled = false;
                }
                break;

            default:
                break;
        }
        return handled;
    }

    private void addImageTo(float x, float y) {
        if (drawingImageBitmap!=null)
        {
            myCustomImages.add(new MyCustomImage(x,y,drawingImageBitmap));
            invalidate();
        }
    }

    public void setDrawingImageBitmap(Bitmap drawingImageBitmap) {
        this.drawingImageBitmap = drawingImageBitmap;
    }

    public Bitmap getBitmap(){
        return this.getDrawingCache();
    }

    private class MyCustomImage
    {
        private float x;
        private float y;
        private Bitmap bmp;
        public MyCustomImage(float x, float y, Bitmap bmp)
        {
            this.x = x;
            this.y = y;
            this.bmp = bmp;
        }
    }

}
