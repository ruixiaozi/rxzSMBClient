package com.gzf01.rxzsmbclient.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gzf01.rxzmvvm.entity.Result;
import com.gzf01.rxzmvvm.view.BaseActivityView;
import com.gzf01.rxzmvvm.view.adapter.MyRecycleAdapter;
import com.gzf01.rxzsmbclient.BR;
import com.gzf01.rxzsmbclient.R;
import com.gzf01.rxzsmbclient.databinding.BrowseDataBinding;
import com.gzf01.rxzsmbclient.model.helper.MyPathUtils;
import com.gzf01.rxzsmbclient.vm.BrowseViewModel;

import java.util.LinkedHashMap;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Result result = new Result();
            result.setCode(requestCode);
            switch (requestCode){
                case BrowseViewModel.REQUEST_FILE_PICKER:

                    LinkedHashMap<String,String> rMap = new LinkedHashMap<>();
                    Uri uri = data.getData();
                    rMap.put("path", MyPathUtils.getFilePathForN(uri,this));

                    result.setData(rMap);
                    break;
            }

            viewModel.onResult(result);
        }
    }
}