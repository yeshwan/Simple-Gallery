package com.simplemobiletools.gallery.dialogs

import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.WindowManager
import com.simplemobiletools.commons.dialogs.FilePickerDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.gallery.R
import com.simplemobiletools.gallery.activities.SimpleActivity
import kotlinx.android.synthetic.main.rename_file.view.*
import java.io.File

class SaveAsDialog(val activity: SimpleActivity, val path: String, val callback: (savePath: String) -> Unit) {

    init {
        var realPath = File(path).parent.trimEnd('/')
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_save_as, null).apply {
            file_path.text = activity.humanizePath(realPath)

            val fullName = path.getFilenameFromPath()
            val dotAt = fullName.lastIndexOf(".")
            var name = fullName

            if (dotAt > 0) {
                name = fullName.substring(0, dotAt)
                val extension = fullName.substring(dotAt + 1)
                file_extension.setText(extension)
            }

            file_name.setText(name)
            file_path.setOnClickListener {
                FilePickerDialog(activity, realPath, false, false, true) {
                    file_path.text = activity.humanizePath(it)
                    realPath = it
                }
            }
        }

        AlertDialog.Builder(activity)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null)
                .create().apply {
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            activity.setupDialogStuff(view, this, R.string.save_as)
            getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener({
                val filename = view.file_name.value
                val extension = view.file_extension.value

                if (filename.isEmpty()) {
                    context.toast(R.string.filename_cannot_be_empty)
                    return@setOnClickListener
                }

                if (extension.isEmpty()) {
                    context.toast(R.string.extension_cannot_be_empty)
                    return@setOnClickListener
                }

                val newFile = File(realPath, "$filename.$extension")
                if (!newFile.name.isAValidFilename()) {
                    context.toast(R.string.filename_invalid_characters)
                    return@setOnClickListener
                }

                callback.invoke(newFile.absolutePath)
                dismiss()
            })
        }
    }
}
