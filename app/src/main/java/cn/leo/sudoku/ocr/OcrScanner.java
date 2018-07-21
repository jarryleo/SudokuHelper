package cn.leo.sudoku.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

import cn.leo.sudoku.BuildConfig;
import cn.leo.sudoku.core.SudokuChecker;

/**
 * create by : Jarry Leo
 * date : 2018/7/21 15:15
 */
public class OcrScanner {
    private String dataPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tesseract/";
    private static TessBaseAPI mTess;
    private Context mContext;
    private boolean mInit;
    private Handler mHandler = new Handler(Looper.myLooper());
    private OnOcrResultListener mOnOcrResultListener;

    public static OcrScanner build(Context context) {
        return new OcrScanner(context);
    }

    private OcrScanner(Context context) {
        mContext = context;
        copyFile();
    }

    private void copyFile() {
        final File dir = new File(dataPath + "tessdata/");
        new Thread() {
            @Override
            public void run() {
                FileUtil.CopyAssets(mContext, "tessdata", dir.getAbsolutePath());
                initTessBaseData(dataPath);
            }
        }.start();
    }

    private void initTessBaseData(String dataPath) {
        mTess = new TessBaseAPI();
        String language = "eng";
        mTess.setDebug(BuildConfig.DEBUG);
        mInit = mTess.init(dataPath, language);
    }

    public void scan(Bitmap bitmap) {
        final StringBuilder sb = new StringBuilder();
        int width = bitmap.getWidth() / 9;
        int height = bitmap.getHeight() / 9;
        int dw = width / 8;
        int dh = height / 8;
        int num = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                final Bitmap cell = Bitmap.createBitmap(bitmap,
                        j * width + dw,
                        i * height + dh,
                        width - dw * 2,
                        height - dh * 2);
                String s = ocrWordScan(cell);
                if (!TextUtils.isEmpty(s) && TextUtils.isDigitsOnly(s)) {
                    sb.append(s);
                    num++;
                } else {
                    sb.append("0");
                }
            }
        }
        final String result = sb.toString();
        final boolean check = SudokuChecker.check(result);
        if (mOnOcrResultListener == null) return;
        final int finalNum = num;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mOnOcrResultListener.onOcrResult(finalNum, result, check);
            }
        });
    }

    private String ocrWordScan(Bitmap bitmap) {
        if (!mInit) return "";
        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_CHAR);
        mTess.setImage(bitmap);
        Log.e("-----", "getBoxText: " + mTess.getBoxText(0));
        return mTess.getUTF8Text();
    }

    public void setOnOcrResultListener(OnOcrResultListener listener) {
        mOnOcrResultListener = listener;
    }

    public interface OnOcrResultListener {
        void onOcrResult(int num, String result, boolean success);
    }
}
