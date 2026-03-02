package art.clovi.maps;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.core.util.Consumer;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Circle;
import com.yandex.mapkit.mapview.MapView;

import org.telegram.messenger.IMapsProvider;
import org.telegram.messenger.R;

import java.util.List;
import java.util.Map;

public class YandexMapsProvider implements IMapsProvider {
    @Override
    public void initializeMaps(Context context) {
        MapKitFactory.initialize(context);
    }

    @Override
    public IMapView onCreateMapView(Context context) {
        return new YandexMapsView(context);
    }

    @Override
    public IMarkerOptions onCreateMarkerOptions() {
        return null;
    }

    @Override
    public ICircleOptions onCreateCircleOptions() {
        return null;
    }

    @Override
    public ILatLngBoundsBuilder onCreateLatLngBoundsBuilder() {
        return null;
    }

    @Override
    public ICameraUpdate newCameraUpdateLatLng(LatLng latLng) {
        return null;
    }

    @Override
    public ICameraUpdate newCameraUpdateLatLngZoom(LatLng latLng, float zoom) {
        return null;
    }

    @Override
    public ICameraUpdate newCameraUpdateLatLngBounds(ILatLngBounds bounds, int padding) {
        return null;
    }

    @Override
    public IMapStyleOptions loadRawResourceStyle(Context context, int resId) {
        return null;
    }

    @Override
    public String getMapsAppPackageName() {
        return "ru.yandex.yandexmaps";
    }

    @Override
    public int getInstallMapsString() {
        return R.string.InstallYandexMaps;
    }

    public final static class YandexMapsView implements IMapView {
        private MapView mapView;
        private ITouchInterceptor dispatchInterceptor;
        private ITouchInterceptor interceptInterceptor;
        private Runnable onLayoutListener;
        private Context context;
        private YandexMapsView(Context context){
            this.context = context;
            mapView = new MapView(context);
        }

        @Override
        public View getView() {
            return mapView;
        }

        @Override
        public void getMapAsync(Consumer<IMap> callback) {

        }

        @Override
        public void onResume() {
            MapKitFactory.getInstance().onStart();
            mapView.onStart();
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onCreate(Bundle savedInstance) {
        }

        @Override
        public void onDestroy() {
            mapView.onStop();
            MapKitFactory.getInstance().onStop();
        }

        @Override
        public void onLowMemory() {

        }

        @Override
        public void setOnDispatchTouchEventInterceptor(ITouchInterceptor touchInterceptor) {
            this.dispatchInterceptor = touchInterceptor;
        }

        @Override
        public void setOnInterceptTouchEventInterceptor(ITouchInterceptor touchInterceptor) {
            this.interceptInterceptor = touchInterceptor;
        }

        @Override
        public void setOnLayoutListener(Runnable callback) {
            onLayoutListener = callback;
        }
    }

    public final static class YandexCircleOptions implements ICircleOptions {
        Circle circle = new Circle();

        @Override
        public ICircleOptions center(LatLng latLng) {

            return this;
        }

        @Override
        public ICircleOptions radius(double radius) {
            return this;
        }

        @Override
        public ICircleOptions strokeColor(int color) {
            return this;
        }

        @Override
        public ICircleOptions fillColor(int color) {
            return this;
        }

        @Override
        public ICircleOptions strokePattern(List<PatternItem> patternItems) {
            return this;
        }

        @Override
        public ICircleOptions strokeWidth(int width) {
            return this;
        }
    }
    public final static class YandexMapsUISettings implements IUISettings {
        MapView mapView;
        private YandexMapsUISettings(MapView mapView){
            this.mapView = mapView;
        }

        @Override
        public void setZoomControlsEnabled(boolean enabled) {
            mapView.getMap().setZoomGesturesEnabled(enabled);
        }

        @Override
        public void setMyLocationButtonEnabled(boolean enabled) {

        }

        @Override
        public void setCompassEnabled(boolean enabled) {
        }
    }
}
