package com.esei.grvidal.nightTimeApi.projections

import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions

interface FriendProjection {

    fun getId(): Long
    fun getUser1(): UserProjection
    fun getUser2(): UserProjection
    fun getAnswer(): AnswerOptions
}