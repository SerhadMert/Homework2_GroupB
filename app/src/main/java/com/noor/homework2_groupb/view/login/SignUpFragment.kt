package com.noor.homework2_groupb.view.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.noor.homework2_groupb.R
import com.noor.homework2_groupb.base.BaseFragment
import com.noor.homework2_groupb.data.model.User
import com.noor.homework2_groupb.databinding.FragmentSignUpBinding
import java.util.regex.Pattern

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate,false) {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val email by lazy { binding.eTextEmail.text }
    private val password by lazy {  binding.eTextPassword.text }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            if (validEmail() && validPassword())
                signUpUser(email.toString(),password.toString())
        }

    }

    private fun signUpUser(email:String,password:String){

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                addUserToCollection(email,password)
                findNavController().navigate(R.id.signUpFragment_to_loginFragment)

            } else {
                Log.d("firebaseAuth", "fail")
            }
        }
    }
    private fun addUserToCollection(email: String,password: String){
        val user by lazy { User(email,password) }
        db.collection("users").document(auth.currentUser!!.uid).set(user)
    }
    private fun validEmail(): Boolean {
        val email2 = binding.eTextEmail.text
        if (email2.isNullOrEmpty()) {
            binding.eTextEmail.requestFocus()
            binding.textInputLayoutEmail.error = "Email is required!"
            return false
        } else if (email2.length < 5) {
            binding.eTextEmail.requestFocus()
            binding.textInputLayoutEmail.error = "Email is invalid!"
            Toast.makeText(context,"Must be longer than 5 characters",Toast.LENGTH_SHORT).show()
            return false
        } else if (email2.length > 50) {
            binding.eTextEmail.requestFocus()
            binding.textInputLayoutEmail.error = "Email is invalid!"
            Toast.makeText(context,"Must be shorter than 50 characters",Toast.LENGTH_SHORT).show()
            return false
        } else if (!email2.contains(Regex("@+."))) {
            binding.eTextEmail.requestFocus()
            binding.textInputLayoutEmail.error = "Email is invalid!"
            Toast.makeText(context,"Must require @ and .",Toast.LENGTH_SHORT).show()
            return false
        }


        binding.textInputLayoutEmail.isErrorEnabled = false

        return true
    } private fun validPassword():Boolean{
        val password2 = binding.eTextPassword.text
        /* val passwordRegex = Pattern.compile(
             "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])([^a-zA-Z0-9 ])$"
         )*/
        if (password2.isNullOrEmpty()) {
            binding.eTextPassword.requestFocus()
            binding.textInputLayoutPassword.error = "Password is required!"
            return false
        } else if (password2.length < 7) {
            binding.eTextPassword.requestFocus()
            binding.textInputLayoutPassword.error = "Password is too short!"
            Toast.makeText(context,"Must be longer than 7 characters",Toast.LENGTH_SHORT).show()
            return false
        } else if (password2.length > 40) {
            binding.eTextPassword.requestFocus()
            binding.textInputLayoutPassword.error = "Password is too long!"
            Toast.makeText(context,"Must be shorter than 40 characters",Toast.LENGTH_SHORT).show()
            return false
        } else if (!password2.contains(Regex("[0-9]"))) {
            binding.eTextPassword.requestFocus()
            binding.textInputLayoutPassword.error =
                "Password must contain one digit, one uppercase letter," +
                        "one lowercase letter and one special character!"
            return false
        } else if (!password2.contains(Regex("[A-Z]"))) {
            binding.eTextPassword.requestFocus()
            binding.textInputLayoutPassword.error =
                "Password must contain one digit, one uppercase letter," +
                        "one lowercase letter and one special character!"
            return false
        } else if (!password2.contains(Regex("[a-z]"))) {
            binding.eTextPassword.requestFocus()
            binding.textInputLayoutPassword.error =
                "Password must contain one digit, one uppercase letter," +
                        "one lowercase letter and one special character!"
            return false
        } else if (!password2.contains(Regex("[^a-zA-Z0-9 ]"))) {
            binding.eTextPassword.requestFocus()
            binding.textInputLayoutPassword.error =
                "Password must contain one digit, one uppercase letter," +
                        "one lowercase letter and one special character!"
            return false
        }
        binding.textInputLayoutPassword.isErrorEnabled = false
        return true
    }
}