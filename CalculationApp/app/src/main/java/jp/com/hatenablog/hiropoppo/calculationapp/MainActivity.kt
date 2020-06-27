package jp.com.hatenablog.hiropoppo.calculationapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import jp.com.hatenablog.hiropoppo.calculationapp.databinding.ActivityMainBinding

/**
 * Main Activity
 */
class MainActivity : AppCompatActivity() {

    //region MARK: - private field
    private lateinit var binding: ActivityMainBinding
    //endregion

    //region MARK: - activity lifeCycle method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
    }
    //endregion
}
