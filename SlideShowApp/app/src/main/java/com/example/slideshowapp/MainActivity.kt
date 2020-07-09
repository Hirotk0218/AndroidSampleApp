package com.example.slideshowapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableInt
import com.example.slideshowapp.databinding.ActivityMainBinding
import com.example.slideshowapp.dialog.MainDialogFragment
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 100
    }

    //region MARK: - private fields
    val imageArray = arrayListOf<Uri>()
    val slideNumber = ObservableInt(1)
    //endregion

    //region MARK: - private fields
    private var mTimer: Timer? = null
    private var count = 0
    private var mHandler = Handler()
    private var isPermissionAccess = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var animation: Animation
    //endregion

    //region MARK: - activity lifeCycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.data = this
        animation = AnimationUtils.loadAnimation(this, R.anim.blink)

        binding.play.setOnClickListener {
            if (isPermissionAccess) {
                onClickPlayButton()
            }
        }

        binding.next.setOnClickListener {
            if (isPermissionAccess) {
                onClickNextButton()
            }
        }

        binding.back.setOnClickListener {
            if (isPermissionAccess) {
                onClickBackButton()
            }
        }

        binding.animation.setOnClickListener {

            val mainDialogFragment = MainDialogFragment()
            mainDialogFragment.apply {
                onClickItem = {
                    setSelectedAnimation(it)
                }
                showDialog(this@MainActivity.supportFragmentManager)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // 初期化
        imageArray.clear()

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                isPermissionAccess = true
                getContentsInfo()
            } else {
                isPermissionAccess = false
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            // Android 5系以下の場合
        } else {
            isPermissionAccess = true
            getContentsInfo()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }
    //endregion

    //region MARK: - private method
    /**
     * ストレージから写真を取得
     */
    @SuppressLint("Recycle")
    private fun getContentsInfo() {

        // 写真の取得
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )
            ?: return

        if (cursor.moveToFirst()) {
            do {
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageArray.add(imageUri)
            } while (cursor.moveToNext())
        }
        cursor.close()

        // 写真がない場合
        if (imageArray.isEmpty()) {

            binding.title.visibility = View.GONE
            binding.emptyTitle.visibility = View.VISIBLE
            binding.imageView.setImageURI(null)

            return
        }

        // 写真がある場合
        binding.title.visibility = View.VISIBLE
        binding.emptyTitle.visibility = View.GONE
        binding.imageView.setImageURI(imageArray[0])
        slideNumber.set(1)
    }

    /**
     * 再生ボタンタップ時
     */
    private fun onClickPlayButton() {

        // 写真がない場合は、警告を表示
        if (imageArray.isEmpty()) {

            Snackbar.make(
                binding.root,
                this.getString(R.string.warning_no_slide),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        if (mTimer == null) {

            mTimer = Timer()
            binding.play.setImageResource(R.drawable.ic_pause)
            mTimer?.schedule(object : TimerTask() {
                override fun run() {
                    count += 1
                    mHandler.post {
                        val itemCount =
                            if (count % imageArray.size < 0) count % imageArray.size + imageArray.size else count % imageArray.size

                        binding.imageView.setImageURI(imageArray[itemCount])
                        binding.imageView.startAnimation(animation)
                        slideNumber.set(itemCount + 1)
                    }
                }
            }, 2000, 2000)

        } else {

            mTimer?.cancel()
            mTimer = null
            binding.play.setImageResource(R.drawable.ic_play)
        }
    }

    /**
     * 次ボタンタップ時
     */
    private fun onClickNextButton() {

        try {
            if (mTimer == null) {

                count += 1
                val itemCount =
                    if (count % imageArray.size < 0) count % imageArray.size + imageArray.size else count % imageArray.size

                binding.imageView.setImageURI(imageArray[itemCount])
                binding.imageView.startAnimation(animation)
                slideNumber.set(itemCount + 1)

            } else {

                Snackbar.make(
                    binding.root,
                    this.getString(R.string.warning_not_play),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {

            Snackbar.make(
                binding.root,
                this.getString(R.string.warning_no_slide),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    /**
     * 戻るボタンタップ時
     */
    private fun onClickBackButton() {

        try {
            if (mTimer == null) {

                count -= 1
                val itemCount =
                    if (count % imageArray.size < 0) count % imageArray.size + imageArray.size else count % imageArray.size

                binding.imageView.setImageURI(imageArray[itemCount])
                binding.imageView.startAnimation(animation)
                slideNumber.set(itemCount + 1)

            } else {

                Snackbar.make(
                    binding.root,
                    this.getString(R.string.warning_not_play),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {

            Snackbar.make(
                binding.root,
                this.getString(R.string.warning_no_slide),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    /**
     * 選択したアニメーションの設定
     *
     * @param position 選択したアニメーションのリスト上の位置
     */
    private fun setSelectedAnimation(position: Int) {
        animation = when (position) {
            1 -> {
                // バウンド
                AnimationUtils.loadAnimation(this, R.anim.bounce)
            }
            2 -> {
                // フェードイン
                AnimationUtils.loadAnimation(this, R.anim.fade_in)
            }
            3 -> {
                // フェードアウト
                AnimationUtils.loadAnimation(this, R.anim.fade_out)
            }
            4 -> {
                // 移動
                AnimationUtils.loadAnimation(this, R.anim.move)
            }
            5 -> {
                // 回転
                AnimationUtils.loadAnimation(this, R.anim.rotate)
            }
            6 -> {
                // スライド(下)
                AnimationUtils.loadAnimation(this, R.anim.slide_down)
            }
            7 -> {
                // スライド(左)
                AnimationUtils.loadAnimation(this, R.anim.slide_left)
            }
            8 -> {
                // スライド(右)
                AnimationUtils.loadAnimation(this, R.anim.slide_right)
            }
            9 -> {
                // スライド(上)
                AnimationUtils.loadAnimation(this, R.anim.slide_up)
            }
            10 -> {
                // 拡大
                AnimationUtils.loadAnimation(this, R.anim.zoom_in)
            }
            11 -> {
                // 縮小
                AnimationUtils.loadAnimation(this, R.anim.zoom_out)
            }
            else -> {
                // まばたき
                AnimationUtils.loadAnimation(this, R.anim.blink)
            }
        }
    }
    //endregion
}
