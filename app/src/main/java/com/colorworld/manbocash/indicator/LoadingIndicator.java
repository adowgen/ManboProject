package com.colorworld.manbocash.indicator;

import android.app.Dialog;
import android.content.Context;
import android.text.Layout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.colorworld.manbocash.R;

public class LoadingIndicator extends Dialog {
    public LoadingIndicator(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_indicator);

        getWindow().setBackgroundDrawableResource(R.color.transparent);

    }

    public LoadingIndicator(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingIndicator(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
