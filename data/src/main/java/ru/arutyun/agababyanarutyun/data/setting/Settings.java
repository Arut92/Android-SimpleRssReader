package ru.arutyun.agababyanarutyun.data.setting;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDateTime;


public class Settings {

    private static final String PREFS = "prefs";

    private static final String LAST_UPDATE_TIME = "last_update_time";

    private final SharedPreferences mPrefs;
    private final SharedPreferences.Editor mEditor;

    public Settings(Context context) {
        mPrefs   = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        mEditor  = mPrefs.edit();
    }

    public void setLastUpdateTime(LocalDateTime localDateTime) {
        mEditor.putString(LAST_UPDATE_TIME, localDateTime.toString());
        mEditor.commit();
    }

    @Nullable
    public LocalDateTime getLastUpdateTime() {
        final String lastUpdateTimeVal = mPrefs.getString(LAST_UPDATE_TIME, null);
        if (lastUpdateTimeVal == null) {
            return null;
        } else {
            return new LocalDateTime(lastUpdateTimeVal);
        }
    }

}
