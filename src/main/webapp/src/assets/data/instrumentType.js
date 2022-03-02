let instrumentType = [
    {
        text: "GS1（-40℃~120℃）",
        key: "GS1",
        range:{
            tempTop: 120,
            tempBottom: -40,
            humTop: 100,
            humBottom: 0,
        }
    },
    {
        text: "GS1T（-20℃~70℃）",
        key: "GS1T",
        range:{
            tempTop: 70,
            tempBottom: -20,
            humTop: 100,
            humBottom: 0,
        }
    },
    {
        text: "GS1W（-200℃~200℃，不带湿度监测）",
        key: "GS1W",
        range:{
            tempTop: 200,
            tempBottom: -200,
            humTop: 100,
            humBottom: 0,
        }
    },
   
    {
        text: "S1（-40℃~70℃，无屏幕，适用于室内）",
        key: "S1",
        range:{
            tempTop: 70,
            tempBottom: -40,
            humTop: 100,
            humBottom: 0,
        }
    },
    {
        text: "S1W（-40℃~70℃，无屏幕，适用于冰箱内）",
        key: "S1W",
        range:{
            tempTop: 70,
            tempBottom: -40,
            humTop: 100,
            humBottom: 0,
        }
    },
]

export default instrumentType