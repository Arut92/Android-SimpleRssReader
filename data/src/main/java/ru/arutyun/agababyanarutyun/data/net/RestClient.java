package ru.arutyun.agababyanarutyun.data.net;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.SimpleXmlConverterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import ru.arutyun.agababyanarutyun.net.XmlRss;
import ru.arutyun.agababyanarutyun.RssSource;
import rx.Observable;

public class RestClient {

    final Map<RssSource, ServerApi> mData;

    public RestClient() {
        mData = new HashMap<>();
        for(RssSource source: RssSource.values()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(source.getBaseUrl())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
            mData.put(source,retrofit.create(ServerApi.class));
        }

    }

    @Nullable
    public Observable<XmlRss> getRss(@NonNull RssSource source) {
        ServerApi serverApi = mData.get(source);
        if (serverApi != null) {
            return serverApi.getRss(source.getPath());
        }
        return null;
    }

    public interface ServerApi {

        @GET("/{path}")
        Observable<XmlRss> getRss(@Path("path") String path);

    }

}
