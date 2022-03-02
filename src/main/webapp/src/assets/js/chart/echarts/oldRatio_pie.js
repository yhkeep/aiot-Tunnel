
var oldRatioOption_value={
    color: ['#ffb980', '#5ab1ef'],
    legend: {
        data: ['剩余价值', '折旧价值', ]
    },
    series: [
        {
            name: '资产折旧价值',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],

            label: {
                normal: {
                    formatter: '{b}\n{c}元',
                    textStyle: {
                        fontSize: 14
                    }
                },
            },

             labelLine: {
                normal: {
                    length: 10,  // 改变标示线的长度
                    lineStyle: {
                        // color: "red"  // 改变标示线的颜色
                    }
                },
            },

            data: [
                { value: 0, name: '剩余价值' },
                { value: 0, name: '折旧价值' },
            ],
            
        }
    ]
};
var oldRatioOption_percentage={
    color: ['#b6a2de','#2ec7c9', ],
    legend: {
        data: ['残值率', '折旧率', ]
    },
    series: [
        {
            name: '资产折旧率',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],


            label: {
                normal: {
                            formatter: '{b}\n{c}%',
                            textStyle: {
                                fontSize: 14,
                            },
                            // position: 'inner'
                }
            },

             labelLine: {
                normal: {
                    length: 10,  // 改变标示线的长度
                    lineStyle: {
                        // color: "red"  // 改变标示线的颜色
                    }
                },
            },

            data: [
                { value: 0, name: '残值率' },
                { value: 0, name: '折旧率' },
            ],
            
        }
    ]
};
var oldRatioOption_month={
    color: ['#ff999a',],
    legend: {
        data: ['月折旧值', ]
    },
    series: [
        {
            name: '每月折旧值',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],

            label: {
                normal: {
                    formatter: '{b}\n{c}元',
                    
                    textStyle: {
                        fontSize: 14
                    },
                },
            },

             labelLine: {
                normal: {
                    length: 10,  // 改变标示线的长度
                    lineStyle: {
                        // color: "red"  // 改变标示线的颜色
                    }
                },
            },

            data: [
                // { value: 0, name: '剩余价值' },
                { value: 0, name: '月折旧值' },
            ],
            
        }
    ]
};

export {oldRatioOption_value,oldRatioOption_percentage,oldRatioOption_month}
