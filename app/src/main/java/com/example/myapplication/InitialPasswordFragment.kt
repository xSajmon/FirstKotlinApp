package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.myapplication.utils.AppPreferences
import com.example.myapplication.utils.MainApplication


class InitialPasswordFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view = inflater.inflate(R.layout.fragment_initial_password, container, false)

        val sharedPreferences =  AppPreferences.getInstance(MainApplication.applicationContext())

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