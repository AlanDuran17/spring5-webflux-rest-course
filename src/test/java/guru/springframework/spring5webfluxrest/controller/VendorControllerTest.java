package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;

public class VendorControllerTest {
    
    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;
    
    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();

    }

    @Test
    public void allCategories() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(
                        Vendor.builder().name("vend1").lastName("sur1").build(),
                        Vendor.builder().name("vend2").lastName("sur2").build()));

        webTestClient.get()
                .uri(VendorController.BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorById() {
        BDDMockito.given(vendorRepository.findById(Mockito.anyString()))
                .willReturn(Mono.just(
                        Vendor.builder().name("vend1").lastName("sur1").build()));

        webTestClient.get()
                .uri(VendorController.BASE_URL.concat("{id}"), "mock")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class);
    }

    @Test
    public void testCreate() {
        BDDMockito.given(vendorRepository.saveAll(Mockito.any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> catToSave = Mono.just(Vendor.builder().name("Pam").lastName("Beesly").build());

        webTestClient.post()
                .uri(VendorController.BASE_URL)
                .body(catToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdate() {
        Vendor vendorOriginal = Vendor.builder().id("1").name("Pam").lastName("Beesly").build();
        Vendor vendorUpdated = Vendor.builder().id("1").name("Pam").lastName("Halpert").build();

        BDDMockito.given(vendorRepository.save(vendorOriginal))
                .willReturn(Mono.just(vendorUpdated));

        webTestClient.put()
                .uri(VendorController.BASE_URL.concat("{id}"), "1")
                .body(Mono.just(vendorOriginal), Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void testPatch() {
        Vendor vendorOriginal = Vendor.builder().id("1").name("Pam").lastName("Beesly").build();
        Vendor vendorUpdated = Vendor.builder().id("1").name("Pam").lastName("Halpert").build();

        BDDMockito.given(vendorRepository.findById("1"))
                .willReturn(Mono.just(vendorOriginal));
        BDDMockito.given(vendorRepository.save(vendorOriginal))
                .willReturn(Mono.just(vendorUpdated));

        webTestClient.patch()
                .uri(VendorController.BASE_URL.concat("{id}"), "1")
                .body(Mono.just(vendorOriginal), Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}