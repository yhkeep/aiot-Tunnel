export default{
    color: '#2c8bfd',
    title: {
        text: '1#核磁共振',
        textStyle: {
            color: '#2c8bfd',
            fontWeight: 'normal',
            fontSize: 14
        }
    },
    tooltip: {
        trigger: 'axis'
    },

    grid: {
        // show:true,
        left: '0',
        right: '0',
        top: '40',
        bottom: '10',
        containLabel: true,
    },
    xAxis: {
        show: false,
        type: 'category',
        data: [1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31],
        // axisLine: {
        //     lineStyle: {
        //         color: '#878787',
        //     }
        // },
    },
    yAxis: {
        show: false,
        type: 'value',
        // axisLine: {
        //     lineStyle: {
        //         color: '#878787',
        //     }
        // },
    },
    series: [{
        data: [0, 23, 43, 35, 44, 45, 56, 37, 40, 45, 56, 7, 10, 44, 45, 56,],
        type: 'line',
        smooth: true,
        showSymbol: false,
        areaStyle: {

            opacity: .5,
        },
    }]
};