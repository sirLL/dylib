package cn.dianyinhuoban.hm.base;

import com.wareroom.lib_base.mvp.IPresenter;
import com.wareroom.lib_base.ui.BaseActivity;

public abstract class BaseCityActivity<P extends IPresenter> extends BaseActivity  {

    @Override
    protected IPresenter getPresenter() {
        return null;
    }
}
