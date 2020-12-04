package guru.springframework.controllers;



import guru.springframework.domain.Product;
import guru.springframework.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class DemoProductController {

    private ProductService productService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient lbClient;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    private String ErukaServiceProvider = "grpc-spring-cloud-consumer";
    private String rest_url_prefix = "http://"+ErukaServiceProvider+"/";


    @RequestMapping(value="/products1", method = RequestMethod.GET)
    private String CallServiceProvider(Model model){
//		StringBuffer sb=null;
//		// 调用USER服务中的/users/{id}服务

        model.addAttribute("products",productService.listAllProducts());
        System.out.println("Returning poducts:");
        return "products";
    }


    @RequestMapping("product/{id}")
    public String showProduct(@PathVariable Integer id, Model model){
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
                url=url+"/grpc/grpcDate";
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
