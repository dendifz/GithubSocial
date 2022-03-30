package com.dicoding.githubsocial.ui.detailuser

import com.dicoding.githubsocial.data.source.remote.RemoteRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class DetailUserViewModelTest {
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var repository: RemoteRepository

    private val username = "dendifz"

    @Before
    fun setUp() {
        repository = mock(RemoteRepository::class.java)
        detailUserViewModel = DetailUserViewModel(repository)
    }

    @Test
    fun cekFavorite() {
        detailUserViewModel.cekFavorite(username)
        verify(repository).cekFavorite(username)
    }
}