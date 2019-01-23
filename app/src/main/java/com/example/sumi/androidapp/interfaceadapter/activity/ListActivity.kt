package com.example.sumi.androidapp.interfaceadapter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.sumi.androidapp.R
import com.example.sumi.androidapp.domain.entity.Item
import com.example.sumi.androidapp.domain.gateway.QiitaClientInterface
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class ListActivity : AppCompatActivity() {

    private lateinit var mAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        mAdapter = ListAdapter(this, R.layout.list_item)

        findViewById<ListView>(R.id.list_view).apply {
            adapter = mAdapter
            onItemClickListener = OnItemClickListener()
        }

        findViewById<EditText>(R.id.edit_text).apply {
            setOnKeyListener(OnKeyListener())
        }

    }

    private inner class ListAdapter(context: Context, resource: Int) : ArrayAdapter<Item>(context, resource) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val view = convertView ?: layoutInflater.inflate(R.layout.list_item, parent, false)
            return view.apply {
                val imageView = findViewById<ImageView>(R.id.image_view)
                val itemTitleView = findViewById<TextView>(R.id.item_title)
                val userNameView = findViewById<TextView>(R.id.user_name)

                // 残ってる画像を消す（再利用された時）
                imageView.setImageBitmap(null)

                // 表示する行番号のデータを取り出す
                getItem(position).apply {
                    Picasso.with(context).load(user?.profile_image_url).into(imageView)
                    itemTitleView.text = title
                    userNameView.text = user?.name
                }
            }
        }
    }

    private inner class OnKeyListener : View.OnKeyListener {

        override fun onKey(view: View, keyCode: Int, keyEvent: KeyEvent): Boolean {

            if (keyEvent.action != KeyEvent.ACTION_UP || keyCode != KeyEvent.KEYCODE_ENTER) {
                return false
            }

            val editText = view as EditText

            // キーボードを閉じる
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                hideSoftInputFromWindow(editText.windowToken, 0)
            }

            var text = editText.text.toString()
            try {
                // url encode
                text = URLEncoder.encode(text, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                Log.e("", e.toString(), e)
                return true
            }

            if (!TextUtils.isEmpty(text)) {
                val request = QiitaClientInterface.create().items(text)
                Log.d("", request.request().url().toString())
                val item = object : Callback<List<Item>> {
                    override fun onResponse(call: Call<List<Item>>?, response: Response<List<Item>>?) {
                        mAdapter.clear()
                        response?.body()?.forEach {
                            mAdapter.add(it)
                        }
                    }

                    override fun onFailure(call: Call<List<Item>>?, t: Throwable?) {
                    }
                }
                request.enqueue(item)
            }
            return true
        }
    }

    private inner class OnItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
            return Intent(this@ListActivity, DetailActivity::class.java).let {
                // タップされた行番号のデータを取り出す
                val result = mAdapter.getItem(position)
                it.putExtra("url", result.url)
                startActivity(it)
            }
        }
    }
}
