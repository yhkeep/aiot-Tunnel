var fstCatData = [
    {
        text: '民用',
        key: '民用',
    },
    {
        text: '医用',
        key: '医用',
    },
]

var secCatData = [
    {
        text: '办公设备类',
        key: '办公设备类',
        parent: '民用',
    },
    {
        text: '电器设备类',
        key: '电器设备类',
        parent: '民用',
    },
    {
        text: '家具类',
        key: '家具类',
        parent: '民用',
    },
    {
        text: '运行保障设备类',
        key: '运行保障设备类',
        parent: '民用',
    },
    {
        text: '病理设备类',
        key: '病理设备类',
        parent: '医用',
    },
    {
        text: '病室护理类',
        key: '病室护理类',
        parent: '医用',
    },
    {
        text: '放射仪器设备类',
        key: '放射仪器设备类',
        parent: '医用',
    },
    {
        text: '检查设备类',
        key: '检查设备类',
        parent: '医用',
    },
    {
        text: '检验分析设备类',
        key: '检验分析设备类',
        parent: '医用',
    },
    {
        text: '手术设备类',
        key: '手术设备类',
        parent: '医用',
    },
    {
        text: '五官仪器设备类',
        key: '五官仪器设备类',
        parent: '医用',
    },
    {
        text: '药械设备类',
        key: '药械设备类',
        parent: '医用',
    },
    {
        text: '治疗仪器类',
        key: '治疗仪器类',
        parent: '医用',
    },
]

var thdCatData = [
    {
        text: 'X射线设备',
        key: 'X射线设备',
        parent: '放射仪器设备类'
    },
    {
        text: '安保设备',
        key: '安保设备',
        parent: '运行保障设备类'
    },
    {
        text: '安全设备',
        key: '安全设备',
        parent: '运行保障设备类'
    },
    {
        text: '办公设备',
        key: '办公设备',
        parent: '办公设备类'
    },
    {
        text: '便携式超声诊断设备',
        key: '便携式超声诊断设备',
        parent: '检查设备类'
    },
    {
        text: '病理设备',
        key: '病理设备',
        parent: '病理设备类'
    },
    {
        text: '病室护理',
        key: '病室护理',
        parent: '病室护理类'
    },
    {
        text: '彩色超声成像设备',
        key: '彩色超声成像设备',
        parent: '检查设备类'
    },
    {
        text: '电器设备',
        key: '电器设备',
        parent: '电器设备类'
    },
    {
        text: '耳科仪器',
        key: '耳科仪器',
        parent: '五官仪器设备类'
    },
    {
        text: '光学内窥镜',
        key: '光学内窥镜',
        parent: '检查设备类'
    },
    {
        text: '柜类',
        key: '柜类',
        parent: '家具类'
    },
    {
        text: '肌电诊断',
        key: '肌电诊断',
        parent: '检查设备类'
    },
    {
        text: '基因生和命科学仪器',
        key: '基因生和命科学仪器',
        parent: '检验分析设备类'
    },
    {
        text: '激光手术和治疗设备',
        key: '激光手术和治疗设备',
        parent: '手术设备类'
    },
    {
        text: '架类',
        key: '架类',
        parent: '家具类'
    },
    {
        text: '监护设备',
        key: '监护设备',
        parent: '检查设备类'
    },
    {
        text: '检验分析设备',
        key: '检验分析设备',
        parent: '检验分析设备类'
    },
    {
        text: '理疗康复仪器',
        key: '理疗康复仪器',
        parent: '治疗仪器类'
    },
    {
        text: '脑电诊断、治疗',
        key: '脑电诊断、治疗',
        parent: '检查设备类'
    },
    {
        text: '其它治疗仪器',
        key: '其它治疗仪器',
        parent: '治疗仪器类'
    },
    {
        text: '器械台、柜',
        key: '器械台、柜',
        parent: '病室护理类'
    },
    {
        text: '腔内手术用内窥镜',
        key: '腔内手术用内窥镜',
        parent: '检查设备类'
    },
    {
        text: '沙发类',
        key: '沙发类',
        parent: '家具类'
    },
    {
        text: '射频治疗设备',
        key: '射频治疗设备',
        parent: '手术设备类'
    },
    {
        text: '手术及急救装置',
        key: '手术及急救装置',
        parent: '手术设备类'
    },
    {
        text: '手术设备',
        key: '手术设备',
        parent: '手术设备类'
    },
    {
        text: '手术显微镜及放大器',
        key: '手术显微镜及放大器',
        parent: '手术设备类'
    },
    {
        text: '输液辅助设备',
        key: '输液辅助设备',
        parent: '检查设备类'
    },
    {
        text: '台类',
        key: '台类',
        parent: '家具类'
    },
    {
        text: '通讯设备',
        key: '通讯设备',
        parent: '电气设备类'
    },
    {
        text: '消毒灭菌设备',
        key: '消毒灭菌设备',
        parent: '药械设备类'
    },
    {
        text: '信息设备',
        key: '信息设备',
        parent: '办公设备类'
    },
    {
        text: '眼科光学仪器',
        key: '眼科光学仪器',
        parent: '五官仪器设备类'
    },
    {
        text: '医用x射线附属设备',
        key: '医用x射线附属设备',
        parent: '放射仪器设备类'
    },
    {
        text: '医用凳、椅、台、床',
        key: '医用凳、椅、台、床',
        parent: '手术设备类'
    },
    {
        text: '医用防护类',
        key: '医用防护类',
        parent: '放射仪器设备类'
    },
    {
        text: '医用高能射线治疗设备',
        key: '医用高能射线治疗设备',
        parent: '放射仪器设备类'
    },
    {
        text: '医用高频设备',
        key: '医用高频设备',
        parent: '手术设备类'
    },
    {
        text: '医用剌激器',
        key: '医用剌激器',
        parent: '检查设备类'
    },
    {
        text: '医用冷温、冷藏设备',
        key: '医用冷温、冷藏设备',
        parent: '检验分析设备类'
    },
    {
        text: '医用培养箱',
        key: '医用培养箱',
        parent: '检验分析设备类'
    },
    {
        text: '医用推车',
        key: '医用推车',
        parent: '病室护理类'
    },
    {
        text: '医用推车',
        key: '医用推车',
        parent: '检验分析设备类'
    },
    {
        text: '音视频设备',
        key: '音视频设备',
        parent: '电器设备类'
    },
    {
        text: '有创式电生理仪器',
        key: '有创式电生理仪器',
        parent: '手术设备类'
    },
    {
        text: '运行保障设备',
        key: '运行保障设备',
        parent: '运行保障设备类'
    },
    {
        text: '专用手术台床',
        key: '专用手术台床',
        parent: '手术设备类'
    },
    {
        text: '桌类',
        key: '桌类',
        parent: '家具类'
    },
]

var fouCatData = [
    {
        text: 'B超',
        key: 'B超',
        parent: '便携式超声诊断设备'
    },
    {
        text: 'YAG激光机',
        key: 'YAG激光机',
        parent: '眼科光学仪器'
    },
    {
        text: '办公桌',
        key: '办公桌',
        parent: '桌类'
    },
    {
        text: '鼻内窥镜',
        key: '鼻内窥镜',
        parent: '耳科仪器'
    },
    {
        text: '笔记本电脑',
        key: '笔记本电脑',
        parent: '信息设备'
    },
    {
        text: '标本车',
        key: '标本车',
        parent: '医用凳、椅、台、床'
    },
    {
        text: '标本柜',
        key: '标本柜',
        parent: '病理设备'
    },
    {
        text: '冰箱',
        key: '冰箱',
        parent: '电器设备'
    },
    {
        text: '玻切机',
        key: '玻切机',
        parent: '眼科光学仪器'
    },
    {
        text: '不锈钢存物柜',
        key: '不锈钢存物柜',
        parent: '器械台、柜'
    },
    {
        text: '彩色超声诊断仪',
        key: '彩色超声诊断仪',
        parent: '彩色超声成像设备'
    },
    {
        text: '操作台',
        key: '操作台',
        parent: '器械台、柜'
    },
    {
        text: '茶几',
        key: '茶几',
        parent: '桌类'
    },
    {
        text: '超声刀',
        key: '超声刀',
        parent: '医用高频设备'
    },
    {
        text: '超声气压弹道碎石机',
        key: '超声气压弹道碎石机',
        parent: '其它治疗仪器'
    },
    {
        text: '超声乳化仪',
        key: '超声乳化仪',
        parent: '眼科光学仪器'
    },
    {
        text: '除湿机',
        key: '除湿机',
        parent: '电器设备'
    },
    {
        text: '储物柜',
        key: '储物柜',
        parent: '柜类'
    },
    {
        text: '穿剌肾镜',
        key: '穿剌肾镜',
        parent: '腔内手术用内窥镜'
    },
    {
        text: '打印机',
        key: '打印机',
        parent: '信息设备'
    },
    {
        text: '担架',
        key: '担架',
        parent: '医用推车'
    },
    {
        text: '导航系统',
        key: '导航系统',
        parent: '手术及急救装置'
    },
    {
        text: '等离子体手术系统',
        key: '等离子体手术系统',
        parent: '耳科仪器'
    },
    {
        text: '等离子治疗仪',
        key: '等离子治疗仪',
        parent: '其它治疗仪器'
    },
    {
        text: '低温冰箱',
        key: '低温冰箱',
        parent: '医用冷温、冷藏设备'
    },
    {
        text: '电动手术刀',
        key: '电动手术刀',
        parent: '手术及急救装置'
    },
    {
        text: '电动手术台',
        key: '电动手术台',
        parent: '专用手术台床'
    },
    {
        text: '电动手术椅',
        key: '电动手术椅',
        parent: '医用凳、椅、台、床'
    },
    {
        text: '电动吸引器',
        key: '电动吸引器',
        parent: '手术设备'
    },
    {
        text: '电动胸骨锯',
        key: '电动胸骨锯',
        parent: '手术及急救装置'
    },
    {
        text: '电动止血仪',
        key: '电动止血仪',
        parent: '手术及急救装置'
    },
    {
        text: '电视机',
        key: '电视机',
        parent: '音视频设备'
    },
    {
        text: '电梯',
        key: '电梯',
        parent: '安全设备'
    },
    {
        text: '电外科工作站',
        key: '电外科工作站',
        parent: '手术及急救装置'
    },
    {
        text: '电子腹腔镜',
        key: '电子腹腔镜',
        parent: '光学内窥镜'
    },
    {
        text: '电子结肠镜',
        key: '电子结肠镜',
        parent: '光学内窥镜'
    },
    {
        text: '电子内窥镜系统',
        key: '电子内窥镜系统',
        parent: '光学内窥镜'
    },
    {
        text: '电子胃镜',
        key: '电子胃镜',
        parent: '光学内窥镜'
    },
    {
        text: '电钻',
        key: '电钻',
        parent: '手术及急救装置'
    },
    {
        text: '动力系统',
        key: '动力系统',
        parent: '手术及急救装置'
    },
    {
        text: '读片灯',
        key: '读片灯',
        parent: '医用x射线附属设备'
    },
    {
        text: '对讲机',
        key: '对讲机',
        parent: '通讯设备'
    },
    {
        text: '耳科治疗台',
        key: '耳科治疗台',
        parent: '耳科仪器'
    },

    {
        text: '二氧化氮激光治疗机',
        key: '二氧化氮激光治疗机',
        parent: '激光手术和治疗设备'
    },
    {
        text: '腹腔镜',
        key: '腹腔镜',
        parent: '腔内手术用内窥镜'
    },
    {
        text: '高频电刀',
        key: '高频电刀',
        parent: '医用高频设备'
    },
    {
        text: '搁物架',
        key: '搁物架',
        parent: '器械台、柜'
    },
    {
        text: '工作柜',
        key: '工作柜',
        parent: '器械台、柜'
    },
    {
        text: '工作台',
        key: '工作台',
        parent: '台类'
    },
    {
        text: '骨科动力系统',
        key: '骨科动力系统',
        parent: '其它治疗仪器'
    },
    {
        text: '光导纤维',
        key: '光导纤维',
        parent: '光学内窥镜'
    },
    {
        text: '恒温加热台座',
        key: '恒温加热台座',
        parent: '检验分析设备'
    },
    {
        text: '环氧乙烷灭菌器、低温灭菌器',
        key: '环氧乙烷灭菌器、低温灭菌器',
        parent: '消毒灭菌设备'
    },
    {
        text: '钬激光治疗机',
        key: '钬激光治疗机',
        parent: '激光手术和治疗设备'
    },
    {
        text: '货柜',
        key: '货柜',
        parent: '柜类'
    },
    {
        text: '货架',
        key: '货架',
        parent: '架类'
    },
    {
        text: '肌电图机',
        key: '肌电图机',
        parent: '肌电诊断'
    },
    {
        text: '激光扫描枪',
        key: '激光扫描枪',
        parent: '信息设备'
    },
    {
        text: '计算机',
        key: '计算机',
        parent: '信息设备'
    },
    {
        text: '间接检眼镜',
        key: '间接检眼镜',
        parent: '眼科光学仪器'
    },
    {
        text: '监护仪',
        key: '监护仪',
        parent: '监护设备'
    },
    {
        text: '经颅多普勒',
        key: '经颅多普勒',
        parent: '彩色超声成像设备'
    },
    {
        text: '考勤机',
        key: '考勤机',
        parent: '办公设备'
    },
    {
        text: '空气灭菌机',
        key: '空气灭菌机',
        parent: '病室护理'
    },
    {
        text: '空调',
        key: '空调',
        parent: '电器设备'
    },
    {
        text: '空压机',
        key: '空压机',
        parent: '运行保障设备'
    },
    {
        text: '冷藏箱',
        key: '冷藏箱',
        parent: '医用冷温、冷藏设备'
    },
    {
        text: '冷冻治疗机',
        key: '冷冻治疗机',
        parent: '其它治疗仪器'
    },
    {
        text: '冷光源',
        key: '冷光源',
        parent: '手术及急救装置'
    },
    {
        text: '冷柜',
        key: '冷柜',
        parent: '电器设备'
    },
    {
        text: '冷循环射频系统',
        key: '冷循环射频系统',
        parent: '射频治疗设备'
    },
    {
        text: '泌尿内窥镜',
        key: '泌尿内窥镜',
        parent: '腔内手术用内窥镜'
    },
    {
        text: '脑电图机',
        key: '脑电图机',
        parent: '脑电诊断、治疗'
    },
    {
        text: '脑内窥镜',
        key: '脑内窥镜',
        parent: '腔内手术用内窥镜'
    },
    {
        text: '膀胱镜',
        key: '膀胱镜',
        parent: '腔内手术用内窥镜'
    },
    {
        text: '气腹管',
        key: '气腹管',
        parent: '光学内窥镜'
    },
    {
        text: '气管镜',
        key: '气管镜',
        parent: '腔内手术用内窥镜'
    },
    {
        text: '器械柜',
        key: '器械柜',
        parent: '器械台、柜'
    },
    {
        text: '器械台',
        key: '器械台',
        parent: '器械台、柜'
    },
    {
        text: '铅屏风',
        key: '铅屏风',
        parent: '医用防护类'
    },
    {
        text: '铅围裙',
        key: '铅围裙',
        parent: '医用防护类'
    },
    {
        text: '铅衣',
        key: '铅衣',
        parent: '医用防护类'
    },
    {
        text: '铅衣架',
        key: '铅衣架',
        parent: '医用防护类'
    },
    {
        text: '前列腺电切镜',
        key: '前列腺电切镜',
        parent: '医用高频设备'
    },
    {
        text: '前哨淋巴结跟踪仪',
        key: '前哨淋巴结跟踪仪',
        parent: '检验分析设备'
    },
    {
        text: '切割机',
        key: '切割机',
        parent: '手术设备'
    },
    {
        text: '清创车',
        key: '清创车',
        parent: '医用推车'
    },
    {
        text: '清洗喷枪',
        key: '清洗喷枪',
        parent: '病室护理'
    },
    {
        text: '热水器',
        key: '热水器',
        parent: '电器设备'
    },
    {
        text: '人体模型',
        key: '人体模型',
        parent: '病室护理'
    },
    {
        text: '乳腺活检系统',
        key: '乳腺活检系统',
        parent: '检验分析设备'
    },
    {
        text: '沙发',
        key: '沙发',
        parent: '沙发类'
    },
    {
        text: '射频消融仪',
        key: '射频消融仪',
        parent: '射频治疗设备'
    },
    {
        text: '摄像机',
        key: '摄像机',
        parent: '音视频设备'
    },
    {
        text: '摄像机臂',
        key: '摄像机臂',
        parent: '手术及急救装置'
    },
    {
        text: '医用剌激器',
        key: '医用剌激器',
        parent: '神经剌激器'
    },
    {
        text: '生理记录仪',
        key: '生理记录仪',
        parent: '脑电诊断、治疗'
    },
    {
        text: '生物培养器',
        key: '生物培养器',
        parent: '医用培养箱'
    },
    {
        text: '食道镜',
        key: '食道镜',
        parent: '腔内手术用内窥镜'
    },
    {
        text: '视网膜凝结机',
        key: '视网膜凝结机',
        parent: '眼科光学仪器'
    },
    {
        text: '手术床配件',
        key: '手术床配件',
        parent: '专用手术台床'
    },
    {
        text: '手术放大镜',
        key: '手术放大镜',
        parent: '手术显微镜及放大器'
    },
    {
        text: '手术体位固定板',
        key: '手术体位固定板',
        parent: '医用凳、椅、台、床'
    },
    {
        text: '手术头灯',
        key: '手术头灯',
        parent: '手术及急救装置'
    },
    {
        text: '手术头架',
        key: '手术头架',
        parent: '手术及急救装置'
    },
    {
        text: '手术显微镜',
        key: '手术显微镜',
        parent: '手术显微镜及放大器'
    },
    {
        text: '数据管理系统',
        key: '数据管理系统',
        parent: '其它治疗仪器'
    },
    {
        text: '数字视频脑电分析系统',
        key: '数字视频脑电分析系统',
        parent: '脑电诊断、治疗'
    },
    {
        text: '双频射频机',
        key: '双频射频机',
        parent: '射频治疗设备'
    },
    {
        text: '送物车',
        key: '送物车',
        parent: '医用推车'
    },
    {
        text: '碎石机',
        key: '碎石机',
        parent: '有创式电生理仪器'
    },
    {
        text: '台式消毒器（手术室）',
        key: '台式消毒器（手术室）',
        parent: '消毒灭菌设备'
    },
    {
        text: '头部固定系统',
        key: '头部固定系统',
        parent: '手术设备'
    },
    {
        text: '投影仪',
        key: '投影仪',
        parent: '信息设备'
    },
    {
        text: '图像记录仪',
        key: '图像记录仪',
        parent: '手术设备'
    },
    {
        text: '推车',
        key: '推车',
        parent: '病室护理'
    },
    {
        text: '托架',
        key: '托架',
        parent: '医用凳、椅、台、床'
    },
    {
        text: '微波治疗仪',
        key: '微波治疗仪',
        parent: '理疗康复仪器'
    },
    {
        text: '文件柜',
        key: '文件柜',
        parent: '柜类'
    },
    {
        text: '污物车',
        key: '污物车',
        parent: '医用推车'
    },
    {
        text: '无影灯',
        key: '无影灯',
        parent: '手术及急救装置'
    },
    {
        text: '吸脂系统',
        key: '吸脂系统',
        parent: '手术设备'
    },
    {
        text: '洗手槽',
        key: '洗手槽',
        parent: '器械台、柜'
    },
    {
        text: '纤维导光关节镜',
        key: '纤维导光关节镜',
        parent: '光学内窥镜'
    },
    {
        text: '显微镜',
        key: '显微镜',
        parent: '检验分析设备'
    },
    {
        text: '消毒柜',
        key: '消毒柜',
        parent: '器械台、柜'
    },
    {
        text: '鞋柜',
        key: '鞋柜',
        parent: '柜类'
    },
    {
        text: '胸腔镜',
        key: '胸腔镜',
        parent: '腔内手术用内窥镜'
    },
    {
        text: '血管闭合系统',
        key: '血管闭合系统',
        parent: '手术设备'
    },
    {
        text: '压力蒸汽灭菌器（包括供应、中药制剂）',
        key: '压力蒸汽灭菌器（包括供应、中药制剂）',
        parent: '消毒灭菌设备'
    },
    {
        text: '眼科A/B超',
        key: '眼科A/B超',
        parent: '眼科光学仪器'
    },
    {
        text: '药品柜',
        key: '药品柜',
        parent: '器械台、柜'
    },
    {
        text: '液体存放柜',
        key: '液体存放柜',
        parent: '器械台、柜'
    },
    {
        text: '液压手术台',
        key: '液压手术台',
        parent: '专用手术台床'
    },
    {
        text: '衣帽柜',
        key: '衣帽柜',
        parent: '柜类'
    },
    {
        text: '医疗垃圾车',
        key: '医疗垃圾车',
        parent: '医用推车'
    },
    {
        text: '医用车',
        key: '医用车',
        parent: '医用推车'
    },
    {
        text: '医用高能射线设备(直线加速器)',
        key: '医用高能射线设备(直线加速器)',
        parent: '医用高能射线治疗设备'
    },
    {
        text: '医用柜',
        key: '医用柜',
        parent: '医用推车'
    },
    {
        text: '仪器台车',
        key: '仪器台车',
        parent: '医用推车'
    },
    {
        text: '移动X光机',
        key: '移动X光机',
        parent: 'X射线设备'
    },
    {
        text: '移动电话',
        key: '移动电话',
        parent: '通讯设备'
    },
    {
        text: '营养泵',
        key: '营养泵',
        parent: '输液辅助设备'
    },
    {
        text: '阅读器',
        key: '阅读器',
        parent: '基因生和命科学仪器'
    },
    {
        text: '照相机',
        key: '照相机',
        parent: '音视频设备'
    },
    {
        text: '支撑喉镜',
        key: '支撑喉镜',
        parent: '耳科仪器'
    },
    {
        text: '植皮机',
        key: '植皮机',
        parent: '医用高频设备'
    },
    {
        text: '纸张柜',
        key: '纸张柜',
        parent: '柜类'
    },
    {
        text: '治疗车',
        key: '治疗车',
        parent: '医用推车'
    },
    {
        text: '治疗工作台',
        key: '治疗工作台',
        parent: '器械台、柜'
    },
    {
        text: '周界防护系统',
        key: '周界防护系统',
        parent: '安保设备'
    },
    {
        text: '椎间盘镜',
        key: '椎间盘镜',
        parent: '手术及急救装置'
    },
]

export { fstCatData, secCatData, thdCatData, fouCatData }
