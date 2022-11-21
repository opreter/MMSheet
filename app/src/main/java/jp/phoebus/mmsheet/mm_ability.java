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

public class mm_ability {

	// データの保存用構造体クラス
	static public class MM_Abili_data {
		String name;		// 名称
		String timing;          // 使用タイミング
		String sp;              // 消費ＳＰ
		String memo;            // 効果
		String move;		// 移動修正
		String mp;		// 消費ＭＰ
		String damage;		// ダメージ増減
		String defense;		// 防御
		String recovery;	// 回復
		String mclass;		// 分類
		String terms;           // 使用条件

		void clear()
		{
			this.name = "";
			this.timing = "";
			this.sp = "";
			this.memo = "";
			this.move = "";
			this.mp = "";
			this.damage = "";
			this.defense = "";
			this.recovery = "";
			this.mclass = "";
			this.terms = "";
		}

		MM_Abili_data()
		{
			clear();
		}
	}

	// データ保存用クラス配列
	private static ArrayList<MM_Abili_data> mm_Ability = new ArrayList<MM_Abili_data>();

	static public int getListSize()
	{
		return mm_Ability.size();
	}

	static public String getListName(int idx)
	{
		if(mm_Ability.size() <= idx){
			// 範囲外
			return "";
		}
		return mm_Ability.get(idx).name;
	}

	// SP
	static public int getListSP(MM_Abili_data mm_ability)
	{
		if(pbslib.isdigit(mm_ability.sp) != true){
			return 0;
		}
		return Integer.parseInt(mm_ability.sp);
	}

	// MP
	static public int getListMP(MM_Abili_data mm_ability)
	{
		if(pbslib.isdigit(mm_ability.mp) != true){
			return 0;
		}
		return Integer.parseInt(mm_ability.mp);
	}

	// 移動力計算
	static public int getListMove(MM_Abili_data mm_ability)
	{
		if(pbslib.isdigit(mm_ability.move) != true){
			return 0;
		}
		return Integer.parseInt(mm_ability.move);
	}

	static public String getSelDispList(int idx)
	{
		if(mm_Ability.size() <= idx){
			// 範囲外
			return "";
		}

		String str = mm_Ability.get(idx).name;
		str += "　　MP：";
		str += Integer.toString(getListMP(mm_Ability.get(idx)));

		return str;
	}

	/*-----*/

	static public class MM_CharaAbility
	{
		MM_Abili_data   ability;
		View subview;

		void clear()
		{
			this.subview = null;
			this.ability.clear();
		}

		MM_CharaAbility()
		{
			this.ability = new MM_Abili_data();
			clear();
		}
	}

	// MP
	static public int getMP(MM_CharaAbility mm_ability)
	{
		if(pbslib.isdigit(mm_ability.ability.mp) != true){
			return 0;
		}
		return Integer.parseInt(mm_ability.ability.mp);
	}

	// 移動力計算
	static public int getMove(MM_CharaAbility mm_ability)
	{
		if(pbslib.isdigit(mm_ability.ability.move) != true){
			return 0;
		}
		return Integer.parseInt(mm_ability.ability.move);
	}

	/*-----*/

	// 名称
	static public String getDispName(MM_CharaAbility mm_ability)
	{
		String str;
		//str = "名称：";
		//str += mm_ability.ability.name;
		str = mm_ability.ability.name;
		return str;
	}

	// SP
	public static String getDispSP(MM_CharaAbility mm_ability)
	{
		String str;
		str = "SP：";
		if(mm_ability.ability.sp.length() < 1){
			// 内容が空白の場合は「－」とする
			str += "－";
		} else {
			str += mm_ability.ability.sp;
		}
		return str;
	}

	// MP
	public static String getDispMP(MM_CharaAbility mm_ability)
	{
		String str;
		str = "MP：";
		str += Integer.toString(getMP(mm_ability));
		return str;
	}

	// 分類
	static public String getDispMclass(MM_CharaAbility mm_ability)
	{
		String str;
		str = "分類：";
		str += mm_ability.ability.mclass;
		return str;
	}

	// 使用タイミング
	static public String getDispTiming(MM_CharaAbility mm_ability)
	{
		String str;
		str = "使用タイミング：";
		str += mm_ability.ability.timing;
		return str;
	}

	// 使用条件
	static public String getDispTerms(MM_CharaAbility mm_ability)
	{
		String str;
		str = "使用条件：";
		str += mm_ability.ability.terms;
		return str;
	}

	// 備考
	public static String getDispmemo(MM_CharaAbility mm_ability)
	{
		String str;
		str = "効果等：\n";
		str += mm_ability.ability.memo;
		return str;
	}

	/*-----*/

	static public void writeline(CSVWriter writer, MM_Abili_data newdata)
	{
		String[] wabili = new String[12];

		wabili[0] = "Ability";

		// 2カラム　名称
		wabili[1] = newdata.name;

		// 3カラム　使用タイミング
		wabili[2] = newdata.timing;

		// 4カラム　消費ＳＰ
		wabili[3] = newdata.sp;

		// 5カラム　効果
		wabili[4] = newdata.memo;

		// 6カラム　移動修正
		wabili[5] = newdata.move;

		// 7カラム　消費ＭＰ
		wabili[6] = newdata.mp;

		// 8カラム　ダメージ増減
		wabili[7] = newdata.damage;

		// 9カラム　防御
		wabili[8] = newdata.defense;

		// 10カラム　回復
		wabili[9] = newdata.recovery;

		// 11カラム　分類
		wabili[10] = newdata.mclass;

		// 12カラム　使用条件
		wabili[11] = newdata.terms;

		writer.writeNext(wabili);
	}
		// データ内容のコピー
	static public void setList(int idx, MM_Abili_data dst)
	{
		MM_Abili_data src = mm_Ability.get(idx);

		dst.name = src.name;
		dst.timing = src.timing;
		dst.sp = src.sp;
		dst.memo = src.memo;
		dst.move = src.move;
		dst.mp = src.mp;
		dst.damage = src.damage;
		dst.defense = src.defense;
		dst.recovery = src.recovery;
		dst.mclass = src.mclass;
		dst.terms = src.terms;
	}

	static public void loadCSVline(MM_Abili_data newdata, String nextLine[])
	{
		// 1カラム目は読み飛ばし

		// 2カラム　名称
		newdata.name = nextLine[1];
		// 3カラム　使用タイミング
		newdata.timing = nextLine[2];

		// 4カラム　消費ＳＰ
		newdata.sp = nextLine[3];

		// 5カラム　効果
		newdata.memo = nextLine[4];

		// 6カラム　移動修正
		newdata.move = nextLine[5];

		// 7カラム　消費ＭＰ
		newdata.mp = nextLine[6];

		// 8カラム　ダメージ増減
		newdata.damage = nextLine[7];

		// 9カラム　防御
		newdata.defense = nextLine[8];

		// 10カラム　回復
		newdata.recovery = nextLine[9];

		// 11カラム　分類
		newdata.mclass = nextLine[10];

		// 12カラム　使用条件
		newdata.terms = nextLine[11];
	}

	// データをCSVから読み込み
	public static boolean load(Context context)
	{
		String [] nextLine;
		int     cnt;

		// AssetManagerの呼び出し
		AssetManager assetManager = context.getResources().getAssets();

		try {
			InputStream is = assetManager.open("m_m_ability.csv");
			InputStreamReader inputStreamReader = new InputStreamReader(is);
			// opencsvで読み込み
			CSVReader reader = new CSVReader(inputStreamReader,',','"','\\');

			cnt = 0;
			mm_Ability.clear();

			while ((nextLine = reader.readNext()) != null) {
				if(cnt == 0){
					// 1行目はダミーなので読み飛ばす
					cnt++;
					continue;
				}

				// 保存用配列加算
				MM_Abili_data newdata = new MM_Abili_data();

				loadCSVline(newdata, nextLine);

				// 登録
				mm_Ability.add(newdata);
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
