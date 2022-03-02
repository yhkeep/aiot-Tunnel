export default {
    color: ['#19be6b', '#ddbd2b', '#3467ba','#e46cbb'],
    legend: {
        show: true,
        data: ['正常', '在修', '待报废','已报废']
    },
    series: [
        {
            name: '资产状态',
            type: 'pie',
            radius: '55%',
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
                { value: 90, name: '正常' },
                { value: 6, name: '在修' },
                { value: 3, name: '待报废' },
                { value: 1, name: '已报废' },
            ],

        }
    ]
}