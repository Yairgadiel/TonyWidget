package com.yair.tonywidget.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.yair.tonywidget.Interfaces.IOnDialogClosed;
import com.yair.tonywidget.R;

/**
 * This class is a color-picker dialog
 * Created by yair on 19/01/18.
 */

public class ColorPickerDialog extends Dialog implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    // region Members
    private Context _context;
    private IOnDialogClosed _listener;
    private int _red;
    private int _green;
    private int _blue;
    // endregion

    // region UI Components
    // The Seekbars
    private SeekBar _seekBarRed;
    private SeekBar _seekBarGreen;
    private SeekBar _seekBarBlue;

    // The presentation views
    private View _viewCurrColor;
    private View _viewPickColor;

    // The action buttons
    private Button _btnOk;
    private Button _btnCancel;
    // endregion

    public ColorPickerDialog(Context context, IOnDialogClosed listener, int currColor) {
        super(context);
        _context = context;
        _listener = listener;

        // Initializing the color
        _red = Color.red(currColor);
        _green = Color.green(currColor);
        _blue = Color.blue(currColor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_color_picker);

        // Initializing the UI Components
        // The SeekBars
        _seekBarRed = findViewById(R.id.seekBarRed);
        _seekBarRed.setProgress(_red);
        _seekBarRed.setOnSeekBarChangeListener(this);

        _seekBarGreen = findViewById(R.id.seekBarGreen);
        _seekBarGreen.setProgress(_green);
        _seekBarGreen.setOnSeekBarChangeListener(this);

        _seekBarBlue = findViewById(R.id.seekBarBlue);
        _seekBarBlue.setProgress(_blue);
        _seekBarBlue.setOnSeekBarChangeListener(this);

        // The presentation views
        _viewCurrColor = findViewById(R.id.viewCurrColor);
        _viewCurrColor.setBackgroundColor(Color.rgb(_red, _green, _blue));
        _viewPickColor = findViewById(R.id.viewPickerColor);
        _viewPickColor.setBackgroundColor(Color.rgb(_red, _green, _blue));

        // The action buttons
        _btnOk = findViewById(R.id.btnOk);
        _btnOk.setOnClickListener(this);
        _btnCancel = findViewById(R.id.btnCancel);
        _btnCancel.setOnClickListener(this);
    }

    /**
     * Formats individual RGB values to be output as a HEX string.
     * <p>
     * Beware: If color value is lower than 0 or higher than 255, it's reset to 0.
     *
     * @param red   Red color value
     * @param green Green color value
     * @param blue  Blue color value
     * @return HEX String containing the three values
     */
    public static String formatColorValues(
            @IntRange(from = 0, to = 255) int red,
            @IntRange(from = 0, to = 255) int green,
            @IntRange(from = 0, to = 255) int blue) {

        return String.format("%02X%02X%02X",
                assertColorValueInRange(red),
                assertColorValueInRange(green),
                assertColorValueInRange(blue)
        );
    }


    /**
     * Checks whether the specified value is between (including bounds) 0 and 255
     *
     * @param colorValue Color value
     * @return Specified input value if between 0 and 255, otherwise 0
     */
    static int assertColorValueInRange(@IntRange(from = 0, to = 255) int colorValue) {
        return ((0 <= colorValue) && (colorValue <= 255)) ? colorValue : 0;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Identifying the changed seekbar
        switch (seekBar.getId()) {
            case R.id.seekBarRed:
                _red = progress;

                break;

            case R.id.seekBarGreen:
                _green = progress;

                break;

            case R.id.seekBarBlue:
                _blue = progress;

                break;

            default:
                System.out.println("ERROR");

                break;
        }

        _viewPickColor.setBackgroundColor(Color.rgb(_red, _green, _blue));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        // Identifying the clicked view
        switch (v.getId()) {
            case R.id.btnOk:
                _listener.onDialogClosed(Color.rgb(_red, _green, _blue));
            case R.id.btnCancel:
                dismiss();

                break;
                default:
                    System.out.println("ERROR");

                    break;
        }
    }
}
