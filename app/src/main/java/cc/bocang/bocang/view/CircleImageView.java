package cc.bocang.bocang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import cc.bocang.bocang.R;
import cc.bocang.bocang.utils.UIUtils;

public class CircleImageView extends ImageView {
    private static final int MODE_CIRCLE = 1;

    private static final int MODE_NONE = 0;

    private static final int MODE_ROUND = 2;

    private int currMode = 0;

    private int currRound = dp2px(5.0F);

    private Paint mPaint;

    public CircleImageView(Context paramContext) {
        super(paramContext);
        initViews();
    }

    public CircleImageView(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, 0); }

    public CircleImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        obtainStyledAttrs(paramContext, paramAttributeSet, paramInt);
        initViews();
    }

    private int dp2px(float paramFloat) { return (int)TypedValue.applyDimension(1, paramFloat, getResources().getDisplayMetrics()); }

    private Bitmap drawable2Bitmap(Drawable paramDrawable) {
        if (paramDrawable == null)
            return null;
        Bitmap bitmap2 = UIUtils.drawableToBitmap(getWidth(), getHeight(), paramDrawable);
        Bitmap bitmap1 = bitmap2;
        return (getImageMatrix() != null) ? bitmap2 : bitmap1;
    }

    private void initViews() { this.mPaint = new Paint(5); }

    private void obtainStyledAttrs(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RoundImageView, paramInt, 0);
        this.currMode = 2;
        this.currRound = dp2px(5.0F);
        typedArray.recycle();
    }

    protected void onDraw(Canvas paramCanvas) {
        Drawable drawable = getDrawable();
        paramCanvas.drawColor(0);
        Matrix matrix = getImageMatrix();
        if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0) {
            Bitmap bitmap = null;
            if (matrix == null && getPaddingTop() == 0 && getPaddingLeft() == 0) {
                drawable.draw(paramCanvas);
                return;
            }
            int i = paramCanvas.getSaveCount();
            paramCanvas.save();
            if (Build.VERSION.SDK_INT >= 16 && getCropToPadding()) {
                int j = getScrollX();
                int k = getScrollY();
                paramCanvas.clipRect(getPaddingLeft() + j, getPaddingTop() + k, getRight() + j - getLeft() - getPaddingRight(), getBottom() + k - getTop() - getPaddingBottom());
            }
            paramCanvas.translate(getPaddingLeft(), getPaddingTop());
            if (this.currMode == 1) {
                bitmap = drawable2Bitmap(drawable);
                this.mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                paramCanvas.drawCircle((getWidth() / 2), (getHeight() / 2), (getWidth() / 2), this.mPaint);
            } else if (this.currMode == 2) {
                bitmap = drawable2Bitmap(drawable);
                this.mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                paramCanvas.drawRoundRect(new RectF(getPaddingLeft(), getPaddingTop(), (getWidth() - getPaddingRight()), (getHeight() - getPaddingBottom())), this.currRound, this.currRound, this.mPaint);
            } else {
                if (matrix != null)
                    paramCanvas.concat(matrix);
                paramCanvas.drawBitmap(bitmap,matrix,mPaint);
//                bitmap.draw(paramCanvas);
            }
            paramCanvas.restoreToCount(i);
            return;
        }
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        if (this.currMode == 1) {
            super.onMeasure(paramInt1, paramInt2);
            paramInt1 = Math.min(getMeasuredHeight(), getMeasuredWidth());
            setMeasuredDimension(paramInt1, paramInt1);
            return;
        }
        super.onMeasure(paramInt1, paramInt2);
    }
}
