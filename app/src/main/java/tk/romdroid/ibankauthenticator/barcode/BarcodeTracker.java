package tk.romdroid.ibankauthenticator.barcode;

/**
 * Created by developer69 on 23-12-2016.
 */
import android.content.Context;

import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

class BarcodeTracker extends Tracker<Barcode> {
    private BarcodeGraphicTrackerCallback mListener;

    public interface BarcodeGraphicTrackerCallback {
        void onDetectedQrCode(Barcode barcode);
    }

    BarcodeTracker(Context listener) {
        mListener = (BarcodeGraphicTrackerCallback) listener;
    }

    @Override
    public void onNewItem(int id, Barcode item) {
        if (item.displayValue != null) {
            mListener.onDetectedQrCode(item);
        }
    }
}