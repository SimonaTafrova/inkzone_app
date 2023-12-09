const userId = document.getElementById("userId");
let pictureContainer = document.getElementById("pictureContainer");
const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content;
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content;

function pictureAsHtml(picture){
    let pictureHtml = '<div class="portfolio-item row">\n'
          pictureHtml += '<div class="item selfie col-lg-4 col-md-4 col-6 col-sm">\n'
           pictureHtml += '<a href="imagePage.html" class="fancylight popup-btn" data-fancybox-group="light">\n'
            pictureHtml += `<img class="img-fluid" src='${picture.url}' alt="">\n`
              pictureHtml += `</a>\n`
           pictureHtml += `</div>\n`
    return pictureHtml;
}

fetch(`http://localhost:8080/api/${userId}/pictures`,{
    headers: {
        "Accept": "application/json"
    }
}).then(res => res.json())
    .then(data => {
        for(let picture of data){
            pictureContainer.innerHTML += pictureAsHtml(picture);
        }

})

