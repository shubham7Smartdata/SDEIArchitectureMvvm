package com.sdi.sdeiarchitecturemvvm.activities

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * Created by shubham on 22/05/19.
 */

abstract class BaseActivity<V : AndroidViewModel> : AppCompatActivity() {

    var TAG: String = "BaseActivity:-"

    // since its going to be common for all the activities
    var mViewModel: V? = null
    lateinit var mContext: Context
    private var mSnackbar: Snackbar? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
//    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    /**
     *
     * @return context
     */
    protected abstract val context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        this.mContext = context
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        onCreate()
        initListeners()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    abstract fun onCreate()

    abstract fun initListeners()

    protected fun showSnackBar(view: View, message: String) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        mSnackbar!!.show()
    }

    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showAlertSnackBar(view: View, message: String) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        mSnackbar!!.view.setBackgroundColor(Color.RED)
        mSnackbar!!.show()
    }

//    private fun performDataBinding() {
//        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
//        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
//        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
//        mViewDataBinding.executePendingBindings()
//    }

}

