package example.test.phong.coffeeapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;

class HomeViewModel : ViewModel() {
    private val offsetPercentAppBar: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }

    fun getOffsetPercentAppbar() = offsetPercentAppBar

    override fun onCleared() {
        super.onCleared()
    }
}
