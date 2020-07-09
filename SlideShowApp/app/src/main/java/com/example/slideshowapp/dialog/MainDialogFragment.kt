package com.example.slideshowapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slideshowapp.R
import com.example.slideshowapp.databinding.FragmentMainDialogBinding

/**
 * アニメーションダイアログ
 */
class MainDialogFragment : DialogFragment() {

    //region MARK: - private field
    private lateinit var binding: FragmentMainDialogBinding
    //endregion

    //region MARK: - public field
    var onClickItem: (position: Int) -> Unit = {}
    //endregion

    //region MARK: - fragment lifeCycle Methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main_dialog,
            container,
            false
        )
        binding.lifecycleOwner = this

        binding.close.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setDialogMatchParent()
    }
    //endregion

    //region MARK: - private Method
    /**
     * ダイアログの位置の設定
     */
    private fun setDialogMatchParent() {
        dialog?.window?.let {
            val params = it.attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            it.attributes = params
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    /**
     * RecyclerViewの設定
     */
    private fun setupRecyclerView() {

        binding.animationList.apply {
            setHasFixedSize(false)
            layoutManager = context?.let { LinearLayoutManager(it) }
            adapter = AnimationAdapter().apply {
                onSelectedItem = {
                    onClickItem(it)
                    dismiss()
                }
            }
        }

        // データの追加
        val animationList = resources.getStringArray(R.array.animation_list).toList()
        (binding.animationList.adapter as? AnimationAdapter)?.submitList(animationList)
    }
    //endregion


    //region MARK: - public Method
    /**
     * ダイアログ表示
     * @param fragmentManager FragmentManager
     */
    fun showDialog(fragmentManager: FragmentManager) =
        show(fragmentManager, MainDialogFragment::class.java.simpleName)
    //endregion
}
