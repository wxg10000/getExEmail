/**
 * Created by root on 2017/12/27.
 */

var fso = new ActiveXObject("Scripting.FileSystemObject");
var f = fso.OpenTextFile(".\\mytest.txt",1);
var s = "";
while (!f.AtEndOfStream)
{
    s+= f.ReadLine();
}
f.Close();
function getINI(item,key)
{
    new RegExp("\\["+item+"\\](.+)").exec(s);
    var str=RegExp.$1;
    var reg2=/(\w+)=(\d+)/;
    var keyValue={};
    str.replace(reg2,function(a,b,c){
        keyValue[b]=c;
    });
    return keyValue[key];
}
alert(getINI("data","up"));
alert(getINI("plugin_page_search","hightlight"));