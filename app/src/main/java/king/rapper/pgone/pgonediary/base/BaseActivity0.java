package king.rapper.pgone.pgonediary.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import king.rapper.pgone.pgonediary.MyApp;
import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.interfaces.PhotoCallBack;
import king.rapper.pgone.pgonediary.util.CaptureFileUtils;
import king.rapper.pgone.pgonediary.util.DensityUtil;
import king.rapper.pgone.pgonediary.util.LogUtils;
import king.rapper.pgone.pgonediary.util.ToastUtil;
import king.rapper.pgone.pgonediary.view.GlideImageLoader0;

/**
 * Activity  基类
 * 无特殊需求时，使用此BaseActivity
 */
public abstract class BaseActivity0 extends AppCompatActivity {

    private PhotoCallBack interfaces;

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
            initView();
            initData();
            initListener();
        } catch (Exception e) {
            LogUtils.e("BaseActivity.e:" + e.getMessage());
            e.printStackTrace();
        }
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
    protected void initView() throws Exception {
    }

    /**
     * 初始化界面数据
     */
    protected void initData() throws Exception {
    }

    /**
     * 绑定监听器与适配器
     */
    protected void initListener() throws Exception {
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

    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final int REQUEST_CODE_SELECT = 5;
    public static int PERMISSION_REQUEST_CAMERA = 3;//请求 相机 权限请求码
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";

    protected void showPhotoDialog(final int count, PhotoCallBack interfaces) {
        this.interfaces = interfaces;
        final CharSequence[] items = {"相册", "拍照"};
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // 这里item是根据选择的方式，
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            StartCapture();
                        } else {
                            if (item == 0) {
                                ImagePicker imagePicker = ImagePicker.getInstance();
                                if (count <= 1) {
                                    //选择1张图片
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(intent,
                                            PHOTO_REQUEST_GALLERY);
                                } else {
                                    //选择多张图片
                                    imagePicker.setSelectLimit(count);
                                    initImagePicker(imagePicker);
                                    Intent intent1 = new Intent(MyApp.getInstance(), ImageGridActivity.class);
                                    startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                }
                            } else {
                                // 直接调起相机

                                ImagePicker.getInstance().setSelectLimit(count);
                                Intent intent = new Intent(MyApp.getInstance(), ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                                    tempFile = new File(Environment
//                                            .getExternalStorageDirectory(),
//                                            PHOTO_FILE_NAME);
//                                    Uri uri = Uri.fromFile(tempFile);
//                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                                    startActivityForResult(intent,
//                                            PHOTO_REQUEST_CAREMA);
//                            } else{
//                                ToastUtil.showShort(BaseActivity.this, "未找到存储卡，无法存储照片！");
//                            }
                            }

                        }
                    }
                }).create();
        dlg.show();
    }

    private void initImagePicker(ImagePicker imagePicker) {
        imagePicker.setImageLoader(new GlideImageLoader0());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    private void StartCapture() {
        if (Build.VERSION.SDK_INT > 22) {
            //第二个参数是需要申请的权限
           /* ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA_STORAGE);*///fragment中要这样子用才对
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //权限还没有授予，需要在这里写申请权限的代码
                this.requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                //权限已经被授予，在这里直接写要执行的相应方法即可
                StartTakePhoto();
            }
        } else {
            StartTakePhoto();
        }

    }

    void StartTakePhoto() {
        tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
        CaptureFileUtils.startActionCapture(this, tempFile, PHOTO_REQUEST_CAREMA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.e("权限回调2----");
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (ContextCompat.checkSelfPermission(BaseActivity0.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                StartTakePhoto();
            } else {
                ToastUtil.showShort("获取相机权限失败");
            }
        }
    }

    private File tempFile;
    public static final float DISPLAY_WIDTH = 200;
    public static final float DISPLAY_HEIGHT = 200;
    public static final int REQUEST_CODE_PREVIEW = 101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ImageItem> images = null;
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                LogUtils.e("图片路径？？" + data.getData() + "");
                String pic = data.getData().toString();
                String realPathFromUri = CaptureFileUtils.getRealPathFromUri(this, uri);
                LogUtils.e("图片路径？？" + realPathFromUri + "");
//                if (interfaces != null) {
//                    interfaces.success(new File(realPathFromUri));
//                }
                if (realPathFromUri.endsWith(".gif")) {
                    LogUtils.e("图片路径？？" + realPathFromUri + "");
                    if (interfaces != null) {
//                        interfaces.success(new File(realPathFromUri));
                    }
                } else {
//                    crop(uri, 1);
                    // 打开裁剪图片界面
                    CropImage.ActivityBuilder builder = CropImage.activity(uri);
                    // 裁剪配置
                    builder.setMultiTouchEnabled(false)
//                            .setCropShape(com.linchaolong.android.imagepicker.cropper.CropImageView.CropShape.OVAL)
                            .setGuidelines(com.linchaolong.android.imagepicker.cropper.CropImageView.Guidelines.OFF)
                            .setCropShape(com.linchaolong.android.imagepicker.cropper.CropImageView.CropShape.RECTANGLE)
                            .setRequestedSize(960, 540)
                            .setAspectRatio(16, 9);
                    // 启动裁剪界面
                    builder.start(this);
                }
            } else {
                Log.e("radish", "图片NOT ");
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Log.e("radish", "tempFile.getPath()：" + tempFile.getPath());
                if (interfaces != null) {
//                    interfaces.success(tempFile);
                }
//                crop(Uri.fromFile(tempFile), 0);
            } else {
                Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                final Bitmap bitmap = data.getParcelableExtra("data");
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);

                    final byte[] bytes = bStream.toByteArray();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e("Thread.currentThread():" + Thread.currentThread());
                            if (interfaces != null) {
//                                interfaces.success(bytes);
                            }
                        }
                    }, 100);
                }
            }
            try {
                if (tempFile != null && tempFile.exists()) {
                    LogUtils.e("删除图片");
//                    tempFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                LogUtils.e("images.size:" + images.size());
                if (images != null) {
                    // TODO: 2018/7/19
                    for (int i = 0; i < images.size(); i++) {
                        LogUtils.e("images:" + images.get(i).path);
                    }
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    // TODO: 2018/7/19
                    for (int i = 0; i < images.size(); i++) {
                        LogUtils.e("images:" + images.get(i).path);
                    }
                }
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // Uri uriForFile = FileProvider.getUriForFile(GRXXActivity.this, "com.rjwl.reginet.yizhangb.fileProvider", tempFile);
                // crop(Uri.fromFile(tempFile), 0);
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    // TODO: 2018/7/19
                    for (int i = 0; i < images.size(); i++) {
                        LogUtils.e("images:" + images.get(i).path);
                    }
                }

            } else {
                ToastUtil.showShort("未找到存储卡，无法存储照片！");
            }

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            // 裁剪图片回调
            handleCropResult(CropImage.getActivityResult(data));
        }
    }

    /**
     * 裁剪图片结果回调
     */
    private void handleCropResult(com.linchaolong.android.imagepicker.cropper.CropImageView.CropResult result) {
        if (result.getError() == null) {
            Uri cropImageUri = result.getUri();
            String realPathFromUri = CaptureFileUtils.getRealPathFromUri(this, cropImageUri);
            LogUtils.e("图片路径？？" + realPathFromUri + "");
            if (TextUtils.isEmpty(realPathFromUri) && interfaces != null) {
//                interfaces.success(new File(realPathFromUri));
            }
        } else {
            LogUtils.e("handleCropResult error:" + result.getError());
        }
    }

    private void crop(Uri uri, int type) {
        if (type == 0) {
            Uri imageUri;
            Uri outputUri;

            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //通过FileProvider创建一个content类型的Uri
                imageUri = FileProvider.getUriForFile(this, "com.rjwl.reginet.yizhangb.fileProvider", tempFile);
                outputUri = Uri.fromFile(tempFile);
                //outputUri不需要ContentUri,否则失败
                //outputUri = FileProvider.getUriForFile(activity, "com.solux.furniture.fileprovider", new File(crop_image));
            } else {
                imageUri = Uri.fromFile(tempFile);
                outputUri = Uri.fromFile(tempFile);
            }
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("crop", "true");
            //设置宽高比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置裁剪图片宽高
            intent.putExtra("outputX", DensityUtil.dp2px(this, 180));
            intent.putExtra("outputY", DensityUtil.dp2px(this, 320));
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PHOTO_REQUEST_CUT);
        } else {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", DensityUtil.dp2px(this, 180));
            intent.putExtra("outputY", DensityUtil.dp2px(this, 320));
            intent.putExtra("outputFormat", "JPEG");
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PHOTO_REQUEST_CUT);
        }
    }

}