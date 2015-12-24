package ru.arutyun.agababyanarutyun.bus.event;


public class FavoriteNewsCountUpdateEvent {
    private final int mFavoriteNewsCount;

    public FavoriteNewsCountUpdateEvent(int count) {
        mFavoriteNewsCount = count;
    }

    public int getCount() {
        return mFavoriteNewsCount;
    }
}
