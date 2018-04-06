package com.pentateuch.watersupply.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AlertDialogMaker {
    private AlertDialog dialog;

    public String getInput() {
        return editText.getText().toString();
    }

    private EditText editText;

    public AlertDialogMaker(Context context) {
        this.context = context;
    }

    private Context context;

    public static AlertDialog getAlertDialog(Context context, int layout, String regex){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(layout,null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     *
     * @param context
     * @param title
     * @param type InputType
     * @param listener
     * @return
     */
    public static AlertDialog getEditTextAlertDialog(final Context context, String title,String text, int type, final AlertClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText editText = new EditText(context);
        editText.setInputType(type);
        editText.setText(text);
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        builder.setTitle(title);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(editText.getText().toString());
                dialog.dismiss();
            }
        });
        return dialog;

    }

    public AlertDialog getEditTextAlertDialog(String title, final String text, int type, View.OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        editText = new EditText(context);
        editText.setInputType(type);
        editText.setText(text);
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        builder.setTitle(title);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(onClickListener);
        return dialog;

    }

    public void close() {
        if (dialog != null)
            dialog.dismiss();
    }

    public interface AlertClickListener{
        void onClick(String text);
    }
}
