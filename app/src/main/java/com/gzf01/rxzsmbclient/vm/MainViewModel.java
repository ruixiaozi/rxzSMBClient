package com.gzf01.rxzsmbclient.vm;

import android.view.View;

import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.vm.BaseViewModel;
import com.gzf01.rxzsmbclient.databinding.MainDataBinding;
import com.gzf01.rxzsmbclient.entity.Link;
import com.gzf01.rxzsmbclient.global.G;
import com.gzf01.rxzsmbclient.view.AddLinkActivityView;
import com.gzf01.rxzsmbclient.view.MainActivityView;

import java.util.ArrayList;

/**
 * Title: Main 类 <br/>
 * Description: 首页 <br/>
 * CreateTime: 2020/7/12 <br/>
 *
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
public class MainViewModel extends BaseViewModel<MainDataBinding, MainActivityView> {

    @Override
    public void onInit(Request request) {
        binding.setMainTitle("Rxz Smb文件浏览器");


    }

    @Override
    public void onShow() {
        //加载本地连接数据
        ArrayList<Link> list = new ArrayList<>(G.daoSession.getLinkDao().loadAll());

        binding.setLinkListinit(list);
    }

    /**
     * Title: openMenu 方法 <br />
     * Description:
     *
     * @return void
     */
    public void openMenu(View v){
        binding.mainSlideMenu.openRightSlide();
        onShow();
    }

    /**
     * Title: toAdd 方法 <br />
     * Description: 打开添加连接页面
     *
     * @return void
     */
    public void toAdd( View v){
        view.turnTo(view, AddLinkActivityView.class,null,true);
    }

}
