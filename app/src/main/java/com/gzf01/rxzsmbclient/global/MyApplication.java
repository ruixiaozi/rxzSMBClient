package com.gzf01.rxzsmbclient.global;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.gzf01.rxzmvvm.global.Rxzmvvm;
import com.gzf01.rxzsmbclient.model.sqldao.DaoMaster;

import java.util.Date;

/**
 * Title: MyApplication 类 <br/>
 * Description: 自定义应用 <br/>
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class MyApplication extends Application {




    @Override
    public void onCreate() {
        super.onCreate();
        //初始化框架
        Rxzmvvm.init(this);
        initGreenDao(this);
    }

    /**
     * Title: initGreenDao 方法 <br />
     * Description: 初始化数据库操作方法
     *
     * @return void
     */
    private void initGreenDao(Context context) {
        //创建数据库mydb.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,"mydb.db");
        //获取可写数据库
        SQLiteDatabase database = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(database);
        //获取Dao对象管理者
        G.daoSession = daoMaster.newSession();
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
            tv.setText(G.df.format(date));
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
