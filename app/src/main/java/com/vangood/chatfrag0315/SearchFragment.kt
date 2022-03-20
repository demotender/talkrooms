package com.vangood.chatfrag0315

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.vangood.chatfrag0315.databinding.FragmentHomePopulerBinding
import com.vangood.chatfrag0315.databinding.FragmentSearchBinding
import com.vangood.chatfrag0315.databinding.RowHotroomsBinding

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    private lateinit var adapter:ChatRoomAdapter

    val viewModel by viewModels<HPViewModel>()
    val roomModel by viewModels<RoomViewModel>()
    val rooms = mutableListOf<Lightyear>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerSearch.setHasFixedSize(true)
        binding.recyclerSearch.layoutManager = GridLayoutManager(requireContext(),2)
        adapter = ChatRoomAdapter()
        binding.recyclerSearch.adapter =adapter

        viewModel.talkRooms.observe(viewLifecycleOwner) { rooms ->
            adapter.submitRooms(rooms)
        }
        viewModel.getALLRooms()
    }
    inner class ChatRoomAdapter: RecyclerView.Adapter<SearchHolder>(){
        val chatRooms = mutableListOf<Lightyear>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
            val binding = RowHotroomsBinding.inflate(layoutInflater,parent,false)
            return SearchHolder(binding)
        }
        //圓角
        val option = RequestOptions()
            .error(R.mipmap.ic_launcher_round)
            .transform(CenterCrop(), RoundedCorners(50))

        override fun onBindViewHolder(holder: SearchHolder, position: Int) {
            val lightYear= chatRooms[position]
            holder.title.setText(lightYear.stream_title)
            holder.nickname.setText(lightYear.nickname)
            Glide.with(this@SearchFragment)
                .applyDefaultRequestOptions(option)
                .load(lightYear.head_photo)
                .into(holder.headpic)
            holder.itemView.setOnClickListener {
                chatRoomClicked(lightYear)
                Log.d("to talkActivity", "$lightYear")

            }
        }

        override fun getItemCount(): Int {
            return chatRooms.size
        }
        fun submitRooms(rooms: List<Lightyear>) {
            chatRooms.clear()
            chatRooms.addAll(rooms)
            notifyDataSetChanged()
        }


    }
    inner class SearchHolder(val binding: RowHotroomsBinding): RecyclerView.ViewHolder(binding.root){
        val title = binding.tvTitle
        val nickname = binding.tvName
        val headpic = binding.imageView

    }
    fun chatRoomClicked(lightyear : Lightyear) {

        val bundle = Bundle().apply {
            putParcelable("room", lightyear)
        }
        val intent = Intent(requireContext(),TalkRoomActivity::class.java)
        intent.putExtra("bundle",bundle)
        startActivity(intent)

        val inf = bundle.getParcelable<Lightyear>("room")
        Log.d("pref room ", "${inf?.nickname}")
        Log.d("to talkActivity clicked", "$bundle")
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_roomlist, menu)
        val item = menu.findItem(R.id.app_bar_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {


                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchword = binding.searchView.query.toString()
                roomModel.getSearchRooms(searchword)

                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}