package jp.com.hatenablog.hiropoppo.calculationapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import jp.com.hatenablog.hiropoppo.calculationapp.databinding.FragmentMainBinding

/**
 * 四則演算画面
 */
class MainFragment : Fragment(), View.OnClickListener {

    //region MARK: - private fields
    private lateinit var binding: FragmentMainBinding

    private var input1: Double = 0.0
    private var input2: Double = 0.0
    private var result: Double? = 0.0
    //endregion

    //region MARK: - fragment lifeCycle methods
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        setupView()
        return binding.root
    }

    override fun onClick(view: View) {

        try {
            // 数値のセット
            input1 = binding.input1.text.toString().toDouble()
            input2 = binding.input2.text.toString().toDouble()

            if (view.id == R.id.divide && input2 == 0.0) {
                // 割り算を行う際、分母が0の場合、スナックバーを表示
                showSnackBar(binding.root.context.getString(R.string.divide_error_text))
                return
            }

            result = when {
                view.id == R.id.add -> input1 + input2
                view.id == R.id.subtract -> input1 - input2
                view.id == R.id.multiply -> input1 * input2
                view.id == R.id.divide -> input1 / input2
                else -> null
            }

            if (result == null) {
                // 計算結果がnullの場合、スナックバーを表示
                showSnackBar(binding.root.context.getString(R.string.calculation_error_text))
                return
            }

            // 計算結果表示画面に遷移する
            activity?.findNavController(R.id.nav_host_fragment)
                ?.navigate(MainFragmentDirections.actionMainFragmentToResultFragment(result.toString()))

        } catch (e: Exception) {

            // 入力されたものが数値以外の場合、スナックバーを表示
            showSnackBar(binding.root.context.getString(R.string.error_text))
        }
    }
    //endregion

    //region MARK: - private methods
    /**
     * Viewの設定
     */
    private fun setupView() {
        binding.add.setOnClickListener(this)
        binding.subtract.setOnClickListener(this)
        binding.multiply.setOnClickListener(this)
        binding.divide.setOnClickListener(this)
    }

    /**
     * スナックバーを表示
     *
     * @param text 表示するテキスト
     */
    private fun showSnackBar(text: String) {
        Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }
    //endregion
}
