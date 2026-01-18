package org.telegram.messenger.maps.yandex;

import android.content.Context;
import androidx.core.util.Consumer;
import org.telegram.messenger.ILocationServiceProvider;

/* loaded from: classes.dex */
public class YandexLocationProvider implements ILocationServiceProvider {
    @Override // org.telegram.messenger.ILocationServiceProvider
    public boolean checkServices() {
        return false;
    }

    @Override // org.telegram.messenger.ILocationServiceProvider
    public void getLastLocation(Consumer consumer) {
    }

    @Override // org.telegram.messenger.ILocationServiceProvider
    public void init(Context context) {
    }

    @Override // org.telegram.messenger.ILocationServiceProvider
    public ILocationServiceProvider.ILocationRequest onCreateLocationRequest() {
        return null;
    }

    @Override // org.telegram.messenger.ILocationServiceProvider
    public ILocationServiceProvider.IMapApiClient onCreateLocationServicesAPI(Context context, ILocationServiceProvider.IAPIConnectionCallbacks iAPIConnectionCallbacks, ILocationServiceProvider.IAPIOnConnectionFailedListener iAPIOnConnectionFailedListener) {
        return null;
    }

    @Override // org.telegram.messenger.ILocationServiceProvider
    public void removeLocationUpdates(ILocationServiceProvider.ILocationListener iLocationListener) {
    }

    @Override // org.telegram.messenger.ILocationServiceProvider
    public void requestLocationUpdates(ILocationServiceProvider.ILocationRequest iLocationRequest, ILocationServiceProvider.ILocationListener iLocationListener) {
    }

    @Override // org.telegram.messenger.ILocationServiceProvider
    public void checkLocationSettings(ILocationServiceProvider.ILocationRequest iLocationRequest, Consumer consumer) {
        if (consumer != null) {
            consumer.accept(2);
        }
    }
}