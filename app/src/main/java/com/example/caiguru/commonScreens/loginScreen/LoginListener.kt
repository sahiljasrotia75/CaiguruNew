package com.example.caiguru.commonScreens.loginScreen

interface LoginListener {
    fun loginListener(email :String, password :String)
    fun facebookListener()
}