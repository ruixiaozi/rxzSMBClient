package com.gzf01.rxzsmbclient.vm;

import android.view.View;

import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.global.Rxzmvvm;
import com.gzf01.rxzmvvm.utils.Strings;
import com.gzf01.rxzmvvm.vm.BaseViewModel;
import com.gzf01.rxzsmbclient.databinding.AddLinkDataBinding;
import com.gzf01.rxzsmbclient.entity.Link;
import com.gzf01.rxzsmbclient.global.G;
import com.gzf01.rxzsmbclient.view.AddLinkActivityView;

/**
 * Title: AddLink 类 <br/>
 * Description:  <br/>
 * CreateTime: 2020/7/19 <br/>
 *
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
public class AddLinkViewModel extends BaseViewModel<AddLinkDataBinding, AddLinkActivityView> {

    @Override
    public void onInit(Request request) {
        binding.setLinkInit(new Link(null,"","", "",""));
    }

    /**
     * Title: save 方法 <br />
     * Description: 保存
     *
     * @return void
     */
    public void save( View v){
        Link link = binding.getLinkInit();
        if(Strings.isEmpty(link.getAddress())){
            Rxzmvvm.toastShow("地址不能为空!");
            return;
        }
        //如果名称为空，则使用地址作为名称
        if(Strings.isEmpty(link.getName()))
            link.setName(link.getAddress());

        G.daoSession.getLinkDao().insert(link);

        view.returnBy(null);

    }

}
