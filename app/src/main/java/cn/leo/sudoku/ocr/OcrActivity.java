package cn.leo.sudoku.ocr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

import cn.leo.sudoku.BuildConfig;
import cn.leo.sudoku.R;
import cn.leo.sudoku.core.SudokuChecker;

public class OcrActivity extends AppCompatActivity implements View.OnClickListener {

    private TessBaseAPI mTess;
    private boolean mInit;
    private CameraView mCameraView;
    private ScannerView mScannerView;
    private TextView mTxtView;
    private String mResult;
    private Button mBtnTakePic;
    private View mBtnCommit;
    private ConstraintLayout mConstraintLayout;
    private ConstraintSet mNormalSet;
    private ConstraintSet mShowSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏无状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ocr);
        mCameraView = findViewById(R.id.cameraPre);
        mScannerView = findViewById(R.id.scanner);
        mTxtView = findViewById(R.id.sample_text);
        mBtnTakePic = findViewById(R.id.btnTakePic);
        mBtnCommit = findViewById(R.id.btnCommit);
        mConstraintLayout = findViewById(R.id.constraintLayout);
        mBtnTakePic.setOnClickListener(this);
        mBtnCommit.setOnClickListener(this);
        mNormalSet = new ConstraintSet();
        mShowSet = new ConstraintSet();
        mNormalSet.clone(mConstraintLayout);
        mShowSet.clone(this, R.layout.activity_ocr_commit);
        copyFile();
    }

    private void copyFile() {
        String dataPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tesseract/";
        File dir = new File(dataPath + "tessdata/");
        FileUtil.CopyAssets(this, "tessdata", dir.getAbsolutePath());
        initTessBaseData(dataPath);
    }

    private void initTessBaseData(String datapath) {
        mTess = new TessBaseAPI();
        String language = "eng";
        mTess.setDebug(BuildConfig.DEBUG);
        mInit = mTess.init(datapath, language);
        if (!mInit) {
            Toast.makeText(this, "加载语言包失败", Toast.LENGTH_SHORT).show();
            return;
        }
        mCameraView.setOnBitmapCreateListener(new CameraView.OnBitmapCreateListener() {
            @Override
            public void onBitmapCreate(final Bitmap bitmap) {
                new Thread() {
                    @Override
                    public void run() {
                        splitBitmap(bitmap);
                    }
                }.start();
            }
        });
    }

    private void splitBitmap(Bitmap bitmap) {
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
        mResult = sb.toString();
        final int finalNum = num;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean check = SudokuChecker.check(mResult);
                if (check && finalNum > 10) {
                    Toast.makeText(OcrActivity.this, "识别成功，请检查识别是否正确", Toast.LENGTH_SHORT).show();
                    showCommit();
                    mScannerView.setResult(mResult);
                } else {
                    Toast.makeText(OcrActivity.this, "识别失败，请重试", Toast.LENGTH_SHORT).show();
                }
                mBtnTakePic.setEnabled(true);
            }
        });
    }

    private String ocrWordScan(Bitmap bitmap) {
        if (!mInit) return "";
        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_CHAR);
        mTess.setImage(bitmap);
        return mTess.getUTF8Text();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnTakePic) {
            mBtnTakePic.setEnabled(false);
            mScannerView.setResult(null);
            hideCommit();
            mCameraView.takePicture();
        } else if (v == mBtnCommit) {
            Intent data = new Intent();
            data.putExtra("title", mResult);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    private void showCommit() {
        mBtnTakePic.setText("重新扫描");
        TransitionManager.beginDelayedTransition(mConstraintLayout);
        mShowSet.applyTo(mConstraintLayout);
    }

    private void hideCommit() {
        TransitionManager.beginDelayedTransition(mConstraintLayout);
        mNormalSet.applyTo(mConstraintLayout);
    }

}
