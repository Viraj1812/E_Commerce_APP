package com.hvdev.ECommerceAPP.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hvdev.ECommerceAPP.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    //shared flow is used to send one time event to the UI to take some action in showing snack bar or navigate
    // to another screen.
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow() // asSharedFLow function convert mutable Sharedflow to immutable shrared flow

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _login.asSharedFlow()

    fun login(email : String, password: String)
    {
        viewModelScope.launch { _login.emit(Resource.Loading()) }
        firebaseAuth.signInWithEmailAndPassword(
            email,password
        ).addOnSuccessListener {
            viewModelScope.launch {
                it.user?.let {
                    // emit is suspended function that's why we are going to use a coroutine scope and we will use the viewModel scope
                    _login.emit(Resource.Success(it))
                }
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                    _login.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    fun resetPassword(email: String){
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())

            firebaseAuth
                .sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _resetPassword.emit(Resource.Success(email))
                    }
                }.addOnFailureListener{
                    viewModelScope.launch {
                        _resetPassword.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }


}