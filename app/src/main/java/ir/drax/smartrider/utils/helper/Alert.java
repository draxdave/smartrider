package ir.drax.smartrider.utils.helper;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import ir.drax.smartrider.R;

/**
 * Created by siamak on 9/29/16.
 */

public class Alert {
    private Context context;
    private ViewGroup root;
    public Alert(Context context){
        this.context=context;
        root = (ViewGroup) ((ViewGroup)((Activity) context).findViewById(android.R.id.content)).getChildAt(0);
        //root = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(16908290);
    }
    public Dialog IfYes(String msg, final CB cb){
        return IfYes(context.getString(R.string.simpleAlert_title),msg,cb);
    }
    public Dialog IfYes(String title,String msg, final CB cb){
        blur();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_alert_default);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((TextView)dialog.findViewById(R.id.titleText)).setText(title);
        ((TextView)dialog.findViewById(R.id.text)).setText(msg);
        ((TextView)dialog.findViewById(R.id.ok)).setText(R.string.yes);
        ((TextView)dialog.findViewById(R.id.nok)).setText(R.string.no);
        ((ViewGroup)dialog.findViewById(R.id.nok).getParent()).setVisibility(View.VISIBLE);
        ((ImageView)dialog.findViewById(R.id.stateIcon)).setColorFilter(Color.parseColor("#FFE100"));

        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeOutUp)
                        .interpolate(new AnticipateOvershootInterpolator())
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                dialog.dismiss();
                                cb.Then();
                            }
                        })
                        .playOn(dialog.findViewById(R.id.card));

            }
        });
        dialog.findViewById(R.id.nok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeOutUp)
                        .interpolate(new AnticipateOvershootInterpolator())
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                dialog.dismiss();
                            }
                        })
                        .playOn(dialog.findViewById(R.id.card));

            }
        });
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                unBlur();
            }
        });

        YoYo.with(Techniques.FadeInDown)
                .interpolate(new AnticipateOvershootInterpolator())
                .playOn(dialog.findViewById(R.id.card));

        return dialog;
    }

    public Dialog Empty(View contentView){
        return Empty(contentView,null);
    }
    public Dialog Empty(View contentView, final CB onDismiss){
        blur();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(contentView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            dialog.getWindow().setFlags(Window.FEATURE_SWIPE_TO_DISMISS,Window.FEATURE_SWIPE_TO_DISMISS);
        }
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onDismiss != null)onDismiss.Then();
                unBlur();
            }
        });

        YoYo.with(Techniques.FadeInDown)
                .interpolate(new AnticipateOvershootInterpolator())
                .playOn(contentView);

        return dialog;
    }

    public Dialog Strict(int msg, int okTxt, int nokTxt, final doubleCallBack dcb){
        return Strict(context.getString(msg),context.getString(okTxt),context.getString(nokTxt),dcb);
    }
    public Dialog Strict(String msg, String okTxt, String nokTxt, final doubleCallBack dcb){
        blur();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_alert_default);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((TextView)dialog.findViewById(R.id.titleText)).setText(R.string.simpleAlert_title);
        ((TextView)dialog.findViewById(R.id.text)).setText(msg);
        ((TextView)dialog.findViewById(R.id.ok)).setText(okTxt);
        ((TextView)dialog.findViewById(R.id.nok)).setText(nokTxt);
        ((ViewGroup)dialog.findViewById(R.id.nok).getParent()).setVisibility(View.VISIBLE);
        ((ImageView)dialog.findViewById(R.id.stateIcon)).setColorFilter(Color.parseColor("#d32f2f"));

        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeOutUp)
                        .interpolate(new AnticipateOvershootInterpolator())
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                dialog.dismiss();
                                dcb.onAccept();
                            }
                        })
                        .playOn(dialog.findViewById(R.id.card));

            }
        });
        dialog.findViewById(R.id.nok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeOutUp)
                        .interpolate(new AnticipateOvershootInterpolator())
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                dialog.dismiss();
                                dcb.onDecline();
                            }
                        })
                        .playOn(dialog.findViewById(R.id.card));

            }
        });
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                unBlur();
            }
        });

        YoYo.with(Techniques.FadeInDown)
                .interpolate(new AnticipateOvershootInterpolator())
                .playOn(dialog.findViewById(R.id.card));

        return dialog;
    }


    public Dialog Notice(int title, int msg){
        return Notice(context.getString(title),context.getString(msg));
    }
    public Dialog Notice(String title, String msg){
        return Notice(title,msg,null);
    }
    public Dialog Notice(String title, String msg, final CB cb){
        blur();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_alert_default);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((TextView)dialog.findViewById(R.id.titleText)).setText(title);
        ((TextView)dialog.findViewById(R.id.text)).setText(msg);

        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeOutUp)
                        .interpolate(new AnticipateOvershootInterpolator())
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                dialog.dismiss();
                            }
                        })
                        .playOn(dialog.findViewById(R.id.card));
            }
        });
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (cb != null)cb.Then();
                unBlur();
            }
        });

        YoYo.with(Techniques.FadeInDown)
                .interpolate(new AnticipateOvershootInterpolator())
                .playOn(dialog.findViewById(R.id.card));

        return dialog;
    }

    public Dialog Caution(String title, String msg, final CB callBack){
        blur();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_alert_default);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((TextView)dialog.findViewById(R.id.titleText)).setText(title);
        ((TextView)dialog.findViewById(R.id.text)).setText(msg);
        ((ImageView)dialog.findViewById(R.id.stateIcon)).setColorFilter(Color.parseColor("#FFE100"));

        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeOutUp)
                        .interpolate(new AnticipateOvershootInterpolator())
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                dialog.dismiss();
                            }
                        })
                        .playOn(dialog.findViewById(R.id.card));

            }
        });
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (callBack != null)callBack.Then();
                unBlur();
            }
        });

        YoYo.with(Techniques.FadeInDown)
                .interpolate(new AnticipateOvershootInterpolator())
                .playOn(dialog.findViewById(R.id.card));

        return dialog;
    }

    public ProgressDialog progress(String text,Boolean cancelable){
        blur();
        ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage(text);
        pd.setTitle("لطفا شکیبا باشید");
        pd.setCancelable(cancelable);
        pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                unBlur();
            }
        });
        pd.show();
        return pd;
    }

    public Dialog Loading(){return Loading(R.string.loading);}
    public Dialog Loading(String msg){
        return Loading(msg, false);
    }
    public Dialog Loading(int msg){
        return Loading(msg, false);
    }
    public Dialog Loading(String msg, Boolean cancelable){
        return Loading(msg, cancelable, new CB() {@Override public void Then() {}});
    }
    public Dialog Loading(int msg, Boolean cancelable){
        return Loading(context.getString(msg), cancelable, new CB() {@Override public void Then() {}});
    }
    public Dialog Loading(int msg, Boolean cancelable, final CB onCancel){
        return Loading(context.getString(msg),cancelable,onCancel);
    }
    public Dialog Loading(String msg, Boolean cancelable, final CB onCancel){
        blur();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCancelable(cancelable);

        TextView text = dialog.findViewById(R.id.searchText);
        text.setText(msg);

        ImageView dialogButton = dialog.findViewById(R.id.negativeBtn);
        dialogButton.setVisibility(cancelable?View.VISIBLE:View.GONE);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel.Then();
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                unBlur();
                onCancel.Then();
            }
        });

        dialog.show();
        return dialog;
    }

    public void DeepBlur(){
       /* Blurry.with(context)
                .radius(4)
                .sampling(15)
                .async()
                .animate(250)
                .onto(root);*/
    }
    public void blur(){
        /*Blurry.with(context)
                .radius(4)
                .sampling(7)
                .async()
                .animate(250)
                .onto(root);*/
    }

    public void unBlur(){
        //Blurry.delete(root);
    }

    public interface doubleCallBack {
        void onAccept();
        void onDecline();
    }

    public interface CB {
        void Then();
    }
}
