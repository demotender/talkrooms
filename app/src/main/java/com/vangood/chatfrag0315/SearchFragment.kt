package com.vangood.chatfrag0315

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.vangood.chatfrag0315.databinding.FragmentSearchBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    lateinit var binding:FragmentSearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_roomlist,menu)
        val item = menu.findItem(R.id.app_bar_search)
        val searchView =item.actionView as SearchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //pause and search key doing
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //一面打一面找
                return false
                /*
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)

                // Do something with selection
                return true
                */
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}