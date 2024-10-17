package com.example.githubviewer.data.model.mappers

interface Mapper<I, out O> {
    fun map(input: I): O
}