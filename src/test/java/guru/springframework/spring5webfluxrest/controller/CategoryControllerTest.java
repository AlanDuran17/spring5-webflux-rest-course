package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @Before
    public void setup() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void testAllCategories() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(
                        Category.builder().description("cat1").build(),
                        Category.builder().description("cat2").build()));

        webTestClient.get()
                .uri(CategoryController.BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void testGetCategoryById() {
        BDDMockito.given(categoryRepository.findById(Mockito.anyString()))
                .willReturn(Mono.just(
                        Category.builder().description("cat1").build()));

        webTestClient.get()
                .uri(CategoryController.BASE_URL.concat("{id}"), "mock")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Category.class);
    }

    @Test
    public void testCreate() {
        BDDMockito.given(categoryRepository.saveAll(Mockito.any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSave = Mono.just(Category.builder().description("test").build());

        webTestClient.post()
                .uri(CategoryController.BASE_URL)
                .body(catToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdate() {
        Category categoryOriginal = Category.builder().id("1").description("original").build();
        Category categoryUpdated = Category.builder().id("1").description("updated").build();

        BDDMockito.given(categoryRepository.save(categoryOriginal))
                .willReturn(Mono.just(categoryUpdated));

        webTestClient.put()
                .uri(CategoryController.BASE_URL.concat("{id}"), "1")
                .body(Mono.just(categoryOriginal), Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}