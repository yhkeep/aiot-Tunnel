export default{
    color: ['#19be6b', '#b797e4',],
    legend: {
        show:true,
        data: ['设备动用率', '设备未动用率',]
    },
    series: [
        {
            name: '当前全院设备动用率',
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
                { value: 30, name: '设备动用率' },
                { value: 50, name: '设备未动用率' },
            ],

        }
    ]
}