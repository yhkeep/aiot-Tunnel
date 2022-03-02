let createPromiseFn = function (code, url) {
    return new Promise((resolve, reject) => {

        var oldScript = document.getElementById(code)
        //如果之前已经创建过的script标签，不再创建新标签
        if (oldScript) {
            // document.head.removeChild(oldScript)
            resolve();
        }
        else {
            let script = document.createElement('script');
            script.type = 'text/javascript';
            script.id = code
            script.src = url;
            document.head.appendChild(script);

            script.onload = () => {
                //成功回调
                resolve();
            }
        }



    })
}

export function leafLetScript() {
    return createPromiseFn("leafLetScript", "/public/static/plugins/leaflet/js/leaflet.js")
}
export function proj4ComScript() {
    return createPromiseFn("proj4ComScript", "/public/static/plugins/leaflet/js/proj4-compressed.js")
}
export function proj4LeafLetScript() {
    return createPromiseFn("proj4LeafLetScript", "/public/static/plugins/leaflet/js/proj4leaflet.js")
}

// export function baseHomeScript() {
//     return createPromiseFn("baseHomeScript", "./static/plugins/leaflet/json/baseHome.js")
// }
// export function home11Script() {
//     return createPromiseFn("home11Script", "./static/plugins/leaflet/json/home11.js")
// }
// export function home12Script() {
//     return createPromiseFn("home12Script", "./static/plugins/leaflet/json/home12.js")
// }
// export function home13Script() {
//     return createPromiseFn("home13Script", "./static/plugins/leaflet/json/home13.js")
// }


export function echartScript() {
    return createPromiseFn("echartScript", "/public/static/plugins/echarts/echarts.min.js")
}
export function leafletAntPath() {
    return createPromiseFn("leafletAntPath", "/public/static/plugins/leaflet/js/leaflet-ant-path.js")
}
export function leafletFullScreen() {
    return createPromiseFn("leafletFullScreen", "/public/static/plugins/leaflet/js/Control.FullScreen.js")
}



