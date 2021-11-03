package com.ezatpanah.commwebviewandroidapp

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mEditText: EditText
    lateinit var mButtonSend: Button
    private val mFilePath = "file:///android_asset/sampleweb.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEditText = findViewById(R.id.editInput)
        webViewSample.settings.javaScriptEnabled = true
        webViewSample.addJavascriptInterface(JSBridge(this, mEditText), "JSBridge")
        webViewSample.loadUrl(mFilePath)
        mButtonSend = findViewById(R.id.sendData)
        mButtonSend.setOnClickListener {
            sendDataToWebView()
        }
    }

    // Receive message from Webview and pass on to native.
    class JSBridge(val context: Context, val editTextInput: EditText) {
        @JavascriptInterface
        fun showMessageInNative(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            editTextInput.setText(message)
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Alert")
            builder.setMessage(message)
            builder.show()
        }
    }


    // Send data to Webview through function updateFromNative.
    private fun sendDataToWebView() {
        webViewSample.evaluateJavascript(
            "javascript: " + "updateFromNative(\"" + mEditText.text + "\")",
            null
        )
    }
}
