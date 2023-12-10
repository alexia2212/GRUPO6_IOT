package com.example.grupo_iot.delactividad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class RoundedImageView extends AppCompatImageView {

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Obtener el bitmap de la imagen
        Bitmap bitmap = getBitmapFromDrawable(getDrawable());

        if (bitmap != null) {
            // Crear un Path para el fondo redondeado
            Path path = new Path();
            float radius = 50.0f; // ajusta el radio según sea necesario
            RectF rect = new RectF(0, 0, getWidth(), getHeight());
            path.addRoundRect(rect, radius, radius, Path.Direction.CW);

            // Clip el canvas alrededor del Path
            canvas.clipPath(path);

            // Dibujar el bitmap en el canvas
            super.onDraw(canvas);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(
                getWidth(),
                getHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
