export default {
    color: ['#3467ba', ],
    title: {
        text: '资产数量排名前三科室',
        x: 'center'
    },
    // tooltip: {
    //     trigger: 'axis',
    //     axisPointer: {
    //         type: 'shadow'
    //     }
    // },
    // legend: {
    //     data: ['2011年', '2012年']
    // },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
        type: 'value'
    },
    yAxis: {
        
        type: 'category',
        data: ['检验科', '药剂科', '手术科',]
    },
    series: [{
        data: [150,200,520 ],
        type: 'bar',
        barWidth:20,
        label: {
            normal: {
                show: true,
                position: 'inside'
            }
        },
    }]
};