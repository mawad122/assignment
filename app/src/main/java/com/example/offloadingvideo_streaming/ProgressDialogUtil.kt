import android.app.ProgressDialog
import android.content.Context

class ProgressDialogUtil(private val context: Context) {
    private var progressDialog: ProgressDialog? = null

    fun showProgressDialog(message: String) {
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
}
