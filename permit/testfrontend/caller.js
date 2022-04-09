//var test = require('./service.js');

$(document).ready(function(){
  $('#btn').click(function(event){
    var url = "https://images.unsplash.com/photo-1465101108990-e5eac17cf76d?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjE0NTg5fQ%3D%3D&s=471ae675a6140db97fea32b55781479e";
    var bums = 10;
    var url2 = "?id=" + bums;

    getImage(url);
    loadData();
  });
  //  test.loadData();
});
