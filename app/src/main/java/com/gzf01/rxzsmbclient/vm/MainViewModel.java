package com.gzf01.rxzsmbclient.vm;

import android.Manifest;
import android.view.View;

import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.global.Rxzmvvm;
import com.gzf01.rxzmvvm.vm.BaseViewModel;
import com.gzf01.rxzsmbclient.databinding.MainDataBinding;
import com.gzf01.rxzsmbclient.entity.Link;
import com.gzf01.rxzsmbclient.global.G;
import com.gzf01.rxzsmbclient.model.helper.PermissionHelper;
import com.gzf01.rxzsmbclient.view.AddLinkActivityView;
import com.gzf01.rxzsmbclient.view.BrowseActivityView;
import com.gzf01.rxzsmbclient.view.EditLinkActivityView;
import com.gzf01.rxzsmbclient.view.MainActivityView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    private static final String TAG = "MainViewModel";

    @Override
    public void onInit(Request request) {
        binding.setMainTitle("Rxz Smb文件浏览器");

        //权限申请
        PermissionHelper.requestPermission(view, Manifest.permission.WRITE_EXTERNAL_STORAGE,()->{});
        PermissionHelper.requestPermission(view, Manifest.permission.READ_EXTERNAL_STORAGE,()->{});


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

    /**
     * Title: toEdit 方法 <br />
     * Description: 打开编辑连接页面
     *
     * @return void
     */
    public void toEdit(Link link){
        Request request = new Request();
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("link", Rxzmvvm.gson.toJson(link));
        request.setData(map);
        request.setCode(EditLinkViewModel.EDIT_REQUEST_CODE);

        view.turnTo(view, EditLinkActivityView.class,request,true);
    }

    /**
     * Title: linkTo 方法 <br />
     * Description: 链接
     *
     * @return void
     */
    @SuppressWarnings("deprecation")
    public void linkTo(Link link,View v){
        Request request = new Request();
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("link", Rxzmvvm.gson.toJson(link));
        request.setData(map);
        request.setCode(BrowseViewModel.REQUEST_LINK_CODE);
        //跳转浏览页面
        view.turnTo(view, BrowseActivityView.class,request,true);
    }

}
