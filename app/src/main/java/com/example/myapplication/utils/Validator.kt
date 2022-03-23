package com.example.myapplication.utils

import android.widget.EditText


class Validator {

    companion object{

        private val atLeastOneUppercase = Regex(".*[A-Z].*")
        private val atLeastOneDigit = Regex(".*\\d.*")
        private val atLeastOneSpecial = Regex(".*\\W.*")

        fun isPasswordValid(passwd: EditText) :Boolean{
            val str = passwd.text
            var valid = true

            if (!str.matches(atLeastOneUppercase)) {
                valid = false
                passwd.error = "Required at least one uppercase."
            }

            if (!str.matches(atLeastOneDigit)) {
                valid = false
                passwd.error = "Required at least one digit."
            }

            if (!str.matches(atLeastOneSpecial)) {
                valid = false
                passwd.error = "Required at least one special."
            }
            return valid
        }

        fun isEmpty(passwd: EditText, error: String): Boolean{
            val str = passwd.text
            var empty = false
            if(str.isEmpty()){
                passwd.error = error
                empty = true
            }
            return empty
        }

        fun isMatching(passwd1: EditText, passwd2: EditText): Boolean{
            if(passwd1.text.toString() != passwd2.text.toString()){
                passwd2.error = "Password does not match."
                return false
            }

            return true
        }

    }
}