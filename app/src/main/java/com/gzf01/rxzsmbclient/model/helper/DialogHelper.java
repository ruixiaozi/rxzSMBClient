package com.gzf01.rxzsmbclient.model.helper;

import android.content.Context;

import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.ActionSheetDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;

/**
 * Title: DialogHelper 类 <br/>
 * Description: 弹出框帮组类 <br/>
 * CreateTime: 2020/7/23 <br/>
 *
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
public class DialogHelper {

    /**
     * Title: SubItem 类 <br/>
     * Description: 底部弹出框，选项类 <br/>
     *
     * @version 0.0.1
     * @since 0.0.1
     */
    public static class SubItem{
        public String title;
        public ActionSheetDialog.OnSheetItemClickListener listener;

        public SubItem(String title, ActionSheetDialog.OnSheetItemClickListener listener) {
            this.title = title;
            this.listener = listener;
        }
    }

    /**
     * Title: InputCallBack 接口 <br/>
     * Description: 输入弹出框回调接口 <br/>
     *
     * @version 0.0.1
     * @since 0.0.1
     */
    public static interface InputCallBack{
        void callback(String re);
    }

    /**
     * Title: VerifyCallBack 接口 <br/>
     * Description: 确认回调 <br/>
     *
     * @version 0.0.1
     * @since 0.0.1
     */
    public static interface VerifyCallBack{
        void callback();
    }


    /**
     * Title: bottomDialog 方法 <br />
     * Description: 底部弹出框
     *
     * @return   void
     */
    public static void bottomDialog(Context context,String title,SubItem...items){
        //显示弹出框
        ActionSheetDialog dialog = new ActionSheetDialog(context).builder().setTitle(title);

        for(SubItem item:items){
            dialog.addSheetItem(item.title,null,item.listener);
        }

        dialog.show();
    }


    /**
     * Title: inputDialog 方法 <br />
     * Description: 输入弹出框
     *
     * @return   void
     */
    public static void inputDialog(Context context,String title,String preText,InputCallBack callBack){
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(context).builder()
                .setTitle(title)
                .setEditText(preText);

        myAlertInputDialog
                .setPositiveButton("确认", v12 -> {
                    callBack.callback(myAlertInputDialog.getResult());
                    myAlertInputDialog.dismiss();

                })
                .setNegativeButton("取消", v1 -> myAlertInputDialog.dismiss());

        myAlertInputDialog.show();
    }

    /**
     * Title: verifyDialog 方法 <br />
     * Description: 确认弹出框
     *
     * @return void
     */
    public static void verifyDialog(Context context, String content, VerifyCallBack callBack){
        MyAlertDialog myAlertDialog = new MyAlertDialog(context).builder()
                .setTitle("确认吗？")
                .setMsg(content)
                .setPositiveButton("确认", v -> {callBack.callback();})
                .setNegativeButton("取消", v -> {});
        myAlertDialog.show();
    }

    /**
     * Title: loadDialog 方法 <br />
     * Description: 加载框
     *
     * @return LoadingDialog
     */
    public static LoadingDialog loadDialog(Context context,String content){
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.setMessage(content);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        return loadingDialog;
    }

}
