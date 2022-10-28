package jp.phoebus.mmsheet;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
/*
	OpenCsvのクラスパス指定が必要なので、app/libsにopencsv-2.3.jarを入れ、app/build.gradleに
	implementation fileTree(include: ['*.jar'], dir: 'libs')
	implementation files('libs\\opencsv-2.3.jar')
	を追加して登録。
*/

public class mm_charadata {

	static public class MM_CharaData
	{
		String chara_name;             // キャラ名
		int     hp;                     // HP
		int     sp;                     // SP
		int     max_hp;                 // 最大HP
		int     max_sp;                 // 最大SP
		int     mp;                     // 合計MP
		int     move_point;             // 移動力
		int	setwepon;		// セット武器
		mm_attribute.Attri_data		attr;
		mm_wepon.MM_CharaWepon[] cwepon;
		ArrayList<mm_wepon.MM_CharaWepon> citem;
		ArrayList<mm_ability.MM_CharaAbility> ability;

		MM_CharaData()
		{
			this.chara_name = "";
			this.max_hp = this.hp = 6;
			this.max_sp = this.sp = 3;
			this.move_point = 4;
			this.mp = 0;
			this.setwepon = 0;
		}
	}

	// キャラ名設定
	static public void setCharaName(MM_CharaData mm_chara, String cname)
	{
		mm_chara.chara_name = cname;
	}

	// キャラ名取得
	static public String getCharaName(MM_CharaData mm_chara)
	{
		return mm_chara.chara_name;
	}

	// HP取得
	static public int getHP(MM_CharaData mm_chara)
	{
		return mm_chara.hp;
	}

	// SPの取得
	static public int getSP(MM_CharaData mm_chara)
	{
		return mm_chara.sp;
	}

	static public int getSetwepon(MM_CharaData mm_chara){
		if(mm_chara.setwepon != 1 && mm_chara.setwepon != 2){
			// 1,2以外は0扱い
			return 0;
		}
		return mm_chara.setwepon;
	}

	static public void setSetwepon(MM_CharaData mm_chara, int data){
		if (data != 1 && data != 2){
			// 1,2以外は0扱い
			data = 0;
		}
		mm_chara.setwepon = data;
	}

	static public int getSetweponMP(MM_CharaData mm_chara)
	{
		int data = getSetwepon(mm_chara);

		if(data == 1){
			// 1の場合はMP10
			return 10;
		} else if(data == 2){
			// 2の場合はMP15
			return 15;
		}

		// それ以外はMP0とする
		return 0;
	}

	/*-------------------------*/

	// プリファレンスへHP/SPを保存
	static public void save_playdata(Context context, int idx, MM_CharaData mm_chara)
	{
		String str;

		str = "HP_"+Integer.toString(idx);
		pbslib.put_Ini_int(context, "", str, mm_chara.hp);
		str = "SP_"+Integer.toString(idx);
		pbslib.put_Ini_int(context, "", str, mm_chara.sp);
	}

	// プリファレンスからHP/SPを読み込み
	static public void load_playdata(Context context, int idx, MM_CharaData mm_chara)
	{
		String str;

		str = "HP_"+Integer.toString(idx);
		mm_chara.hp = pbslib.get_Ini_int(context, "", str);
		str = "SP_"+Integer.toString(idx);
		mm_chara.sp = pbslib.get_Ini_int(context, "", str);
	}

	/*-------------------------*/

	// HP加算
	static public void HPinc(MM_CharaData mm_chara)
	{
		mm_chara.hp++;
		if(mm_chara.max_hp < mm_chara.hp) {
			mm_chara.hp = mm_chara.max_hp;
		}
	}

	// HP減算
	static public void HPdec(MM_CharaData mm_chara)
	{
		if(mm_chara.hp > 0){
			mm_chara.hp--;
		}
	}

	// HPリセット
	static public void HPreset(MM_CharaData mm_chara)
	{
		mm_chara.hp = mm_chara.max_hp;
	}

	// SP加算
	static public void SPinc(MM_CharaData mm_chara)
	{
		mm_chara.sp++;
		if(mm_chara.max_sp < mm_chara.sp) {
			mm_chara.sp = mm_chara.max_sp;
		}
	}

	// SP減算
	static public void SPdec(MM_CharaData mm_chara)
	{
		if(mm_chara.sp > 0){
			mm_chara.sp--;
		}
	}

	// SPリセット
	static public void SPreset(MM_CharaData mm_chara)
	{
		mm_chara.sp = mm_chara.max_sp;
	}

	/*-------------------------*/

	// HP最大再計算
	static public int MM_Recalc_HPmax(MM_CharaData mm_chara)
	{
		/*
		最大HP計算は特殊なので別関数とする
		*/
		int i;

		//　基本の最大HPは6
		int whpmax = 6;

		for(i = 0; i < mm_chara.ability.size(); i++){
			mm_ability.MM_CharaAbility wablity = mm_chara.ability.get(i);
			if(wablity.ability.name.equals("頑丈")){
				// 「頑丈」を持つ場合、HPの最大が８
				whpmax = 8;
			} else if(wablity.ability.name.equals("病弱")){
				// 「病弱」を持つ場合、HPの最大が5
				whpmax = 5;
			}
		}

		if(mm_chara.attr.name.equals("少女少年")){
			// 属性が「少女少年」の場合+1
			whpmax += 1;
		}

		mm_chara.max_hp = whpmax;

		return whpmax;
	}

	// MP再計算
	static public int MM_Recalc_MP(MM_CharaData mm_chara)
	{
		int     xmp;
		int     i;

		xmp = 0;
		xmp += mm_attribute.getMP(mm_chara.attr);
		xmp += mm_wepon.getMP(mm_chara.cwepon[0]);
		xmp += mm_wepon.getMP(mm_chara.cwepon[1]);
		xmp += mm_wepon.getMP(mm_chara.cwepon[2]);
		xmp += getSetweponMP(mm_chara);

		for(i = 0; i < mm_chara.citem.size(); i++){
			mm_wepon.MM_CharaWepon witem = mm_chara.citem.get(i);
			xmp += mm_wepon.getMP(witem);
		}

		for(i = 0; i < mm_chara.ability.size(); i++){
			mm_ability.MM_CharaAbility wablity = mm_chara.ability.get(i);
			xmp += mm_ability.getMP(wablity);
		}

		// 計算済みMP
		mm_chara.mp = xmp;

		return xmp;
	}

	// 移動力再計算
	static public int MM_Recalc_Move(MM_CharaData mm_chara)
	{
		int     xmov;
		int	i;

		xmov = 4;
		xmov += mm_wepon.getMove(mm_chara.cwepon[0]);
		xmov += mm_wepon.getMove(mm_chara.cwepon[1]);
		xmov += mm_wepon.getMove(mm_chara.cwepon[2]);

		for(i = 0; i < mm_chara.citem.size(); i++){
			mm_wepon.MM_CharaWepon witem = mm_chara.citem.get(i);
			xmov += mm_wepon.getMove(witem);
		}

		for(i = 0; i < mm_chara.ability.size(); i++){
			mm_ability.MM_CharaAbility wablity = mm_chara.ability.get(i);
			xmov += mm_ability.getMove(wablity);
		}

		//計算済み移動力
		if(xmov < 0){
			xmov = 0;
		}
		mm_chara.move_point = xmov;

		return xmov;
	}

	/*-------------------------*/

	// 表示用HP
	static public String getDispHP(MM_CharaData mm_chara)
	{
		String str;

		str = "HP：";
		str += Integer.toString(getHP(mm_chara));
		return str;
	}

	// 表示用SP
	static public String getDispSP(MM_CharaData mm_chara)
	{
		String str;
		str = "SP：";
		str += Integer.toString(getSP(mm_chara));
		return str;
	}
	static public String getDispMove(MM_CharaData mm_chara)
	{
		String str;
		int     mov;

		str = "移動力：";

		mov = mm_chara.move_point;
		if(mov < 1){
			// 移動力が１未満は「移動不可」の表記
			str += "移動不可";
			return str;
		}

		str += Integer.toString(mov);

		return str;
	}

	static public String getDispMP(MM_CharaData mm_chara)
	{
		String str;
		str = "合計MP：";
		str += Integer.toString(mm_chara.mp);
		return str;
	}

	static public String getDispSetweponMP(MM_CharaData mm_chara)
	{
		String str;

		str = "MP：";
		str += Integer.toString(getSetweponMP(mm_chara));

		return str;
	}


	/*-------------------------*/

	static public void writeline(CSVWriter writer, MM_CharaData mm_chara)
	{
		String wcname[] = new String[3];
		wcname[0] = "CharaName";
		wcname[1] = mm_chara.chara_name;
		wcname[2] = Integer.toString(mm_chara.setwepon);
		writer.writeNext(wcname);
	}
}
