package ru.arutyun.agababyanarutyun.ui.view;


import android.support.annotation.NonNull;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import ru.arutyun.agababyanarutyun.R;
import ru.arutyun.agababyanarutyun.data.FilterType;

public class DrawerItemFactory {

    public static PrimaryDrawerItem getDrawerItem(@NonNull FilterType filterType) {
        int iconRes = 0;
        int titleRes = 0;
        switch (filterType) {
            case ALL_NEWS:
                iconRes = R.drawable.ic_all_news;
                titleRes = R.string.all_news;
                break;
            case UNREAD:
                iconRes = R.drawable.ic_envelope_close_black;
                titleRes = R.string.unread;
                break;
            case FAVORITES:
                iconRes = R.drawable.ic_favorite_border_black;
                titleRes = R.string.favorites;
                break;
        }
        return new PrimaryDrawerItem()
                .withIcon(iconRes)
                .withName(titleRes)
                .withIdentifier(filterType.ordinal())
                .withTag(filterType);
    }

}