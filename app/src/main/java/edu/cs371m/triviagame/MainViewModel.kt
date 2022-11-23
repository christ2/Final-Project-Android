package edu.cs371m.triviagame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import edu.cs371m.triviagame.api.Repository
import edu.cs371m.triviagame.api.TriviaApi
import edu.cs371m.triviagame.api.TriviaQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var difficulty = "medium"
    private var api: TriviaApi = TriviaApi.create()
    private var repository: Repository = Repository(api)
    private val data = MutableLiveData<List<TriviaQuestion>>()

    fun netFetchData() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        data.postValue(repository.getQuestions(difficulty))
    }

    fun observeData(): LiveData<List<TriviaQuestion>> {
        return data
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}
