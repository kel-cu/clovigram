/*

 This is the source code of exteraGram for Android.

 We do not and cannot prevent the use of our code,
 but be respectful and credit the original author.

 Copyright @immat0x1, 2023

*/

package art.clovi;

import android.app.Activity;
import android.content.SharedPreferences;
import org.telegram.messenger.ApplicationLoader;

public class CloviConfig {

    private static final Object sync = new Object();

    // Appearance
    public static boolean hidePhoneNumber;
    public static boolean replaceAppName;
    public static boolean useYandexMaps;
    public static boolean hideStories;
    public static boolean disableTabletMode;
    public static boolean isMd3ContainersEnabled;
    public static boolean isMd3ComponentsEnabled;
    public static boolean hideDeveloperSite;
    public static boolean useSystemFont;
    public static boolean useSystemEmoji;
    public static boolean disableDoubleTapReaction;
    public static boolean showProfileButton;
    public static boolean replaceEditedToPen;
    public static boolean addPostStatsButton;
    // Camera
    public static boolean startWithBackCamera;
    // Nothing Glyphs
    public static boolean enableGlyphs;
    public static boolean allowShowProgressGlyph;
    public static boolean allowShowRecordGlyph;
    // ээ
    private static boolean configLoaded;

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    static {
        loadConfig();
    }

    public static void loadConfig() {
        synchronized (sync) {
            if (configLoaded) {
                return;
            }
            preferences = ApplicationLoader.applicationContext.getSharedPreferences("cloviconfig", Activity.MODE_PRIVATE);
            editor = preferences.edit();

            hidePhoneNumber = preferences.getBoolean("hidePhoneNumber", true);
            replaceAppName = preferences.getBoolean("replaceAppName", true);
            useYandexMaps = preferences.getBoolean("useYandexMaps", true);
            startWithBackCamera = preferences.getBoolean("startWithBackCamera", true);
            hideStories = preferences.getBoolean("hideStories", true);
            disableTabletMode = preferences.getBoolean("disableTabletMode", true);
            isMd3ContainersEnabled = preferences.getBoolean("isMd3ContainersEnabled", true);
            isMd3ComponentsEnabled = preferences.getBoolean("isMd3ComponentsEnabled", true);
            hideDeveloperSite = preferences.getBoolean("hideDeveloperSite", false);
            useSystemFont = preferences.getBoolean("useSystemFont", true);
            useSystemEmoji = preferences.getBoolean("useSystemEmoji", false);
            disableDoubleTapReaction = preferences.getBoolean("disableDoubleTapReaction", false);
            showProfileButton = preferences.getBoolean("showProfileButton", false);
            replaceEditedToPen = preferences.getBoolean("replaceEditedToPen", true);
            addPostStatsButton = preferences.getBoolean("addPostStatsButton", true);

            // Nothing Glyphs
            enableGlyphs = preferences.getBoolean("enableGlyphs", true);
            allowShowProgressGlyph = preferences.getBoolean("allowShowProgressGlyph", true);
            allowShowRecordGlyph = preferences.getBoolean("allowShowRecordGlyph", true);
            configLoaded = true;
        }
    }


    public static boolean getLogging() {
        return ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", Activity.MODE_PRIVATE).getBoolean("logsEnabled", false); //BuildVars.DEBUG_VERSION);
    }

    public static boolean getValue(int FLAG){
        if(FLAG == 0){
            return hidePhoneNumber;
        } else if(FLAG == 1){
            return replaceAppName;
        } else if(FLAG == 2){
            return useYandexMaps;
        } else if(FLAG == 3){
            return startWithBackCamera;
        } else if(FLAG == 4){
            return enableGlyphs;
        } else if(FLAG == 5){
            return allowShowProgressGlyph;
        } else if(FLAG == 6){
            return allowShowRecordGlyph;
        } else if(FLAG == 7){
            return hideStories;
        } else if(FLAG == 8){
            return disableTabletMode;
        } else if(FLAG == 9){
            return isMd3ContainersEnabled;
        } else if(FLAG == 10){
            return hideDeveloperSite;
        } else if(FLAG == 11){
            return useSystemFont;
        } else if(FLAG == 12){
            return useSystemEmoji;
        } else if(FLAG == 13){
            return disableDoubleTapReaction;
        } else if(FLAG == 14){
            return showProfileButton;
        } else if(FLAG == 15){
            return isMd3ComponentsEnabled;
        } else if(FLAG == 16) return replaceEditedToPen;
        else return addPostStatsButton;
    }
    public static void setValue(int FLAG){
        if(FLAG == 0){
            CloviConfig.hidePhoneNumber = !CloviConfig.hidePhoneNumber;
            CloviConfig.editor.putBoolean("hidePhoneNumber", hidePhoneNumber);
        } else if(FLAG == 1){
            CloviConfig.replaceAppName = !CloviConfig.replaceAppName;
            CloviConfig.editor.putBoolean("replaceAppName", replaceAppName);
        } else if(FLAG == 2){
            CloviConfig.useYandexMaps = !CloviConfig.useYandexMaps;
            CloviConfig.editor.putBoolean("useYandexMaps", useYandexMaps);
        } else if(FLAG == 3){
            CloviConfig.startWithBackCamera = !CloviConfig.startWithBackCamera;
            CloviConfig.editor.putBoolean("startWithBackCamera", startWithBackCamera);
        } else if(FLAG == 4){
            CloviConfig.enableGlyphs = !CloviConfig.enableGlyphs;
            CloviConfig.editor.putBoolean("enableGlyphs", enableGlyphs);
        } else if(FLAG == 5){
            CloviConfig.allowShowProgressGlyph = !CloviConfig.allowShowProgressGlyph;
            CloviConfig.editor.putBoolean("allowShowProgressGlyph", allowShowProgressGlyph);
        } else if(FLAG == 6){
            CloviConfig.allowShowRecordGlyph = !CloviConfig.allowShowRecordGlyph;
            CloviConfig.editor.putBoolean("allowShowRecordGlyph", allowShowRecordGlyph);
        } else if(FLAG == 7){
            CloviConfig.hideStories = !CloviConfig.hideStories;
            CloviConfig.editor.putBoolean("hideStories", hideStories);
        } else if(FLAG == 8){
            CloviConfig.disableTabletMode = !CloviConfig.disableTabletMode;
            CloviConfig.editor.putBoolean("disableTabletMode", disableTabletMode);
        } else if(FLAG == 9){
            CloviConfig.isMd3ContainersEnabled = !CloviConfig.isMd3ContainersEnabled;
            CloviConfig.editor.putBoolean("isMd3ContainersEnabled", isMd3ContainersEnabled);
        } else if(FLAG == 10){
            CloviConfig.hideDeveloperSite = !CloviConfig.hideDeveloperSite;
            CloviConfig.editor.putBoolean("hideDeveloperSite", hideDeveloperSite);
        } else if(FLAG == 11){
            CloviConfig.useSystemFont = !CloviConfig.useSystemFont;
            CloviConfig.editor.putBoolean("useSystemFont", useSystemFont);
        } else if(FLAG == 12){
            CloviConfig.useSystemEmoji = !CloviConfig.useSystemEmoji;
            CloviConfig.editor.putBoolean("useSystemEmoji", useSystemEmoji);
        } else if(FLAG == 13){
            CloviConfig.disableDoubleTapReaction = !CloviConfig.disableDoubleTapReaction;
            CloviConfig.editor.putBoolean("disableDoubleTapReaction", disableDoubleTapReaction);
        } else if(FLAG == 14){
            CloviConfig.showProfileButton = !CloviConfig.showProfileButton;
            CloviConfig.editor.putBoolean("showProfileButton", showProfileButton);
        } else if(FLAG == 15){
            CloviConfig.isMd3ComponentsEnabled = !CloviConfig.isMd3ComponentsEnabled;
            CloviConfig.editor.putBoolean("isMd3ComponentsEnabled", isMd3ComponentsEnabled);
        } else if(FLAG == 16){
            CloviConfig.replaceEditedToPen = !CloviConfig.replaceEditedToPen;
            CloviConfig.editor.putBoolean("replaceEditedToPen", replaceEditedToPen);
        } else if(FLAG == 17){
            CloviConfig.addPostStatsButton = !CloviConfig.addPostStatsButton;
            CloviConfig.editor.putBoolean("addPostStatsButton", addPostStatsButton);
        }
        CloviConfig.editor.apply();
    }

    public static void clearPreferences() {
        configLoaded = false;
        CloviConfig.editor.clear().apply();
        CloviConfig.loadConfig();
    }
}