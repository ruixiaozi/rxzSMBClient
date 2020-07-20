package com.gzf01.rxzsmbclient.vm;

import android.view.View;

import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.global.Rxzmvvm;
import com.gzf01.rxzmvvm.utils.Strings;
import com.gzf01.rxzmvvm.vm.BaseViewModel;
import com.gzf01.rxzsmbclient.databinding.EditLinkDataBinding;
import com.gzf01.rxzsmbclient.entity.Link;
import com.gzf01.rxzsmbclient.global.G;
import com.gzf01.rxzsmbclient.view.EditLinkActivityView;

/**
 * Title: EditLink 类 <br/>
 * Description: 链接编辑 <br/>
 * CreateTime: 2020/7/20 <br/>
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */public class EditLinkViewModel extends BaseViewModel<EditLinkDataBinding, EditLinkActivityView> {

      public static final int EDIT_REQUEST_CODE = 1;

     @Override
     public void onInit(Request request) {
          if(request!=null && request.getCode()==EDIT_REQUEST_CODE){
               //获取传过来的link对象
               binding.setLinkInit(Rxzmvvm.gson.fromJson(request.getData().get("link"), Link.class));
          }
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

          G.daoSession.getLinkDao().update(link);

          Rxzmvvm.toastShow("保存成功");

          view.returnBy(null);
     }

     /**
      * Title: delete 方法 <br />
      * Description: 删除
      *
      * @return void
      */
     public void delete( View v){
          G.daoSession.getLinkDao().deleteByKey(binding.getLinkInit().getId());

          Rxzmvvm.toastShow("删除成功");

          view.returnBy(null);
     }
}
