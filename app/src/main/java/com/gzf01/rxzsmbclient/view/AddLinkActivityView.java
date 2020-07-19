package com.gzf01.rxzsmbclient.view;

import android.os.Bundle;

import com.gzf01.rxzmvvm.view.BaseActivityView;
import com.gzf01.rxzsmbclient.R;
import com.gzf01.rxzsmbclient.databinding.AddLinkDataBinding;
import com.gzf01.rxzsmbclient.vm.AddLinkViewModel;

/**
 * Title: AddLink 类 <br/>
 * Description: 添加连接 <br/>
 * CreateTime: 2020/7/19 <br/>
 *
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
public class AddLinkActivityView extends BaseActivityView<AddLinkViewModel, AddLinkDataBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.add_link_layout);

    }
}