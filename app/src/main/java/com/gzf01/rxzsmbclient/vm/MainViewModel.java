package com.gzf01.rxzsmbclient.vm;

import android.view.View;

import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.vm.BaseViewModel;
import com.gzf01.rxzsmbclient.databinding.MainDataBinding;
import com.gzf01.rxzsmbclient.entity.Link;
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
        ArrayList<Link> list = new ArrayList<>();
        list.add(new Link());
        list.add(new Link());
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

}
