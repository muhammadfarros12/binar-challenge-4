package com.example.noteapp.ui.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.MainActivity
import com.example.noteapp.R
import com.example.noteapp.data.NoteDatabase
import com.example.noteapp.data.entity.NoteEntity
import com.example.noteapp.databinding.ItemNoteLayoutBinding
import com.example.noteapp.ui.NoteFragmentDirections
import java.util.concurrent.Executors

class NoteAdapter : ListAdapter<NoteEntity, NoteAdapter.ViewHolder>(NoteDiffCallback()){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemNoteLayoutBinding.bind(itemView)

        fun bind(data: NoteEntity){
            binding.txtTitle.text = data.judul
            binding.txtDesc.text = data.catatan
            binding.imgUpdate.setOnClickListener {
                // for update
                val dataArgs = NoteFragmentDirections.actionNoteFragmentToNoteEditFragment(data)
                it.findNavController().navigate(dataArgs)
            }
            binding.imgDelete.setOnClickListener {
                AlertDialog.Builder(it.context)
                    .setMessage("Apakah Anda yakin akan menghapus catatan ini?")
                    .setCancelable(false)
                    .setPositiveButton("Ya"){_, _ ->
                        val db = NoteDatabase.getInstance(itemView.context)

                        val executor = Executors.newCachedThreadPool()

                        executor.execute{
                            val result = db?.noteDao()?.deleteNote(data)

                            (itemView.context as MainActivity).runOnUiThread{
                                if (result != 0){
                                    Toast.makeText(itemView.context, "Data berhasil didelete", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(itemView.context, "Data gagal didelete", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    .setNegativeButton("Tidak"){dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_note_layout, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }
}

class NoteDiffCallback: DiffUtil.ItemCallback<NoteEntity>(){
    override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return oldItem == newItem
    }

}
