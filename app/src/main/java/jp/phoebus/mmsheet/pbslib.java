package jp.phoebus.mmsheet;

import android.content.Context;
import android.content.SharedPreferences;

public class pbslib {

	public static boolean isdigit(String data)
	{
		boolean isDigit;
		char cc;

		isDigit = false;
		for (int i = 0; i < data.length(); i++) {
			cc=data.charAt(i);
			if(cc=='-'){
				continue;
			}
			if(cc=='+'){
				continue;
			}

			isDigit = Character.isDigit(cc);
			if (!isDigit) {
				break;
			}
		}

		return isDigit;
	}


	/* プリファレンスへInt値を書き込み	*/
	static public void put_Ini_int(Context context, String inif, String tag, int wd)
	{
		SharedPreferences pref = context.getSharedPreferences(inif, context.MODE_PRIVATE);
		SharedPreferences.Editor e = pref.edit();
		e.putInt(tag,wd);
		e.commit();
	}

	/* プリファレンスからInt値を読み込み	*/
	static public int get_Ini_int(Context context, String inif, String tag)
	{
		SharedPreferences pref = context.getSharedPreferences(inif,context.MODE_PRIVATE);

		int dat = pref.getInt(tag,0);

		return dat;
	}
}
