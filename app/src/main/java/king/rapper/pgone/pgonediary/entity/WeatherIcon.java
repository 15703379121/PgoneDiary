package king.rapper.pgone.pgonediary.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @ Create_time: 2018/7/20 on 17:10.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 */
@Entity
public class WeatherIcon {

    @Id(autoincrement = true) // id自增长
    private Long weatherIconId; // 天气图片id
    protected String url;
    protected String type;
    protected Long time;

    public WeatherIcon(String url, String type, Long time) {
        this.url = url;
        this.type = type;
        this.time = time;
    }

    public WeatherIcon(String url) {
        this.url = url;
    }

    @Generated(hash = 1008700176)
    public WeatherIcon(Long weatherIconId, String url, String type, Long time) {
        this.weatherIconId = weatherIconId;
        this.url = url;
        this.type = type;
        this.time = time;
    }

    @Generated(hash = 1635404675)
    public WeatherIcon() {
    }

    public Long getWeatherIconId() {
        return weatherIconId;
    }

    public void setWeatherIconId(Long weatherIconId) {
        this.weatherIconId = weatherIconId;
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
        return "WeatherIcon{" +
                "weatherIconId=" + weatherIconId +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
