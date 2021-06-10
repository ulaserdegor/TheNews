package com.ulaserdegor.thenews.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ulaserdegor.thenews.R
import com.ulaserdegor.thenews.ui.fragments.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onBackPressed() {

        // TODO: 10.06.2021 düzeltilecek
        if (supportFragmentManager.primaryNavigationFragment is MainFragment) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Uygulamayı Kapat")
            builder.setMessage("Uygulamayı kapatmak istediğinize emin misiniz?")

            builder.setPositiveButton("Evet") { _, _ ->
                finish()
            }

            builder.setNegativeButton("Hayır") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()

        } else {
            super.onBackPressed()
            supportActionBar!!.title = "Haber Kaynakları"
        }
    }
}