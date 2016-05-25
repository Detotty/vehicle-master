package pk.roadpartner.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class AnySectionPageAdapter extends FragmentStatePagerAdapter {

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    public AnySectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new LoginDetailsFragment();
            case 1:
                return new CarSelectedFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
