package com.kotlin.metaplexandroid

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.metaplexandroid.util.verifyAsString
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject  constructor() : ViewModel() {

    var publicKey = MutableLiveData<String?>().apply { value = null }
    var publicKeyError = MutableLiveData<Int?>()
    var nftLiveData = MutableLiveData<List<NFT>>()

    private fun loadWithKey(key: String){
        viewModelScope.launch {
            //load the sdk here and set reply for observer
            Log.v("key", key)
            val ownerPublicKey = PublicKey(key)
            val solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
            val solanaIdentityDriver = ReadOnlyIdentityDriver(ownerPublicKey, solanaConnection.solanaRPC)
            val storageDriver = OkHttpSharedStorageDriver()
            val metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
            metaplex.nft.findAllByOwner(ownerPublicKey){ result ->
                result.onSuccess { nfts ->
                    val nftList = nfts.filterNotNull()
                    nftLiveData.postValue(nftList)

                }.onFailure {
                    publicKeyError.postValue(R.string.no_nft_response)
                }
            }
        }
    }

    fun onGoTapped(){
        publicKeyError.value = publicKey.value.verifyAsString()
        if (publicKeyError.value==null){
            loadWithKey(publicKey.value!!)
        }
    }
}