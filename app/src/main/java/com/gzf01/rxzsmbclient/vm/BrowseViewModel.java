package com.gzf01.rxzsmbclient.vm;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.entity.Result;
import com.gzf01.rxzmvvm.global.Rxzmvvm;
import com.gzf01.rxzmvvm.utils.Strings;
import com.gzf01.rxzmvvm.vm.BaseViewModel;
import com.gzf01.rxzsmbclient.R;
import com.gzf01.rxzsmbclient.databinding.BrowseDataBinding;
import com.gzf01.rxzsmbclient.entity.File;
import com.gzf01.rxzsmbclient.entity.Link;
import com.gzf01.rxzsmbclient.model.api.Complete;
import com.gzf01.rxzsmbclient.model.api.Success;
import com.gzf01.rxzsmbclient.model.helper.DialogHelper;
import com.gzf01.rxzsmbclient.view.BrowseActivityView;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyImageMsgDialog;
import com.litesuits.common.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

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
    public static final int REQUEST_FILE_PICKER = 10;

    private String baseUrl = "";        //保存根目录地址
    private SmbFile smbFile = null;     //当前目录
    private SmbFile[] subFiles = null;      //当前目录下的子文件、目录


    private Date preBackDate = new Date();

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


    @Override
    public boolean onBack() {
        Date nowDate = new Date();
        if(nowDate.getTime()-preBackDate.getTime()<=500){
            return false;
        }
        preBackDate = nowDate;
        Rxzmvvm.toastShow("按两次返回断开连接");

        return true;
    }

    /**
     * Title: add 方法 <br />
     * Description: 添加文件夹
     *
     * @return void
     */
    public void add( View v){

        //弹出框
        DialogHelper.bottomDialog(view,smbFile.getName(),
                new DialogHelper.SubItem("新建文件夹",(w1)->{
                    //弹出输入框
                    DialogHelper.inputDialog(view,"新建文件夹","输入文件夹名称",(re)->{
                        //弹出创建进度框
                        LoadingDialog loadingDialog = DialogHelper.loadDialog(view,"创建中...");
                        new Thread(()->{
                            try {
                                //验证输入
                                if(Strings.isEmpty(re) || !re.matches("[^/\\\\]+")){
                                    throw new Exception("输入错误");
                                }
                                //创建文件夹
                                SmbFile newDir = new SmbFile(smbFile.getPath()+re.trim());
                                newDir.mkdirs();
                                newDir.close();
                                //重新获取目录
                                getRemoteDir(smbFile.getPath(),null,null);
                                view.runOnUiThread(()->Rxzmvvm.toastShow("创建成功"));

                            } catch (Exception e) {
                                view.runOnUiThread(()->Rxzmvvm.toastShow("创建失败"));
                            }
                            finally {
                                loadingDialog.dismiss();
                            }

                        }).start();
                    });
                }),
                new DialogHelper.SubItem("上传文件",(w2)->{
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    view.startActivityForResult(intent, REQUEST_FILE_PICKER);

                }));


    }

    @Override
    public void onResult(Result result) {
        if(result == null)
            return;

        switch (result.getCode()){
            case REQUEST_FILE_PICKER:
                String path = result.getData().get("path");
                Log.i("TAG", "onResult: "+path);
                //弹出上传进度框
                LoadingDialog loadingDialog = DialogHelper.loadDialog(view,"上传中...");
                //请求上传
                new Thread(()->{
                    String fileName = path.substring(path.lastIndexOf("/")+1);
                    try (SmbFile remoteFile = new SmbFile( smbFile.getPath()+fileName);
                        FileInputStream fi = new FileInputStream(new java.io.File(path));
                        SmbFileOutputStream fo = new SmbFileOutputStream(remoteFile)){


                        IOUtils.copy(fi,fo);

                        getRemoteDir(smbFile.getPath(),null,null);

                        view.runOnUiThread(()->Rxzmvvm.toastShow("上传成功"));
                    } catch (Exception e) {
                        view.runOnUiThread(()->Rxzmvvm.toastShow("上传失败"));
                    }
                    finally {
                        loadingDialog.dismiss();
                    }
                }).start();




                break;
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
                if(!smbFile.getPath().equals(baseUrl)){
                    //弹出加载进度框
                    LoadingDialog loadingDialog = DialogHelper.loadDialog(view,"加载中...");
                    getRemoteDir(smbFile.getParent(),null,()->{
                        loadingDialog.dismiss();
                    });
                }
                else
                    Rxzmvvm.toastShow("已经是顶级目录了");
            }
            //点击目录进入到子目录
            else {
                //弹出加载进度框
                LoadingDialog loadingDialog = DialogHelper.loadDialog(view,"加载中...");
                getRemoteDir(subFiles[file.getIndex()].getPath(),null,()->{
                    loadingDialog.dismiss();
                });
            }
        }
        else {
            //点击文件,弹出选项框
            DialogHelper.bottomDialog(view,file.getName(),
                    new DialogHelper.SubItem("下载",(w1)->{


                        //弹出下载进度框
                        LoadingDialog loadingDialog = DialogHelper.loadDialog(view,"下载中...");

                        java.io.File myRoot  = new java.io.File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"smbClient");
                        if(!myRoot.exists()) {
                            boolean re = myRoot.mkdirs();
                            System.out.println(re);
                        }


                        new Thread(()->{
                            try(SmbFileInputStream fi = new SmbFileInputStream(subFiles[file.getIndex()]);
                                FileOutputStream fo = new FileOutputStream(new java.io.File(myRoot,file.getName()))){

                                IOUtils.copy(fi,fo);
                                subFiles[file.getIndex()].close();
                                view.runOnUiThread(()->Rxzmvvm.toastShow("下载成功:"+myRoot.getPath()+file.getName()));
                            }catch (Exception e){

                                view.runOnUiThread(()->Rxzmvvm.toastShow("下载失败"));
                            }
                            finally {
                                loadingDialog.dismiss();
                            }
                        }).start();



                    }),
                    new DialogHelper.SubItem("删除",(w1)->{
                        //打开确认弹出
                        DialogHelper.verifyDialog(view,"删除 "+file.getName(),()->{
                            //弹出删除进度框
                            LoadingDialog loadingDialog = DialogHelper.loadDialog(view,"删除中...");
                            new Thread(()->{
                                try {
                                    subFiles[file.getIndex()].delete();
                                    getRemoteDir(smbFile.getPath(),null,null);
                                    view.runOnUiThread(()->Rxzmvvm.toastShow("删除成功"));
                                } catch (SmbException e) {
                                    view.runOnUiThread(()->Rxzmvvm.toastShow("删除失败"));
                                }
                                finally {
                                    loadingDialog.dismiss();
                                }
                            }).start();
                        });


                    }));


        }
    }

    /**
     * Title: longClickFile 方法 <br />
     * Description: 长按文件
     *
     * @return boolean
     */
    public boolean longClickFile( File file){
        if(!file.isDir() || file.getIndex()==-1){
            return false;//如果是文件或者返回上一级，则直接返回，让单击事件进行处理
        }

        //显示选择弹出框
        DialogHelper.bottomDialog(view,file.getName(),
                new DialogHelper.SubItem("删除",(w1)->{
                    //显示确认弹出框
                    DialogHelper.verifyDialog(view,"删除 "+file.getName(),()->{
                        //弹出删除进度框
                        LoadingDialog loadingDialog = DialogHelper.loadDialog(view,"删除中...");
                        new Thread(()->{
                            try {
                                subFiles[file.getIndex()].delete();
                                getRemoteDir(smbFile.getPath(),null,null);
                                view.runOnUiThread(()->Rxzmvvm.toastShow("删除成功"));
                            } catch (SmbException e) {
                                view.runOnUiThread(()->Rxzmvvm.toastShow("删除失败"));
                            }
                            finally {
                                loadingDialog.dismiss();
                            }
                        }).start();
                    });

                }));



        Log.i(TAG, "longClickFile: 长按"+file.getName());
        return true;
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
                Log.i(TAG, "getRemoteDir: "+url);

                //获取到目录本身
                smbFile = new SmbFile(url);


                //判断是否是一个目录
                if(smbFile.isDirectory()){
                    //显示列表
                    ArrayList<File> list = new ArrayList<>();

                    //添加返回上级目录
                    list.add(new File(-1,"../",null,true));

                    //获取当前目录下的文件、目录列表(仅存在的)
                    subFiles = Arrays.stream(smbFile.listFiles())
                            .filter((e)->{
                                try {
                                    if(e.exists())
                                        return true;
                                    else
                                        return false;
                                } catch (SmbException ex) {
                                    return false;
                                }
                            })
                            .sorted((sf1, sf2) -> {
                                //进行排序，目录在前，文件在后。按名字排序
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
                            })
                            .toArray(SmbFile[]::new);




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
