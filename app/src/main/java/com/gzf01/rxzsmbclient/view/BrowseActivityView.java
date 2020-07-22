package com.gzf01.rxzsmbclient.view;

import android.os.Bundle;

import com.gzf01.rxzmvvm.view.BaseActivityView;
import com.gzf01.rxzmvvm.view.adapter.MyRecycleAdapter;
import com.gzf01.rxzsmbclient.BR;
import com.gzf01.rxzsmbclient.R;
import com.gzf01.rxzsmbclient.databinding.BrowseDataBinding;
import com.gzf01.rxzsmbclient.vm.BrowseViewModel;

/**
 * Title: Browse 类 <br/>
 * Description: 浏览文件 <br/>
 * CreateTime: 2020/7/22 <br/>
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */public class BrowseActivityView extends BaseActivityView<BrowseViewModel, BrowseDataBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.browse_layout);
        binding.setFileAdapter(new MyRecycleAdapter(viewModel,R.layout.item_file, BR.itemFile,BR.itemFileParentViewModel));
    }
}