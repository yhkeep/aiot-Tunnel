//今日温度
var temperatureOption = {
    color: '#5c7bd9',
    title: {
        text: '今日温度',
        left: 'center'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            
        }
    },
    // visualMap: {
    //     top: 10,
    //     right: 10,
    //     show: true,
    //     dimension: 1,
    //     pieces: [{ min: parseFloat(23.6), max: parseFloat(23.7), color: '#5c7bd9' }],
    //     outOfRange: {
    //         symbol: 'rect',
    //         symbolSize: [10, 10],
    //         color: "red",
    //     }
    // },
    toolbox: {
       
        feature: {
           
            saveAsImage: {}
        }
    },
    grid: {
        show: true,
        // top:'10',
        left: '30',
        right: '40',
        bottom: '10',
        containLabel: true,
    },
    calculable: true,
    xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
        type: 'value',
    },
    series: [
                {
                    data: [120, 200, 150, 80, 70, 110, 130],
                    type: 'line',
                    showSymbol: false,
                   
                    // barWidth:30,
                    // smooth: true
                    markLine: {
                        silent: true,
                        data: [
                            {
                                yAxis: 20,
                                lineStyle: {color: '#FF1D00'},
                                label: {color: '#FF1D00',formatter:'上限'},
                            },
                            {
                                yAxis: 16,
                                lineStyle: { color: '#FF7804' }, 
                                label: {color: '#FF7804', formatter: '下限'},
                            },
                        ],
                        
                        
                    },
                },
              
            ]
};

//今日湿度
var humidityOption = {
    color: '#5c7bd9',
    title: {
        text: '今日湿度',
        left: 'center'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {

        }
    },
    // visualMap: {
    //     top: 10,
    //     right: 10,
    //     show: true,
    //     dimension: 1,
    //     pieces: [{ min: parseFloat(61.4), max: parseFloat(61.7), color: '#5c7bd9' }],
    //     outOfRange: {
    //         symbol: 'rect',
    //         symbolSize: [10, 10],
    //         color: "red",
    //     }
    // },
    toolbox: {

        feature: {

            saveAsImage: {}
        }
    },
    grid: {
        show: true,
        // top: '10',
        left: '30',
        right: '40',
        bottom: '10',
        containLabel: true,
    },
    calculable: true,
    xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
        type: 'value',
        // min:0,
        // max:100,
    },
    series: [{
        data: [120, 200, 150, 80, 70, 110, 130],
        type: 'line',
        showSymbol: false,
        markLine: {
            silent: true,
            data: [
                {
                    yAxis: 20,
                    lineStyle: { color: '#FF1D00' },
                    label: { color: '#FF1D00', formatter: '上限' },
                },
                {
                    yAxis: 16,
                    lineStyle: { color: '#FF7804' },
                    label: { color: '#FF7804', formatter: '下限' },
                },
            ],
        },
        // barWidth: 30,
        // smooth: true
    }]
};

//一周温度
var weekTemOption = {
    color: '#5c7bd9',
    title: {
        text: '一周温度',
        left: 'center'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {

        }
    },
    toolbox: {

        feature: {

            saveAsImage: {}
        }
    },
    grid: {
        show: true,
        // top: '10',
        left: '30',
        right: '40',
        bottom: '10',
        containLabel: true,
    },
    calculable: true,
    xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
        type: 'value'
    },
    series: [{
        data: [120, 200, 150, 80, 70, 110, 130],
        type: 'line',
        // barWidth: 30,
        showSymbol: false,
        // hoverAnimation: false,
        markLine: {
            silent: true,
            data: [
                {
                    yAxis: 20,
                    lineStyle: { color: '#FF1D00' },
                    label: { color: '#FF1D00', formatter: '上限' },
                },
                {
                    yAxis: 16,
                    lineStyle: { color: '#FF7804' },
                    label: { color: '#FF7804', formatter: '下限' },
                },
            ],
        },
        
        // smooth: true
    }]
};

export { temperatureOption, humidityOption, weekTemOption}
