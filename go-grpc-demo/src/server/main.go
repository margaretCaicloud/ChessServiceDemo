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

	"io"

	"golang.org/x/net/context"
	"google.golang.org/grpc"
	"google.golang.org/grpc/grpclog"
)

const (
	// Address gRPC服务地址
	Address = "127.0.0.1:50052"
)

// 定义helloService并实现约定的接口
type greeterService struct{}
type HttpRet struct {
	UserId    int8  `json:"userId"`
	Id int8  `json:"id"`
	Title    string `json:"title"`
	Body    string `json:"body"`
}


// HelloService Hello服务
var GreeterServer = greeterService{}

// SayHello 实现Hello服务接口
func (h greeterService) SayHello(ctx context.Context, in *pb.HelloRequest) (*pb.HelloReply, error) {
	resp := new(pb.HelloReply)

	//-----------------------------------------
	url := "https://jsonplaceholder.typicode.com/posts/1"

	//提交请求
	respHttp, _ := sendRequest( url, nil,nil,"GET")


	var httpRet = HttpRet{}
	json.Unmarshal(respHttp,&httpRet)

	//formet grpc ret
	resp.Message = fmt.Sprintf("from 3th api: %s.", httpRet.Title)
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