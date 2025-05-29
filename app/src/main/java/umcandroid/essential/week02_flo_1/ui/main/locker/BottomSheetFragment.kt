package umcandroid.essential.week02_flo_1.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import umcandroid.essential.week02_flo_1.R

class BottomSheetFragment : BottomSheetDialogFragment() {

    interface OnSongActionListener {
        fun onUnlike(songId: Int, position: Int)
    }

    private var listener: OnSongActionListener? = null
    private var songId: Int = -1
    private var position: Int = -1

    fun setSongData(songId: Int, position: Int, listener: OnSongActionListener) {
        this.songId = songId
        this.position = position
        this.listener = listener
    }

    interface OnDeleteClickListener {
        fun onDeleteClicked()
    }

    private var deleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        this.deleteClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

        val deleteBtn = view.findViewById<ImageView>(R.id.bottom_sheet_iv4)
        deleteBtn.setOnClickListener {
            deleteClickListener?.onDeleteClicked()
            dismiss()
        }

        return view
    }
}


