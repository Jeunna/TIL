package com.example.firstapp.fragment

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.view.*
import com.example.firstapp.R
import com.example.firstapp.data.ContentModel
import com.example.firstapp.databinding.FragmentDetailBinding
import com.example.firstapp.viewmodel.DetailViewModel
import com.example.firstapp.viewmodel.DetailViewModelFactory

class DetailFragment : Fragment() {

    var rootConstraintLayout: ConstraintLayout? = null
    var content: ContentModel? = null

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        content = DetailFragmentArgs.fromBundle(requireArguments()).selected

        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val viewModelFactory = DetailViewModelFactory(content!!, application)
        binding.viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(DetailViewModel::class.java)

        rootConstraintLayout = binding.constraintLayout

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onGroupItemClick(item: MenuItem) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by <code><a href="/reference/android/app/Activity.html#onOptionsItemSelected(android.view.MenuItem)">onOptionsItemSelected()</a></code>
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId){
        R.id.action_toggle_detail -> {
            true
        }
        else ->{
            super.onOptionsItemSelected(item)
        }
    }

}