package com.txtled.gp_a012.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.txtled.gp_a012.R;
import com.txtled.gp_a012.application.MyApplication;
import com.txtled.gp_a012.widget.CustomEditText;


public class AlertUtils {

    public static void showErrorMessage(Context context, int titleRes,
                                        String errorCode, DialogInterface.OnClickListener listener) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setMessage(context.getResources().getIdentifier("ERROR_CODE_" + errorCode,
                            "string", context.getPackageName()));
            if (titleRes != 0) {
                builder.setTitle(titleRes);
            }
            if (listener == null) {
                builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            } else {
                builder.setNegativeButton(R.string.ok, listener);
            }
            Dialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public static void showErrorMessage(Context context, String errorCode) {
        showErrorMessage(context, 0, errorCode, null);
    }

    public static void showErrorMessage(Context context,
                                        String errorCode, DialogInterface.OnClickListener listener) {
        showErrorMessage(context, 0, errorCode, listener);
    }

    public static void showAlertDialog(Context context, String message,
                                       DialogInterface.OnClickListener listener0,
                                       DialogInterface.OnClickListener listener1) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setNegativeButton(R.string.cancel, listener0)
                    .setPositiveButton(R.string.confirm, listener1)
                    .create();
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public static void showAlertDialog(Context context, int viewId,
                                       DialogInterface.OnClickListener listener0,
                                       DialogInterface.OnClickListener listener1) {
        if (!((Activity) context).isFinishing()) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(viewId, null);
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(view)
                    .setNegativeButton(R.string.cancel, listener0)
                    .setPositiveButton(R.string.confirm, listener1)
                    .create();
            dialog.setCanceledOnTouchOutside(false);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    MyApplication.getAppComponent().getDataManagerModel().setInitDialog(b);
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public static void showAlertDialog(Context context, int title, int message) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public static void showAlertDialog(Context context, String message) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public static void showAlertDialog(Context context, int messageRes) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage(messageRes)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.setCancelable(true);
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public static void showAlertDialog(Context context, int messageRes,
                                       DialogInterface.OnClickListener listener) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage(messageRes)
                    .setPositiveButton(R.string.ok, listener)
                    .create();
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public static void showProgressDialog(Context context, int id) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(id));
        progressDialog.show();
    }

    public static void showEditDialog(final Context context, final OnEditDialogClickListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.alert_dialog, null);
        final CustomEditText etName = (CustomEditText) dialogView.
                findViewById(R.id.et_name);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etName.getText().toString().length() > 20) {
                    etName.setError(context.getString(R.string.input_limit));
                }
            }
        });

        builder.setView(dialogView);
        builder.setTitle(R.string.collect);
        builder.setPositiveButton(R.string.ok, null);
        builder.setNegativeButton(R.string.cancel, null);

        final AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface mDialog) {
                final Button positionButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                final Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                positionButton.setTextColor(context.getResources().getColor(R.color.yellow_seek_bar));
//                negativeButton.setTextColor(context.getResources().getColor(R.color.yellow_seek_bar));

                positionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etName.getText().toString().length() < 21 &&
                                etName.getText().toString().length() > 0) {
                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                            if (imm.isActive()){
//                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                            }
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            dialog.dismiss();
                            listener.onEditDialogClick(etName.getText().toString());
                        }
                    }
                });
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                        if (imm.isActive()){
//                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                        }
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        dialog.dismiss();
                    }
                });
            }
        });
        //dialog.getWindow().setBackgroundDrawableResource(R.color.pop_window_bg);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    public interface OnEditDialogClickListener {
        void onEditDialogClick(String str);
    }

    public static void showSnackbar(Context context, View view, int text){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        snackbar.show();
    }
}
