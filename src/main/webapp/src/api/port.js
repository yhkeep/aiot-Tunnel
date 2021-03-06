// let SOCKET_HOST ="ws://ibeacon.yikecha.net:8009";
// let SOCKET_HOST = "ws://192.168.0.117:8009";
// let SOCKET_HOST ="ws://192.168.0.166:8009";
let SOCKET_HOST ="ws://120.79.1.166:8009";
// let SOCKET_HOST ="ws://192.168.218.179:80";
var NET_PORT={
    mapSocket: SOCKET_HOST+"/map/websocket",
    deviceMsgSocket: SOCKET_HOST+"/websocket",
    tempDetailSocket: SOCKET_HOST+"/hum/websocket",
    tempWarningSocket: SOCKET_HOST +"/hum/warn/websocket",
    
    login:process.env.API_HOST + 'login',
    accountCreate: process.env.API_HOST + 'huaxi/user/add',
    accountEdit: process.env.API_HOST + '/user/update',
    accountDelete: process.env.API_HOST + 'huaxi/user/del?username=',
    accountQuery: process.env.API_HOST + 'huaxi/user/query',
    assetsCreate: process.env.API_HOST + 'huaxi/upload',
    inputQuery: process.env.API_HOST + 'huaxi/assetmarked',
    imgUpload: process.env.API_HOST + "uploadimage",
    assetsUpdate: process.env.API_HOST + "huaxi/update",
    imgShow: process.env.API_HOST + 'showImage?assetID=',
    imgShowLeft: process.env.API_HOST + 'showImageLeft?assetID=',
    imgShowRight: process.env.API_HOST + 'showImageRight?assetID=',
    imgShowAbove: process.env.API_HOST + 'showImageAbove?assetID=',
    imgShowAllround: process.env.API_HOST + 'showImageAllround?assetID=',
    imgShowPaperlabel: process.env.API_HOST + 'showImagePaperlabel?assetID=',
    imgShowOnecodelable: process.env.API_HOST + 'showImageOnecodelable?assetID=',
    assetsDelete: process.env.API_HOST + 'huaxi/del?assetID=',
    assetsQuery: process.env.API_HOST + 'huaxi/assetManagement/param',
    assetsTotalNum: process.env.API_HOST + 'huaxi/statement',
    assetsExport: process.env.API_HOST + 'huaxi/poi/export',
    assetsCheck: process.env.API_HOST + 'huaxi/assertChecked?',
    assetsCheckHistory: process.env.API_HOST + 'huaxi/assertChecked/History',
    assetsCheckDetail: process.env.API_HOST + 'huaxi/assertCheckedDetail',
    gatewayCreate: process.env.API_HOST + 'huaxi/addGateway',
    gatewayUpdate: process.env.API_HOST + 'huaxi/updateGateway',
    gatewayRestart: process.env.API_HOST + 'huaxi/gateway?',
    gatewayRestartAll: process.env.API_HOST + 'huaxi/allGateway?',
    gatewayQuery: process.env.API_HOST + 'huaxi/getGateway?address=',
    gatewayDelete: process.env.API_HOST + 'huaxi/deleteGateway',
    gatewayEdit: process.env.API_HOST + 'huaxi/editGateway',
    passwordUpdate: process.env.API_HOST + 'user/update',
    tempHistory: process.env.API_HOST + 'luzhou/assetManagement/param',
    tempCreate: process.env.API_HOST + 'huaxi/addHumiture',
    tempSummary: process.env.API_HOST + 'luzhou/daySummary?mac=',
    tempEditUpdate: process.env.API_HOST + 'luzhou/edit',
    tempEditDetail: process.env.API_HOST + 'luzhou/section',
    tempQuery: process.env.API_HOST + 'luzhou/hum',
    tempDelete: process.env.API_HOST + 'huaxi/delHumiture?delEquipment=',
    tempExport: process.env.API_HOST + '/huaxi/hygrothermograph/export',
    repairRegister: process.env.API_HOST + 'maintainhistory/uploadimage',
    deliveryRegister: process.env.API_HOST + 'lendouthistory/uploadimage',
    damageRegisterHistory: process.env.API_HOST + 'maintainhistory/query',
    departmentList: process.env.API_HOST + '/huaxi/departmentType',
}
export default NET_PORT
