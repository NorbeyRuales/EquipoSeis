package com.example.pico_botella.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.pico_botella.model.Reto
import com.example.pico_botella.repository.RetoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class RetosViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repository: RetoRepository

    private lateinit var viewModel: RetosViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = RetosViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insert reto calls repository insert`() = runTest {
        val reto = Reto(descripcion = "Test Reto")
        viewModel.insert(reto)
        testDispatcher.scheduler.advanceUntilIdle()
        verify(repository).insert(reto)
    }

    @Test
    fun `update reto calls repository update`() = runTest {
        val reto = Reto(id = "1", descripcion = "Updated Reto")
        viewModel.update(reto)
        testDispatcher.scheduler.advanceUntilIdle()
        verify(repository).update(reto)
    }

    @Test
    fun `delete reto calls repository delete`() = runTest {
        val reto = Reto(id = "1", descripcion = "Delete Reto")
        viewModel.delete(reto)
        testDispatcher.scheduler.advanceUntilIdle()
        verify(repository).delete(reto)
    }
}
