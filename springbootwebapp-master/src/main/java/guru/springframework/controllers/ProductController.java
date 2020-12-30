package guru.springframework.controllers;

import guru.springframework.domain.Product;
import guru.springframework.services.ProductService;
import guru.springframework.utils.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Controller
public class ProductController {

    private ProductService productService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient lbClient;

    @Value("${noEruakeHttpServer.adder}")
    public String noEruakeHttpServerAdder;


    private String ErukaServiceProvider = "provider";
    private String rest_url_prefix = "http://"+ErukaServiceProvider+"/";

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("products", productService.listAllProducts());
        System.out.println("Returning rpoducts:");
        return "products";
    }

//    @RequestMapping("product/{id}")
//    public String showProduct(@PathVariable Integer id, Model model){
//        model.addAttribute("product", productService.getProductById(id));
//        return "productshow";
//    }

    @RequestMapping("product/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("product", productService.getProductById(id));
        return "productform";
    }

    @RequestMapping("product/new")
    public String newProduct(Model model){
        model.addAttribute("product", new Product());
        return "productform";
    }

    @RequestMapping(value = "product", method = RequestMethod.POST)
    public String saveProduct(Product product){
        productService.saveProduct(product);
        return "redirect:/product/" + product.getId();
    }


    @RequestMapping("product/{id}")
    public String showProduct(@PathVariable Integer id, Model model) throws IOException {
        Product tmp=productService.getProductById(id);
        ServiceInstance si=lbClient.choose(ErukaServiceProvider);
        if(null==si){
            System.out.println("get service provider name fail! provider name:"+ErukaServiceProvider);
            return null;
        }
        String url="http://"+si.getServiceId()+":"+si.getPort();

        switch (id)
        {
            case 1:
                url=url+"/grpc/grpc3thAPI";
                break;
            case 2:
                url="http://"+noEruakeHttpServerAdder+"/db/noEureka";
                String respStr=HttpClient.getSimple(url);
                if(respStr != null) {
                    tmp.setDescription(respStr);
                    model.addAttribute("product", tmp);
                }
                return "productshow";
            case 3:
                url=url+"/db/Eureka";
                break;
            case 4:
                url=url+"/grpc/grpcloop";
                break;
            default:
                url=url+"/db/mary";
        }

        ResponseEntity<String> rep=restTemplate.exchange(url,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<String>() {}
        );

        System.out.println("get service provider rep:"+rep.getBody());
        tmp.setDescription(rep.getBody());
        model.addAttribute("product", tmp);
        return "productshow";
    }
}
