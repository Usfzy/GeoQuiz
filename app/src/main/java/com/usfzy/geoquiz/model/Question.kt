package com.usfzy.geoquiz.model

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean)