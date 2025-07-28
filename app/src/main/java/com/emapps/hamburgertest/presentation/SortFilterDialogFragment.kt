package com.emapps.hamburgertest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.emapps.hamburgertest.databinding.DialogSortFilterBinding
import com.emapps.hamburgertest.domain.model.SortOption

class SortFilterDialogFragment : DialogFragment() {

    private var _binding: DialogSortFilterBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "rendering issue: sort/filter dialog"
        }

    var onApply: ((sortOptions: List<SortOption>) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogSortFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.applyBtn.setOnClickListener {
            val sortOptions = mutableListOf<SortOption>()
            if (binding.sortPriceDesc.isChecked) sortOptions.add(SortOption.PRICE_HIGH)
            if (binding.sortPriceAsc.isChecked) sortOptions.add(SortOption.PRICE_LOW)
            if (binding.sortRatingDesc.isChecked) sortOptions.add(SortOption.RATING_HIGH)
            if (binding.sortRatingAsc.isChecked) sortOptions.add(SortOption.RATING_LOW)
            if (binding.sortVegan.isChecked) sortOptions.add(SortOption.VEGAN)
            if (binding.sortNonVegan.isChecked) sortOptions.add(SortOption.NOT_VEGAN)
            if (binding.sortHot.isChecked) sortOptions.add(SortOption.HOT)
            if (binding.sortNotHot.isChecked) sortOptions.add(SortOption.NOT_HOT)
            if (binding.sortAvailable.isChecked) sortOptions.add(SortOption.AVAILABLE)
            if (binding.sortNotAvailable.isChecked) sortOptions.add(SortOption.NOT_AVAILABLE)
            onApply?.invoke(sortOptions)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
            window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SortFilterDialogFragment()
    }
}