package com.kotlin.metaplexandroid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.metaplexandroid.databinding.ActivityMainBinding
import com.kotlin.metaplexandroid.databinding.ItemViewBinding
import com.metaplex.lib.modules.nfts.models.NFT
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityMainBinding

    private val viewModel : MainViewModel by viewModels()

    private lateinit var adapter: NFTViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.model = viewModel
        mBinding.lifecycleOwner = this

        setupView()
        observer()
    }
    private fun setupView(){
        adapter = NFTViewAdapter(this)
        val layoutManager = GridLayoutManager(this, 2)
        mBinding.recyclerView.layoutManager = layoutManager
        mBinding.recyclerView.adapter = adapter
    }

    private fun observer(){
        viewModel.nftLiveData.observe(this, {
            adapter.updateList(it)
        })
    }

}

class NFTViewAdapter constructor(private val context: Context):
    RecyclerView.Adapter<NFTViewAdapter.NftViewHolder>(){

    private var dataSet: List<NFT> = emptyList()

    class NftViewHolder (val itemViewBinding: ItemViewBinding): RecyclerView.ViewHolder(itemViewBinding.root)

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemViewBinding.inflate(inflater, parent, false)
        return NftViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: NftViewHolder, position: Int) {
        holder.itemViewBinding.nameTextView.text = dataSet[position].metadataAccount.data.name
        holder.itemViewBinding.mintTextView.text = dataSet[position].metadataAccount.mint.toBase58()
        Glide
            .with(context)
            .load(dataSet[position].metadataAccount.data.uri)
            .centerCrop()
            .placeholder(R.drawable.ic_mage)
            .into(holder.itemViewBinding.nftImageView)
    }

    fun updateList(it: List<NFT>) {
        this.dataSet = it
        notifyDataSetChanged()
    }
}

