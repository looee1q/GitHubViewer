package com.example.githubviewer.data.model.mappers

interface Mapper<in I, out O> {
    fun map(input: I): O
}