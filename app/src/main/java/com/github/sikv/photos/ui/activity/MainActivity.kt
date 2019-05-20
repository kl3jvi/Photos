package com.github.sikv.photos.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.github.sikv.photos.R
import com.github.sikv.photos.data.DataSourceState
import com.github.sikv.photos.data.PhotoSource
import com.github.sikv.photos.model.Photo
import com.github.sikv.photos.ui.adapter.PhotoPagedListAdapter
import com.github.sikv.photos.ui.popup.PhotoPreviewPopup
import com.github.sikv.photos.viewmodel.PhotosViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading_error.*
import kotlinx.android.synthetic.main.layout_no_results_found.*


class MainActivity : BaseActivity() {

    companion object {
        private const val TOOLBAR_ELEVATION = 12f
    }

    private val viewModel: PhotosViewModel by lazy {
        ViewModelProviders.of(this).get(PhotosViewModel::class.java)
    }

    private var photoAdapter: PhotoPagedListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        init()
        initPhotoAdapter()

        observeRecentPhotos()
        observeState()
    }

    private fun observeRecentPhotos() {
        viewModel.getPhotos(PhotoSource.PEXELS)?.observe(this, Observer<PagedList<Photo>> { pagedList ->
            photoAdapter?.submitList(pagedList)
        })
    }

    private fun observeState() {
        viewModel.getState(PhotoSource.PEXELS)?.observe(this, Observer { state ->
            mainLoadingLayout.visibility = View.GONE
            loadingErrorLayout.visibility = View.GONE

            when (state) {
                DataSourceState.LOADING_INITIAL -> {
                    mainLoadingLayout.visibility = View.VISIBLE
                }

                DataSourceState.ERROR -> {
                    loadingErrorLayout.visibility = View.VISIBLE
                }

                else -> {
                }
            }
        })
    }

    private fun onPhotoClick(photo: Photo, view: View) {
        PhotoActivity.startActivity(this, view, photo)
    }

    private fun onPhotoLongClick(photo: Photo, view: View) {
        PhotoPreviewPopup.show(this, mainRootLayout, photo)
    }

    private fun init() {
        mainSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))

        mainSwipeRefreshLayout.setOnRefreshListener {
            mainSwipeRefreshLayout.isRefreshing = false

            // TODO Implement
        }

        mainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                recyclerView?.computeVerticalScrollOffset()?.let {
                    if (it > 0) {
                        ViewCompat.setElevation(mainAppBarLayout, TOOLBAR_ELEVATION)
                    } else {
                        ViewCompat.setElevation(mainAppBarLayout, 0.0f)
                    }
                }
            }
        })

        mainSearchButton.setOnClickListener {
            SearchActivity.startActivity(this)
        }

        mainFavoritesButton.setOnClickListener {
            FavoritesActivity.startActivity(this)
        }
    }

    private fun initPhotoAdapter() {
        photoAdapter = PhotoPagedListAdapter(Glide.with(this), ::onPhotoClick, ::onPhotoLongClick)
        mainRecycler.adapter = photoAdapter

        loadingErrorLayout.visibility = View.GONE
        noResultsFoundLayout.visibility = View.GONE
    }
}