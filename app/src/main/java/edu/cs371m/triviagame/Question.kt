package edu.cs371m.triviagame

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(val text: String, val answers: List<String>): Parcelable

@Parcelize
class Questions: ArrayList<List<Question>>(), Parcelable