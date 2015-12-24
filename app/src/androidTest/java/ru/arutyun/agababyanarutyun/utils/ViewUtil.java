package ru.arutyun.agababyanarutyun.utils;

import android.app.Activity;
import android.support.annotation.NonNull;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.CharMatcher.is;
import static android.support.test.espresso.core.deps.guava.base.Predicates.not;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


public class ViewUtil {

    public static void checkSnackBar(@NonNull Activity activity, int mesRes) {
        final String matcherText = activity.getString(mesRes);
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(matcherText)))
                .check(matches(isDisplayed()));
    }

}
