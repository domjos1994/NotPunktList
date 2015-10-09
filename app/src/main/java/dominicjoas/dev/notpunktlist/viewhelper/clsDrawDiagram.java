package dominicjoas.dev.notpunktlist.viewhelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class clsDrawDiagram extends SurfaceView implements Runnable {
    Thread thread = null;
    SurfaceHolder surfaceHolder;
    int maxWidth = 100, maxHeight = 100, maxPoints = 20;
    Paint paint;
    boolean dictMode = false;

    public clsDrawDiagram(Context context) {
        super(context);
        this.surfaceHolder = getHolder();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setMaxPoints(int max) {
        this.maxPoints = max;
    }

    public void setDictMode(boolean dict) {
        this.dictMode = dict;
    }

    public int getMaxPoints() {
        return this.maxPoints;
    }

    public boolean getDictMode() {
        return this.dictMode;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void run() {
        while(true){
            if(surfaceHolder.getSurface().isValid()) {
                Canvas cvs = surfaceHolder.lockCanvas();
                this.maxWidth = cvs.getWidth();
                this.maxHeight = cvs.getHeight();
                drawDiagram(cvs);
                surfaceHolder.unlockCanvasAndPost(cvs);
                break;
            }
        }
    }

    public void onResumeMySurfaceView() {
        thread = new Thread(this);
        thread.start();
    }

    public void onPauseMySurfaceView() {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawDiagram(Canvas cvs) {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        cvs.drawColor(Color.BLACK);
        cvs.drawLine(40, 40, 40, maxHeight - 40, paint);
        cvs.drawLine(40, maxHeight - 40, maxWidth - 40, maxHeight - 40, paint);
        cvs.drawText("1", 20, maxHeight - 40, paint);
        cvs.drawText("6",20, 40,paint);
        cvs.drawText("Note", 20, 20, paint);
        if(dictMode) {
            cvs.drawText(String.valueOf(this.maxPoints), maxWidth-50, maxHeight - 20, paint);
            cvs.drawText("0", 40, maxHeight - 20, paint);
            cvs.drawText("Fehler", maxWidth-35, maxHeight - 35, paint);
        } else {
            cvs.drawText("0", maxWidth-50, maxHeight - 20, paint);
            cvs.drawText(String.valueOf(this.maxPoints), 40, maxHeight - 20, paint);
            cvs.drawText("Pkt", maxWidth-35, maxHeight - 35, paint);
        }
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setAlpha(100);
        for(int i = 0; i<=this.maxPoints; i++) {
            cvs.drawLine(getRealWidth(this.maxPoints, i), 40, getRealWidth(this.maxPoints, i), maxHeight-40,paint);
        }
        for(int i = 0; i<=5; i++) {
            cvs.drawLine(40, getRealHeight(i), maxWidth-40, getRealHeight(i), paint);
        }
    }

    public void calcDiagram() {
        if(surfaceHolder.getSurface().isValid()) {
            Canvas cvs = surfaceHolder.lockCanvas();
            this.maxWidth = cvs.getWidth();
            this.maxHeight = cvs.getHeight();
            drawDiagram(cvs);
            surfaceHolder.unlockCanvasAndPost(cvs);
        }
    }

    private int getRealWidth(int maxPoints, double currentPoints) {
        double diag = this.maxWidth-80;
        diag = 40 + ((diag/maxPoints) * (maxPoints-currentPoints));
        return (int)diag;
    }

    private int getRealHeight(double currentMark) {
        double diag = this.maxHeight-80;
        diag = 40 + ((diag/50)*((6-currentMark)*10));
        return (int)diag;
    }
}
