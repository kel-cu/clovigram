package art.clovi.config;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public Config(String name, Context context){
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public String getString(String key, String defaultValue){
        return prefs.getString(key, defaultValue);
    }
    public int getInt(String key, int defaultValue){
        return prefs.getInt(key, defaultValue);
    }
    public long getLong(String key, long defaultValue){
        return prefs.getLong(key, defaultValue);
    }
    public float getFloat(String key, float defaultValue){
        return prefs.getFloat(key, defaultValue);
    }
    public boolean getBoolean(String key, boolean defaultValue){
        return prefs.getBoolean(key, defaultValue);
    }

    public Config setString(String key, String defaultValue){
        editor.putString(key, defaultValue);
        editor.apply();
        return this;
    }
    public Config setInt(String key, int defaultValue){
        editor.putInt(key, defaultValue);
        editor.apply();
        return this;
    }
    public Config setLong(String key, long defaultValue){
        editor.putLong(key, defaultValue);
        editor.apply();
        return this;
    }
    public Config setFloat(String key, float defaultValue){
        editor.putFloat(key, defaultValue);
        editor.apply();
        return this;
    }
    public Config setBoolean(String key, boolean defaultValue){
        editor.putBoolean(key, defaultValue);
        editor.apply();
        return this;
    }


}
