package ir.drax.smartrider.utils.view;

import android.content.Context;
import android.os.Build;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import ir.drax.smartrider.utils.helper.Alert;


/**
 * Created by siamak on 10/7/16.
 */

public class ViewBaker {
    private ViewGroup viewGroup;
    private View target;
    private Context context;
    private int posiotion;

    public ViewBaker(Context context) {
        this.context=context;
    }
    public ViewBaker(Context context,ViewGroup root) {
        this.viewGroup=root;this.context=context;
    }
    public ViewBaker(Context context,RelativeLayout root,View targetView,int position) {
        this.viewGroup=root;
        this.context=context;
        this.target = targetView;
        this.posiotion=position;
    }
    public ImageView Image(int image){
        return Image(image, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public ImageView Image(int image, int height,int width){
        ImageView imageView=new ImageView(context);

        imageView.setImageResource(image);

        if(target!=null) {
            int r=randInt();
            target.setId(target.getId()<=0?r:target.getId());
            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(width,height);
            layoutParams.addRule(posiotion, target.getId());
            imageView.setLayoutParams(layoutParams);
        }else {
            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(width,height);
            imageView.setLayoutParams(layoutParams);
        }

        if (viewGroup!=null)viewGroup.addView(imageView);
        return imageView;
    }

    public TextView Text(String text, int style){
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView=new TextView(context);
        textView.setText(text);
        textView.setTextAppearance(context, style);

        if (Build.VERSION.SDK_INT < 23) {
            textView.setTextAppearance(context, style);
        } else {
            textView.setTextAppearance(style);
        }

        return Text_binding(textView,layoutParams);
    }
    public TextView Text(String text,int textSize,int gravity,int padding){
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView=new TextView(context);
        textView.setTextSize(textSize);
        textView.setText(text);
        textView.setPadding(padding,padding,padding,padding);
        textView.setGravity(gravity);

        return Text_binding(textView,layoutParams);
    }
    private TextView Text_binding(TextView textView,RelativeLayout.LayoutParams layoutParams){
        if(target!=null) {
            int r=randInt();
            target.setId(target.getId()<=0?r:target.getId());
            layoutParams.addRule(posiotion, target.getId());
        }
        //textView.setLayoutParams(layoutParams);
        if (viewGroup!=null)viewGroup.addView(textView);
        return textView;
    }

    public Button Button(String text, int drawable, int textSize, int gravity, final Alert.CB CB){
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button button=new Button(context);
        button.setTextSize(textSize);
        button.setText(text);
        button.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
        button.setPadding(8,5,8,5);
        button.setGravity(gravity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CB.Then();
            }
        });
        if(target!=null) {
            int r=randInt();
            target.setId(target.getId()<=0?r:target.getId());
            layoutParams.addRule(posiotion, target.getId());
        }
        button.setLayoutParams(layoutParams);
        if (viewGroup!=null)viewGroup.addView(button);
        return button;
    }

    public EditText input(int inputType, String hint, int textSize, int gravity, int padding, int lines){
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText edittext=new EditText(context);
        edittext.setTextSize(textSize);
        edittext.setHint(hint);
        edittext.setPadding(padding,padding,padding,padding);
        edittext.setGravity(gravity);
        edittext.setMaxLines(lines);
        edittext.setInputType(inputType);
        if(target!=null) {
            int r=randInt();
            target.setId(target.getId()<=0?r:target.getId());
            layoutParams.addRule(posiotion, target.getId());
        }
        edittext.setLayoutParams(layoutParams);
        if (viewGroup!=null)viewGroup.addView(edittext);
        return edittext;
    }

    public RecyclerView Recycler(){
        RecyclerView recyclerView=new RecyclerView(context);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setClipToPadding(false);

        if(target!=null) {
            int r=randInt();
            target.setId(target.getId()<=0?r:target.getId());
            layoutParams.addRule(posiotion, target.getId());
        }
        recyclerView.setLayoutParams(layoutParams);
        if(viewGroup!=null)viewGroup.addView(recyclerView);
        return recyclerView;
    }

    public ProgressBar Progress(Boolean indeterminate){
        return Progress(indeterminate,0,0);
    }
    public ProgressBar Progress(Boolean indeterminate,int progress,int max){
        ProgressBar bar=new ProgressBar(context);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        bar.setIndeterminate(indeterminate);
        bar.setProgress(progress);
        bar.setMax(max);
        if(target!=null) {
            int r=randInt();
            target.setId(target.getId()<=0?r:target.getId());
            layoutParams.addRule(posiotion, target.getId());
        }
        bar.setLayoutParams(layoutParams);
        if(viewGroup!=null)viewGroup.addView(bar);
        return bar;
    }

    public LinearLayout Linear() {
        return Linear(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    }
    public LinearLayout Linear(int width,int height){
        LinearLayout layout=new LinearLayout(context);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(width, height);

        if(target!=null) {
            int r=randInt();
            target.setId(target.getId()<=0?r:target.getId());
            layoutParams.addRule(posiotion, target.getId());
        }
        layout.setLayoutParams(layoutParams);
        if(viewGroup!=null)viewGroup.addView(layout);
        return layout;
    }

    public ListView List(){
        ListView listView=new ListView(context);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listView.setClipToPadding(false);

        if(target!=null) {
            int r=randInt();
            target.setId(target.getId()<=0?r:target.getId());
            layoutParams.addRule(posiotion, target.getId());
        }
        listView.setLayoutParams(layoutParams);
        if(viewGroup!=null)viewGroup.addView(listView);
        return listView;
    }

    private int randInt() {
        int highRand = 999999;
        int minRand = 100000;
        return randInt(minRand, highRand);
    }

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public ViewPager Pager() {
        ViewPager viewPager=new ViewPager(context);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if(target!=null) {
            int r=randInt();
            target.setId(target.getId()<=0?r:target.getId());
            layoutParams.addRule(posiotion, target.getId());
        }
        viewPager.setLayoutParams(layoutParams);
        if(viewGroup!=null)viewGroup.addView(viewPager);
        return viewPager;
    }
    public GridView GridView(int NumColumns){
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250);
        GridView gridView = new GridView(context);
        gridView.setGravity(Gravity.CENTER);
        gridView.setNumColumns(NumColumns);
        gridView.setLayoutParams(layoutParams);

        if(viewGroup!=null)viewGroup.addView(gridView);
        return gridView;
    }
}
