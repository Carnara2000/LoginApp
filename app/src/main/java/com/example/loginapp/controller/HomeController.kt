package com.example.loginapp.controller

import com.example.loginapp.model.Role
import com.example.loginapp.model.User

class HomeController {

    interface View {
        fun showUserData(user: User, role: Role)
    }

    fun loadUserData(user: User, role: Role, view: View) {
        view.showUserData(user, role)
    }
}