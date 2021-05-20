package com.example.firstapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.firstapp.R
import com.example.firstapp.activity.DetailActivity
import com.example.firstapp.adapter.ContentListAdapter
import com.example.firstapp.databinding.FragmentContentListBinding
import com.example.firstapp.viewmodel.ContentListViewModel

class ContentListFragment : Fragment() {
    private val TAG = this::class.java.simpleName

    private val contentViewModel: ContentListViewModel by lazy {
        ViewModelProvider(this).get(ContentListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentContentListBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.contentView = contentViewModel

        // Java
        binding.contentListRecyclerView.adapter = ContentListAdapter(ContentListAdapter.OnClickListener {
            contentViewModel.showContent(it)
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("Link", it.contentLink)
            startActivity(intent)
        })

//        val contentListAdapter = ContentListAdapter(this, contentList){ content ->
//            val intent = Intent(this, DetailActivity::class.java)
//            intent.putExtra("Link", content.contentLink)
//            startActivity(intent)
//        }
//
        // Kotlin
        contentViewModel.navigateToSelectedContent.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                Log.d(TAG, it.title)
                // Must find the NavController from the Fragment
                this.findNavController().navigate(ContentListFragmentDirections.actionShowContent(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                contentViewModel.showContentComplete()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        contentViewModel.updateContents()
        return true
    }
}
