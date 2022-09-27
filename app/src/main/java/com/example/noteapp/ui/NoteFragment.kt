package com.example.noteapp.ui

import android.app.ActionBar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.data.NoteDatabase
import com.example.noteapp.data.SharedPreference
import com.example.noteapp.data.entity.NoteEntity
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.runOnUiTread
import com.example.noteapp.ui.adapter.NoteAdapter
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.Executors

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding as FragmentNoteBinding

    private var noteDb: NoteDatabase? = null
    private var sharedPref: SharedPreference? = null
    private var data: List<NoteEntity>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(layoutInflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPreference(view.context)
        noteDb = NoteDatabase.getInstance(view.context)

        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.apply {
            homeToolbar.inflateMenu(R.menu.home_menu)
            homeToolbar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.refresh -> {
                        fetchData()
                        Snackbar.make(binding.root, "Data Terbaru berhasil didapat", Snackbar.LENGTH_SHORT).show()
                        true
                    }
                    R.id.logout -> {
                        sharedPref!!.clearUsername()
                        findNavController().navigate(R.id.action_noteFragment_to_loginFragment)
                        true
                    }
                    else -> false
                }
            }

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            txtUsername.text = getString(R.string.hello_user, sharedPref?.getPrefKey("username"))

            fabAdd.setOnClickListener {
                val add = NoteAddFragment()
                add.show(childFragmentManager, "add")
            }
        }
        fetchData()
    }

    private fun fetchData(){
        val executor = Executors.newSingleThreadExecutor()
        executor.execute{
            data = noteDb?.noteDao()?.getAllNote()

            runOnUiTread {
                val adapter = NoteAdapter()
                adapter.submitList(data)
                binding.recyclerView.adapter = adapter
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}