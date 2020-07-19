package com.gzf01.rxzsmbclient.global;

import android.annotation.SuppressLint;

import com.gzf01.rxzsmbclient.model.sqldao.DaoSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Title: G 类 <br/>
 * Description:  <br/>
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class G {
    public static DaoSession daoSession;
    @SuppressLint("SimpleDateFormat")
    public final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
