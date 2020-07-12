package com.gzf01.rxzsmbclient.view;

import android.os.Bundle;

import com.gzf01.rxzmvvm.view.BaseActivityView;
import com.gzf01.rxzmvvm.view.adapter.MyRecycleAdapter;
import com.gzf01.rxzsmbclient.BR;
import com.gzf01.rxzsmbclient.R;
import com.gzf01.rxzsmbclient.databinding.MainDataBinding;
import com.gzf01.rxzsmbclient.vm.MainViewModel;

/**
 * Title: Main 类 <br/>
 * Description: 首页 <br/>
 * CreateTime: 2020/7/12 <br/>
 *
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
public class MainActivityView extends BaseActivityView<MainViewModel, MainDataBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.main_layout);
        binding.setLinkAdapter(new MyRecycleAdapter(viewModel,R.layout.item_link, BR.itemLink,BR.itemLinkParentViewModel));
    }
}