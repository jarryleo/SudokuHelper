package cn.leo.sudoku.ocr;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.leo.permission.PermissionRequest;
import cn.leo.sudoku.R;

public class OcrActivity extends AppCompatActivity implements View.OnClickListener {

    private CameraView mCameraView;
    private ScannerView mScannerView;
    private TextView mTextView;
    private Button mBtnTakePic;
    private View mBtnCommit;
    private ConstraintLayout mConstraintLayout;
    private ConstraintSet mNormalSet;
    private ConstraintSet mShowSet;
    private OcrScanner mOcrScanner;
    private String mResult;

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
        mTextView = findViewById(R.id.sample_text);
        mBtnTakePic = findViewById(R.id.btnTakePic);
        mBtnCommit = findViewById(R.id.btnCommit);
        mConstraintLayout = findViewById(R.id.constraintLayout);
        mBtnTakePic.setOnClickListener(this);
        mBtnCommit.setOnClickListener(this);
        mNormalSet = new ConstraintSet();
        mShowSet = new ConstraintSet();
        mNormalSet.clone(mConstraintLayout);
        mShowSet.clone(this, R.layout.activity_ocr_commit);
        init();
    }

    @PermissionRequest({Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA})
    private void init() {
        mOcrScanner = OcrScanner.build(this);
        mOcrScanner.setOnOcrResultListener(new OcrScanner.OnOcrResultListener() {
            @Override
            public void onOcrResult(int num, String result, boolean success) {
                mResult = result;
                if (success && num > 10) {
                    Toast.makeText(OcrActivity.this, "识别成功，请检查识别结果是否正确", Toast.LENGTH_SHORT).show();
                    showCommit();
                    mScannerView.setResult(result);
                } else {
                    if (num > 10) {
                        showCommit();
                        mScannerView.setResult(result);
                        Toast.makeText(OcrActivity.this, "题目不合法，请提取后自行修改", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OcrActivity.this, "识别失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
                mBtnTakePic.setEnabled(true);
            }
        });
        mCameraView.setOnBitmapCreateListener(new CameraView.OnBitmapCreateListener() {
            @Override
            public void onBitmapCreate(final Bitmap bitmap) {
                new Thread() {
                    @Override
                    public void run() {
                        mOcrScanner.scan(bitmap);
                    }
                }.start();
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
