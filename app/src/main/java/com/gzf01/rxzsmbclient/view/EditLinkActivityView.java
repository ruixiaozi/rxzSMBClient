package com.gzf01.rxzsmbclient.view;

import android.os.Bundle;

import com.gzf01.rxzmvvm.view.BaseActivityView;
import com.gzf01.rxzsmbclient.R;
import com.gzf01.rxzsmbclient.databinding.EditLinkDataBinding;
import com.gzf01.rxzsmbclient.vm.EditLinkViewModel;

/**
 * Title: EditLink 类 <br/>
 * Description: 链接编辑 <br/>
 * CreateTime: 2020/7/20 <br/>
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */public class EditLinkActivityView extends BaseActivityView<EditLinkViewModel, EditLinkDataBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.edit_link_layout);
       
    }
}