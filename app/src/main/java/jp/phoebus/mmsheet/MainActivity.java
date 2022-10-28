/*
	Maid & Maidens キャラクターシートアプリ

0.00	スケルトン
0.01	キャラシート用xmlを確保。flagment_firstからincludeすることで表示させる。
	とりあえずはキャラ名だけ。
0.02	HPとSPを表示。増減ボタンもいちおうつけるが機能はまだ。
0.03	キャラ名変更ダイアログを出す。変更処理はまだ。
0.04	キャラ名格納用領域を設定。キャラ名の書き換えを可能にした。
0.05	HPのinc、dec、リセットボタンの処理を実装。
0.06	SPのinc、dec、リセットボタンの処理を実装。
0.07	移動力とMPの表示枠を設定。枠だけ。
0.08	属性エリアのレイアウトxmlを実装。
0.09	属性情報のcsvを登録、opencsvをリンクして読み込み処理を実装。
0.10	属性情報選択ボタン処理を実装。属性エリアへの表示も併せて実装。
0.11	武装xmlをLinearLayoutに追加する処理を作成。とりあえずは名前だけ。
0.12	追加する処理をFirstFlagment.javaからmm_charasheet.javaへ移した。
0.13	武器xmlの内容を修正。内容の書き換えはまだ。
0.14	武装Aのボタンを押すとダイアログを表示。内容はレイアウトxmlで指定する方式。
0.15	武装ダイアログのスピナー内容を設定。OKボタン処理がまだなので選択するだけ。
0.16	武装ダイアログの処理、設定用関数をmm_charasheet.javaへ移した。
0.17	武装ダイアログのスピナーのリスナーを作成。とりあえずは変数に入れるだけ。
0.18	よく考えたら武装ダイアログのリスナーは不要だった。OKボタンで確定するからね。
0.19	武装ダイアログのスピナーを武装のみ(装備品以外)にした。
0.20	武装入力ダイアログのOKボタンを実装。初期値設定処理も追加。
0.21	武装Aの処理を、B、Cへ展開
0.22	セット武器用スピナーを設定。
0.23	装備品エリアを追加。動作はまだ。
0.24	装備品追加処理を追加。削除はまだ。
0.25	MP計算、移動力計算処理を追加。再表示は全体表示処理をコールすることにして、
	全体表示処理内に計算処理を入れる。
0.26	技能、特殊能力の情報ロード
0.27	技能、特殊能力のxmlを追加。とりあえずはincludeしただけ。
0.28	includeしていたのをadd()するようにした。
0.29	追加ボタンの処理実装。リスト表示して選択させただけ
0.30	能力の追加ボタン処理内容実装
0.31	頑丈、病弱、少女少年の判定処理を追加。
0.32	キャラごとのデータはいちおうMM_CharaDataの配下とした。
	Recalc関連をmm_charadataへ移すため。
0.33	装備品の削除処理を実装。削除前にワンクッション置いたほうがいいか？
0.34	装備品削除処理をコピーして技能削除処理作成。
0.35	マニフェストにファイルR/Wを追加。パーミッションチェック判定を追加。
	パーミッションを許可する処理は面倒なのでやらない。
0.36	Android11(API19)での保存処理がわかった(?)のでその方法で書けるような
	プロト処理を埋め込む。実際にはまだ書いていない。
0.37	メニュー動作をActivityではなく、Flagmentから呼び出すようになんとかした。
0.38	OpenCSVでキャラデータの一時保存処理。とりあえず一時保存用のファイル作成処理作成
	0.36で追加した処理は一時停止。
0.39	キャラ名保存
0.40	属性情報を保存
0.41	武装情報を保存
0.42	装備品情報を保存
0.43	能力情報を保存
0.44	名前のロード
0.45	属性情報ロード
0.46	武装情報をロード
0.47	装備品をロード
0.48	各データ決定時に一時ファイルへセーブし、起動時に一時ファイルをロードすることにした。
	これに伴い、MM_Charasheetに一時ファイル名、保存ファイル名の項目を追加。
	一時ファイルへのセーブ、ロードもmm_charasheet.javaへ移す。
0.49	技能のロード処理を追加。
0.50	セーブ関数の引数をOutputStreamWriterで指定できるようにした。
	ファイルピッカー対応のため。セーブメイン処理もmm_charasheet.javaへ移動。
0.51	ロード処理で装備品、技能をclear()していたが、viewの解放も行うように
	ループを回した。同様に武装もクリアするが、こちらはviewを消去するとまずいので
	武装変数と別名だけ消去。
0.51	ロード処理の引数もInputStreamReaderにした。
0.52	ファイルピッカーでファイルを指定し、保存できるようにした。
	上書き保存がどうしてもうまくいかなかったため、必ずファイル名を指定するようにする。
0.53	新規メニューで内容の全消去する。
0.54	読み込みメニュー選択でファイルピッカーを始動、ファイル指定後そのファイルを読み込む
	処理を入れる。
0.55	プリファレンスにHP/SPの値を保存する。ライフサイクルが死んでも再読み込みして
	復旧するわけね。
0.56	ロード直後と初期化直後は一時ファイルへの保存を実行する。
0.57	セット武器の選択スピナー処理を実装
0.58	セット武器の設定を保存、ロード処理作成
0.60	左右スワイプでflagmentを切り替えれられるようにViewPagerを設定。
	とりあえず2面固定
0.61	SecondFragmentの内容をFirstFragmentと同じ内容で作成。
0.62	パーミッションチェック処理を更新。共通UIで楽になったらしい。
0.63	ファイル名のデフォルトを変更。
0.64	属性、武装、装備品、技能の選択スピナーの表示内容にMP表示を追加。
	プリファレンスのR/W処理をpbslibへ移した。
0.65	セット武器の項目がロードはできてもスピナーへ反映されてなかったので
	実質読めなくなっていたのを修正。
 */

package jp.phoebus.mmsheet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import jp.phoebus.mmsheet.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private AppBarConfiguration appBarConfiguration;
	private ActivityMainBinding binding;

	private ViewPager pager;
	private PagerTabStrip pagerStrip;
	private MyFragmentPagerAdapter pagerAdapter;

	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
		Manifest.permission.READ_EXTERNAL_STORAGE,
		Manifest.permission.WRITE_EXTERNAL_STORAGE
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Android6.0(マシュマロ)以上の環境かチェック
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

			int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

			if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
				// Android 6.0 以降、該当パーミッションが許可されていない場合
				ActivityCompat.requestPermissions(
					this,
					PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE
				);
			}
		}

		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		setSupportActionBar(binding.toolbar);

		if(savedInstanceState == null){
			// 最初の起動(再起動ではない)

			// 属性情報のロード
			mm_attribute.load(this);
			// 武装、装備品情報のロード
			mm_wepon.load(this);
			// 技能、特殊能力のロード
			mm_ability.load(this);
		}

		//NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
		//appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
		//NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

		//binding.fab.setOnClickListener(new View.OnClickListener() {
		//	@Override
		//	public void onClick(View view) {
		//		Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
		//			.setAction("Action", null).show();
		//	}
		//});

		pager = (ViewPager) findViewById(R.id.pager);
		pagerStrip = (PagerTabStrip) findViewById(R.id.pagerStrip);

		/** PagerTabStripの設定 */
		pagerStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		pagerStrip.setTextColor(0xFF00FFFF);
		pagerStrip.setNonPrimaryAlpha(0.3f);
		pagerStrip.setDrawFullUnderline(true);
		pagerStrip.setTabIndicatorColor(0xFF00FFFF);

		pagerAdapter = new MyFragmentPagerAdapter(
			getSupportFragmentManager());

		PageData data1 = new PageData();
		data1.fragmentId = 1;

		PageData data2 = new PageData();
		data2.fragmentId = 2;

		pagerAdapter.addItem(data1);
		pagerAdapter.addItem(data2);

		pager.setAdapter(pagerAdapter);
	}

	//@Override
	//public boolean onCreateOptionsMenu(Menu menu) {
	//	// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.menu_main, menu);
	//	return true;
	//}

	//@Override
	//public boolean onOptionsItemSelected(MenuItem item) {
	//	// Handle action bar item clicks here. The action bar will
	//	// automatically handle clicks on the Home/Up button, so long
	//	// as you specify a parent activity in AndroidManifest.xml.
	//	int id = item.getItemId();

	//	//noinspection SimplifiableIfStatement
	//	if (id == R.id.action_settings) {
	//		return true;
	//	}

	//	return super.onOptionsItemSelected(item);
	//}

	//@Override
	//public boolean onSupportNavigateUp() {
	//	NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
	//	return NavigationUI.navigateUp(navController, appBarConfiguration)
	//		|| super.onSupportNavigateUp();
	//}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter
	{
		private ArrayList<PageData> pageDataList = new ArrayList<PageData>();

		public MyFragmentPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		/** フラグメントを追加する。 */
		public void addItem(PageData data)
		{ pageDataList.add(data); }

		/** 特定のインデックスのフラグメントを返す(なければnull)。 */
		@Override
		public Fragment getItem(int index)
		{
			if(index < 0 || pageDataList.size() <= index) return null;

			/** フラグメントIDに応じたフラグメントを返す。 */
			switch(pageDataList.get(index).fragmentId){
				case 1:
					return new FirstFragment();
				case 2:
					return new SecondFragment();
				case 3:
					return new FirstFragment();
				default:
					return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return pageDataList.get(position).title;
		}

		@Override
		public int getCount()
		{ return pageDataList.size(); }
	}

	/** カスタムデータクラス */
	class PageData
	{
		/** フラグメントのID */
		public int fragmentId = 0;

		/** PagerTabStripに表示されるタイトル */
		public String title;
	}
}