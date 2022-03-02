var allUtilizationOption={
    color: ['#19be6b', '#9a66e4',],
    title: {
        // text: '设备最近一月动用率'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['可移动设备动用率', '不可移动设备动用率',]
    },
    grid: {
        show: true,
        left: '20',
        right: '20',
        bottom: '20',
        containLabel: true,
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['08.19', '08.20', '08.21', '08.22', '08.23', '08.24', '08.25', '08.26', '08.27', '08.28', '08.29', '08.30', '08.31', '09.01', '09.02', '09.03', '09.04', '09.05', '09.06', '09.07', '09.08', '09.09', '09.10', '09.11', '09.12', '09.13', '09.14', '09.15', '09.16', '09.17'],
        axisLine: {
            lineStyle: {
                color: '#878787',
            }
        },


    },
    yAxis: {
        type: 'value',
        max: 150,
        axisLabel: {
            // formatter: '{value} %'
        },
        axisLine: {
            lineStyle: {
                color: '#878787',
            }
        },
        splitLine: {
            show: true,
            lineStyle: {
                color: ['#ddd'],
                width: 1,
                type: 'solid'
            }
        }
    },
    series: [
        {
            name: '可移动设备动用率',
            type: 'line',
            // stack: '总量',
            data: [0, 97, 7, 58, 82, 49, 74, 8, 92, 80, 33, 77, 41, 23, 89, 62, 92, 8, 62, 23, 45, 68, 17, 44, 63, 86, 68, 43, 37, 0],
            smooth: true,
            // showSymbol: false,
            areaStyle: {

                opacity: .5,
            },
            itemStyle: {
                normal: {
                    lineStyle: {
                        // color: 'transparent'
                    },
                }
            },
        },
        {
            name: '不可移动设备动用率',
            type: 'line',
            // stack: '总量',
            data: [0, 91, 30, 54, 76, 89, 88, 27, 61, 24, 7, 62, 17, 88, 92, 24, 37, 71, 93, 83, 19, 23, 11, 23, 56, 39, 49, 97, 21, 0],
            smooth: true,
            // showSymbol: false,
            areaStyle: {

                opacity: .5,
            },
            itemStyle: {
                normal: {
                    lineStyle: {
                        // color: 'transparent'
                    },

                }
            },
        },


    ]
};


var utiliDetailOption={
    color: '#19be6b',
    tooltip: {
        trigger: 'axis'
    },
    grid: {
        show: true,
        left: '20',
        right: '20',
        top:'10',
        bottom: '20',
        containLabel: true,
    },
   
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ["01:00", "03:00", "05:00", "07:00", "09:00", "11:00", "13:00", "15:00", "17:00", "19:00", "21:00", "23:00"],
        axisLine: {
            lineStyle: {
                color: '#878787',
            }
        },


    },
    yAxis: {
        type: 'value',
        // max: 150,
        axisLabel: {
            // formatter: '{value} %'
        },
        axisLine: {
            lineStyle: {
                color: '#878787',
            }
        },
        splitLine: {
            show: true,
            lineStyle: {
                color: ['#ddd'],
                width: 1,
                type: 'solid'
            }
        }
    },
    series: [
        {
            name: '电流',
            type: 'line',
            // stack: '总量',
            data: [800, 552, 1000, 562, 921, 82, 524, 321, 123, 521, 1211, 424,],
            smooth: true,
           
            itemStyle: {
                normal: {
                    lineStyle: {
                        // color: 'transparent'
                        width:'3',
                    },
                }
            },
        },
        


    ]
};

export {allUtilizationOption,utiliDetailOption}

