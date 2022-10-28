package jp.phoebus.mmsheet;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class mm_wepon {

	// データの保存用構造体クラス
	static class MM_Wepon_data
	{
		String name;		// 名称
		String mclass;		// 分類
		String type;		// タイプ
		String scope;		// 範囲
		String range;		// 射程
		String counter;		// 反撃
		String antiair;		// 対空
		String flying;		// 空中
		String daice;		// 攻撃判定
		String move;		// 移動修正
		String mp;		// ＭＰ
		String memo;		// 備考
		String eff_range;	// 有効射程
		String max_range;	// 最大射程
		String damage;		// ダメージ増減
		String dis_armor;	// 装甲無効
		String defense;		// 防御
		String recovery;	// 回復

		void clear()
		{
			name="";
			mclass="";
			type="";
			scope = "";
			range = "";
			counter = "";
			antiair = "";
			flying = "";
			daice = "";
			move = "";
			mp = "";
			memo = "";
			eff_range = "";
			max_range = "";
			damage = "";
			dis_armor = "";
			defense = "";
			recovery = "";
		}

		MM_Wepon_data()
		{
			clear();
		}
	}

	// データ保存用クラス配列
	private static ArrayList<MM_Wepon_data> mm_Wepon = new ArrayList<MM_Wepon_data>();

	static public int getListSize()
	{
		return mm_Wepon.size();
	}

	static public String getListName(int idx)
	{
		if(mm_Wepon.size() <= idx){
			// 範囲外
			return "";
		}
		return mm_Wepon.get(idx).name;
	}

	static public int getListMP(MM_Wepon_data mm_wepon)
	{
		if(pbslib.isdigit(mm_wepon.mp) != true){
			return 0;
		}
		return Integer.parseInt(mm_wepon.mp);
	}

	static public int getListMove(MM_Wepon_data mm_wepon)
	{
		if(pbslib.isdigit(mm_wepon.move) != true){
			return 0;
		}
		return Integer.parseInt(mm_wepon.move);
	}

	static public String getSelDispList(int idx)
	{
		if(mm_Wepon.size() <= idx){
			// 範囲外
			return "";
		}

		String str = mm_Wepon.get(idx).name;
		str += "　　MP：";
		str += Integer.toString(getListMP(mm_Wepon.get(idx)));

		return str;
	}

	// 名称からmm_Weponのテーブルインデックスを探す
	public static int serch_name(String entname)
	{
		String wname;

		// mm_Weponの０番目はインデックス名なので1オリジン
		for(int i = 1; i < mm_Wepon.size(); i++)
		{
			// 名前を引く
			wname=getListName(i);
			if(wname.indexOf(entname) >= 0){
				// 名前が含まれていれば見つかった扱いにして、そのインデックスを返す
				return i;
			}
		}

		// 見つからなければ０
		return 0;
	}

	// 特殊装備か否か。特殊装備ならtrue
	public static boolean isItem(int idx)
	{
		if(mm_Wepon.get(idx).type.equals("特殊")) {
			// 特殊装備は武器ではない扱いとする
			return true;
		}

		return false;
	}

	/*-----------------*/

	static public class MM_CharaWepon
	{
		MM_Wepon_data   wepon;
		int     idx;
		String  exname;
		View subview;

		void	clear()
		{
			this.idx = 0;
			this.exname = "";
			this.subview = null;
			this.wepon.clear();
		}

		MM_CharaWepon()
		{
			this.wepon = new mm_wepon.MM_Wepon_data();
			this.clear();
		}
	}


	static public int getMP(MM_CharaWepon cwepon)
	{
		if(pbslib.isdigit(cwepon.wepon.mp) != true){
			return 0;
		}
		return Integer.parseInt(cwepon.wepon.mp);
	}

	static public int getMove(MM_CharaWepon cwepon)
	{
		if(cwepon.wepon.move.equals("×")){
			// 移動不可武器は-3として扱う
			return -3;
		}
		if(pbslib.isdigit(cwepon.wepon.move) != true){
			return 0;
		}
		return Integer.parseInt(cwepon.wepon.move);
	}

	/*-----------------*/
	// エリアタイトル
	static public String getDispTitle(int ss)
	{
		String str;

		// タイトル文字列
		switch(ss){
			case 0:
				str = "武装A";
				break;
			case 1:
				str = "武装B";
				break;
			case 2:
				str = "武装C";
				break;
			default:
				str="";
				break;
		}

		return str;
	}

	// 名称
	public static String getDispName(MM_CharaWepon cwepon)
	{
		String str;

		str = cwepon.exname;
		if(str.length() < 1) {
			// exnameに内容が設定されてないならnameから名称を引く
			str = cwepon.wepon.name;
		}

		return str;
	}

	// MP
	public static String getDispMP(MM_CharaWepon cwepon)
	{
		String str;
		str = "MP：";
		str += Integer.toString(getMP(cwepon));
		return str;
	}

	// 移動修正
	public static String getDispmove(MM_CharaWepon cwepon)
	{
		String str;
		str = "移動修正：";
		if(cwepon.wepon.move.equals("×")){
			// 移動不可武器の場合
			str += "移動不可";
		} else {
			str += Integer.toString(getMove(cwepon));
		}
		return str;
	}

	// 分類
	public static String getDispmclass(MM_CharaWepon cwepon)
	{
		String str;
		str = "分類：";
		str += cwepon.wepon.mclass;
		return str;
	}

	// 射程
	public static String getDisprange(MM_CharaWepon cwepon)
	{
		String str;
		str = "射程：";
		str += cwepon.wepon.range;
		return str;
	}

	// 判定
	public static String getDispdaice(MM_CharaWepon cwepon)
	{
		String str;
		str = "判定：";
		str += cwepon.wepon.daice;
		return str;
	}

	// タイプ
	public static String getDisptype(MM_CharaWepon cwepon)
	{
		String str;
		str = "タイプ：";
		str += cwepon.wepon.type;
		return str;
	}

	// 範囲
	public static String getDispscope(MM_CharaWepon cwepon)
	{
		String str;
		str = "範囲：";
		str += cwepon.wepon.scope;
		return str;
	}

	// 反撃
	public static String getDispcounter(MM_CharaWepon cwepon)
	{
		String str;
		str = "反撃：";
		str += cwepon.wepon.counter;
		return str;
	}

	// 対空
	public static String getDispantiair(MM_CharaWepon cwepon)
	{
		String str;
		str = "対空：";
		str += cwepon.wepon.antiair;
		return str;
	}

	// 空中
	public static String getDispflying(MM_CharaWepon cwepon)
	{
		String str;
		str = "空中：";
		str += cwepon.wepon.flying;
		return str;
	}

	// 備考
	public static String getDispmemo(MM_CharaWepon cwepon)
	{
		String str;
		str = "備考：\n";
		if(cwepon.exname.length() > 0) {
			str += "（" + cwepon.wepon.name + "扱い）\n";
		}
		str += cwepon.wepon.memo;
		return str;
	}

	/*-----------------*/

	/*-----------------*/

	static public void writeline_one(CSVWriter writer, MM_CharaWepon cwepon, String wname, String widx)
	{
		MM_Wepon_data wepon = cwepon.wepon;
		String[] wename = new String[2];
		String[] wwepon = new String[19];

		wename[0] = wname;
		wename[1] = cwepon.exname;

		writer.writeNext(wename);

		// インデックス
		wwepon[0] = widx;
		// 1カラム：名称
		wwepon[1] = wepon.name;
		// 2カラム：分類
		wwepon[2] = wepon.mclass;
		// 3カラム：タイプ
		wwepon[3] = wepon.type;
		// 4カラム：範囲
		wwepon[4] = wepon.scope;
		// 5カラム：射程
		wwepon[5] = wepon.range;
		// 6カラム：反撃
		wwepon[6] = wepon.counter;
		// 7カラム：対空
		wwepon[7] = wepon.antiair;
		// 8カラム：空中
		wwepon[8] = wepon.flying;
		// 9カラム：攻撃判定
		wwepon[9] = wepon.daice;
		// 10カラム：移動修正
		wwepon[10] = wepon.move;
		// 11カラム：ＭＰ
		wwepon[11] = wepon.mp;
		// 12カラム：備考
		wwepon[12] = wepon.memo;
		// 13カラム：有効射程
		wwepon[13] = wepon.eff_range;
		// 14カラム：最大射程
		wwepon[14] = wepon.max_range;
		// 15カラム：ダメージ増減
		wwepon[15] = wepon.damage;
		// 16カラム：装甲無効
		wwepon[16] = wepon.dis_armor;
		// 17カラム：防御
		wwepon[17] = wepon.defense;
		// 18カラム：回復
		wwepon[18] = wepon.recovery;

		writer.writeNext(wwepon);
	}

	static public void writeline_wepon(CSVWriter writer, MM_CharaWepon cwepon, int idx)
	{
		String wname;
		String widx;

		wname = "WeponName"+Integer.toString(idx);
		widx = "Wepon"+Integer.toString(idx);

		writeline_one(writer, cwepon, wname, widx);
	}

	static public void writeline_item(CSVWriter writer, MM_CharaWepon cwepon)
	{
		writeline_one(writer, cwepon, "ItemName", "Item");
	}

	// データ内容のコピー
	static public void setList(int idx, MM_Wepon_data dst)
	{
		MM_Wepon_data src = mm_Wepon.get(idx);

		dst.name=src.name;
		dst.mclass=src.mclass;
		dst.type=src.type;
		dst.scope =src.scope;
		dst.range =src.range;
		dst.counter =src.counter;
		dst.antiair =src.antiair;
		dst.flying =src.flying;
		dst.daice =src.daice;
		dst.move =src.move;
		dst.mp =src.mp;
		dst.memo =src.memo;
		dst.eff_range =src.eff_range;
		dst.max_range =src.max_range;
		dst.damage =src.damage;
		dst.dis_armor =src.dis_armor;
		dst.defense =src.defense;
		dst.recovery =src.recovery;
	}

	static public void loadCSVline(MM_Wepon_data newdata, String nextLine[])
	{
		// 0カラム：空き

		// 1カラム：名称
		newdata.name = nextLine[1];
		// 2カラム：分類
		newdata.mclass = nextLine[2];
		// 3カラム：タイプ
		newdata.type = nextLine[3];
		// 4カラム：範囲
		newdata.scope = nextLine[4];
		// 5カラム：射程
		newdata.range = nextLine[5];
		// 6カラム：反撃
		newdata.counter = nextLine[6];
		// 7カラム：対空
		newdata.antiair = nextLine[7];
		// 8カラム：空中
		newdata.flying = nextLine[8];
		// 9カラム：攻撃判定
		newdata.daice = nextLine[9];
		// 10カラム：移動修正
		newdata.move = nextLine[10];
		// 11カラム：ＭＰ
		newdata.mp = nextLine[11];
		// 12カラム：備考
		newdata.memo = nextLine[12];
		// 13カラム：有効射程
		newdata.eff_range = nextLine[13];
		// 14カラム：最大射程
		newdata.max_range = nextLine[14];
		// 15カラム：ダメージ増減
		newdata.damage = nextLine[15];
		// 16カラム：装甲無効
		newdata.dis_armor = nextLine[16];
		// 17カラム：防御
		newdata.defense = nextLine[17];
		// 18カラム：回復
		newdata.recovery = nextLine[18];
	}

	// データをCSVから読み込み
	public static boolean load(Context context)
	{
		String [] nextLine;

		// 保存用エリア初期化
		mm_Wepon.clear();

		// AssetManagerの呼び出し
		AssetManager assetManager = context.getResources().getAssets();

		try {
			InputStream is = assetManager.open("m_m_wepon.csv");
			InputStreamReader inputStreamReader = new InputStreamReader(is);
			// opencsvで読み込み
			CSVReader reader = new CSVReader(inputStreamReader, ',', '"', '\\');

			while ((nextLine = reader.readNext()) != null) {

				// 保存用配列加算
				MM_Wepon_data newdata = new MM_Wepon_data();

				// 分離したcsv内容を設定
				loadCSVline(newdata, nextLine);

				// 登録
				mm_Wepon.add(newdata);
			}
		} catch(FileNotFoundException ex){
			return false;
		} catch(IOException ex){
			return false;
		}

		return true;
	}
}
