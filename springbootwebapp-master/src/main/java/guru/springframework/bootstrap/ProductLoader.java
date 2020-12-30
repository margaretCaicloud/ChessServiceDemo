package guru.springframework.bootstrap;

import guru.springframework.domain.Product;
import guru.springframework.repositories.ProductRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductLoader implements ApplicationListener<ContextRefreshedEvent> {

    private ProductRepository productRepository;

    private Logger log = LogManager.getLogger(ProductLoader.class);

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(event.getApplicationContext().getDisplayName().contains("SpringClientFactory")){
            return;
        }
        Product shirt = new Product();
        shirt.setDescription("访问第三方API服务（http+grpc）");
        shirt.setPrice(new BigDecimal("18.95"));
        shirt.setImageUrl("user->webServer->serviceA(no Erueka)->serviceB-> 3th RestfulAPI");
        shirt.setProductId("1");
        productRepository.save(shirt);

        log.info("Saved Shirt - id: " + shirt.getId());

        Product mug = new Product();
        mug.setDescription("访问mysql服务(http+jdbc)");
        mug.setImageUrl("user->webServer->serviceA(no Erueka)->mysql");
        mug.setProductId("2");
        mug.setPrice(new BigDecimal("11.95"));
        productRepository.save(mug);


        log.info("Saved Mug - id:" + mug.getId());

        Product mug2 = new Product();
        mug2.setDescription("访问mysql服务(http with Eureka+jdbc)");
        mug2.setImageUrl("user->webServer->serviceC(with Erueka)->mysql");
        mug2.setProductId("3");
        mug2.setPrice(new BigDecimal("8"));
        productRepository.save(mug2);

        log.info("Saved Mug - id:" + mug2.getId());

        Product mug3 = new Product();
        mug3.setDescription("访问内部grpc服务循环调用（http+grpc）");
        mug3.setImageUrl("user->webServer->serviceA(no Erueka)->serverB->serviceA(no Erueka)");
        mug3.setProductId("4");
        mug3.setPrice(new BigDecimal("13"));
        productRepository.save(mug3);
    }
}
