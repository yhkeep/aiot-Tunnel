export default function(code){
    var str = '';
    if (code === '') {
        return str
    } else {
        var arr = code.split(':')

        arr.forEach((item, index) => {
            str += item.toUpperCase()
        })
        return str;
    }
}