package com.example.dukatrack

import com.example.dukatrack.data.ProductDao
import com.example.dukatrack.event.ProductEvent
import com.example.dukatrack.ui.products.ProductViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private val productDao = mockk<ProductDao>(relaxed = true)
    private lateinit var viewModel: ProductViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { productDao.getAllCategories() } returns flowOf(emptyList())
        every { productDao.searchProductsWithStock("") } returns flowOf(emptyList())
        viewModel = ProductViewModel(productDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `SearchQueryChanged event updates state`() = runTest {
        val query = "test query"
        viewModel.onEvent(ProductEvent.SearchQueryChanged(query))
        
        advanceUntilIdle()
        
        assertEquals(query, viewModel.state.value.searchQuery)
    }
}
