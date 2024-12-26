package com.jimin.noteapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import androidx.fragment.app.Fragment

class NoteFormFragment : Fragment() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var saveButton: Button
    private lateinit var dbHelper: NoteDatabaseHelper
    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_note_form, container, false)

        editTextTitle = binding.findViewById(R.id.editTextTitleForm)
        editTextContent = binding.findViewById(R.id.editTextContentForm)
        saveButton = binding.findViewById(R.id.saveButton)

        dbHelper = NoteDatabaseHelper(requireContext())

        arguments?.let {
            note = it.getSerializable("note") as? Note
            note?.let {
                editTextTitle.setText(it.title)
                editTextContent.setText(it.content)
            }
        }

        saveButton.setOnClickListener {
            val title = editTextTitle.text.toString()
            val content = editTextContent.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                if (note == null) {
                    dbHelper.addNote(title, content)
                } else {
                    note?.title = title
                    note?.content = content

                    note?.id?.let { id ->
                        dbHelper.updateNote(id, title, content)
                    }
                }

                requireActivity().supportFragmentManager.popBackStack()
                (activity as MainActivity).loadNotes()
            }
        }

        return binding
    }
}
