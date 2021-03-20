package me.tolkstudio.convertapp.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import me.tolkstudio.convertapp.App
import me.tolkstudio.convertapp.ConverterLogik
import me.tolkstudio.convertapp.IBackClickListener
import me.tolkstudio.convertapp.OPEN_IMAGE_REQUEST_CODE
import me.tolkstudio.convertapp.databinding.FragmentConverterBinding
import me.tolkstudio.convertapp.mvp.prezenter.ConverterPrezenter
import me.tolkstudio.convertapp.mvp.view.ConverterView
import moxy.MvpAppCompatActivity
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class FragmentConverter : MvpAppCompatFragment(), ConverterView, IBackClickListener {
    private var dialog: Dialog? = null
    private var ui: FragmentConverterBinding? = null

    companion object {
        fun newInstance() = FragmentConverter()
    }

    private val presenter by moxyPresenter {
        ConverterPrezenter(AndroidSchedulers.mainThread(), App.instance.router)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentConverterBinding.inflate(inflater, container, false).also {
        ui = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui?.btnOpenFile?.setOnClickListener { openFile() }
        ui?.btnConvert?.setOnClickListener {
            presenter.btnConvertClick()
            ui?.btnConvert?.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ui = null
    }

    override fun setImg(data: Uri) {
        ui?.imgSource?.setImageURI(data)
    }

    override fun setText(data: String) {
        ui?.txtSource?.text = data
    }

    fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/png"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, OPEN_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == OPEN_IMAGE_REQUEST_CODE && resultCode == MvpAppCompatActivity.RESULT_OK) {
            resultData?.data?.also {
                presenter.setData(ConverterLogik(requireContext(), it))
                ui?.btnConvert?.visibility = View.VISIBLE
            }
        }
    }

    override fun showProgress() {
        dialog = AlertDialog.Builder(requireContext())
            .setMessage("Processing")
            .setNegativeButton("Cancel") { _, _ -> presenter.cancelPressed() }
            .create()
        dialog?.show()
    }

    override fun hideProgress() {
        dialog?.dismiss()
    }

    override fun showSuccess() {
        Toast.makeText(context, "Convert complete", Toast.LENGTH_SHORT).show()
    }

    override fun showError() {
        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun showCancel() {
        Toast.makeText(context, "Convert Cancel", Toast.LENGTH_SHORT).show()
    }

    override fun backPressed() = presenter.backPressed()

}