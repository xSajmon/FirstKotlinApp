package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


class InitialPasswordFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_initial_password, container, false)

        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "xyz",
            masterKey,
            view.context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val initialPasswordBtn = view.findViewById<Button>(R.id.initialPasswordBtn)

        initialPasswordBtn.setOnClickListener{

        val initialPasswordField = view.findViewById<EditText>(R.id.initialPasswordField)
            if(initialPasswordField.text.toString() != ""){
                sharedPreferences.edit().putString("passwd", initialPasswordField.text.toString()).apply()

                dismiss()
            }

        }
        return view
    }



}