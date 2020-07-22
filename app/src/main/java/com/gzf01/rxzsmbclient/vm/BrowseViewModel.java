package com.gzf01.rxzsmbclient.vm;

import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.global.Rxzmvvm;
import com.gzf01.rxzmvvm.utils.Strings;
import com.gzf01.rxzmvvm.vm.BaseViewModel;
import com.gzf01.rxzsmbclient.R;
import com.gzf01.rxzsmbclient.databinding.BrowseDataBinding;
import com.gzf01.rxzsmbclient.entity.File;
import com.gzf01.rxzsmbclient.entity.Link;
import com.gzf01.rxzsmbclient.model.api.Complete;
import com.gzf01.rxzsmbclient.model.api.Success;
import com.gzf01.rxzsmbclient.view.BrowseActivityView;
import com.hb.dialog.myDialog.MyImageMsgDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * Title: Browse 类 <br/>
 * Description: 浏览文件 <br/>
 * CreateTime: 2020/7/22 <br/>
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings("deprecation")
public class BrowseViewModel extends BaseViewModel<BrowseDataBinding, BrowseActivityView> {

    private static final String TAG = "BrowseViewModel";

    public static final int REQUEST_LINK_CODE = 1;

    private String baseUrl = "";        //保存根目录地址
    private SmbFile smbFile = null;     //当前目录
    private SmbFile[] subFiles = null;      //当前目录下的子文件、目录

    @Override
    public void onInit(Request request) {
        if(request != null && request.getCode() == REQUEST_LINK_CODE){
            Link link = Rxzmvvm.gson.fromJson(request.getData().get("link"), Link.class);
            binding.setLinkInit(link);


            //组合链接
            StringBuilder baseUrlBuilder = new StringBuilder("smb://");

            //用户名密码
            if(!Strings.isEmpty(link.getUsername())){
                baseUrlBuilder.append(link.getUsername());
                baseUrlBuilder.append(':');
                baseUrlBuilder.append(link.getPassword());
                baseUrlBuilder.append('@');
            }

            //去除多余的/符号，保证最后只有一位/磨耗
            baseUrlBuilder.append(link.getAddress());

            while (baseUrlBuilder.lastIndexOf("/") == baseUrlBuilder.length()-1){
                baseUrlBuilder.deleteCharAt(baseUrlBuilder.length()-1);
            }
            baseUrlBuilder.append('/');

            //保存到属性汇总
            baseUrl = baseUrlBuilder.toString();

            //打开连接中的弹出框
            MyImageMsgDialog myImageMsgDialog = new MyImageMsgDialog(view).builder()
                    .setImageLogo(view.getResources().getDrawable(R.drawable.connect_anim))
                    .setMsg("连接中...");
            ImageView logoImg = myImageMsgDialog.getLogoImg();
            //让动画动起来
            AnimationDrawable connectAnimation = (AnimationDrawable) logoImg.getDrawable();
            connectAnimation.start();
            //不可关闭弹出窗
            myImageMsgDialog.setCancelable(false);
            myImageMsgDialog.show();

            //获取远程目录
            getRemoteDir(baseUrl,null,()->{
                myImageMsgDialog.dismiss();
            });

        }


    }


    /**
     * Title: clickFile 方法 <br />
     * Description: 点击文件或者目录
     *
     * @return void
     */
    public void clickFile(File file){
        //判断是目录还是文件
        if(file.isDir()){
            //点击的返回上一层
            if(file.getIndex() == -1){

                //将当前目录和根目录比较，如果相等则停止返回上一级
                if(!smbFile.getPath().equals(baseUrl))
                    getRemoteDir(smbFile.getParent(),null,null);
                else
                    Rxzmvvm.toastShow("已经是顶级目录了");
            }
            //点击目录进入到子目录
            else {
                getRemoteDir(subFiles[file.getIndex()].getPath(),null,null);
            }
        }
        else {
            //点击文件
            view.runOnUiThread(()->{
                Rxzmvvm.toastShow("点击文件："+file.getName());
            });

        }
    }



    /**
     * Title: getRemoteDir 方法 <br />
     * Description: 获取远程目录
     *
     * @return void
     */
    public void getRemoteDir(String url, Success success, Complete complete){
        //开启链接服务器的线程
        new Thread(()->{
            try {
                //获取到目录本身
                smbFile = new SmbFile(url);

                //判断是否是一个目录
                if(smbFile.isDirectory()){
                    //显示列表
                    ArrayList<File> list = new ArrayList<>();

                    //添加返回上级目录
                    list.add(new File(-1,"../",null,true));

                    //获取当前目录下的文件、目录列表
                    subFiles = smbFile.listFiles();

                    //进行排序，目录在前，文件在后。按名字排序
                    Arrays.sort(subFiles, (sf1, sf2) -> {
                        try {
                            //同为目录或者文件
                            if(sf1.isDirectory() == sf2.isDirectory()){
                                return sf1.getName().compareTo(sf2.getName());
                            }
                            else{
                                //一个是目录一个是文件，则目录在前
                                return sf1.isDirectory()?-1:1;
                            }


                        } catch (SmbException e) {
                            return 0;
                        }
                    });


                    //依次加入显示列表
                    for(int i=0;i<subFiles.length;i++){
                        SmbFile sf = subFiles[i];
                        list.add(new File(i,sf.getName(), new Date(sf.createTime()), sf.isDirectory()));
                    }

                    //更新界面
                    binding.setFileListInit(list);


                    if(success!=null)
                        view.runOnUiThread(()->{
                            success.success();
                        });



                }
                else{
                    throw new Exception("这不是一个目录");
                }




            } catch (Exception e) {
                view.runOnUiThread(()->{
                    Rxzmvvm.toastShow("获取目录失败");
                });
            }
            finally {
                if(complete!=null)
                    view.runOnUiThread(()->{
                        complete.complete();
                    });

            }
        }).start();
    }
}
