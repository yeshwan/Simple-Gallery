package com.simplemobiletools.gallery.activities

import android.os.Bundle

class PhotoActivity : PhotoVideoActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        PhotoVideoActivity.mIsVideo = false
        super.onCreate(savedInstanceState)
    }

    override fun systemUiVisibilityChanged(visibility: Int) {
    }
}
