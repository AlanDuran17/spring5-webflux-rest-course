package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Loading Bootstrap Data");
        setUpVendors();
        setUpCategory();
    }

    private void setUpVendors() {
        vendorRepository.save(Vendor.builder().name("Michael").lastName("Scott").build()).block();
        vendorRepository.save(Vendor.builder().name("Jim").lastName("Halpert").build()).block();
        vendorRepository.save(Vendor.builder().name("Dwight").lastName("Schrute").build()).block();

        log.info("Vendors Count: {}", vendorRepository.count().block());
    }

    private void setUpCategory() {
        categoryRepository.save(Category.builder().description("Fruits").build()).block();
        categoryRepository.save(Category.builder().description("Vegetables").build()).block();
        categoryRepository.save(Category.builder().description("Meats").build()).block();

        log.info("Category Count: {}", categoryRepository.count().block());
    }
}
