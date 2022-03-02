package com.apps.programmingheroquiz.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.apps.programmingheroquiz.R
import com.apps.programmingheroquiz.network.ServiceGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val url = imgUrl
        //val imgUri = url.toUri().buildUpon().scheme("http").build()
        Glide.with(imgView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_baseline_broken_image_24))
            .into(imgView)
    }
}