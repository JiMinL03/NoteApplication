package com.jimin.noteapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jimin.noteapplication.databinding.ItemNoteBinding

class NoteAdapter(
    private val list: ArrayList<Note>,
    private val listener: OnNoteClickListener,
    private val deleteListener: OnDeleteClickListener // 삭제 리스너 추가
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface OnNoteClickListener {
        fun onNoteClick(note: Note)
    }

    // 삭제 리스너 인터페이스
    interface OnDeleteClickListener {
        fun onDeleteClick(note: Note)
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note) {
            binding.noteTitle.text = item.title
            binding.noteContent.text = item.content

            binding.root.setOnClickListener {
                Log.d("NoteAdapter", "Item clicked: ${item.title}")
                listener.onNoteClick(item)
            }

            // 삭제 버튼 클릭 리스너 설정
            binding.deleteButton.setOnClickListener {
                Log.d("NoteAdapter", "Delete button clicked: ${item.title}")
                deleteListener.onDeleteClick(item) // 삭제 리스너 호출
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}

