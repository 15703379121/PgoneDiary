package king.rapper.pgone.pgonediary.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @ Create_time: 2018/7/20 on 17:10.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
@Entity
public class ExpressionIcon {

    @Id(autoincrement = true) // id自增长
    private Long expressionIconId; // 天气图片id
    protected String url;
    protected String type;
    protected Long time;

    public ExpressionIcon(String url, String type, Long time) {
        this.url = url;
        this.type = type;
        this.time = time;
    }

    public ExpressionIcon(String url) {
        this.url = url;
    }

    @Generated(hash = 782030062)
    public ExpressionIcon(Long expressionIconId, String url, String type,
            Long time) {
        this.expressionIconId = expressionIconId;
        this.url = url;
        this.type = type;
        this.time = time;
    }

    @Generated(hash = 2141609012)
    public ExpressionIcon() {
    }

    public Long getExpressionIconId() {
        return expressionIconId;
    }

    public void setExpressionIconId(Long expressionIconId) {
        this.expressionIconId = expressionIconId;
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
        return "ExpressionIcon{" +
                "expressionIconId=" + expressionIconId +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
