package com.woodnsoft.airmajor_bs_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

import org.json.JSONArray;
import org.json.JSONObject;

import com.woodnsoft.airmajor_bs_android.bsHandax.common.DBActivity;
import com.woodnsoft.airmajor_bs_android.bsHandax.common.Util;
import com.woodnsoft.airmajor_bs_android.bsHandax.db.DBH;
import com.woodnsoft.airmajor_bs_android.bsHandax2.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Detail extends DBActivity {
	TextView txtMsg;
	String is_code, is_name, is_date;
	Executor httpClient;
	private static final String TAG = "Detail";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Custom title should be set before setContentView
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail);

		txtMsg = findViewById(R.id.txtMsg);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		is_name = bundle.getString("data0");
		is_code = bundle.getString("data1");
		is_date = bundle.getString("data2");

		fChk();

		txtMsg.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				txtMsg.setText("");
				return false;
			}
		});

		Button lbback = findViewById(R.id.bBack);
		lbback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		Button lbOk = findViewById(R.id.bOk);
		lbOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pd = ProgressDialog.show(Detail.this, "", "데이타를 가져오고 있습니다.", true);
				v.postDelayed(new Runnable() {
					public void run() {
						// dbSelect();
						// f_BE_DEPT_EMP();
					}
				}, 200);
			}
		});
	}

	public void fChk() {
		long ll_cnt = 0, ll_cntm = 0;
		String ls_d = "", ls_dm = "";

		try {
			try {
				f_web_select("SELECT COUNT(1) CNT, MAX((SELECT MODI_DATE FROM AZ_MODI_DATE WHERE TABLE_CODE = '" + is_code + "')) MODI_DATE FROM " + is_code);
				txtMsg.append("★★★[" + is_code + "] " + is_name + "★★★\n\n");
				txtMsg.append("---ERP 데이타-------------------->>>>>>\n");

				if (gjArr.length() <= 0) {
					txtMsg.append("서버에서 데이타를 조회할 수 없습니다.\n");
				} else {
					JSONObject jObj = gjArr.getJSONObject(0);  //추출
					ll_cnt = jObj.getLong("CNT");
					txtMsg.append("총건수 : " + ll_cnt + "건\n");
					ls_d = jObj.getString("MODI_DATE");
					if ((Util.f_isnull(ls_d)) || ls_d.equals("null")) {
						txtMsg.append("최종수정일 : 미수신\n");
					} else {
						txtMsg.append("최종수정일 : " + String.format("%s년%s월%s일 (%s시%s분%s초)", ls_d.substring(0, 4), ls_d.substring(4, 6), ls_d.substring(6, 8), ls_d.substring(9, 11), ls_d.substring(11, 13), ls_d.substring(13, 15)) + "\n");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				txtMsg.append("서버에서 데이타를 가져오는중에 오류가 발생하였습니다.\n");
			}

			try {
				txtMsg.append("\n---모바일 데이타----------------->>>>>>\n");
				Cursor cur;
				cur = DBH.DB.rawQuery("SELECT COUNT(1) CNT FROM " + is_code, null);
				cur.moveToNext();
				ll_cntm = cur.getInt(cur.getColumnIndex("CNT"));
				txtMsg.append("총건수 : " + ll_cntm + "건\n");

				cur = DBH.selectAZ_MODI_DATE(is_code);
				if (cur.getCount() <= 0) {
					txtMsg.append("최종수정일 : 미수신\n");
				} else {
					cur.moveToNext();
					ls_dm = cur.getString(cur.getColumnIndex("MODI_DATE_M"));
					if (Util.f_isnull(ls_dm)) {
						txtMsg.append("최종수정일 : 미수신\n");
					} else {
						txtMsg.append("최종수정일 : " + String.format("%s년%s월%s일 (%s시%s분%s초)", ls_dm.substring(0, 4), ls_dm.substring(4, 6), ls_dm.substring(6, 8), ls_dm.substring(9, 11), ls_dm.substring(11, 13), ls_dm.substring(13, 15)) + "\n");
					}
				}
				cur.close();
			} catch (Exception e) {
				e.printStackTrace();
				txtMsg.append("모바일에서 데이타를 가져오는중에 오류가 발생하였습니다.\n");
			}

			if ((ll_cnt == ll_cntm) && (ls_d.equals(ls_dm))) {
				if (Util.f_isnull(ls_d) || Util.f_isnull(ls_dm)) {
					txtMsg.append("\n\n결과 : 미수신");
				} else {
					txtMsg.append("\n\n결과 : 일치");
				}
			} else {
				txtMsg.append("\n\n결과 : 불일치");
			}
		} finally {
			if (pd != null)
				pd.hide();
		}
	}

	public void fAZ_MODI_DATE() {
		try {
			f_web_select("SELECT TABLE_CODE, MODI_DATE FROM AZ_MODI_DATE");
			txtMsg.append("[결과 : " + gsDbRtn + " ----- 전체건수 : " + gjArr.length() + "-------------]\n");

			DBH.fCreate("AZ_MODI_DATE");
			for (int i = 0; i < gjArr.length(); i++) {
				JSONObject jObj = gjArr.getJSONObject(i);  //추출

				String resultStr = String.format("테이블코드 : %s  최종수정일 : %s\n",
						jObj.getString("TABLE_CODE"), jObj.getString("MODI_DATE"));

				txtMsg.append(resultStr);
				try {
					DBH.DB.execSQL(
							"UPDATE AZ_MODI_DATE" +
									"   SET MODI_DATE = '" + jObj.getString("MODI_DATE") + "'" +
									" WHERE TABLE_CODE = '" + jObj.getString("TABLE_CODE") + "'"
					);
				} catch (SQLiteException es) {
					DBH.DB.execSQL(
							"INSERT INTO AZ_MODI_DATE" +
									"           (TABLE_CODE, MODI_DATE)" +
									"    VALUES ('" + jObj.getString("TABLE_CODE") + "', '" + jObj.getString("MODI_DATE") + "')"
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Util.f_dialog(Detail.this, "서버에서 데이타를 가져오는중에 오류가 발생하였습니다.");
		} finally {
			if (pd != null)
				pd.hide();
		}
	}

	public void fHttpTest() {
		try {
			f_web_select("SELECT EMP_CODE, EMP_NAME FROM BE_DEPT_EMP");
			txtMsg.append("[결과 : " + gsDbRtn + " ----- 전체건수 : " + gjArr.length() + "-------------]\n");

			for (int i = 0; i < gjArr.length(); i++) {
				JSONObject jObj = gjArr.getJSONObject(i);  //추출

				String resultStr = String.format("사번 : %s  사원명 : %s\n",
						jObj.getString("EMP_CODE"), jObj.getString("EMP_NAME"));

				txtMsg.append(resultStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Util.f_dialog(Detail.this, "서버에서 데이타를 가져오는중에 오류가 발생하였습니다.");
		} finally {
			if (pd != null)
				pd.hide();
		}
	}

	public void f_web_select(String asSql) {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL("http://" + BaseActivity.WEB_HOST + "/bsHandax/oracle/gateway.php?p_hash=" + BaseActivity.P_HASH);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Accept", "text/xml");
			urlConnection.setRequestProperty("Content-Type", "application/xml");
			urlConnection.setDoOutput(true);

			asSql = asSql.replace("+", "┼"); //post로 넘길때 문자"+"가 깨져서 다른문자로 변환해서 넘김
			asSql = asSql.replace(", 'null'", ", null");

			StringBuilder sb = new StringBuilder();
			sb.append("{\"service\":\"SESSION\",");
			sb.append("\"className\":\"common.Session\",");
			sb.append("\"method\":\"f_user_select\",");
			sb.append("\"argus\":{");
			sb.append("\"p_sql\":\"" + asSql + "\"");
			sb.append("}}");

			try (OutputStream os = urlConnection.getOutputStream()) {
				os.write(sb.toString().getBytes("UTF-8"));
			}

			int responseCode = urlConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				try (InputStream is = urlConnection.getInputStream()) {
					String lsContent = convertStreamToString(is);
					int ll = lsContent.indexOf("W&s|") + 4;
					lsContent = lsContent.substring(ll);

					String gsDbRtn = lsContent.substring(0, 1);  //1:성공 0:실패
					String gsValue = lsContent.substring(1);

					JSONArray gjArr = new JSONArray(gsValue);
				}
			} else {
				Log.e(TAG, "Error in connection, response code: " + responseCode);
			}
		} catch (Exception e) {
			Log.e(TAG, "Exception in f_web_select", e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
