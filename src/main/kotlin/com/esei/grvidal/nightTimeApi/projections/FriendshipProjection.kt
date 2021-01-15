package com.esei.grvidal.nightTimeApi.projections

import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions

interface FriendshipProjection {

    fun getId(): Long
    fun getUserAsk(): UserProjection
    fun getUserAnswer(): UserProjection
    fun getAnswer(): AnswerOptions
}