package com.yair.tonywidget.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by yair on 05/01/18.
 */

public class BitmapHelper {

    /**
     * Encodes Bitmap image to String
     * @param bitmap the Bitmap image to convert
     * @return the Encoded String
     */
    public static String BitmapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);

        return (Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
    }

    /**
     * Decodes String to Bitmap image
     * @param encodedString the encoded string
     * @return the decoded Bitmap image
     */
    public static Bitmap StringToBitmap(String encodedString)
    {
        try
        {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            
            return (BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length));
        }
        catch (Exception e)
        {
            return (null);
        }
    }

    /**
     * Makes a given Bitmap Circle
     * @param bitmapToCircle the Bitmap object to make circle
     * @return a circled Bitmap
     */
    public static Bitmap CircleBitmap(Bitmap bitmapToCircle)
    {
        Bitmap bitmapCircled = Bitmap.createBitmap(bitmapToCircle.getWidth(),
                                                   bitmapToCircle.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCircled);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapToCircle.getWidth(), bitmapToCircle.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(bitmapToCircle.getWidth() / 2, bitmapToCircle.getHeight() / 2,
                          bitmapToCircle.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapToCircle, rect, rect, paint);

        return (bitmapCircled);
    }

    public static Bitmap decodeBitmapFromList(String path) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > WidgetProvider.IMAGE_SIZE || width > WidgetProvider.IMAGE_SIZE) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= WidgetProvider.IMAGE_SIZE
                    || (halfWidth / inSampleSize) >= WidgetProvider.IMAGE_SIZE) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
