export default {
    color: ['#009688', '#ddbd2b',],
    legend: {
        show: true,
        data: ['今日已盘', '今日未盘',]
    },
    series: [
        {
            name: '资产盘点情况',
            type: 'pie',
            radius: '50%',
            center: ['50%', '60%'],


            label: {
                normal: {
                    formatter: '{c}%',
                    textStyle: {
                        fontSize: 14,
                    },
                    // position: 'inner'
                }
            },

            labelLine: {
                normal: {
                    length: 5,  // 改变标示线的长度
                    lineStyle: {
                        // color: "red"  // 改变标示线的颜色
                    }
                },
            },

            data: [
                { value: 60, name: '今日已盘' },
                { value: 40, name: '今日未盘' },
            ],

        }
    ]
}