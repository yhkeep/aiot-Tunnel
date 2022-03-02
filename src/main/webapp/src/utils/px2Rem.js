function setFontSizeFn() {

    var bodyElement = document.documentElement || document.body,

        RC = {

            w: 750,

            h: 1206

        }, //默认设计稿宽高

        GC = {

            w: document.documentElement.clientWidth || window.innerWidth || screen.width,

            h: document.documentElement.clientHeight || window.innerHeight || screen.height

        };

    var rightSize = parseFloat((RC.w / RC.h).toFixed(1)),

        currentSize = parseFloat((GC.w / GC.h).toFixed(1)),

        lastHTMLSize = 16, // 默认16是因为html默认字号是16px

        html = document.getElementsByTagName("html")[0];



    if (rightSize > currentSize) {  // 长屏

        lastHTMLSize = 16;

    } else if (rightSize < currentSize) {  //宽屏

        lastHTMLSize = (RC.h / GC.h * GC.w) / RC.w * 16;

    }

    html.style.fontSize = GC.w / lastHTMLSize + 'px';



}

export default setFontSizeFn