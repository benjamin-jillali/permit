function loadData(){
  const mainDiv = document.createElement('div');
  $(mainDiv).addClass("test");
  //mainDiv.addClass("profil");
    // console.log("js is the worst.")
    const data = document.createElement('h2');
    data.classList.add('data');
    data.innerHTML = "This is a test for js file using methods in another file. ";
    mainDiv.appendChild(data);
    document.querySelector('body').appendChild(mainDiv);

}

function getImage(link){
  $.ajax({
    url : link,
    method : 'GET',
    data: {"id" : 10},
    xhrFields : {
      responseType : 'blob'
    },
    success: function (data){

      const url = window.URL || window.webkitURL;
      const src = url.createObjectURL(data);
      console.log(url);
      $('#pic').attr('src', src);
    }
  })
}
