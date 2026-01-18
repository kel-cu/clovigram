package art.clovi;

import android.content.Context;

import art.clovi.config.Config;
import art.clovi.util.GlyphsUtils;

public class Clovigram {
    public static Config config;
    public static void init(Context context){
        config = new Config("cloviconfig", context);
        GlyphsUtils.init(context);
    }
}
