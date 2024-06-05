package woodnsoft.bsHandax.common;

import java.io.File;

import woodnsoft.bsHandax.db.DBH;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DBActivity extends BaseActivity implements DatabaseAdapterEvent {
	private static final String TAG = "DBActivity";
	private static final int PERMISSION_REQUEST_CODE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (checkPermission()) {
			initializeDatabase();
		} else {
			requestPermission();
		}
	}

	private void initializeDatabase() {
		File fsdcard = Environment.getExternalStorageDirectory();  // SD 카드 경로
		File fdbpath = new File(fsdcard.getAbsolutePath() + File.separator + "WoodnSoft" + File.separator + "bsHandax");
		if (!fdbpath.exists()){
			Log.i(TAG, "fsdcard path = " + fsdcard.getAbsolutePath());
			Log.i(TAG,"Create Db directory : " + fdbpath.getAbsolutePath());
			if (fdbpath.mkdirs()) {
				Log.i(TAG, "OK");
			} else {
				Toast.makeText(getBaseContext(), "폴더 생성 실패!!!", Toast.LENGTH_SHORT).show();
				Log.e(TAG, "NO");
			}
		}

		DBH.dbPath = fdbpath.getAbsolutePath();  // DB 설치 경로
		DBH.dbFile = "bsHandax2.db";  // DB 파일명
		DBH.dbPathFile = DBH.dbPath + File.separator + DBH.dbFile;  // DB 경로 + 파일명

		Log.i("TEST", "dbPathFile = " + DBH.dbPathFile);

		try {
			Log.i(TAG, "in try....................");
			DBH.DB = openOrCreateDatabase(DBH.dbPathFile, MODE_PRIVATE, null);
		} catch (Exception e) {
			Log.e(TAG, "데이터베이스 열기 실패: " + e.getMessage());
		}

		if (DBH.DB == null) DBH.openDatabase();
	}

	private boolean checkPermission() {
		int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
		return result == PackageManager.PERMISSION_GRANTED;
	}

	private void requestPermission() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_CODE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					initializeDatabase();
				} else {
					Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
}