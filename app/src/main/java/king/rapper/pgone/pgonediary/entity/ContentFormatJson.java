package king.rapper.pgone.pgonediary.entity;

import java.util.List;

/**
 * @ Create_time: 2018/7/17 on 8:54.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class ContentFormatJson {

    private Integer code;
    private String message;
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    private List<FormatDataBean> FontTheme;

    public List<FormatDataBean> getFontTheme() {
        return FontTheme;
    }

    public void setFontTheme(List<FormatDataBean> fontTheme) {
        FontTheme = fontTheme;
    }

    @Override
    public String toString() {
        return "ContentFormatJson{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", FontTheme=" + FontTheme +
                '}';
    }

    public static class FormatDataBean {
        private String content;
        private Integer startPosition;
        private Integer endPosition;
        private String type;
        private String theme;
        private String url;

        @Override
        public String toString() {
            return "FormatDataBean{" +
                    "content='" + content + '\'' +
                    ", startPosition=" + startPosition +
                    ", endPosition=" + endPosition +
                    ", type='" + type + '\'' +
                    ", theme='" + theme + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getStartPosition() {
            return startPosition;
        }

        public void setStartPosition(Integer startPosition) {
            this.startPosition = startPosition;
        }

        public Integer getEndPosition() {
            return endPosition;
        }

        public void setEndPosition(Integer endPosition) {
            this.endPosition = endPosition;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class DataBean {
        private Long id;
        private String title;
        private Long date;
        private String mood;
        private String weather;

        private List<FormatDataBean> spanSuperscriptList;//上标
        private List<FormatDataBean> spanSubscriptList;//下标
        private List<FormatDataBean> spanStyle_BList;//加粗
        private List<FormatDataBean> spanStyle_IList;//斜体
        private List<FormatDataBean> spanFrontColorList;//前景色
        private List<FormatDataBean> spanDeleteLineList;//删除线
        private List<FormatDataBean> spanUnderLineList;//下划线
        private List<FormatDataBean> spanFontSizeList;//字号
        private List<FormatDataBean> spanExpressionList;//表情
        private List<FormatDataBean> spanImgList;//图片
        private List<FormatDataBean> spanTaggingList;//标注
        private List<FormatDataBean> spanLinkList;//链接

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getDate() {
            return date;
        }

        public void setDate(Long date) {
            this.date = date;
        }

        public String getMood() {
            return mood;
        }

        public void setMood(String mood) {
            this.mood = mood;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public List<FormatDataBean> getSpanSuperscriptList() {
            return spanSuperscriptList;
        }

        public void setSpanSuperscriptList(List<FormatDataBean> spanSuperscriptList) {
            this.spanSuperscriptList = spanSuperscriptList;
        }

        public List<FormatDataBean> getSpanSubscriptList() {
            return spanSubscriptList;
        }

        public void setSpanSubscriptList(List<FormatDataBean> spanSubscriptList) {
            this.spanSubscriptList = spanSubscriptList;
        }

        public List<FormatDataBean> getSpanStyle_BList() {
            return spanStyle_BList;
        }

        public void setSpanStyle_BList(List<FormatDataBean> spanStyle_BList) {
            this.spanStyle_BList = spanStyle_BList;
        }

        public List<FormatDataBean> getSpanStyle_IList() {
            return spanStyle_IList;
        }

        public void setSpanStyle_IList(List<FormatDataBean> spanStyle_IList) {
            this.spanStyle_IList = spanStyle_IList;
        }

        public List<FormatDataBean> getSpanFrontColorList() {
            return spanFrontColorList;
        }

        public void setSpanFrontColorList(List<FormatDataBean> spanFrontColorList) {
            this.spanFrontColorList = spanFrontColorList;
        }

        public List<FormatDataBean> getSpanDeleteLineList() {
            return spanDeleteLineList;
        }

        public void setSpanDeleteLineList(List<FormatDataBean> spanDeleteLineList) {
            this.spanDeleteLineList = spanDeleteLineList;
        }

        public List<FormatDataBean> getSpanUnderLineList() {
            return spanUnderLineList;
        }

        public void setSpanUnderLineList(List<FormatDataBean> spanUnderLineList) {
            this.spanUnderLineList = spanUnderLineList;
        }

        public List<FormatDataBean> getSpanFontSizeList() {
            return spanFontSizeList;
        }

        public void setSpanFontSizeList(List<FormatDataBean> spanFontSizeList) {
            this.spanFontSizeList = spanFontSizeList;
        }

        public List<FormatDataBean> getSpanExpressionList() {
            return spanExpressionList;
        }

        public void setSpanExpressionList(List<FormatDataBean> spanExpressionList) {
            this.spanExpressionList = spanExpressionList;
        }

        public List<FormatDataBean> getSpanImgList() {
            return spanImgList;
        }

        public void setSpanImgList(List<FormatDataBean> spanImgList) {
            this.spanImgList = spanImgList;
        }

        public List<FormatDataBean> getSpanTaggingList() {
            return spanTaggingList;
        }

        public void setSpanTaggingList(List<FormatDataBean> spanTaggingList) {
            this.spanTaggingList = spanTaggingList;
        }

        public List<FormatDataBean> getSpanLinkList() {
            return spanLinkList;
        }

        public void setSpanLinkList(List<FormatDataBean> spanLinkList) {
            this.spanLinkList = spanLinkList;
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
