package com.gzf01.rxzsmbclient.global;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.gzf01.rxzmvvm.global.Rxzmvvm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Title: MyApplication 类 <br/>
 * Description: 自定义应用 <br/>
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class MyApplication extends Application {

    public final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化框架
        Rxzmvvm.init(this);

    }

    /**
     * Title: setDateTV 方法 <br />
     * Description: 设置日期显示
     *
     * @return void
     */
    @BindingAdapter(value = {"myDate"})
    public static void setDateTV(TextView tv, Date date){
        if(date!=null){
            tv.setText(df.format(date));
        }
    }

    /**
     * Title: setVisible 方法 <br />
     * Description: 设置是否显示
     *
     * @return void
     */
    @BindingAdapter(value = {"isVisible"})
    public static void setVisible(View view, boolean isVisible){
        if(isVisible)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }


}
