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
import com.example.noteapp.databinding.FragmentNoteAddBinding
import com.example.noteapp.runOnUiTread
import java.util.concurrent.Executors


class NoteAddFragment : DialogFragment() {

    private var _binding: FragmentNoteAddBinding? = null
    private val binding get() = _binding as FragmentNoteAddBinding

    private var noteDb: NoteDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteAddBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteDb = NoteDatabase.getInstance(view.context)

        binding.apply {
            btnSave.setOnClickListener {
                val note = NoteEntity(
                    null,
                    edtJudul.text.toString(),
                    edtCatatan.text.toString()
                )

                val executor = Executors.newFixedThreadPool(1)

                executor.execute {
                    val result = noteDb?.noteDao()?.insertNote(note)
                    runOnUiTread {
                        if (result!=0.toLong()){
                            Toast.makeText(context, "Berhasil disave", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Ada Kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                dialog?.dismiss()

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
        NoteDatabase.destroyInstance()
    }

}