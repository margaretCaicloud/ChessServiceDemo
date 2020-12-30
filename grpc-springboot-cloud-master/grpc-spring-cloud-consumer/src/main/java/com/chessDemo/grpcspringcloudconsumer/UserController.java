package com.chessDemo.grpcspringcloudconsumer;


import com.chessDemo.grpcspringcloudconsumer.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/db")     // 通过这里配置使下面的映射都在/users下
public class UserController {
    @Autowired
    private UserMapper userMapper;

    // 创建线程安全的Map，模拟users信息的存储
    static Map<Integer, User> users = Collections.synchronizedMap(new HashMap<Integer, User>());



    @GetMapping("/loop")
    public String test() {
//        return "back to grpc server";
        User u = userMapper.findByName("loop");
        return u.getDescribes();
    }
    /**
     * 处理"/users/"的GET请求，用来获取用户列表
     *
     * @return
     */
    @GetMapping("/")
    public List<User> getUserList() {
        // 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递
        List<User> r = new ArrayList<User>(users.values());
        return r;
    }

    @GetMapping("/mary")
    public String getUser() {
        User u = userMapper.findByName("mary");
        return u.toString();
    }

    @GetMapping("/Eureka")
    public String getUserEureka() {
        User u = userMapper.findByName("Eureka");
        return u.getDescribes();
    }

    /**
     * 处理"/users/"的POST请求，用来创建User
     *
     * @param user
     * @return
     */
    @PostMapping("/")
    public String postUser(@RequestBody User user) {
        // @RequestBody注解用来绑定通过http请求中application/json类型上传的数据
        users.put(user.getId(), user);
        return "success";
    }

    /**
     * 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        // url中的id可通过@PathVariable绑定到函数的参数中
        return users.get(id);
    }

    /**
     * 处理"/users/{id}"的PUT请求，用来更新User信息
     *
     * @param id
     * @param user
     * @return
     */
//    @PutMapping("/{id}")
//    public String putUser(@PathVariable Long id, @RequestBody User user) {
//        User u = users.get(id);
//        u.setName(user.getName());
//        u.setAge(user.getAge());
//        users.put(id, u);
//        return "success";
//    }

    /**
     * 处理"/users/{id}"的DELETE请求，用来删除User
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        users.remove(id);
        return "success";
    }

}