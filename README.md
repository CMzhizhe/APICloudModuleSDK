# APICloudModuleSDK

最近我想做一个倒计时功能(类似商城那种倒计时)，但是我发现我按下了home键后，倒计时就被暂停了，当你重新返回到应用，倒计时才继续执行。
然后看到有人在论坛里面说，使用timer模块就可以实现，然后我就去测试了下，发现没有用，也有人说使用setInterval，我去测试了下，也是没有用的。
不知道是不是手机的原因导致的？
然后我就自己写了一个 **timer模块（定时器)** ，我发现如果你这个模块在原生应用里面，也就是混合开发，你按下了home键，它还是会去执行js代码， 当你把它
弄成模块然后用自定义Loader去使用，你按下home键，它就不会去执行JS代码，只有你重新返回应用才会去执行。所以我就不知道他们是怎么弄的，哎
后面想到的解决办法是，监听resume，从桌面返回到应用，重新请求服务器
<pre>
<code>
 api.addEventListener({
 name:'resume'
}, function(ret, err){        
    alert('应用回到前台台');
    //TODO 请求服务器
});
</code>
</pre>

### 1、如何打包上传自定义模块

    首先编译出aar文件

![Alt text](https://github.com/caocao123/APICloudModuleSDK/blob/master/%E6%88%AA%E5%9B%BE%E5%9B%BE%E7%89%87/3.png)

    拷贝出来后，自己创建module.json文件，注意module.json中的格式，然后对这2个文件夹压缩，我已经放了一个在模块压缩包了
    
![Alt text](https://github.com/caocao123/APICloudModuleSDK/blob/master/%E6%88%AA%E5%9B%BE%E5%9B%BE%E7%89%87/4.png)

    进入APIClud开发控制台，上传压缩文件
    
![Alt text](https://github.com/caocao123/APICloudModuleSDK/blob/master/%E6%88%AA%E5%9B%BE%E5%9B%BE%E7%89%87/1.png)

    点击编译自定义Loader，要勾选使用升级编译，当初我没有勾选，然后一直说找不到我上传的这个模块，我不知道为什么
    
 ![Alt text](https://github.com/caocao123/APICloudModuleSDK/blob/master/%E6%88%AA%E5%9B%BE%E5%9B%BE%E7%89%87/2.png)  
 
 最后就是扫描下载APK，下载下来的APK，跟官网的APPLoader用法一样
 
 
 我说下我这个模块的用法：startTime 开始执行

<pre>
<code>
     var  timerModule = api.require('timerModule'); //我的模块
     //delay 时隔多久调用开始调用，0表示立即调用   period  多久调用1次，1000 为1秒  isLoop是否循环调用
      var param = {delay:0,period:1000,isLoop:true};
      timerModule.startTime(param, function(ret, err){
           timerModule.startTime(param, function(ret, err){
            if(ret.status==1){//成功
                console.log("JS被调用了");
            }else if(ret.status==0){//失败
                console.log(err.errorMessage);
            }
        });
       });
        
      timerModule.closeTime(function(ret,err){
             if(ret.status==1){//成功
                console.log("JS 关闭了");
            }else if(ret.status==0){//失败
            }
       });
</code>
</pre>    



