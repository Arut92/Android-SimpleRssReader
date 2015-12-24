package ru.arutyun.agababyanarutyun;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.arutyun.agababyanarutyun.ui.activity.RssActivity;
import ru.arutyun.agababyanarutyun.utils.ViewUtil;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTests  {

    @Rule
    public ActivityTestRule<RssActivity> mActivityRule = new ActivityTestRule<>(RssActivity.class);

    @Test
    public void testReadAllClick() {
        onView(withId(R.id.fab_read_all)).perform(click());
        ViewUtil.checkSnackBar(mActivityRule.getActivity(), R.string.read_all_news);
    }



}
