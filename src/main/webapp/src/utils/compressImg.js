const compressImgFn = function(base64String, w, quality){
    let getMimeType = function (urlData) {
        let arr = urlData.split(',');
        let mime = arr[0].match(/:(.*?);/)[1];
        return mime;
    };
    let newImage = new Image();
    let imgWidth, imgHeight;

    let promise = new Promise(resolve => newImage.onload = resolve);
    newImage.src = base64String;
    return promise.then(() => {
        imgWidth = newImage.width;
        imgHeight = newImage.height;
        let canvas = document.createElement("canvas");
        let ctx = canvas.getContext("2d");
       
        // ctx.clearRect(0, 0, imgWidth, imgHeight);
        // ctx.drawImage(newImage, 0, 0, imgWidth, imgHeight);
        if (Math.max(imgWidth, imgHeight) > w) {
            if (imgWidth < imgHeight) {
                canvas.width = w;
                canvas.height = w * imgHeight / imgWidth;
            } else {
                canvas.height = w;
                canvas.width = w * imgWidth / imgHeight;
            }
        } else {
            canvas.width = imgWidth;
            canvas.height = imgHeight;
        }
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.drawImage(newImage, 0, 0, canvas.width, canvas.height);
        let base64 = canvas.toDataURL(getMimeType(base64String), quality);
        return base64;
    });
}

export default compressImgFn