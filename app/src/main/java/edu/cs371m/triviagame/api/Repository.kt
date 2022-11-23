package edu.cs371m.triviagame.api

class Repository(private val api: TriviaApi) {
    suspend fun getQuestions(diff: String): List<TriviaQuestion> {
        return api.getQuestions(diff).results
    }
}
