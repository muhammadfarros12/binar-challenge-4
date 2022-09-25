package com.example.noteapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.SharedPreference
import com.example.noteapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding as FragmentRegisterBinding

    private var sharedPref: SharedPreference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPreference(view.context)

        binding.btnRegister.setOnClickListener {

            val username = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (binding.edtUsername.text.isNullOrBlank() || binding.edtEmail.text.isNullOrBlank() || binding.edtPassword.text.isNullOrBlank()){
                Toast.makeText(context, "Lengkapi Field diatas", Toast.LENGTH_SHORT).show()
            } else {
                sharedPref?.saveKey(username,email, password)
                Toast.makeText(context, "Akun Telah Berhasil dibuat", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }

        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}