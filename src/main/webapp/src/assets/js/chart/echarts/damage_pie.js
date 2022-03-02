export default {
    color: ['#19be6b', '#ddbd2b', '#ff9900', '#e46cbb', '#ed4014','#9a66e4'],
    legend: {
        show: true,
        data: ['正常', '待维修','维修接单' ,'待报废','报废','外借']
    },
    series: [
        {
            name: '资产状态',
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
                { value: 90, name: '正常' },
                { value: 3, name: '待维修' },
                { value: 1, name: '维修接单' },
                { value: 2, name: '待报废' },
                { value: 1, name: '报废' },
                { value: 3, name: '外借' },
            ],

        }
    ]
}