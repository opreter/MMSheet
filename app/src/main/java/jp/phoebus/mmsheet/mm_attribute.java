package jp.phoebus.mmsheet;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/*
	OpenCsvのクラスパス指定が必要なので、app/libsにopencsv-2.3.jarを入れ、app/build.gradleに
	implementation fileTree(include: ['*.jar'], dir: 'libs')
	implementation files('libs\\opencsv-2.3.jar')
	を追加して登録。
*/
public class mm_attribute {

	// データの保存用構造体クラス
	static public class Attri_data {
		String name;
		String mp;
		String memo;

		Attri_data()
		{
			clear();
		}

		void clear()
		{
			this.name = "";
			this.mp = "0";
			this.memo = "";
		}
	}

	// データ保存用クラス配列
	private static ArrayList<Attri_data> mm_Attr = new ArrayList<Attri_data>();

	// 配列要素数
	public static int Listsize()
	{
		return mm_Attr.size();
	}
	// 保存用配列クリア
	public static void Listclear()
	{
		mm_Attr.clear();
	}

	// リストの名称を得る
	public static String getListName(int idx)
	{
		Attri_data wkdata;

		if(idx < 0){
			return "";
		} else
		if(mm_Attr.size() > idx) {
			wkdata = mm_Attr.get(idx);
			return getName(wkdata);
		} else {
			return "";
		}
	}

	static public String getSelDispList(int idx)
	{
		Attri_data wkdata;

		if(idx < 0){
			return "";
		} else
		if(mm_Attr.size() > idx) {
			String str;
			wkdata = mm_Attr.get(idx);
			str = getName(wkdata);
			str += "　　MP：";
			if(pbslib.isdigit(wkdata.mp)) {
				str += wkdata.mp;
			} else {
				str += "0";
			}
			return str;
		} else {
			return "";
		}
	}

	/*------*/

	// 名称の取得
	public static String getName(Attri_data attr)
	{
		return attr.name;
	}

	// 説明の取得
	public static String getMemo(Attri_data attr)
	{
		return attr.memo;
	}

	// MP取得
	public static int getMP(Attri_data attr)
	{
		if(pbslib.isdigit(attr.mp) != true){
			return 0;
		}
		return Integer.parseInt(attr.mp);
	}

	/*------*/

	public static String getDispName(Attri_data attr)
	{
		String str;
		str = "属性：";
		str += getName(attr);
		return str;
	}

	public static String getDispMP(Attri_data attr)
	{
		String str;
		str = "MP：";
		str += Integer.toString(getMP(attr));
		return str;
	}

	public static String getDispMemo(Attri_data attr)
	{
		String str;
		str = "効果：";
		str += getMemo(attr);
		return str;
	}

	/*------*/

	static public void writeline(CSVWriter writer, Attri_data attr)
	{
		String[] wattr = new String[4];

		wattr[0] = "Attribute";
		wattr[1] = attr.name;
		wattr[2] = attr.mp;
		wattr[3] = attr.memo;

		writer.writeNext(wattr);
	}

		// データ内容のコピー
	static public void setList(int idx, Attri_data dst)
	{
		Attri_data src = mm_Attr.get(idx);

		dst.name = src.name;
		dst.mp = src.mp;
		dst.memo = src.memo;
	}

	static public void loadCSVline(Attri_data attr, String line[])
	{
		// 2カラム目：名称
		attr.name = line[1];

		// 3カラム目：消費MP(文字列として登録)
		attr.mp = line[2];

		// 4カラム目：説明
		attr.memo = line[3];

	}

	// データをCSVから読み込み
	public static boolean load(Context context)
	{
		String [] nextLine;
		int     cnt;

		Listclear();

		// AssetManagerの呼び出し
		AssetManager assetManager = context.getResources().getAssets();

		try {
			InputStream is = assetManager.open("m_m_attribute.csv");
			InputStreamReader inputStreamReader = new InputStreamReader(is);
			// opencsvで読み込み
			CSVReader reader = new CSVReader(inputStreamReader,',','"','\\');

			cnt = 0;

			while ((nextLine = reader.readNext()) != null) {

				Attri_data attr = new Attri_data();

				// 分離したcsv内容を設定
				loadCSVline(attr, nextLine);

				while( mm_Attr.size() < (cnt + 1)){
					Attri_data newdata = new Attri_data();
					mm_Attr.add(newdata);
				}

				// 配列へ登録
				mm_Attr.set(cnt, attr);

				cnt++;
			}
		} catch(FileNotFoundException ex){
			return false;
		} catch(IOException ex){
			return false;
		}

		return true;
	}
}
