package com.example.aieffectdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.aieffectdemo.R;
import com.example.aieffectdemo.databinding.LayoutRetardDialogBinding;

public class MarketingTeamIsAllMentallyRetardedDialog extends Dialog {

    private LayoutRetardDialogBinding binding;

    public static MarketingTeamIsAllMentallyRetardedDialog newInstance(Context context) {
        return new MarketingTeamIsAllMentallyRetardedDialog(context);
    }
    public MarketingTeamIsAllMentallyRetardedDialog(@NonNull Context context) {
        super(context);
        binding = LayoutRetardDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String textRetardHint1 = getContext().getString(R.string.text_retard_1_hint);
        String textRetardHint2 = getContext().getString(R.string.text_retard_2_hint);

        SpannableStringBuilder stringBuilder1 = new SpannableStringBuilder(textRetardHint1);
        stringBuilder1.setSpan(new ForegroundColorSpan(
                ContextCompat.getColor(getContext(), R.color.red)),
                3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder1.setSpan(new StyleSpan(Typeface.BOLD),
                3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder stringBuilder2 = new SpannableStringBuilder(textRetardHint2);
        stringBuilder2.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(getContext(), R.color.red)),
                3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder2.setSpan(new StyleSpan(Typeface.BOLD),
                3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        binding.tvFirst.setText(stringBuilder1);
        binding.tvSecond.setText(stringBuilder2);

        binding.tvClose.setOnClickListener(v -> cancel());
    }

}
