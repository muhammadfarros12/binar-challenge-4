package com.example.noteapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.noteapp.R
import com.example.noteapp.data.NoteDatabase
import com.example.noteapp.data.entity.NoteEntity
import com.example.noteapp.databinding.FragmentNoteEditBinding
import com.example.noteapp.runOnUiTread
import java.util.concurrent.Executors


class NoteEditFragment : DialogFragment() {

    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding as FragmentNoteEditBinding
    private var noteDb: NoteDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteEditBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteArgs = arguments?.getParcelable<NoteEntity>("note")
        noteDb = NoteDatabase.getInstance(view.context)

        binding.apply {
            edtJudul.setText(noteArgs?.judul)
            edtCatatan.setText(noteArgs?.catatan)

            btnUpdate.setOnClickListener {
                noteArgs?.judul = edtJudul.text.toString()
                noteArgs?.catatan = edtCatatan.text.toString()

                val executor = Executors.newFixedThreadPool(1)

                executor.execute {
                    val result = noteDb?.noteDao()?.updateNote(noteArgs!!)
                    runOnUiTread {
                        if (result != null){
                            Toast.makeText(context, "Berhasil diupdate", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Terdapat Kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog?.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialog?.dismiss()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}