package jp.phoebus.mmsheet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class mm_charasheet {

	static class MM_Sheet {
		View view;			// ベースView
		LayoutInflater inflater;        // レイアウトinflater
		LinearLayout weponview;         // 武装表示用View
		LinearLayout itemview;          // 装備品表示用View
		LinearLayout abilityview;       // 能力表示用View;
		mm_charadata.MM_CharaData	cdata;		// キャラデータ
		String	tmppath;		// 一時保存パス
		Uri uripath;			// 保存パス

		ExpandableLayout expand_attr;
		LinearLayout attrview;
		boolean isSel_attr;

		ExpandableLayout[] expand_wepon = new ExpandableLayout[3];
		LinearLayout[] weponexview = new LinearLayout[3];
		boolean[] isSel_wepon = new boolean[3];

		ArrayList<ExpandableLayout> expand_item = new ArrayList<ExpandableLayout>();
		ArrayList<LinearLayout> itemexview = new ArrayList<LinearLayout>();
		ArrayList<Integer> isSel_Item = new ArrayList<Integer>();

		ArrayList<ExpandableLayout> expand_ability = new ArrayList<ExpandableLayout>();
		ArrayList<LinearLayout> abilityexview = new ArrayList<LinearLayout>();
		ArrayList<Integer> isSel_ability = new ArrayList<Integer>();

		MM_Sheet()
		{
			view = null;
			this.cdata = new mm_charadata.MM_CharaData();
			this.cdata.attr = new mm_attribute.Attri_data();
			this.cdata.cwepon = new mm_wepon.MM_CharaWepon[3];
			this.cdata.cwepon[0] = new mm_wepon.MM_CharaWepon();
			this.cdata.cwepon[1] = new mm_wepon.MM_CharaWepon();
			this.cdata.cwepon[2] = new mm_wepon.MM_CharaWepon();
			this.cdata.citem = new ArrayList<mm_wepon.MM_CharaWepon>();
			this.cdata.citem.clear();
			this.cdata.ability = new ArrayList<mm_ability.MM_CharaAbility>();
			this.cdata.ability.clear();
			this.tmppath = new String();
			this.uripath = Uri.parse("");
		}
	}

	// 一時保存ファイルパス設定
	static public void setTmpPath(MM_Sheet mm_sheet, String tmppath)
	{
		mm_sheet.tmppath = tmppath;
	}

	static public String getTmpPath(MM_Sheet mm_sheet)
	{
		return mm_sheet.tmppath;
	}

	/*-------------------------*/

	// キャラ名表示
	static public void DispCharaName(View view, MM_Sheet mm_sheet)
	{
		String dname;
		dname = mm_charadata.getCharaName(mm_sheet.cdata);
		TextView txa_1 = view.findViewById(R.id.mk_chara_name);
		txa_1.setText(dname);
	}

	// HP表示
	static private void DispHP(View view, MM_Sheet mm_sheet)
	{
		TextView tview2;
		tview2=(TextView)view.findViewById(R.id.hp_count);
		tview2.setText(mm_charadata.getDispHP(mm_sheet.cdata));
	}

	// SP表示
	static private void DispSP(View view, MM_Sheet mm_sheet)
	{
		TextView tview3;
		tview3=(TextView)view.findViewById(R.id.sp_count);
		tview3.setText( mm_charadata.getDispSP(mm_sheet.cdata));
	}


	// 移動力の表示
	static public void DispMovePoint(View view, MM_Sheet mm_sheet)
	{
		TextView tview;
		tview=(TextView)view.findViewById(R.id.movepoint);
		tview.setText(mm_charadata.getDispMove(mm_sheet.cdata));
	}

	// 合計MPの表示
	static public void DispMP(View view, MM_Sheet mm_sheet)
	{
		TextView tview1;
		tview1=(TextView)view.findViewById(R.id.mp_count);
		tview1.setText(mm_charadata.getDispMP(mm_sheet.cdata));
	}

	static private  void DispAttr(View view, MM_Sheet mm_sheet)
	{
		TextView tview1 = (TextView) view.findViewById(R.id.textView_att_name);
		tview1.setText(mm_attribute.getDispName(mm_sheet.cdata.attr));

		TextView tview2 = (TextView) view.findViewById(R.id.textView_att_mp);
		tview2.setText(mm_attribute.getDispMP(mm_sheet.cdata.attr));

		TextView tview3 = (TextView) view.findViewById(R.id.textView_att_memo);
		tview3.setText(mm_attribute.getDispMemo(mm_sheet.cdata.attr));
	}

	// 武装エリア表示
	static public void DispWepon_one(View view, mm_wepon.MM_CharaWepon mm_cwepon)
	{
		View subView = mm_cwepon.subview;
		String str;

		// タイトル設定
		//TextView txa_0 = subView.findViewById(R.id.textView_wepon_title);
		//str = mm_wepon.getDispTitle(mm_cwepon.idx);
		//txa_0.setText(str);

		// 名称
		TextView txa_1 = subView.findViewById(R.id.textView_wepon_name);
		str = mm_wepon.getDispName(mm_cwepon);
		txa_1.setText(str);

		// MP
		TextView tx_mp = subView.findViewById(R.id.textView_wepon_MP);
		str = mm_wepon.getDispMP(mm_cwepon);
		tx_mp.setText(str);

		// 移動修正
		TextView tx_move = subView.findViewById(R.id.textView_wepon_move);
		str = mm_wepon.getDispmove(mm_cwepon);
		tx_move.setText(str);

		// 分類
		TextView tx_mclass = subView.findViewById(R.id.textView_wepon_mclass);
		str = mm_wepon.getDispmclass(mm_cwepon);
		tx_mclass.setText(str);

		// 射程
		TextView txa_2 = subView.findViewById(R.id.textView_wepon_range);
		str = mm_wepon.getDisprange(mm_cwepon);
		txa_2.setText(str);

		// 判定
		TextView txa_3 = subView.findViewById(R.id.textView_wepon_daice);
		str = mm_wepon.getDispdaice(mm_cwepon);
		txa_3.setText(str);

		// タイプ
		TextView txa_4 = subView.findViewById(R.id.textView_wepon_type);
		str = mm_wepon.getDisptype(mm_cwepon);
		txa_4.setText(str);

		// 範囲
		TextView txa_5 = subView.findViewById(R.id.textView_wepon_scope);
		str=mm_wepon.getDispscope(mm_cwepon);
		txa_5.setText(str);

		// 反撃
		TextView txa_6 = subView.findViewById(R.id.textView_wepon_counter);
		str=mm_wepon.getDispcounter(mm_cwepon);
		txa_6.setText(str);

		// 対空
		TextView txa_7 = subView.findViewById(R.id.textView_wepon_antiair);
		str=mm_wepon.getDispantiair(mm_cwepon);
		txa_7.setText(str);

		// 空中
		TextView txa_8 = subView.findViewById(R.id.textView_wepon_flying);
		str=mm_wepon.getDispflying(mm_cwepon);
		txa_8.setText(str);

		// 備考
		TextView txa_9 = subView.findViewById(R.id.textView_wepon_memo);
		str=mm_wepon.getDispmemo(mm_cwepon);
		txa_9.setText(str);
	}

	static public void DispWepon(View view, mm_charasheet.MM_Sheet mm_sheet)
	{
		DispWepon_one(view, mm_sheet.cdata.cwepon[0]);
		DispWepon_one(view, mm_sheet.cdata.cwepon[1]);
		DispWepon_one(view, mm_sheet.cdata.cwepon[2]);
	}

	static public void DispSetwepon(View view, MM_Sheet mm_sheet)
	{
		Spinner spinner = view.findViewById(R.id.spinner_setwepon);
		spinner.setSelection(mm_charadata.getSetwepon(mm_sheet.cdata));

		TextView wktxt = view.findViewById(R.id.setwepon_mp);
		String str = mm_charadata.getDispSetweponMP(mm_sheet.cdata);
		wktxt.setText(str);
	}

	static public void DispItem_one(View view, mm_wepon.MM_CharaWepon mm_items)
	{
		View subView = mm_items.subview;
		String str;

		// 名称
		TextView txa_1 = subView.findViewById(R.id.textView_item_name);
		str = mm_wepon.getDispName(mm_items);
		txa_1.setText(str);

		// 移動修正
		TextView tx_move = subView.findViewById(R.id.textView_item_move);
		str = mm_wepon.getDispmove(mm_items);
		tx_move.setText(str);

		// MP
		TextView tx_mp = subView.findViewById(R.id.textView_item_mp);
		str = mm_wepon.getDispMP(mm_items);
		tx_mp.setText(str);

		// 備考
		TextView txa_9 = subView.findViewById(R.id.textView_item_memo);
		str=mm_wepon.getDispmemo(mm_items);
		txa_9.setText(str);
	}

	static public void DispItem(View view, mm_charasheet.MM_Sheet mm_sheet)
	{
		int     i;

		for(i = 0; i < mm_sheet.cdata.citem.size(); i++){
			mm_wepon.MM_CharaWepon mm_items = mm_sheet.cdata.citem.get(i);
			DispItem_one(view, mm_items);
		}
	}

	static public void DispAbility_one(View view, mm_ability.MM_CharaAbility mm_Ability)
	{
		View subView = mm_Ability.subview;
		String str;

		// 名称
		TextView txa_1 = subView.findViewById(R.id.textView_ability_name);
		str = mm_ability.getDispName(mm_Ability);
		txa_1.setText(str);

		// SP
		TextView tx_sp = subView.findViewById(R.id.textView_ability_sp);
		str = mm_ability.getDispSP(mm_Ability);
		tx_sp.setText(str);

		// MP
		TextView tx_mp = subView.findViewById(R.id.textView_ability_mp);
		str = mm_ability.getDispMP(mm_Ability);
		tx_mp.setText(str);

		// 分類
		TextView txa_2 = subView.findViewById(R.id.textView_ability_mclass);
		str = mm_ability.getDispMclass(mm_Ability);
		txa_2.setText(str);

		// 使用タイミング
		TextView txa_3 = subView.findViewById(R.id.textView_ability_timing);
		str = mm_ability.getDispTiming(mm_Ability);
		txa_3.setText(str);

		// 使用条件
		TextView txa_4 = subView.findViewById(R.id.textView_ability_terms);
		str = mm_ability.getDispTerms(mm_Ability);
		txa_4.setText(str);

		// 備考
		TextView txa_9 = subView.findViewById(R.id.textView_ability_memo);
		str=mm_ability.getDispmemo(mm_Ability);
		txa_9.setText(str);
	}

	static public void DispAbility(View view, mm_charasheet.MM_Sheet mm_sheet)
	{
		int     i;

		for(i = 0; i < mm_sheet.cdata.ability.size(); i++){
			mm_ability.MM_CharaAbility mm_ability = mm_sheet.cdata.ability.get(i);
			DispAbility_one(view, mm_ability);
		}
	}

	// 全表示
	static public void redisp(View view, MM_Sheet mm_sheet) {

		// MP再計算
		mm_charadata.MM_Recalc_MP(mm_sheet.cdata);

		// 移動力再計算
		mm_charadata.MM_Recalc_Move(mm_sheet.cdata);

		DispCharaName(view, mm_sheet);
		DispHP(view, mm_sheet);
		DispSP(view, mm_sheet);
		DispMovePoint(view, mm_sheet);
		DispMP(view, mm_sheet);
		DispSetwepon(view, mm_sheet);
		DispAttr(view, mm_sheet);
		DispWepon(view, mm_sheet);
		DispItem(view, mm_sheet);
		DispAbility(view, mm_sheet);
	}

	/*-------------------------*/

	// キャラ名変更ボタンのOnClick処理(ダイアログ表示・内容入力変更)
	static public void OnClick_EditCharaName(Context context , View scrview, MM_Sheet mm_sheet)
	{
		String str = "";

		// 現在設定されている内容を得る
		str = mm_charadata.getCharaName(mm_sheet.cdata);

		//テキスト入力を受け付けるビューを作成します。
		final EditText editView = new EditText(context);

		// 現在の設定内容を設定
		editView.setText(str);

		// ダイアログを作成
		new AlertDialog.Builder(context)
			// タイトル文字列
			.setTitle("キャラ名")
			//setViewにてビューを設定します。
			.setView(editView)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				// OKボタン処理
				public void onClick(DialogInterface dialog, int whichButton) {
					String str;
					str=editView.getText().toString();

					// キャラ名を設定
					mm_charadata.setCharaName(mm_sheet.cdata, str);

					// 再表示
					DispCharaName(scrview, mm_sheet);

					// 一時保存
					tmpsave(mm_sheet);
				}
			})
			.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
				// キャンセルボタン処理
				public void onClick(DialogInterface dialog, int whichButton) {
					// キャンセルはなにもしない
				}
			})
			.show();
	}

	// HPのinc
	static public void OnClick_HP_inc(View scrview, MM_Sheet mm_sheet)
	{
		mm_charadata.HPinc(mm_sheet.cdata);
		DispHP(scrview, mm_sheet);
	}

	// HPのdec
	static public void OnClick_HP_dec(View scrview, MM_Sheet mm_sheet)
	{
		mm_charadata.HPdec(mm_sheet.cdata);
		DispHP(scrview, mm_sheet);
	}

	// HPのリセット
	static public void OnClick_HP_reset(View scrview, MM_Sheet mm_sheet)
	{
		mm_charadata.HPreset(mm_sheet.cdata);
		DispHP(scrview, mm_sheet);
	}

	// SPのinc
	static public void OnClick_SP_inc(View scrview, MM_Sheet mm_sheet)
	{
		mm_charadata.SPinc(mm_sheet.cdata);
		DispSP(scrview, mm_sheet);
	}

	// SPのdec
	static public void OnClick_SP_dec(View scrview, MM_Sheet mm_sheet)
	{
		mm_charadata.SPdec(mm_sheet.cdata);
		DispSP(scrview, mm_sheet);
	}

	// SPのリセット
	static public void OnClick_SP_reset(View scrview, MM_Sheet mm_sheet)
	{
		mm_charadata.SPreset(mm_sheet.cdata);
		DispSP(scrview, mm_sheet);
	}

	/*-------------------------*/

	// 属性変更ダイアログ
	static public void OnClick_EditAttr(Context context, View scrview, mm_charasheet.MM_Sheet mm_sheet)
	{
		String str = "";
		int i;

		// ダイアログを生成
		AlertDialog m_Dlg = new AlertDialog.Builder(context).create();

		/* リストビューを宣言    */
		ListView lv = new ListView(context);

		/* 項目内容登録用アダプタ  */
		final ArrayList<String> rows = new ArrayList<String>();

		// アダプタへ登録。0番目は名前なので1から始める
		for(i = 1; i < mm_attribute.Listsize(); i++){
			str = mm_attribute.getSelDispList(i);
			rows.add(str);
		}

		/* リストビューへ項目内容登録    */
		lv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, rows){
			/* 文字色変更のため、ArrayAdapterをオーバーライド  */
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView view = (TextView)super.getView(position, convertView, parent);

				/* 文字食を黒に設定 */
				view.setTextColor(Color.BLACK);

				return view;
			}
		});

		/* 背景色を白に設定 */
		lv.setBackgroundColor(Color.WHITE);

		// スクロール許可
		lv.setScrollingCacheEnabled(true);

		/* 項目選択時の動作設定   */
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> items, View view, int position, long id) {
				/* ダイアログ消去  */
				m_Dlg.dismiss();

				// 1オリジンなので+1
				position++;
				if(position < 1){
					// 0番目は無視
					return;
				}

				// 選択したデータを展開
				mm_attribute.setList(position, mm_sheet.cdata.attr);

				// HP最大再計算
				mm_charadata.MM_Recalc_HPmax(mm_sheet.cdata);
				// HPリセット
				mm_charadata.HPreset(mm_sheet.cdata);

				// 再表示
				redisp(scrview, mm_sheet);

				// 一時保存
				tmpsave(mm_sheet);
			}
		});

		// ダイアログタイトル
		m_Dlg.setTitle("属性");

		// 表示内容(リスト)を登録
		m_Dlg.setView(lv);

		/* ダイアログ表示  */
		m_Dlg.show();
	}

	// スピナーへ武装内容登録
	public static ArrayList<Integer> set_wepondrop(Context context, Spinner spinner, boolean fwepon)
	{
		int i;

		// 変換用テーブルList
		ArrayList<Integer> selidx = new ArrayList<Integer>();

		// アダプタ設定。パターンはデフォルトのandroid.R.layout.simple_spinner_item
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
		// ドロップダウンのレイアウトはデフォルトにする
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		selidx.clear();

		// ０番目は未設定用にする
		adapter.add("(なし)");
		selidx.add(0);

		for(i = 1; i < mm_wepon.getListSize(); i++){
			if (fwepon == true){
				// fweponがtrueなら、特殊武器は武装としない
				if(mm_wepon.isItem(i)){
					// 特殊装備は武器としてカウントしない
					continue;
				}
 			} else {
				// fweponがfalseなら、特殊武器以外を装備品とする
				if(!mm_wepon.isItem(i)){
					// 特殊装備以外は装備品としてカウントしない
					continue;
				}
			}

			// 名称をスピナーのアダプタに登録
			adapter.add(mm_wepon.getSelDispList(i));
			selidx.add(i);
		}

		// アダプタの内容をスピナーへ登録
		spinner.setAdapter(adapter);

		// selidxを返す。
		return selidx;
	}

	// 名称からspinnerのテーブルインデックスを探す(SpinnerIDを渡すのでwepon/itemどっちもいけるはず)
	public static int serch_spinner(Spinner spinner, String entname)
	{
		String wname;

		// spinnerのアダプタを得る
		Adapter adp = (ArrayAdapter)spinner.getAdapter();

		if (entname.equals("")){
			// 中身が未設定であれば０
			return 0;
		}

		// spinnerの０番目はインデックス名なので1オリジン
		for(int i = 1; i < adp.getCount() ; i++)
		{
			// 名前を引く
			wname=adp.getItem(i).toString();

			if(wname.indexOf(entname) >= 0){
				// 名前が含まれていれば見つかった扱いにして、そのインデックスを返す
				return i;
			}
		}

		// 見つからなければ０
		return 0;
	}

	static public void OnClick_EditWepon(Context context, View scrview, MM_Sheet mm_sheet, int wsel)
	{
		LayoutInflater factory = LayoutInflater.from(context);
		int	widx;
		String str;
		ArrayList<Integer> sidx;

		// Viewを設定。内容はレイアウトxmlから取得する
		final View inputView = factory.inflate(R.layout.mm_wepon_entry, null);

		// スピナーを生成。レイアウトxmlのスピナーを指定
		Spinner spinner = (Spinner)inputView.findViewById(R.id.wepon_spinner);

		// スピナーへ内容展開
		sidx = set_wepondrop(context, spinner, true);
		// スピナーのリスナーは不要。Okボタン押下時に確定するので

		// 設定済みデータの武装名称
		str = mm_sheet.cdata.cwepon[wsel].wepon.name;

		// 武装名称から武装スピナーを検索
		widx = serch_spinner(spinner, str);

		// スピナーを選択
		spinner.setSelection(widx);

		// 別名内容をテキスト入力に設定
		EditText ename_edit = (EditText)inputView.findViewById(R.id.wepon_name_edit);
		ename_edit.setText(mm_sheet.cdata.cwepon[wsel].exname);

		// ダイアログ内容設定
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("武装");
		builder.setView(inputView);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				String str;
				int	idx;

				// 変換テーブルを元に武装リストの番号を得る
				idx = sidx.get(spinner.getSelectedItemPosition());
				if(idx < 1){
					// idxが０の場合は内容を消去する。
					// exnameとweponのみ消去。cwepon[]をcloearすると表示用Viewまで消えるのでダメ。
					mm_sheet.cdata.cwepon[wsel].exname = "";
					mm_sheet.cdata.cwepon[wsel].wepon.clear();

					// 再表示
					redisp(scrview, mm_sheet);

					// 一時保存
					tmpsave(mm_sheet);

					return;
				}

				// リスト内容を設定
				mm_wepon.setList(idx, mm_sheet.cdata.cwepon[wsel].wepon);

				// 別名を設定
				str = ename_edit.getText().toString();
				mm_sheet.cdata.cwepon[wsel].exname = str;

				// 再表示
				redisp(scrview, mm_sheet);

				// 一時保存
				tmpsave(mm_sheet);
			}
		});

		builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// ダイアログ作成
		AlertDialog dialog = builder.create();

		// ダイアログ表示
		dialog.show();

	}

	static public void OnItemSelected_Setwepon(Context context, View scrview, MM_Sheet mm_sheet, int pos)
	{
		TextView wkmp = scrview.findViewById(R.id.setwepon_mp);

		mm_charadata.setSetwepon(mm_sheet.cdata, pos);

		// 再表示
		redisp(scrview, mm_sheet);

		// 一時保存
		tmpsave(mm_sheet);

	}

	static public void OnClick_AddItem(Context context, View scrview, MM_Sheet mm_sheet)
	{
		LayoutInflater factory = LayoutInflater.from(context);
		int	widx;
		String str;
		ArrayList<Integer> sidx;

		// Viewを設定。内容はレイアウトxmlから取得する
		final View inputView = factory.inflate(R.layout.mm_wepon_entry, null);

		// スピナーを生成。レイアウトxmlのスピナーを指定
		Spinner spinner = (Spinner)inputView.findViewById(R.id.wepon_spinner);

		// スピナーへ内容展開
		sidx = set_wepondrop(context, spinner, false);

		// 別名内容をテキスト入力に設定
		EditText ename_edit = (EditText)inputView.findViewById(R.id.wepon_name_edit);
		ename_edit.setText("");

		// ダイアログ内容設定
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("装備品");
		builder.setView(inputView);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				String str;
				int	idx;

				// 変換テーブルを元に武装リストの番号を得る
				idx = sidx.get(spinner.getSelectedItemPosition());
				if(idx < 1){
					// idxが０の場合は登録しない。キャンセル扱い
					return;
				}

				// 別名を取得
				str = ename_edit.getText().toString();

				// 登録
				addItem(mm_sheet, idx, str);

				// 折りたたみ再設定
				setOnExpandProc(mm_sheet);

				// 再表示
				redisp(scrview, mm_sheet);

				// 一時保存
				tmpsave(mm_sheet);
			}
		});

		builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// ダイアログ作成
		AlertDialog dialog = builder.create();

		// ダイアログ表示
		dialog.show();

	}

	// 装備品の削除処理
	static public void OnClick_DelItem(Context context, View scrview, MM_Sheet mm_sheet)
	{
		int	i;
		int	cnt = 0;

		for(i = 0; i < mm_sheet.cdata.citem.size(); i++){
			// 対象の装備品情報を得る
			mm_wepon.MM_CharaWepon wk = mm_sheet.cdata.citem.get(i);
			View subview = wk.subview;
			// チェックボックスコントロールを得る
			CheckBox fchk = subview.findViewById(R.id.item_checkBox);
			if(fchk.isChecked() == true){
				// チェックされた数を数える
				cnt++;
			}
		}

		if(cnt < 1){
			// チェックされたのが1つもなければなにもしない
			return;
		}

		for(i = mm_sheet.cdata.citem.size() - 1; i >= 0; i--){
			// ループは逆からループする
			// 対象の装備品情報を得る
			mm_wepon.MM_CharaWepon wk = mm_sheet.cdata.citem.get(i);
			View subview = wk.subview;
			// チェックボックスコントロールを得る
			CheckBox fchk = subview.findViewById(R.id.item_checkBox);
			if(fchk.isChecked() == true){
				// 削除対象
				delItem(mm_sheet, i);
			}
		}

		// 折りたたみ再設定
		setOnExpandProc(mm_sheet);

		// 再表示
		redisp(scrview, mm_sheet);

		// 一時保存
		tmpsave(mm_sheet);
	}

	// 技能追加
	static public void OnClick_AddAbility(Context context, View scrview, MM_Sheet mm_sheet)
	{
		LayoutInflater factory = LayoutInflater.from(context);
		int	widx;
		String str;
		int	i;

		// Viewを設定。内容はレイアウトxmlから取得する
		final View inputView = factory.inflate(R.layout.mm_wepon_entry, null);

		/* リストビューを宣言    */
		ListView lv = new ListView(context);

		/* 項目内容登録用アダプタ  */
		final ArrayList<String> rows = new ArrayList<String>();

		// アダプタへ登録。0番目は名前なので1から始める
		for(i = 1; i < mm_ability.getListSize(); i++){
			rows.add(mm_ability.getSelDispList(i));
		}

		/* リストビューへ項目内容登録    */
		lv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, rows){
			/* 文字色変更のため、ArrayAdapterをオーバーライド  */
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView view = (TextView)super.getView(position, convertView, parent);

				/* 文字食を黒に設定 */
				view.setTextColor(Color.BLACK);

				return view;
			}
		});

		// ダイアログ内容設定
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("技能/特殊能力");
		builder.setView(lv);

		/* 背景色を白に設定 */
		lv.setBackgroundColor(Color.WHITE);

		// スクロール許可
		lv.setScrollingCacheEnabled(true);

		// ダイアログ作成
		AlertDialog dialog = builder.create();

		/* 項目選択時の動作設定   */
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> items, View view, int position, long id) {
				/* ダイアログ消去  */
				dialog.dismiss();

				// 1オリジンなので+1
				position++;
				if(position < 1){
					// 0番目は無視
					return;
				}

				// 登録
				addAbility(mm_sheet, position);

				// 折りたたみ再設定
				setOnExpandProc(mm_sheet);

				// 再表示
				redisp(scrview, mm_sheet);

				// 一時保存
				tmpsave(mm_sheet);
			}
		});

		// ダイアログ表示
		dialog.show();

	}

	// 技能の削除処理
	static public void OnClick_DelAbility(Context context, View scrview, MM_Sheet mm_sheet)
	{
		int	i;
		int	cnt = 0;

		for(i = 0; i < mm_sheet.cdata.ability.size(); i++){
			// 対象の技能情報を得る
			mm_ability.MM_CharaAbility wk = mm_sheet.cdata.ability.get(i);
			View subview = wk.subview;
			// チェックボックスコントロールを得る
			CheckBox fchk = subview.findViewById(R.id.checkBox_ability);
			if(fchk.isChecked() == true){
				// チェックされた数を数える
				cnt++;
			}
		}

		if(cnt < 1){
			// チェックされたのが1つもなければなにもしない
			return;
		}

		for(i = mm_sheet.cdata.ability.size() - 1; i >= 0; i--){
			// ループは逆からループする
			// 対象の技能情報を得る
			mm_ability.MM_CharaAbility wk = mm_sheet.cdata.ability.get(i);
			View subview = wk.subview;
			// チェックボックスコントロールを得る
			CheckBox fchk = subview.findViewById(R.id.checkBox_ability);
			if(fchk.isChecked() == true){
				// 削除対象
				delAbility(mm_sheet, i);
			}
		}

		// 折りたたみ再設定
		setOnExpandProc(mm_sheet);

		// 再表示
		redisp(scrview, mm_sheet);

		// 一時保存
		tmpsave(mm_sheet);
	}

	/*-------------------------*/

	static public void initattr(View scrview, MM_Sheet mm_sheet)
	{
		mm_sheet.expand_attr = scrview.findViewById(R.id.expand_attr);
		mm_sheet.attrview = scrview.findViewById(R.id.expandLayout_attr);
		mm_sheet.isSel_attr = false;
	}

	static public void addwepon(int ss, MM_Sheet mm_sheet) {

		// Viewを生成して、rootViewへ登録
		//View subView = mm_sheet.inflater.inflate(R.layout.wepon_list, null);
		View subView = mm_sheet.inflater.inflate(R.layout.wepon_expand, null);
		mm_sheet.weponview.addView(subView, mm_sheet.weponview.getChildCount());

		// 表示用subviewを保存
		mm_sheet.cdata.cwepon[ss].subview = subView;

		mm_sheet.cdata.cwepon[ss].idx = ss;

		mm_sheet.expand_wepon[ss] = subView.findViewById(R.id.expand_wepon);
		mm_sheet.weponexview[ss] = subView.findViewById(R.id.expandLayout_wepon);
		mm_sheet.isSel_wepon[ss] = false;

		// 一時保存
		tmpsave(mm_sheet);
	}

	static public void addItem(mm_charasheet.MM_Sheet mm_sheet, int idx, String str)
	{
		// 登録用MM_CharaWeponを確保
		mm_wepon.MM_CharaWepon wk = new mm_wepon.MM_CharaWepon();

		// リスト内容を設定
		mm_wepon.setList(idx, wk.wepon);

		// 別名を設定
		wk.exname = str;

		// Viewを生成して、rootViewへ登録
		//View subView = mm_sheet.inflater.inflate(R.layout.item_list, null);
		View subView = mm_sheet.inflater.inflate(R.layout.item_expand, null);
		mm_sheet.itemview.addView(subView, mm_sheet.itemview.getChildCount());

		// View保存
		wk.subview = subView;

		// 登録
		mm_sheet.cdata.citem.add(wk);
	}

	static public void delItem(MM_Sheet mm_sheet, int idx)
	{
		// 対象の装備品情報を得る
		mm_wepon.MM_CharaWepon wk = mm_sheet.cdata.citem.get(idx);
		View subview = wk.subview;

		// rootViewを得る
		LinearLayout rootview = mm_sheet.itemview;
		// 子Viewを削除
		rootview.removeView(subview);
		// 保存領域 MM_CharaWepon を削除
		mm_sheet.cdata.citem.remove(idx);
	}

	static public void addAbility(MM_Sheet mm_sheet, int position)
	{
		// 新しい MM_CharaAbility を確保
		mm_ability.MM_CharaAbility wk = new mm_ability.MM_CharaAbility();

		// 選択したデータを展開
		mm_ability.setList(position, wk.ability);

		// Viewを生成して、rootViewへ登録
		//View subView = mm_sheet.inflater.inflate(R.layout.ability_list, null);
		View subView = mm_sheet.inflater.inflate(R.layout.abili_expand, null);
		mm_sheet.abilityview.addView(subView, mm_sheet.abilityview.getChildCount());

		// View保存
		wk.subview = subView;

		// MM_sheetへ登録
		mm_sheet.cdata.ability.add(wk);

		// HP最大再計算
		mm_charadata.MM_Recalc_HPmax(mm_sheet.cdata);
		// HPリセット
		mm_charadata.HPreset(mm_sheet.cdata);
	}

	static public void delAbility(MM_Sheet mm_sheet, int idx)
	{
		mm_ability.MM_CharaAbility wk = mm_sheet.cdata.ability.get(idx);
		View subview = wk.subview;

		// rootViewを得る
		LinearLayout rootview = mm_sheet.abilityview;
		// 子Viewを削除
		rootview.removeView(subview);
		// 保存領域 MM_CharaAbility を削除
		mm_sheet.cdata.ability.remove(idx);
	}

	// キャラクターデータ保存
	static public boolean save_main(OutputStreamWriter osw, mm_charadata.MM_CharaData mm_chara)
	{
		int	i;

		try {
			BufferedWriter bw = new BufferedWriter(osw);
			CSVWriter writer = new CSVWriter(bw);;

			mm_charadata.writeline(writer, mm_chara);
			mm_attribute.writeline(writer, mm_chara.attr);
			mm_wepon.writeline_wepon(writer, mm_chara.cwepon[0],0);
			mm_wepon.writeline_wepon(writer, mm_chara.cwepon[1],1);
			mm_wepon.writeline_wepon(writer, mm_chara.cwepon[2],2);

			for(i = 0; i < mm_chara.citem.size(); i++){
				mm_wepon.MM_CharaWepon witem = mm_chara.citem.get(i);
				mm_wepon.writeline_item(writer, witem);
			}

			for(i = 0; i < mm_chara.ability.size(); i++){
				mm_ability.MM_CharaAbility wabili = mm_chara.ability.get(i);
				mm_ability.writeline(writer, wabili.ability);
			}

			writer.close();
		} catch(FileNotFoundException ex){
			return false;
		} catch(IOException ex){
			return false;
		}

		return true;
	}

	// シートデータクリア
	public static void sheet_clear(MM_Sheet mm_sheet)
	{
		int	i;

		mm_sheet.cdata.chara_name="";
		mm_sheet.cdata.attr.clear();

		for(i = 0; i < 3; i++){
			// subviewを消去するとまずいのでweponとexnameだけ
			mm_sheet.cdata.cwepon[i].wepon.clear();
			mm_sheet.cdata.cwepon[i].exname="";
		}

		// 装備品と技能はViewも消去しないといけない
		for(i = mm_sheet.cdata.citem.size() - 1; i >= 0; i--){
			// ループは逆ループにして末尾から削除
			delItem(mm_sheet, i);
		}
		mm_sheet.cdata.citem.clear();

		for(i = mm_sheet.cdata.ability.size() - 1; i >= 0; i--){
			// ループは逆ループにして末尾から削除
			delAbility(mm_sheet, i);
		}

		// 折りたたみ再設定(消去)
		setOnExpandProc(mm_sheet);

		mm_sheet.cdata.ability.clear();
	}

	public static boolean load_main(InputStreamReader isw, MM_Sheet mm_sheet)
	{
		String [] nextLine;
		int	itemcnt = 0;
		int	itemxcnt = 0;
		int	abcnt = 0;
		int	i;

		try {
			// opencsvで読み込み
			CSVReader reader = new CSVReader(isw, ',', '"', '\\');

			// シートデータクリア
			sheet_clear(mm_sheet);

			while ((nextLine = reader.readNext()) != null) {

				if(nextLine[0].equals("CharaName")){
					// キャラ名
					mm_charadata.setCharaName(mm_sheet.cdata,nextLine[1]);
					mm_charadata.setSetwepon(mm_sheet.cdata, Integer.parseInt(nextLine[2]));
				} else if(nextLine[0].equals("Attribute")){
					// 属性
					mm_attribute.loadCSVline(mm_sheet.cdata.attr, nextLine);
				} else if(nextLine[0].equals("WeponName0")){
					// 武装A別名
					mm_sheet.cdata.cwepon[0].exname = nextLine[1];
				} else if(nextLine[0].equals("Wepon0")){
					// 武装A
					mm_wepon.loadCSVline(mm_sheet.cdata.cwepon[0].wepon, nextLine);
				} else if(nextLine[0].equals("WeponName1")){
					// 武装B別名
					mm_sheet.cdata.cwepon[1].exname = nextLine[1];
				} else if(nextLine[0].equals("Wepon1")){
					// 武装B
					mm_wepon.loadCSVline(mm_sheet.cdata.cwepon[1].wepon, nextLine);
				} else if(nextLine[0].equals("WeponName2")){
					// 武装C別名
					mm_sheet.cdata.cwepon[2].exname = nextLine[1];
				} else if(nextLine[0].equals("Wepon2")){
					// 武装C
					mm_wepon.loadCSVline(mm_sheet.cdata.cwepon[2].wepon, nextLine);
				} else if(nextLine[0].equals("ItemName")){
					// 装備品別名
					mm_wepon.MM_CharaWepon witem;
					if(mm_sheet.cdata.citem.size() <= itemxcnt) {
						// いったん空で登録
						addItem(mm_sheet, 0, "");
					}

					// 対象カウント(最後尾)のデータ取得
					witem = mm_sheet.cdata.citem.get(itemxcnt);
					// 内容を再設定
					witem.exname = nextLine[1];
					// 対象カウント(最後尾)へデータ内容設定
					mm_sheet.cdata.citem.set(itemxcnt, witem);
					itemxcnt++;
				} else if(nextLine[0].equals("Item")){
					// 装備品別名
					mm_wepon.MM_CharaWepon witem;
					if(mm_sheet.cdata.citem.size() <= itemcnt) {
						// いったん空で登録
						addItem(mm_sheet, 0, "");
					}

					// 対象カウント(最後尾)のデータ取得
					witem = mm_sheet.cdata.citem.get(itemcnt);
					// 内容を再設定
					mm_wepon.loadCSVline(witem.wepon, nextLine);
					// 対象カウント(最後尾)へデータ内容設定
					mm_sheet.cdata.citem.set(itemcnt, witem);
					itemcnt++;
				} else if(nextLine[0].equals("Ability")){
					// 装備品別名
					mm_ability.MM_CharaAbility wabili;
					if(mm_sheet.cdata.ability.size() <= abcnt) {
						// いったん空で登録
						addAbility(mm_sheet, 0);
					}

					// 対象カウント(最後尾)のデータ取得
					wabili = mm_sheet.cdata.ability.get(abcnt);
					// 内容を再設定
					mm_ability.loadCSVline(wabili.ability, nextLine);
					// 対象カウント(最後尾)へデータ内容設定
					mm_sheet.cdata.ability.set(abcnt, wabili);
					abcnt++;
				}
			}

			reader.close();
		} catch(FileNotFoundException ex){
			return false;
		} catch(IOException ex){
			return false;
		}

		return true;
	}

	// 一時保存ファイルへセーブ
	static public void tmpsave(MM_Sheet mm_sheet)
	{
		String filePath = getTmpPath(mm_sheet);

		try {
			File f = new File(filePath);
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			save_main(osw, mm_sheet.cdata);
		} catch(FileNotFoundException ex){
			return;
		} catch(IOException ex){
			return;
		}
	}

	// 一時保存ファイルからロード
	static public boolean tmpload(MM_Sheet mm_sheet)
	{
		String filePath = getTmpPath(mm_sheet);

		try {
			File f = new File(filePath);
			InputStreamReader isw = new InputStreamReader(new FileInputStream(f), "UTF-8");
			return load_main(isw, mm_sheet);
		} catch(FileNotFoundException ex){
			return false;
		} catch(IOException ex){
			return false;
		}
	}

	/*------------------*/

	// 属性エリアの開閉
	static public void OnClickExpandAttr(View view, View scrview, MM_Sheet mm_sheet)
	{
		if(mm_sheet.isSel_attr){
			mm_sheet.expand_attr.collapse();
			mm_sheet.isSel_attr = false;
		} else {
			mm_sheet.expand_attr.expand();
			mm_sheet.isSel_attr = true;
		}
	}

	// 武装折りたたみ
	static public void OnClickExpand_wepon(View scrview, MM_Sheet mm_sheet, int wsel)
	{
		if(mm_sheet.isSel_wepon[wsel]){
			mm_sheet.expand_wepon[wsel].collapse();
			mm_sheet.isSel_wepon[wsel] = false;
		} else {
			mm_sheet.expand_wepon[wsel].expand();
			mm_sheet.isSel_wepon[wsel] = true;
		}
	}

	// 装備品表示折りたたみ/展開(メイン)
	static public void OnClickExpand_item(View view, MM_Sheet mm_sheet)
	{
		// 対象のViewを探す
		for(int i = 0; i < mm_sheet.cdata.citem.size(); i++){
			if(mm_sheet.itemexview.get(i).equals(view)){
				// 対象Viewが見つかった
				if(mm_sheet.isSel_Item.get(i) != 0){
					mm_sheet.expand_item.get(i).collapse();
					mm_sheet.isSel_Item.set(i,0);
				} else {
					mm_sheet.expand_item.get(i).expand();
					mm_sheet.isSel_Item.set(i,1);
				}
			}
		}
	}

	// 装備品表示折りたたみ/展開
	static public void setExpand_item(MM_Sheet mm_sheet)
	{
		int	i;

		mm_sheet.expand_item.clear();
		mm_sheet.itemexview.clear();
		mm_sheet.isSel_Item.clear();

		for (i = 0; i < mm_sheet.cdata.citem.size(); i++){
			View wks = mm_sheet.cdata.citem.get(i).subview;

			ExpandableLayout wkex = wks.findViewById(R.id.expand_item);
			mm_sheet.expand_item.add(wkex);
			LinearLayout wkl = wks.findViewById(R.id.expandLayout_item);
			mm_sheet.itemexview.add(wkl);
			mm_sheet.isSel_Item.add(0);

			wkl.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					OnClickExpand_item(view, mm_sheet);
				}
			});
		}
	}

	// 装備品表示折りたたみ/展開(メイン)
	static public void OnClickExpand_ability(View view, MM_Sheet mm_sheet)
	{
		// 対象のViewを探す
		for(int i = 0; i < mm_sheet.cdata.ability.size(); i++){
			if(mm_sheet.abilityexview.get(i).equals(view)){
				// 対象Viewが見つかった
				if(mm_sheet.isSel_ability.get(i) != 0){
					mm_sheet.expand_ability.get(i).collapse();
					mm_sheet.isSel_ability.set(i,0);
				} else {
					mm_sheet.expand_ability.get(i).expand();
					mm_sheet.isSel_ability.set(i,1);
				}
			}
		}
	}

	static public void setExpand_ability(MM_Sheet mm_sheet)
	{
		int i;

		mm_sheet.expand_ability.clear();
		mm_sheet.abilityexview.clear();
		mm_sheet.isSel_ability.clear();

		for(i = 0; i < mm_sheet.cdata.ability.size(); i++){
			View wks = mm_sheet.cdata.ability.get(i).subview;

			ExpandableLayout wkex = wks.findViewById(R.id.expand_ability);
			mm_sheet.expand_ability.add(wkex);
			LinearLayout wkl = wks.findViewById(R.id.expandLayout_ability);
			mm_sheet.abilityexview.add(wkl);
			mm_sheet.isSel_ability.add(0);

			wkl.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					OnClickExpand_ability(view, mm_sheet);
				}
			});
		}
	}

	// 折りたたみ処理登録
	static public void setOnExpandProc(MM_Sheet mm_sheet)
	{
		setExpand_item(mm_sheet);
		setExpand_ability(mm_sheet);
	}
}
