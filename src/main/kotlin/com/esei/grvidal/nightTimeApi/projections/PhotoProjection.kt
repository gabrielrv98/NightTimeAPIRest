package com.esei.grvidal.nightTimeApi.projections

interface PhotoProjection{
    fun getId() : Long
    fun getDir(): String
}