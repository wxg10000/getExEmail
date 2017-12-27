//创建一个casper实例，加入依赖的模块并向其传递一些基本参数
var casper = require("casper").create({
    waitTimeout: 15000,
    stepTimeout: 15000,
    verbose: true,
    clientScripts:  [
        'libs/jquery-1.10.2.js',      // These two scripts will be injected in remote
        'libs/underscore-1.8.3.js'   // DOM on every request
    ],
    pageSettings: {
        loadImages: false,
        loadPlugins: false,
        webSecurityEnabled: false
        //userAgent: 'Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0'
    },
    //logLevel: "info",              // Only "info" level messages will be logged

    onWaitTimeout: function() {
        this.echo('** Wait-TimeOut **');
    },
    onStepTimeout: function() {
        this.echo('** Step-TimeOut **');
    }
});
//var URL = casper.cli.get('url')
var fs =  require('fs');        // 引入 fs 模块，这是 PhantomJS的模块
//casper.userAgent('Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.36 (KHTML, like Gecko) chrome_6494_1.html' target='_blank'>Chrome/30.0.1599.101 Safari/537.36');
casper.userAgent('Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36');

phantom.outputEncoding="GBK";
//创建鼠标事件
//var mouse = require("mouse").create(casper);

//启动casper实例并将其指向我们希望爬取的页面
casper.start();
//设置页面尺寸
casper.viewport(1600, 900);
casper.open("https://exmail.qq.com/login");
//casper.open(URL);
//登录qq企业邮箱
casper.then(function(){
    casper.evaluate(function(obj) {
        var form = document.getElementsByName("form1")[0];
        form.elements["inputuin"].value = 'hr@datahunter.cn';
        form.elements["pp"].value = '123Qwe';
        form.elements["btlogin"].click();
        return document.title;
    });
});

//截取登录后的图片
casper.then(function() {
    // Click on 1st result link
    //   this.click('input.green_btn');
    this.wait(3000,function(){
      //  this.capture("qqex.png");
    });
});

 var sid;
 var url;//我的文件夹链接
 var url2;//mainframe链接
//打印当前url   打开新链接
casper.then(function() {
    sid = this.getCurrentUrl().split("?")[1].split("&")[0];
    url = "https://exmail.qq.com/cgi-bin/mail_list?"+sid+"&folderid=personal&page=0&stype=myfolders&loc=folderlist,,,21";
    url2 = "https://exmail.qq.com/cgi-bin/mail_list?folderid=personal&page=0&stype=myfolders&"+sid+"&nocheckframe=true";
});

//单击我的文件夹
casper.then(function(){
    this.open(url).then(function(){
        if (this.exists('ul#folder_129_td')) {
        this.click('#folder_129_td');
    }
      //  require("utils").dump(sid);
    });
});
//打开mainframe链接
casper.then(function(){
    this.open(url2).then(function(){
        if (this.exists('body#list')) {
          //  this.echo('body#list is exists!');
        }
    });
});
//截取打开新网页的图片
casper.then(function() {
    this.wait(3000,function(){
      //  this.capture("D:/csv/png/wex.png");
        // console.log(casper.comment());
    });
});

var pages;
//获取页码数
casper.then(function() {
    if (this.exists('span#_ut_c')) {
        // this.echo('span#_ut_c is exists!');
        // this.echo(this.getHTML('span#_ut_c'));

        var lines = parseInt(this.getHTML('span#_ut_c'));
        if (lines % 100 == 0) {
            pages = lines / 100;
        } else {
            pages = Math.ceil(lines / 100);
        }
        // console.log(pages);
    }
//遍历打开每页
var i=0;
    casper.repeat(pages,function(){
        var url3 = "https://exmail.qq.com/cgi-bin/mail_list?folderid=personal&page=" + i + "&stype=myfolders&" + sid + "&nocheckframe=true";
       i++;
        casper.then(function () {
            this.open(url3).then(function () {
                if (this.exists('body#list')) {
                    //  this.echo('body#list is exists!');
                }
            });
        });
        casper.then(function () {
            if (this.exists('form#frm')) {
                // this.echo('div.toarea is exists!');
                this.echo(this.getHTML('form#frm'));
            }
        });
        casper.then(function () {
            this.wait(3000, function () {
                this.capture("D:/csv/png/"+i+".png");
                // console.log(casper.comment());
            });
        });
    });
});
casper.run();
