package jp.com.hatenablog.hiropoppo.calculationapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import jp.com.hatenablog.hiropoppo.calculationapp.databinding.FragmentResultBinding

/**
 * 計算結果画面
 */
class ResultFragment : Fragment() {

    //region MARK: - private fields
    private lateinit var binding: FragmentResultBinding
    private val args: ResultFragmentArgs by navArgs()
    //endregion

    //region MARK: - fragment lifeCycle method
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.result.text = args.result
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        return binding.root
    }
    //endregion
}
