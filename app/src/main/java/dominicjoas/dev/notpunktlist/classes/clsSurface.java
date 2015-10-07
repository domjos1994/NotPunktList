package dominicjoas.dev.notpunktlist.classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Dominic Joas on 09.09.2015.
 */
public class clsSurface extends SurfaceView implements Runnable {

    Thread thread = null;
    SurfaceHolder surfaceHolder;
    volatile boolean running = false;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextView txtX, txtY;
    Random random;

    volatile boolean touched = false;
    volatile float touched_x = 0, touched_y = 0, old_touched_x = 0, old_touched_y = 0;
    int min, max, worst = 0, best = 0, width, height;
    boolean reset = false;

    public clsSurface(Context context, int min, int max) {
        super(context);
        // TODO Auto-generated constructor stub
        surfaceHolder = getHolder();
        random = new Random();
        this.min = min;
        this.max = max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setBest(int best) {
        this.best = best;
    }

    public void setWorst(int worst) {
        this.worst = worst;
    }

    public void setTextViewX(TextView txtX){this.txtX = txtX;};

    public void setTextViewY(TextView txtY){this.txtY = txtY;};

    public void reset() {
        reset = true;
    }

    public void onResumeMySurfaceView() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void onPauseMySurfaceView() {
        boolean retry = true;
        running = false;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while(running) {
            drawDiagram(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touched_x = event.getX();
        touched_y = event.getY();
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                touched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touched = true;
                break;
            case MotionEvent.ACTION_UP:
                touched = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                touched = false;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                touched = false;
                break;
            default:
        }
        if(touched) {
            drawDiagram(true);
            drawLineTouched();
            txtX.setText(String.valueOf(((width-40)/max)/touched_x));
            txtY.setText(String.valueOf(((height-40)/5)/touched_y));
        }
        return true;
    }

    public void drawLineTouched() {
        Canvas canvas = surfaceHolder.lockCanvas();
        width = canvas.getWidth();
        height = canvas.getHeight();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        canvas.drawLine(20 + ((canvas.getWidth() - 40) / max) * (max-best), canvas.getHeight() - 20, touched_x, touched_y, paint);
        canvas.drawLine(touched_x, touched_y, (canvas.getWidth()-20)-((canvas.getWidth()-40)/max)*worst, 20, paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void drawDiagram(boolean isAlready) {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.WHITE);
            p.setStrokeWidth(2);
            p.setTextSize(10);
            canvas.drawText("6", 10, 25, p);
            canvas.drawText("1", 10, canvas.getHeight() - 20, p);
            canvas.drawText(String.valueOf(max), 20, canvas.getHeight() - 10, p);
            canvas.drawText(String.valueOf(min), canvas.getWidth() - 25, canvas.getHeight() - 10, p);
            canvas.drawLine(20, 20, 20, canvas.getHeight() - 20, p);
            canvas.drawLine(20, canvas.getHeight() - 20, canvas.getWidth() - 20, canvas.getHeight() - 20, p);
            float stepPoints = (canvas.getWidth()-40) / max;
            float stepMarks = (canvas.getHeight()-40) / 5;
            p.setAlpha(100);
            for(int i = 1; i<=max; i++) {
                canvas.drawLine(20+(i*stepPoints), 20, 20+(i*stepPoints), canvas.getHeight()-20, p);
            }
            for(int i = 1; i<=6; i++) {
                canvas.drawLine(20, 20+(i*stepMarks), canvas.getWidth()-20, 20+(i*stepMarks), p);
            }
            p.setAlpha(255);
            if(!isAlready) {
                p.setColor(Color.RED);
                canvas.drawLine(20, canvas.getHeight() - 20, canvas.getWidth() - 20, 20, p);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
            running = false;
        }
    }
}