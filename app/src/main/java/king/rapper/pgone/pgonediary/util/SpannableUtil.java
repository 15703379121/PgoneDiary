package king.rapper.pgone.pgonediary.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import king.rapper.pgone.pgonediary.R;
import king.rapper.pgone.pgonediary.entity.ContentFormatJson;

/**
 * @ Create_time: 2018/7/17 on 9:46.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class SpannableUtil {

    private final EditText etText;
    private SpannableStringBuilder spannableString;
    private StyleSpan styleSpan;

    public SpannableUtil(EditText etText, String content) {
        this.etText = etText;
        spannableString = new SpannableStringBuilder(content);
    }

    public SpannableUtil(EditText etText) {
        this.etText = etText;
    }

    public void setSpannerString(String content) {
//        spannableString.replace
        spannableString = new SpannableStringBuilder(content);
    }

    public SpannableStringBuilder getSpannable() {
        return spannableString;
    }

    public void setSpannableString(SpannableStringBuilder spannableString) {
        this.spannableString = spannableString;
    }

    public String getSpannableString() {
        return spannableString.toString();
    }

    /**
     * 上标
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatSuperscriptSpan(Integer beginPosition, Integer endPosition) {
        //上标
        if (spannableString == null)
            return;
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        spannableString.setSpan(superscriptSpan, beginPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//上标
        etText.setText(spannableString);
    }

    /**
     * 下标
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatSubscriptSpan(Integer beginPosition, Integer endPosition) {
        //下标
        if (spannableString == null)
            return;
        Object[] spans = spannableString.getSpans(beginPosition, endPosition, Object.class);
        for (Object object : spans) {
            if (object instanceof SubscriptSpan) {

            }
        }
        SubscriptSpan subscriptSpan = new SubscriptSpan();
        spannableString.setSpan(subscriptSpan, beginPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//下标
        etText.setText(spannableString);
    }


    /**
     * 前景色
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatFrontColorSpan(String color, Integer beginPosition, Integer endPosition) {
        //前景色
        if (spannableString == null)
            return;
        ForegroundColorSpan frontColorSpan = new ForegroundColorSpan(Color.parseColor(color));
        spannableString.setSpan(frontColorSpan, beginPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//前景色
        etText.setText(spannableString);
    }


    /**
     * 背景色
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatBackgroundColorSpan(String color, Integer beginPosition, Integer endPosition) {
        //背景色
        if (spannableString == null || etText == null)
            return;
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor(color));
        spannableString.setSpan(backgroundColorSpan, beginPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//背景色
        etText.setText(spannableString);
    }

    /**
     * 加粗或斜体
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatStyleBISpan(Integer type, Integer beginPosition, Integer endPosition) {
        //加粗或倾斜
        if (spannableString == null || etText == null)
            return;
        getFormatStyleSpan(type, beginPosition, endPosition);
    }

    /**
     * 添加图片
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatImageSpan(String url, Integer beginPosition, Integer endPosition) {
        if (spannableString == null || etText == null)
            return;
        Drawable drawable = Drawable.createFromPath(url);
        drawable.setBounds(0, 0, 42, 42);
        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableString.setSpan(imageSpan, beginPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//表情
    }

    /**
     * 标注
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatClickSpan(String text, Integer beginPosition, Integer endPosition) {
        if (spannableString == null || etText == null)
            return;
        MyClickableSpan clickableSpan = new MyClickableSpan(text);
        spannableString.setSpan(clickableSpan, beginPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//标注
    }


    /**
     * 超链接
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatURLSpan(String url, Integer beginPosition, Integer endPosition) {
        if (spannableString == null || etText == null)
            return;
        URLSpan urlSpan = new URLSpan(url);
        spannableString.setSpan(urlSpan, beginPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//标注
    }


    /**
     * 字号
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatFontSizeSpan(int size, Integer beginPosition, Integer endPosition) {
        if (spannableString == null || etText == null)
            return;
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size);//字号
        spannableString.setSpan(sizeSpan, beginPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//字号
    }


    /**
     * 删除
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatUnderlineSpan(Integer beginPosition, Integer endPosition) {
        if (spannableString == null || etText == null)
            return;
        UnderlineSpan underlineSpan = new UnderlineSpan();//下划线
        spannableString.setSpan(underlineSpan, beginPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//下划线
    }


    /**
     * 删除
     *
     * @param beginPosition
     * @param endPosition
     */
    public void formatStrikethroughSpan(Integer beginPosition, Integer endPosition) {
        if (spannableString == null || etText == null)
            return;
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();//删除
        spannableString.setSpan(strikethroughSpan, beginPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//删除
    }

    public void formatSpannable(Context context) {
        if (spannableString == null)
            spannableString = new SpannableStringBuilder("这是一段[测试]文字，有图有文啥啥啥都有");

        StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);//加粗
        StyleSpan styleSpan_I = new StyleSpan(Typeface.ITALIC);//斜体
        StyleSpan styleSpan_normal = new StyleSpan(Typeface.NORMAL);//啥都没有
        StyleSpan styleSpan_both = new StyleSpan(Typeface.BOLD_ITALIC);//啥都有
        styleSpan_both.getStyle();//获取当前样式


        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));//前景色
        BackgroundColorSpan colorBackSpan = new BackgroundColorSpan(Color.parseColor("#AC00FF30"));//背景色

        //表情
        Drawable drawable = context.getResources().getDrawable(R.mipmap.weather0);
        drawable.setBounds(0, 0, 42, 42);
        ImageSpan imageSpan = new ImageSpan(drawable);

        //标注
        MyClickableSpan clickableSpan = new MyClickableSpan("http://www.baidu.com");

        RelativeSizeSpan sizeSpan04 = new RelativeSizeSpan(1.8f);//字号
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();//删除
        UnderlineSpan underlineSpan = new UnderlineSpan();//下划线
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();//上标

        spannableString.setSpan(styleSpan_B, 1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//加粗
        spannableString.setSpan(styleSpan_I, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//斜体
        spannableString.setSpan(colorSpan, 3, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//前景色
        spannableString.setSpan(colorBackSpan, 1, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//背景色
        spannableString.setSpan(imageSpan, 4, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//表情
        spannableString.setSpan(clickableSpan, 8, 11, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//标注
        spannableString.setSpan(sizeSpan04, 11, 13, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//字号
        spannableString.setSpan(strikethroughSpan, 11, 15, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//删除
        spannableString.setSpan(underlineSpan, 11, 17, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//下划线

        etText.setMovementMethod(LinkMovementMethod.getInstance());
        etText.setHighlightColor(Color.BLUE);//点击后的颜色
        etText.setText(spannableString);
    }

    public class MyClickableSpan extends ClickableSpan {

        private String content;

        public MyClickableSpan(String content) {
            this.content = content;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
//            ds.setUnderlineText(false);
            ds.setColor(Color.RED);
        }

        @Override
        public void onClick(View widget) {
//            Uri uri = Uri.parse(content);
//            Context context = widget.getContext();
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
//            try {
//                context.startActivity(intent);
//            } catch (ActivityNotFoundException e) {
//                Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
//            }
        }
    }

    public void saveFormat() {
//        BackgroundColorSpan preBackgroundColorSpan = null;
//        ForegroundColorSpan preForegroundColorSpan = null;
//        AbsoluteSizeSpan preAbsoluteSizeSpan = null;
//        StyleSpan preStyleSpan = null;
//        ImageSpan preImageSpan = null;
//        MyClickableSpan preMyClickableSpan = null;
        URLSpan preURLSpan = null;
        int startUrlSpan = -1;
        int endUrlSpan = -1;


        ContentFormatJson contentFormatJson = new ContentFormatJson();
        List<ContentFormatJson.FormatDataBean> fontTheme = new ArrayList<>();
        for (int i = 0; i < spannableString.toString().length(); i++) {
            Object[] spans = spannableString.getSpans(i, i + 1, Object.class);
            for (Object object : spans) {
                if (object instanceof BackgroundColorSpan) {
                    LogUtils.e("背景色：" + i);
                    int backgroundColor = ((BackgroundColorSpan) object).getBackgroundColor();
                    LogUtils.e("背景色：" + backgroundColor);
                    LogUtils.e("背景色：" + ColorUtil.int2Hex(backgroundColor));
                } else if (object instanceof ForegroundColorSpan) {
                    LogUtils.e("前景色：" + i);
                    int backgroundColor = ((ForegroundColorSpan) object).getForegroundColor();
                    LogUtils.e("前景色：" + backgroundColor);
                    LogUtils.e("前景色：" + ColorUtil.int2Hex(backgroundColor));
                } else if (object instanceof AbsoluteSizeSpan) {
                    int size = ((AbsoluteSizeSpan) object).getSize();
                    LogUtils.e("字体大小" + size);
                } else if (object instanceof StyleSpan) {
                    int style = ((StyleSpan) object).getStyle();
                    LogUtils.e("字体加粗或倾斜" + style);
                } else if (object instanceof ImageSpan) {
                    Drawable drawable = ((ImageSpan) object).getDrawable();
                    LogUtils.e("插入的图片" + drawable);
                } else if (object instanceof MyClickableSpan) {
                    String content = ((MyClickableSpan) object).content;
                    CharacterStyle underlying = ((MyClickableSpan) object).getUnderlying();
                    LogUtils.e("标注" + underlying + "  content:" + content);
                } else if (object instanceof URLSpan) {
                    if (preURLSpan != null) {
                        //有记录
                        int preSpanTypeId = preURLSpan.getSpanTypeId();
                        int spanTypeId = ((URLSpan) object).getSpanTypeId();
                        LogUtils.e("preSpanTypeId == spanTypeId:" + preSpanTypeId + "    " + spanTypeId);
                        if (preSpanTypeId != spanTypeId) {
                            //新记录
                            LogUtils.e("startUrlSpan, endUrlSpan:" + startUrlSpan + "," + endUrlSpan);
                            ContentFormatJson.FormatDataBean formatDataBean = new ContentFormatJson.FormatDataBean();
                            formatDataBean.setStartPosition(startUrlSpan);
                            formatDataBean.setEndPosition(endUrlSpan);
                            formatDataBean.setTheme("URLSpan");
                            String url = preURLSpan.getURL();
                            if (!TextUtils.isEmpty(url))
                                formatDataBean.setUrl(url);
                            formatDataBean.setContent(spannableString.toString().substring(startUrlSpan, endUrlSpan));
                            fontTheme.add(formatDataBean);
                            String content = ((URLSpan) object).getURL();
                            CharacterStyle underlying = preURLSpan.getUnderlying();
                            LogUtils.e("超链接" + underlying + "  content:" + content);
                            preURLSpan = (URLSpan) object;
                            startUrlSpan = i;
                            endUrlSpan = i + 1;
                        } else {
                            endUrlSpan = i + 1;
                        }
                    } else {
                        //无记录
                        preURLSpan = (URLSpan) object;
                        startUrlSpan = i;
                        endUrlSpan = i + 1;
                    }
                }
            }
        }

        LogUtils.e("preStyleSpan:" + preURLSpan + "   startUrlSpan:" + startUrlSpan + "  endUrlSpan:" + endUrlSpan);
        if (preURLSpan != null && endUrlSpan > startUrlSpan) {
            ContentFormatJson.FormatDataBean formatDataBean = new ContentFormatJson.FormatDataBean();
            formatDataBean.setStartPosition(startUrlSpan);
            formatDataBean.setEndPosition(endUrlSpan);
            formatDataBean.setTheme("URLSpan");
            String url = preURLSpan.getURL();
            if (!TextUtils.isEmpty(url))
                formatDataBean.setUrl(url);
            formatDataBean.setContent(spannableString.toString().substring(startUrlSpan, endUrlSpan));
            LogUtils.e("formatDataBean:" + formatDataBean);
            fontTheme.add(formatDataBean);
        }
        LogUtils.e("fontTheme:" + fontTheme);
        contentFormatJson.setFontTheme(fontTheme);
        LogUtils.e("contentFormatJson:" + contentFormatJson);
    }

    public void getFormatStyleSpan(Integer type, Integer startPosition, Integer endPosition) {
        Object[] spans = spannableString.getSpans(startPosition, endPosition, Object.class);
        int style = Typeface.NORMAL;
        for (Object object : spans) {
            if (object instanceof StyleSpan) {
                style = ((StyleSpan) object).getStyle();
                switch (style) {
                    case Typeface.NORMAL:
                        //既无加粗又无倾斜
                        if (type == Typeface.BOLD) {
                            styleSpan = new StyleSpan(Typeface.BOLD);
                        } else if (type == Typeface.ITALIC) {
                            styleSpan = new StyleSpan(Typeface.ITALIC);
                        }
                        break;
                    case Typeface.BOLD:
                        //加粗
                        if (type == Typeface.BOLD) {
                            styleSpan = new StyleSpan(Typeface.NORMAL);
                        } else if (type == Typeface.ITALIC) {
                            styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
                        }
                        break;
                    case Typeface.ITALIC:
                        //倾斜
                        if (type == Typeface.BOLD) {
                            styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
                        } else if (type == Typeface.ITALIC) {
                            styleSpan = new StyleSpan(Typeface.NORMAL);
                        }
                        break;
                    case Typeface.BOLD_ITALIC:
                        //既是加粗又是倾斜
                        if (type == Typeface.BOLD) {
                            styleSpan = new StyleSpan(Typeface.ITALIC);
                        } else if (type == Typeface.ITALIC) {
                            styleSpan = new StyleSpan(Typeface.BOLD);
                        }
                        break;
                    default:
                        break;
                }
                break;
            }
        }
        if (style == Typeface.NORMAL) {
            if (type == Typeface.BOLD) {
                styleSpan = new StyleSpan(Typeface.BOLD);
            } else if (type == Typeface.ITALIC) {
                styleSpan = new StyleSpan(Typeface.ITALIC);
            }
        }
        spannableString.setSpan(styleSpan, startPosition, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//加粗
        etText.setText(spannableString);
    }
}
