package art.clovi.util;

import static com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG;

import static java.lang.Thread.sleep;

import android.content.ComponentName;
import android.content.Context;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.nothing.ketchum.Common;
import com.nothing.ketchum.Glyph;
import com.nothing.ketchum.GlyphException;
import com.nothing.ketchum.GlyphFrame;
import com.nothing.ketchum.GlyphManager;

import art.clovi.CloviConfig;

public class GlyphsUtils {
    public static GlyphManager mGM;
    private static GlyphManager.Callback mCallback;
    public static void init(Context context){
        initCallback();
        mGM = GlyphManager.getInstance(context);
        mGM.init(mCallback);
    }
    private static void initCallback() {
        mCallback = new GlyphManager.Callback() {
            @Override
            public void onServiceConnected(ComponentName componentName) {
                if (Common.is20111()) mGM.register(Glyph.DEVICE_20111);
                if (Common.is22111()) mGM.register(Glyph.DEVICE_22111);
                if (Common.is23111()) mGM.register(Glyph.DEVICE_23111);
                if (Common.is23113()) mGM.register(Glyph.DEVICE_23113);
                if (Common.is24111()) mGM.register(Glyph.DEVICE_24111);
                try {
                    mGM.openSession();
                } catch(GlyphException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                try {
                    mGM.closeSession();
                } catch (GlyphException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    public static void destroy(){
        try {
            mGM.closeSession();
        } catch (GlyphException e) {
            Log.e(TAG, e.getMessage());
        }
        mGM.unInit();
    }

    public static String getDeviceName(){
        if(!isSupportedDevice()) return "";
        if(Common.is24111()) return "Nothing Phone (3a) Series";
        if(Common.is23113()) return "Nothing Phone (2a) Plus";
        if(Common.is23111()) return "Nothing Phone (2a)";
        if(Common.is22111()) return "Nothing Phone (2)";
        if(Common.is20111()) return "Nothing Phone (1)";
        return "";
    }
    public static void sendTest(){
        if(!isSupportedDevice()) return;
        isTurnedOff = false;
        int maxSize;
        if(Common.is24111()){
            maxSize = 35;
            int size = (int) (35 * Math.random());
            mGM.turnOff();
            new Thread(() -> {
                try {
                    int seconds = 60000 / maxSize;//60000
                    GlyphFrame.Builder builder = mGM.getGlyphFrameBuilder();
                    for(int i = 0; i<=maxSize;i++){
                        builder.buildChannel(i);
                        mGM.toggle(builder.build());
                        sleep(seconds);
                    }
                    mGM.turnOff();
                } catch (Exception ex){
                    mGM.turnOff();
                }
            }).start();
        } else {
            maxSize = 0;
        }
    }
    private static long lastTime = 0;
    public static void sendVideoRecording(){
        if(!isSupportedDevice()) return;
        if(!CloviConfig.enableGlyphs || !CloviConfig.allowShowRecordGlyph){
            turnOff();
            return;
        }
        isTurnedOff = false;
        int index = 0;
        if(Common.is24111()) index = 35;
        else if(Common.is20111()) {
            index = 6;
        } else if(Common.is22111()) {
            index = 24;
        } else if(Common.is23113() || Common.is23111()) {
            index = 25;
        }
        GlyphFrame.Builder builder = mGM.getGlyphFrameBuilder();
        if(System.currentTimeMillis() - lastTime >= 1500) {
            lastTime = System.currentTimeMillis();
            mGM.turnOff();
        }
        else {
            builder.buildChannel(index);
            mGM.toggle(builder.build());
        }
    }
    public static void sendRoundVideo(float f, boolean isRecord){
        if(!isSupportedDevice()) return;
        if(!CloviConfig.enableGlyphs || (isRecord && !CloviConfig.allowShowRecordGlyph) || (!isRecord && !CloviConfig.allowShowProgressGlyph)){
            turnOff();
            return;
        }
        if(f == 0) turnOff();
        isTurnedOff = false;
        int start = 0;
        int maxSize = -1;
        if(Common.is24111()){
            maxSize = 35;
        } else if(Common.is20111()) {
            start = 7;
            maxSize = 14;
        } else if(Common.is22111()) {
            start = 3;
            maxSize = 18;
        } else if(Common.is23113() || Common.is23111()) {
            maxSize = 23;
        }
        if(maxSize == -1) return;
        GlyphFrame.Builder builder = mGM.getGlyphFrameBuilder();
        int size = (int) ((maxSize-start) * f);
        for(int i = start; i<=(start+size);i++) builder.buildChannel(i);
        mGM.toggle(builder.build());
    }
    public static boolean isTurnedOff = false;
    public static Thread turnOFFThread;
    public static void turnOff(){
        if(!isSupportedDevice()) return;
        if(!isTurnedOff) {
            if(turnOFFThread != null) turnOFFThread.interrupt();
            isTurnedOff = true;
            turnOFFThread = new Thread(() -> {
                try {
                    GlyphFrame.Builder builder = mGM.getGlyphFrameBuilder();
                    builder.buildChannelA().buildChannelB().buildChannelC().buildChannelD().buildChannelE().buildPeriod(1000);
                    mGM.animate(builder.build());
                    sleep(1000);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                mGM.turnOff();
            });
            turnOFFThread.start();
        }
    }

    public static boolean isSupportedDevice(){
        return Util.SDK_INT >= 36 && mGM != null && (Common.is24111());
    }
}
