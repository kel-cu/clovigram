package art.clovi;

import android.content.Context;

import com.yandex.mapkit.MapKitFactory;

import org.telegram.messenger.BuildConfig;

import art.clovi.config.Config;
import art.clovi.util.GlyphsUtils;

public class Clovigram {
    public static Config config;
    public static void init(Context context){
        config = new Config("cloviconfig", context);
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY);
        GlyphsUtils.init(context);
    }
}
