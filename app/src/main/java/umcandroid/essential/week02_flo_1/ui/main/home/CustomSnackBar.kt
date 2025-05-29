package umcandroid.essential.week02_flo_1.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import umcandroid.essential.week02_flo_1.databinding.CustomSnackbarBinding
import androidx.databinding.DataBindingUtil
import umcandroid.essential.week02_flo_1.R

class CustomSnackbar(view: View, private val message: String) {

    companion object {
        fun make(view: View, message: String) = CustomSnackbar(view, message)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
    private val snackbarLayout: ViewGroup = snackbar.view as ViewGroup

    private val inflater = LayoutInflater.from(context)
    private val snackbarBinding: CustomSnackbarBinding = DataBindingUtil.inflate(
        inflater, R.layout.custom_snackbar, null, false
    )

    init {
        initView()
        initData()
    }

    private fun initView() {
        // 기본 snackbarLayout을 완전히 제거하고, custom 뷰로 교체
        snackbarLayout.removeAllViews()
        snackbarLayout.addView(snackbarBinding.root)

        // 커스터마이징된 배경을 적용할 수 있습니다.
        snackbarLayout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
    }

    private fun initData() {
        snackbarBinding.customSnackbarTv.text = message
        snackbarBinding.customSnackbarBtn.setOnClickListener {
            snackbar.dismiss()
        }
    }

    fun show() {
        snackbar.show()
    }
}
