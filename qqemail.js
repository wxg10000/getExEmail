//����һ��casperʵ��������������ģ�鲢���䴫��һЩ��������
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
var fs =  require('fs');        // ���� fs ģ�飬���� PhantomJS��ģ��
//casper.userAgent('Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.36 (KHTML, like Gecko) chrome_6494_1.html' target='_blank'>Chrome/30.0.1599.101 Safari/537.36');
casper.userAgent('Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36');

phantom.outputEncoding="GBK";
//��������¼�
//var mouse = require("mouse").create(casper);

//����casperʵ��������ָ������ϣ����ȡ��ҳ��
casper.start();
//����ҳ��ߴ�
casper.viewport(1600, 900);
casper.open("https://exmail.qq.com/login");
//casper.open(URL);
//��¼qq��ҵ����
casper.then(function(){
    casper.evaluate(function(obj) {
        var form = document.getElementsByName("form1")[0];
        form.elements["inputuin"].value = 'hr@datahunter.cn';
        form.elements["pp"].value = '123Qwe';
        form.elements["btlogin"].click();
        return document.title;
    });
});

//��ȡ��¼���ͼƬ
casper.then(function() {
    // Click on 1st result link
    //   this.click('input.green_btn');
    this.wait(3000,function(){
      //  this.capture("qqex.png");
    });
});

 var sid;
 var url;//�ҵ��ļ�������
 var url2;//mainframe����
//��ӡ��ǰurl   ��������
casper.then(function() {
    sid = this.getCurrentUrl().split("?")[1].split("&")[0];
    url = "https://exmail.qq.com/cgi-bin/mail_list?"+sid+"&folderid=personal&page=0&stype=myfolders&loc=folderlist,,,21";
    url2 = "https://exmail.qq.com/cgi-bin/mail_list?folderid=personal&page=0&stype=myfolders&"+sid+"&nocheckframe=true";
});

//�����ҵ��ļ���
casper.then(function(){
    this.open(url).then(function(){
        if (this.exists('ul#folder_129_td')) {
        this.click('#folder_129_td');
    }
      //  require("utils").dump(sid);
    });
});
//��mainframe����
casper.then(function(){
    this.open(url2).then(function(){
        if (this.exists('body#list')) {
          //  this.echo('body#list is exists!');
        }
    });
});
//��ȡ������ҳ��ͼƬ
casper.then(function() {
    this.wait(3000,function(){
      //  this.capture("D:/csv/png/wex.png");
        // console.log(casper.comment());
    });
});

var pages;
//��ȡҳ����
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
//������ÿҳ
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
