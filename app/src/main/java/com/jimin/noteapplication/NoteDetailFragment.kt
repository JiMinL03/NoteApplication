package com.jimin.noteapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jimin.noteapplication.databinding.FragmentNoteDetailBinding

class NoteDetailFragment : Fragment() {
    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    private var note: Note? = null

    companion object {
        private const val ARG_NOTE = "note"

        // 새로운 인스턴스를 생성하는 메서드
        @JvmStatic
        fun newInstance(note: Note?): NoteDetailFragment {
            val fragment = NoteDetailFragment()
            val args = Bundle()

            // Note 객체가 null일 수 있음을 처리
            note?.let {
                args.putSerializable(ARG_NOTE, it) // Serializable 객체를 Bundle에 추가
                Log.d("MainActivity", "Passing note to fragment: ${note.title}")
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 바인딩 객체 초기화
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)

        // arguments에서 note 객체를 읽어오기 전에 로그 추가
        Log.d("NoteDetailFragment", "Arguments: ${arguments?.getSerializable(ARG_NOTE)}")

        // Note 객체를 전달받아 텍스트뷰에 설정
        arguments?.let {
            note = it.getSerializable(ARG_NOTE) as? Note
            Log.d("NoteDetailFragment", "Fragment created with note: ${note?.title}")
            note?.let {
                binding.noteDetailTitle.text = it.title
                binding.noteDetailContent.text = it.content
            }
        }

        binding.updateNote.setOnClickListener {
            // 업데이트할 데이터와 함께 NoteFormFragment로 이동
            val noteFormFragment = NoteFormFragment()
            val bundle = Bundle()
            bundle.putSerializable("note", note) // 데이터를 전달
            noteFormFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, noteFormFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 뷰 바인딩 객체 해제
    }
}

