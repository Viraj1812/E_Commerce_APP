package com.hvdev.ECommerceAPP.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hvdev.ECommerceAPP.data.User
import com.hvdev.ECommerceAPP.fragments.RegisterFragment
import com.hvdev.ECommerceAPP.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


/*

Without dagger hilt : as we know in the view models we pass the dependencies from the outside
and what we are going to use in this class is an instance from the firebase authentication so
to get that we use here firebaseAuth as mention below

Now without dagger hilt if you want to get an insert from this viewmodel class you need first to
create a viewmodel provider factory class and injectory dependency which is this one from data class
to the view model but with dagger hilt there is no need for that we can just ude this annotation
(@HiltViewModel) and using one more which is @inject before the constructure and use the constructor
keyword

    class LoginRegisterViewModel(
        private val firebaseAuth: FirebaseAuth
    ): ViewModel() {
    }
*/
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register : Flow<Resource<FirebaseUser>> = _register

    private val _validation = Channel<RegisterFieldsState> ()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user : User , password : String){
        if(checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        _register.value = Resource.Success(it)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }else{
            val registerFieldsState = RegisterFieldsState(
                validateEmail(user.email),
                validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun checkValidation(user: User, password: String) : Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success

        return shouldRegister
    }

}