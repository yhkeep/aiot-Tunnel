export default{
    color: ['#19be6b', '#5ab1ef','#e46cbb'],
    legend: {
        show:true,
        data: ['小于30%', '大于30%小于80%','大于80%',]
    },
    series: [
        {
            name: '资产折旧率',
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
                { value: 30, name: '小于30%' },
                { value: 50, name: '大于30%小于80%' },
                { value: 20, name: '大于80%' },
            ],

        }
    ]
}