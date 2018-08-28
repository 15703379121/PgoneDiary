package king.rapper.pgone.pgonediary.base;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import king.rapper.pgone.pgonediary.MyApp;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.interfaces.PhotoCallBack;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.view.GlideImageLoader0;

/**
 * Activity  基类
 * 无特殊需求时，使用此BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {

    private PhotoCallBack interfaces;
    private ArrayList<Uri> imageList;
    private Activity childActivity;


    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ActivityCollector.addActivity(this);
        LogUtils.e(getClass().getName() + "-----------onCreate");
        //得到布局文件
        setContentView(getLayoutId());

        try {
            Unbinder bind = ButterKnife.bind(this);
            getPreIntent();
        } catch (Exception e) {
            LogUtils.e("BaseActivity.e:" + e.getMessage());
            e.printStackTrace();
        }
        initView();
        initListener();
        initData();
        initViewTheme();

    }

    protected void initViewTheme()/* throws Exception*/ {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    /**
     * @return 布局文件id
     */
    public abstract int getLayoutId();

    /**
     * 获取上一个页面传递来的intent数据
     */
    protected void getPreIntent() throws Exception {
    }

    /**
     * 初始化View
     */
    protected void initView() {
    }

    /**
     * 初始化界面数据
     */
    protected void initData() /*throws Exception */ {
    }

    /**
     * 绑定监听器与适配器
     */
    protected void initListener() {
    }

    /**
     * 初始化标题栏
     *
     * @param title
     */
    protected void initTitleBar(String title) throws Exception {
        TextView tvTitle = (TextView) findViewById(R.id.title_bar_title);
        if (tvTitle == null) return;
        tvTitle.setText(title);
        ImageView ivBack = findViewById(R.id.title_bar_back_iv);
        if (ivBack == null) return;
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void initTitleBar(String title, String right, final TitleListener listener) throws Exception {
        initTitleBar(title);

        TextView tvRight = (TextView) findViewById(R.id.layout_title_bar_right_text);
        if (tvRight == null) return;
        tvRight.setText(right);
        if (right == null) return;
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.RightClick();
            }
        });
    }

    /**
     * 带分享按钮的 标题栏初始化
     *
     * @param title
     * @param listener
     */
    protected void initTitleBar(String title, final TitleListener listener) throws Exception {
        initTitleBar(title);

        ImageView ivShare = (ImageView) findViewById(R.id.layout_title_bar_share);
        if (ivShare == null) return;
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.RightClick();
            }
        });
    }

    private static final int REQUEST_CODE_SELECT = 5;

    private king.rapper.pgone.pgonediary.view.ImagePicker imagePicker = new king.rapper.pgone.pgonediary.view.ImagePicker();

    protected void showPhotoDialog(Activity childActivity, final int count, PhotoCallBack interfaces) {
        showPhotoDialog(childActivity, count, 0, 0, 0, 0, interfaces);
    }

    protected void showPhotoDialog(Activity childActivity, final int count, final int reqWidth, final int reqHeight, final int aspectRatioX, final int aspectRatioY, final PhotoCallBack interfaces) {
        this.childActivity = childActivity;
        this.interfaces = interfaces;
        checkMyPermission();
        CharSequence[] items = null;
        if (count <= 1) {
            items = new CharSequence[]{"相册", "拍照"};
        } else {

            items = new CharSequence[]{"相册", "拍照", "批量选择"};
        }
        checkMyPermission();
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // 从相册中选取图片    选择1张图片
                            king.rapper.pgone.pgonediary.view.ImagePicker.Callback callback = callBackImagePicker(reqWidth, reqHeight, aspectRatioX, aspectRatioY);
                            imagePicker.startGallery(BaseActivity.this.childActivity, callback);
                        } else if (item == 1) {
                            // 拍照
                            king.rapper.pgone.pgonediary.view.ImagePicker.Callback callback = callBackImagePicker(reqWidth, reqHeight, aspectRatioX, aspectRatioY);
                            imagePicker.startCamera(BaseActivity.this.childActivity, callback);
                        } else if (item == 2) {
                            // 从相册中选取图片    选择多张图片
                            ImagePicker imagePicker = ImagePicker.getInstance();
                            imagePicker.setSelectLimit(count);
                            initImagePicker(imagePicker, reqWidth, reqHeight, aspectRatioX, aspectRatioY);
                            Intent intent1 = new Intent(MyApp.getInstance(), ImageGridActivity.class);
                            startActivityForResult(intent1, REQUEST_CODE_SELECT);
                        }
                    }
                }).create();
        dlg.show();
    }

    private king.rapper.pgone.pgonediary.view.ImagePicker.Callback callBackImagePicker(final int reqWidth, final int reqHeight, final int aspectRatioX, final int aspectRatioY) {
        // 回调
        return new king.rapper.pgone.pgonediary.view.ImagePicker.Callback() {
            @Override
            public void onPickImage(Uri imageUri) {
                //选择图片回调
            }

            @Override
            public void onCropImage(Uri imageUri) {
                //剪裁图片回调
                if (imageList == null)
                    imageList = new ArrayList<>();
                imageList.clear();
                imageList.add(imageUri);
                if (interfaces != null) interfaces.success(imageList);
            }

            // 自定义裁剪配置
            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                int width = 540, height = 960, ratioX = 9, ratioY = 16;
                if (reqWidth > 0) width = reqWidth;
                if (reqHeight > 0) height = reqHeight;
                if (aspectRatioX > 0) ratioX = aspectRatioX;
                if (aspectRatioY > 0) ratioY = aspectRatioY;
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(com.linchaolong.android.imagepicker.cropper.CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(com.linchaolong.android.imagepicker.cropper.CropImageView.CropShape.RECTANGLE)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(width, height)
                        // 宽高比
                        .setAspectRatio(ratioX, ratioY);
            }

            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
                ToastUtil.showShort("获取相机权限失败");
            }
        };
    }

    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {     //API 19
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight(); //earlier version
        }
    }

    private void checkMyPermission() {
        if (Build.VERSION.SDK_INT >= 22) {
            List<String> permissionList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (!permissionList.isEmpty()) {  //申请的集合不为空时，表示有需要申请的权限
                ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
            } else { //所有的权限都已经授权过了

            }
        }
    }


    private void initImagePicker(ImagePicker imagePicker, int reqWidth, int reqHeight, int aspectRatioX, int aspectRatioY) {
        imagePicker.setImageLoader(new GlideImageLoader0());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        if (reqWidth > 0)
            imagePicker.setFocusWidth(reqWidth);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        if (reqHeight > 0)
            imagePicker.setFocusHeight(reqHeight);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        if (aspectRatioX > 0)
            imagePicker.setOutPutX(aspectRatioX);//保存文件的宽度。单位像素
        if (aspectRatioY > 0)
            imagePicker.setOutPutY(aspectRatioY);//保存文件的高度。单位像素
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                List<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                LogUtils.e("images.size:" + images.size());
                if (images != null && images.size() > 0) {
                    if (imageList == null)
                        imageList = new ArrayList<>();
                    imageList.clear();
                    for (int i = 0; i < images.size(); i++) {
                        imageList.add(Uri.parse(images.get(i).path));
                    }
                    if (interfaces != null) {
                        interfaces.success(imageList);
                    }
                }
            }
        } else {
            //单张图片返回
            imagePicker.onActivityResult(BaseActivity.this.childActivity, requestCode, resultCode, data);
        }
    }

}