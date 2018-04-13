package code.examples.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import code.examples.R;


public class WidgetCalorieMan extends View {

    // paint and rect objects
    private int height, width;
    private Bitmap bitmapObject;
    private Paint paintBitmap, paintText1, paintText2, paintText1Measure, paintText2Measure;
    private Rect rectText1, rectText2, rectText1Measure, rectText2Measure, rectBitmap;
    private float textHeight, textWidth;
    private float measureLetterSize;

    // parameters
    private int iconDrawableId;
    private int textMode=0;         // 0- no text, 1- show text1, 2- show tex1 and text2
    private int colorText1, colorText2, colorText1Measure, colorText2Measure;
    private String text1, text2, text1MeasureElement, text2MeasureElement;


    public WidgetCalorieMan(Context context) {
        super(context);
        init();
    }

    public WidgetCalorieMan(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetCalorieMan(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        bitmapObject = BitmapFactory.decodeResource(getResources(), R.drawable.man_normal);
        textMode=0;

        paintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBitmap.setColor(Color.WHITE);
        paintBitmap.setDither(true);
        paintBitmap.setFilterBitmap(true);
        rectBitmap = new Rect();


        text1=""; text2="";
        text1MeasureElement =""; text2MeasureElement="";
        colorText1 = Color.BLACK;
        colorText2 = Color.BLACK;
        colorText1Measure = Color.BLACK;
        colorText2Measure = Color.BLACK;

        paintText1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText1.setTextAlign(Paint.Align.RIGHT);
        paintText2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText2.setTextAlign(Paint.Align.RIGHT);
        paintText1Measure = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText1Measure.setTextAlign(Paint.Align.LEFT);
        paintText2Measure = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText2Measure.setTextAlign(Paint.Align.LEFT);

        rectText1 = new Rect();
        rectText2 = new Rect();
        rectText1Measure = new Rect();
        rectText2Measure = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width=w;
        height=h;

        setRectBitmap(w,h);
        textInit();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = (int)getResources().getDimension(R.dimen.widget_calorie_man_width);
        int desiredHeight = (int)getResources().getDimension(R.dimen.widget_calorie_man_height);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmapObject, null, rectBitmap, paintBitmap);

        float xPos,yPos;
        if(textMode==1){
            xPos = rectBitmap.width()  + rectText1.width();
            yPos = rectBitmap.height()/2 + rectText1.height() / 2f + rectText1.bottom;
            canvas.drawText(text1, xPos, yPos, paintText1);

            xPos = rectBitmap.width() + rectText1.width() - rectText1Measure.left + measureLetterSize;
            canvas.drawText(text1MeasureElement, xPos, yPos, paintText1Measure);

        }else if(textMode==2){

            float widthText = Math.max(rectText1.width(), rectText2.width());

            // top text position
            xPos = rectBitmap.width()  + widthText;
            yPos = rectBitmap.height()/2 - rectText1Measure.bottom;
            canvas.drawText(text1, xPos, yPos, paintText1);

            xPos = rectBitmap.width() + rectText1.width() - rectText1Measure.left + measureLetterSize;
            canvas.drawText(text1MeasureElement, xPos, yPos, paintText1Measure);

            // bottom text position
            xPos = rectBitmap.width()  + widthText;
            yPos = rectBitmap.height() - rectText2Measure.bottom;
            canvas.drawText(text2, xPos, yPos, paintText2);

            xPos = rectBitmap.width() + rectText1.width() - rectText1Measure.left + measureLetterSize;
            canvas.drawText(text2MeasureElement, xPos, yPos, paintText2Measure);
        }

    }

    /**
     * according to width and height of widget
     * set destination rect for bitmap
     * bitmap will be scale or translating,
     * relation between sides would constant
     *
     * if no text on widget view, then
     * set destination rect to center of widget view
     *
     * @param width
     * @param height
     */
    private void setRectBitmap(int width, int height){
        if(bitmapObject==null || width==0 || height==0)
            return;

        int bitmapHeight = bitmapObject.getHeight();
        float relationBetweenHeights = (float)bitmapHeight/(float)height;
        int bitmapWidth = (int)(bitmapObject.getWidth()/relationBetweenHeights);

        if(textMode>0){
            rectBitmap.set(0,0, bitmapWidth, height);
        }else {
            int offset = (width-bitmapWidth)/2;
            rectBitmap.set(offset,0, offset+bitmapWidth, height);
        }

    }

    private void textInit(){

        if(rectBitmap.width()==0)
            return;

        if(textMode==1){
            paintText1.setColor(colorText1);
            paintText1.setTextSize(rectBitmap.height()*3/4);
            paintText1.getTextBounds(text1, 0, text1.length(), rectText1);

            rectText2.set(0,0,0,0);

        }else if(textMode==2){
            paintText1.setColor(colorText1);
            paintText1.setTextSize(rectBitmap.height()/2);
            paintText1.getTextBounds(text1, 0, text1.length(), rectText1);

            paintText2.setColor(colorText2);
            paintText2.setTextSize(rectBitmap.height()/2);
            paintText2.getTextBounds(text2, 0, text2.length(), rectText2);
        }

        if(textMode>0) {
            paintText1Measure.setTextSize(rectBitmap.height() / 2);
            paintText1Measure.getTextBounds(text1MeasureElement, 0, text1MeasureElement.length(), rectText1Measure);
            paintText2Measure.setTextSize(rectBitmap.height() / 2);
            paintText2Measure.getTextBounds(text2MeasureElement, 0, text2MeasureElement.length(), rectText2Measure);

            measureLetterSize = rectText1Measure.width()/(text1MeasureElement.length()+1);
            float widthForText = width - rectBitmap.width()
                    - Math.max(rectText1.width(), rectText2.width())
                    - measureLetterSize;

            float widthForMeasureText = textMode==1 ? rectText1Measure.width()
                    : Math.max(rectText1Measure.width(), rectText2Measure.width());
            if (widthForMeasureText > widthForText) {
                float desiredTextSize = (rectBitmap.height()/2) * widthForText / widthForMeasureText;
                // not show text of measure element if size very small
                if(desiredTextSize<height/5){
                    text1MeasureElement="";
                    return;
                }

                paintText1Measure.setTextSize(desiredTextSize);
                paintText1Measure.getTextBounds(text1MeasureElement, 0, text1MeasureElement.length(), rectText1Measure);
                paintText2Measure.setTextSize(desiredTextSize);
                paintText2Measure.getTextBounds(text2MeasureElement, 0, text2MeasureElement.length(), rectText2Measure);
            }
        }
    }

    public void updateWidget(){
        textInit();
        invalidate();
    }

    //region Parameter's setters and getters
    public void setIconDrawableId(int iconDrawableId) {
        this.iconDrawableId = iconDrawableId;
        bitmapObject = BitmapFactory.decodeResource(getResources(),iconDrawableId);
        setRectBitmap(getWidth(), getHeight());
    }
    public void setTextMode(int textMode) {
        this.textMode = textMode;
    }
    public void setColorText1(int colorText1) {
        this.colorText1 = colorText1;
    }
    public void setColorText2(int colorText2) {
        this.colorText2 = colorText2;
    }
    public void setColorText1Measure(int colorText1Measure) {
        this.colorText1Measure = colorText1Measure;
    }
    public void setColorText2Measure(int colorText2Measure) {
        this.colorText2Measure = colorText2Measure;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }
    public void setText2(String text2) {
        this.text2 = text2;
    }
    public void setText1MeasureElement(String text1MeasureElement) {
        this.text1MeasureElement = text1MeasureElement;
    }
    public void setText2MeasureElement(String text2MeasureElement) {
        this.text2MeasureElement = text2MeasureElement;
    }
    //endregion Parameter's setters and getters
}
