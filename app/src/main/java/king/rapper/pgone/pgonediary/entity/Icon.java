package king.rapper.pgone.pgonediary.entity;

/**
 * @ Create_time: 2018/7/21 on 13:41.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
public class Icon {
    protected String url;
    protected String type;
    protected Long time;

    public Icon(){}

    public Icon(String url, String type, Long time) {
        this.url = url;
        this.type = type;
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Icon{" +
                "url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
