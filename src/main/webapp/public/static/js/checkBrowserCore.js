if ("ActiveXObject" in window) {
    var notWebkitBox = document.createElement('div');
    var notWebkitContent = document.createElement('div');
    notWebkitBox.setAttribute('id', 'not-webkit-box');
    notWebkitContent.setAttribute('id', 'not-webkit-content');

    notWebkitBox.setAttribute('style',
        'display:none;' +
        'position:relative;' +
        'width:100%;' +
        'height:100%;' +
        'background-color:rgba(41, 41, 41, 1);' +
        'color:#ccc;'
    );

    notWebkitContent.setAttribute('style',
        'text-align:center;' +
        'position:absolute;' +
        'top:50%;' +
        'left:50%;' +
        'transform:translate(-50%, -50%);'
    );

    notWebkitContent.innerHTML =
        '<h2>请将360/QQ等浏览器的内核模式由兼容模式切换为极速模式，或升级浏览器至最新，建议使用chrome浏览器！</h2>' 

    document.body.style.width = "100%";
    document.body.style.height = "100%";
    document.getElementById('app').style.display = "none";

    notWebkitBox.appendChild(notWebkitContent)
    document.body.appendChild(notWebkitBox)
    document.getElementById("not-webkit-box").style.display = "block"

} else {

}