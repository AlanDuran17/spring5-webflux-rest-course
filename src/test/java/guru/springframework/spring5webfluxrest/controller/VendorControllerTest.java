package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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
}