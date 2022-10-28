package jp.phoebus.mmsheet;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import jp.phoebus.mmsheet.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

	private FragmentSecondBinding binding;
	mm_charasheet.MM_Sheet MM_sheet;
	private boolean initproc = false;

	@Override
	public View onCreateView(
		LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState
	) {
		initproc = false;

		binding = FragmentSecondBinding.inflate(inflater, container, false);
		View v = binding.getRoot();

		//オプションメニュー利用フラグを立てる
		setHasOptionsMenu(true);

		MM_sheet = new mm_charasheet.MM_Sheet();
		MM_sheet.view = v;
		MM_sheet.inflater = inflater;

		// 武器エリアView取得
		MM_sheet.weponview = v.findViewById(R.id.root1);
		mm_charasheet.addwepon(0, MM_sheet);
		mm_charasheet.addwepon(1, MM_sheet);
		mm_charasheet.addwepon(2, MM_sheet);

		// 装備品エリアView取得
		MM_sheet.itemview = v.findViewById(R.id.root2);

		// 技能エリアView取得
		MM_sheet.abilityview = v.findViewById(R.id.root3);

		// 一時保存ファイルパス生成
		File file = new File(requireContext().getFilesDir(), "tmpchara2.csv");
		String filePath = file.toString();
		// 一時保存ファイルパス保存
		mm_charasheet.setTmpPath(MM_sheet, filePath);

		// 一時保存ファイルから読み込み
		mm_charasheet.tmpload(MM_sheet);

		// HP/SP読み込み
		mm_charadata.load_playdata(requireContext(), 1, MM_sheet.cdata);

		mm_charasheet.redisp(v, MM_sheet);

		initproc = true;

		return v;
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Viewを得る
		View v = binding.getRoot();

		// Viewからキャラ名変更ボタンを得て。OnClickListenerを設定
		Button button_chara_name = (Button) v.findViewById(R.id.button_chara_name);
		button_chara_name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view){
				mm_charasheet.OnClick_EditCharaName(requireContext(), v, MM_sheet);
			}
		});

		// HPのinc
		Button button_hp_inc = (Button) v.findViewById(R.id.button_hp_inc);
		button_hp_inc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mm_charasheet.OnClick_HP_inc(v, MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// HPのdec
		Button button_hp_dec = (Button) v.findViewById(R.id.button_hp_dec);
		button_hp_dec.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mm_charasheet.OnClick_HP_dec(v, MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// HPのリセット
		Button button_hp_reset = (Button) v.findViewById(R.id.button_hp_reset);
		button_hp_reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mm_charasheet.OnClick_HP_reset(v, MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// SPのinc
		Button button_sp_inc = (Button) v.findViewById(R.id.button_sp_inc);
		button_sp_inc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mm_charasheet.OnClick_SP_inc(v, MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// SPのdec
		Button button_sp_dec = (Button) v.findViewById(R.id.button_sp_dec);
		button_sp_dec.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mm_charasheet.OnClick_SP_dec(v, MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// SPのリセット
		Button button_sp_reset = (Button) v.findViewById(R.id.button_sp_reset);
		button_sp_reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mm_charasheet.OnClick_SP_reset(v, MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// 属性の変更
		Button button_att = (Button) v.findViewById(R.id.button_attr);
		button_att.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				mm_charasheet.OnClick_EditAttr(requireContext(), v, MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// 武装選択ダイアログ
		Button button_w1 = (Button) MM_sheet.cdata.cwepon[0].subview.findViewById(R.id.button_wepon);
		button_w1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mm_charasheet.OnClick_EditWepon(requireContext(),v , MM_sheet, 0);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// 武装選択ダイアログ
		Button button_w2 = (Button) MM_sheet.cdata.cwepon[1].subview.findViewById(R.id.button_wepon);
		button_w2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mm_charasheet.OnClick_EditWepon(requireContext(),v , MM_sheet, 1);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// 武装選択ダイアログ
		Button button_w3 = (Button) MM_sheet.cdata.cwepon[2].subview.findViewById(R.id.button_wepon);
		button_w3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mm_charasheet.OnClick_EditWepon(requireContext(),v , MM_sheet, 2);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// セット武器選択spinner
		Spinner spinner_setwepon = (Spinner)v.findViewById(R.id.spinner_setwepon);
		spinner_setwepon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
						   View view, int position, long id) {
				if (initproc == true){
					mm_charasheet.OnItemSelected_Setwepon(requireContext(), v, MM_sheet, position);
				}
			}

			//　アイテムが選択されなかった
			public void onNothingSelected(AdapterView<?> parent) {
				//
			}
		});

		// 装備品追加ボタン
		Button button_itemadd = (Button)v.findViewById(R.id.button_item_add);
		button_itemadd.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view)
			{
				mm_charasheet.OnClick_AddItem(requireContext(),v , MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// 装備品削除ボタン
		Button button_itemdel = (Button)v.findViewById(R.id.button_item_del);
		button_itemdel.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view)
			{
				mm_charasheet.OnClick_DelItem(requireContext(), v, MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// 技能追加ボタン
		Button button_abilityadd = (Button)v.findViewById(R.id.button_ability_add);
		button_abilityadd.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view)
			{
				mm_charasheet.OnClick_AddAbility(requireContext(),v , MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		// 技能削除ボタン
		Button button_abilitydel = (Button)v.findViewById(R.id.button_ability_del);
		button_abilitydel.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view)
			{
				mm_charasheet.OnClick_DelAbility(requireContext(), v, MM_sheet);
				mm_charadata.save_playdata(requireContext(), 2, MM_sheet.cdata);
			}
		});

		/*
		binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				NavHostFragment.findNavController(SecondFragment.this)
					.navigate(R.id.action_SecondFragment_to_FirstFragment);
			}
		});
		 */
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	// オプションメニューのオーバーライド
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.menu_main, menu);
	}

	// メニュー選択時処理
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_save) {
			String fileName = new String();
			fileName = "*.csv";

			// ファイル保存のためのピッカーUI起動
			Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("*/*");
			intent.putExtra(Intent.EXTRA_TITLE, fileName);
			startActivityForResult(intent, 12000);

			return true;
		} else if(id == R.id.action_newdata) {
			// 新規作成

			// シートデータクリア
			mm_charasheet.sheet_clear(MM_sheet);
			MM_sheet.uripath = Uri.parse("");

			// 再表示
			mm_charasheet.redisp(MM_sheet.view, MM_sheet);

			// 一時保存
			mm_charasheet.tmpsave(MM_sheet);

			return true;
		} else if(id == R.id.action_load){
			String fileName = new String();
			fileName = "page2.csv";

			// ファイル読み込みのためのピッカーUI起動
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("*/*");
			intent.putExtra(Intent.EXTRA_TITLE, fileName);
			startActivityForResult(intent, 13000);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	// ファイル選択ビッカーUIの結果処理
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);

		if (requestCode == 12000 && resultCode == RESULT_OK) {
			if (resultData.getData() != null) {
				// データを作成されたファイルに書き込む
				Uri uri = resultData.getData();

				try {
					OutputStream outputStream = requireContext().getContentResolver().openOutputStream(uri);
					OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");

					ProgressDialog progressDialog = new ProgressDialog(requireContext());
					progressDialog.setTitle("");
					progressDialog.setMessage("保存中");
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.show();

					// ファイルへ保存
					mm_charasheet.save_main(osw, MM_sheet.cdata);
					mm_charadata.save_playdata(requireContext(), 1, MM_sheet.cdata);

					// URIを保存
					MM_sheet.uripath = uri;

					progressDialog.dismiss();
				} catch (IOException ex) {
					return;
				}
			}
		} else if (requestCode == 13000 && resultCode == RESULT_OK) {
			if(resultData.getData() != null) {
				// データを作成されたファイルに書き込む
				Uri uri = resultData.getData();

				try {
					InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
					InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");

					ProgressDialog progressDialog = new ProgressDialog(requireContext());
					progressDialog.setTitle("");
					progressDialog.setMessage("読み込み中");
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.show();

					// ファイルへ保存
					mm_charasheet.load_main(isr, MM_sheet);

					// URIを保存
					MM_sheet.uripath = uri;

					progressDialog.dismiss();

					// 再表示
					mm_charasheet.redisp(MM_sheet.view, MM_sheet);

					// 一時保存
					mm_charasheet.tmpsave(MM_sheet);
					mm_charadata.save_playdata(requireContext(), 1, MM_sheet.cdata);
				} catch (IOException ex) {
					return;
				}
			}
		}
	}
}