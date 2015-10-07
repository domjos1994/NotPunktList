package dominicjoas.dev.notpunktlist.classes;

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
    float touchX = 0, touchY = 0;
    double settedMark = 3.5, settedPoints = 10, bestMarkAt = 20, worstMarkAt = 0;
    TextView txtMark, txtPoints;
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

    public void setBestMarkAt(double points) {
        this.bestMarkAt = points;
    }

    public void setWorstMarkAt(double points) {
        this.worstMarkAt = points;
    }

    public void setTextViewMark(TextView txt) {
        this.txtMark = txt;
        try {
            double mark = Double.parseDouble(txtMark.getText().toString());
            if(mark<=6 && mark>=1) {
                settedMark = mark;
            }
        } catch(Exception ex) {}
    }

    public void setDictMode(boolean dict) {
        this.dictMode = dict;
    }

    public void setTextViewPoints(TextView txt) {
        this.txtPoints = txt;
        try {
            double points = Double.parseDouble(txtPoints.getText().toString());
            if(points<maxPoints&&points>0) {
                settedPoints = points;
            }
        } catch(Exception ex) {}
    }

    public double getSettedPoints() {
        return this.settedPoints;
    }

    public double getSettedMark() {
        return this.settedMark;
    }

    public double getBestMarkAt() {
        return this.bestMarkAt;
    }

    public double getWorstMarkAt() {
        return this.worstMarkAt;
    }

    public int getMaxPoints() {
        return this.maxPoints;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        if(touchX>getRealWidth(maxPoints, bestMarkAt)&&touchX<getRealWidth(maxPoints,worstMarkAt)) {
            if(touchY>=40&&touchY<=maxHeight-40) {
                txtPoints.setText(String.valueOf(new DecimalFormat("0.0").format((int)(maxPoints - (touchX - 40) / ((maxWidth - 80) / maxPoints)))));
                txtMark.setText(String.valueOf(new DecimalFormat("0.0").format(6-(touchY-40)/((maxHeight-80)/5))));
            }
        }
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
                drawLine(cvs, false);
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

    private void drawLine(Canvas cvs, boolean touch) {
        paint.setColor(Color.RED);
        cvs.drawLine(40, maxHeight - 40, getRealWidth(maxPoints, bestMarkAt), maxHeight - 40, paint);
        if(touch) {
            cvs.drawLine(getRealWidth(maxPoints, bestMarkAt), maxHeight - 40, touchX, touchY, paint);
            cvs.drawLine(touchX, touchY, getRealWidth(maxPoints, worstMarkAt), 40, paint);
        } else {
            cvs.drawLine(getRealWidth(maxPoints, bestMarkAt), maxHeight - 40, getRealWidth(maxPoints, settedPoints), getRealHeight(settedMark), paint);
            cvs.drawLine(getRealWidth(maxPoints, settedPoints), getRealHeight(settedMark), getRealWidth(maxPoints, worstMarkAt), 40, paint);
        }
        cvs.drawLine(getRealWidth(maxPoints, worstMarkAt), 40, maxWidth-40, 40, paint);
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
        paint.setAlpha(100);
        for(int i = 0; i<=this.maxPoints; i++) {
            cvs.drawLine(getRealWidth(this.maxPoints, i), 40, getRealWidth(this.maxPoints, i), maxHeight-40,paint);
        }
        for(int i = 0; i<=5; i++) {
            cvs.drawLine(40, getRealHeight(i), maxWidth-40, getRealHeight(i), paint);
        }
    }

    public void calcDiag(boolean touch) {
        if(surfaceHolder.getSurface().isValid()) {
            Canvas cvs = surfaceHolder.lockCanvas();
            this.maxWidth = cvs.getWidth();
            this.maxHeight = cvs.getHeight();
            drawDiagram(cvs);
            drawLine(cvs, touch);
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
