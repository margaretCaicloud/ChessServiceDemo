package main

import (
	"encoding/json"
	"errors"
	"fmt"
	"github.com/golang/glog"
	"io/ioutil"
	pb "myq/go-grpc-demo/src/proto"
	"net"
	"net/http"
	"os"

	"io"

	"github.com/spf13/viper"
	"golang.org/x/net/context"
	"google.golang.org/grpc"
	"google.golang.org/grpc/grpclog"
)


// 定义helloService并实现约定的接口
type greeterService struct{}
type HttpRet struct {
	UserId    int8  `json:"userId"`
	Id int8  `json:"id"`
	Title    string `json:"title"`
	Body    string `json:"body"`
}

type Config struct {
	GrpcServerAddr    string `json:"title"`
	Url3th    string `json:"title"`
	UrlBack    string `json:"body"`
}

// HelloService Hello服务
var GreeterServer = greeterService{}


func  ReadConfig(confName string) (*Config, error){
	viper.SetConfigName(confName)     //把json文件换成yaml文件，只需要配置文件名 (不带后缀)即可
	viper.AddConfigPath(".")           //添加配置文件所在的路径
	//viper.SetConfigType("json")       //设置配置文件类型
	cfg := new(Config)
	err := viper.ReadInConfig()
	if err != nil {
		fmt.Printf("config file fail: %s, and read from env!\n", err)
		//os.Exit(1)
		cfg.GrpcServerAddr = os.Getenv("GRPC_SERVER_ADDR")
		cfg.Url3th = os.Getenv("URL3th")
		cfg.UrlBack = os.Getenv("URL_BACK")
		return cfg,nil
	}else {
		cfg.GrpcServerAddr = viper.GetString("GrpcServerAddr")
		cfg.Url3th = viper.GetString("Url3th")
		cfg.UrlBack = viper.GetString("UrlBack")
		return cfg, nil
	}
}

// SayHello 实现Hello服务接口
func (h greeterService) SayHello(ctx context.Context, in *pb.HelloRequest) (*pb.HelloReply, error) {
	resp := new(pb.HelloReply)
	config,_ := ReadConfig("config_yaml")
	url :=""

	if (in.Name == "grpcloop" ){
		url = config.UrlBack
		respHttp, _ := sendRequest(url, nil, nil, "GET")
		resp.Message = fmt.Sprintf("from Loop: %s.", string(respHttp))
	}else {
		url = config.Url3th
		respHttp, _ := sendRequest(url, nil, nil, "GET")
		var httpRet = HttpRet{}
		json.Unmarshal(respHttp, &httpRet)
		resp.Message = fmt.Sprintf("from 3th api: %s.", httpRet.Title)
	}

	fmt.Println(resp.Message)
	return resp, nil
}

// sendRequest 发送request
func sendRequest(url string, body io.Reader, addHeaders map[string]string, method string) (resp []byte, err error) {
	// 1、创建req
	req, err := http.NewRequest(method, url, body)
	if err != nil {
		return
	}
	req.Header.Add("Content-Type", "application/json")

	// 2、设置headers
	if len(addHeaders) > 0 {
		for k, v := range addHeaders {
			req.Header.Add(k, v)
		}
	}

	// 3、发送http请求
	client := &http.Client{}
	response, err := client.Do(req)
	if err != nil {
		return
	}
	defer response.Body.Close()

	if response.StatusCode != 200 {
		err = errors.New("http status err")
		glog.Errorf("sendRequest failed, url=%v, response status code=%d", url, response.StatusCode)
		return
	}

	// 4、结果读取
	resp, err = ioutil.ReadAll(response.Body)
	return
}

func main() {
	Address := "127.0.0.1:50052"

	cfg,err := ReadConfig("config_yaml")
	if err != nil {
		fmt.Printf("config file fail: %s, and read from env!\n", err)
		//os.Exit(1)
		cfg.GrpcServerAddr = os.Getenv("GRPC_SERVER_ADDR")
	}

	Address = cfg.GrpcServerAddr

	fmt.Println("grpcServer adderss:", Address)

	// 实例化grpc Server
	grpcS := grpc.NewServer()
	// 注册HelloService
	pb.RegisterGreeterServer(grpcS, &GreeterServer)

	listen, err := net.Listen("tcp", Address)
	if err != nil {
		grpclog.Fatalf("Failed to listen: %v", err)
	}

	grpclog.Println("Listen on " + Address)
	grpcS.Serve(listen)

}