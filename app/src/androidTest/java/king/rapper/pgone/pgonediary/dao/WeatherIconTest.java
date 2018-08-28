package king.rapper.pgone.pgonediary.dao;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import king.rapper.pgone.pgonediary.entity.WeatherIcon;
import king.rapper.pgone.pgonediary.dao.WeatherIconDao;

public class WeatherIconTest extends AbstractDaoTestLongPk<WeatherIconDao, WeatherIcon> {

    public WeatherIconTest() {
        super(WeatherIconDao.class);
    }

    @Override
    protected WeatherIcon createEntity(Long key) {
        WeatherIcon entity = new WeatherIcon();
        entity.setWeatherIconId(key);
        return entity;
    }

}
