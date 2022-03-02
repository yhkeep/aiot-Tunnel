package com.example.demo.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class Asset {
//	资产id
	@Excel(name = "资产id", orderNum = "0")
	private String assetID;
//	通用名称
	@Excel(name = "通用名称", orderNum = "1")
	private String generalName;
//	资产名称
	@Excel(name = "资产名称", orderNum = "2")
	private String assetName;
//	类型
	@Excel(name = "类型", orderNum = "3")
	private String type;
//	品牌
	@Excel(name = "品牌", orderNum = "4")
	private String brand;
//	申请科室
	@Excel(name = "申请科室", orderNum = "5")
	private String applyDept;
//	放置科室（资产原自定义所在部门）
	@Excel(name = "所在科室名称", orderNum = "6")
	private String locDept;
//	坐落位置(实时)
	@Excel(name = "坐落位置", orderNum = "7")
	private String location;
//	状态
	@Excel(name = "状态", orderNum = "8")
	private String status;
//	标签mac
	@Excel(name = "标签mac", orderNum = "9")
	private String mac;
//	手术室
	@Excel(name = "手术室", orderNum = "10")
	private String operatingRoom;
//	数量
	@Excel(name = "数量", orderNum = "11")
	private String amount;
//	单位
	@Excel(name = "单位", orderNum = "12")
	private String unit;
//	购买日期
	@Excel(name = "购买日期", orderNum = "13")
	private String buyDate;
//	金额
	@Excel(name = "金额", orderNum = "14")
//	private String money;
	private BigDecimal money;
//	设备动态实时更新时间，用于判断单位时间内设备是否进行盘点
	@Excel(name = "盘点", orderNum = "15")
	private String check;

//	部门（资产设备实时所在部门）
	@Excel(name = "部门", orderNum = "16")
	private String department;
//	备注
	@Excel(name = "备注", orderNum = "17")
	private String remark;
	
//	记录人员
	@Excel(name = "记录人员", orderNum = "18")
	private String recorder;
	
//	一级分类名称
	@Excel(name = "一级分类名称", orderNum = "19")
	private String oneclassify;
//	二级分类名称
	@Excel(name = "二级分类名称", orderNum = "20")
	private String secondclassify;
//	三级分类名称
	@Excel(name = "三级分类名称", orderNum = "21")
	private String threeclassify;
//	四级分类名称
	@Excel(name = "四级分类名称", orderNum = "22")
	private String fourclassify;
//	规格
	@Excel(name = "规格", orderNum = "23")
	private String specification;
//	产地
	@Excel(name = "产地", orderNum = "24")
	private String placeoforigin;
//	部门编码
	@Excel(name = "部门编码", orderNum = "25")
	private String departmentcode;
//	部门名称
	@Excel(name = "部门名称", orderNum = "26")
	private String departmentroom;
//	大科室编码
	@Excel(name = "大科室编码", orderNum = "27")
	private String homeofficenumber;
//	大科室名称
	@Excel(name = "大科室名称", orderNum = "28")
	private String homeofficename;
//	是否进口
	@Excel(name = "是否进口", orderNum = "29")
	private String isentrance;
//	供应商名称
	@Excel(name = "供应商名称", orderNum = "30")
	private String suppliername;
//	生产厂商名称
	@Excel(name = "生产厂商名称", orderNum = "31")
	private String generatebusinessname;
//	处置申请单号
	@Excel(name = "处置申请单号", orderNum = "32")
	private String applyoddnumbers;
	
	
//	正在接受的标签数据
	private Received received;
	
//	图片路径
	private String imageUrl;
	
//	电量
	@Excel(name = "电量", orderNum = "33")
	private String electric;
//	地址
	private String address;
	
//	缩小图路径
	private String compressImageUrl;
//	左侧面图片照
	private String leftImageUrl;
//	全面图片照
	private String allroundImageUrl;
//	上方图片照
	private String aboveImageUrl;
//	右侧面图片照
	private String rightImageUrl;
//	纸质标签图片照
	private String paperlabelImageUrl;
//	一维码标签照
	private String onecodelableImageUrl;
//	楼层
	@Excel(name = "楼层", orderNum = "34")
	private String floor;
//	建筑
	@Excel(name = "建筑名称", orderNum = "35")
	private String architecture;
//	院区
	@Excel(name = "院区名称", orderNum = "36")
	private String academy;
//	cad房间名
	private String cadMapRoomName;
//	资产识别号
	@Excel(name = "资产识别号", orderNum = "37")
	private String sn;
	
	
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getRecorder() {
		return recorder;
	}
	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}
	public String getPaperlabelImageUrl() {
		return paperlabelImageUrl;
	}
	public void setPaperlabelImageUrl(String paperlabelImageUrl) {
		this.paperlabelImageUrl = paperlabelImageUrl;
	}
	public String getOnecodelableImageUrl() {
		return onecodelableImageUrl;
	}
	public void setOnecodelableImageUrl(String onecodelableImageUrl) {
		this.onecodelableImageUrl = onecodelableImageUrl;
	}
	public String getLeftImageUrl() {
		return leftImageUrl;
	}
	public void setLeftImageUrl(String leftImageUrl) {
		this.leftImageUrl = leftImageUrl;
	}
	public String getAllroundImageUrl() {
		return allroundImageUrl;
	}
	public void setAllroundImageUrl(String allroundImageUrl) {
		this.allroundImageUrl = allroundImageUrl;
	}
	public String getAboveImageUrl() {
		return aboveImageUrl;
	}
	public void setAboveImageUrl(String aboveImageUrl) {
		this.aboveImageUrl = aboveImageUrl;
	}
	public String getRightImageUrl() {
		return rightImageUrl;
	}
	public void setRightImageUrl(String rightImageUrl) {
		this.rightImageUrl = rightImageUrl;
	}
	public String getOneclassify() {
		return oneclassify;
	}
	public void setOneclassify(String oneclassify) {
		this.oneclassify = oneclassify;
	}
	public String getSecondclassify() {
		return secondclassify;
	}
	public void setSecondclassify(String secondclassify) {
		this.secondclassify = secondclassify;
	}
	public String getThreeclassify() {
		return threeclassify;
	}
	public void setThreeclassify(String threeclassify) {
		this.threeclassify = threeclassify;
	}
	public String getFourclassify() {
		return fourclassify;
	}
	public void setFourclassify(String fourclassify) {
		this.fourclassify = fourclassify;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getPlaceoforigin() {
		return placeoforigin;
	}
	public void setPlaceoforigin(String placeoforigin) {
		this.placeoforigin = placeoforigin;
	}
	public String getDepartmentcode() {
		return departmentcode;
	}
	public void setDepartmentcode(String departmentcode) {
		this.departmentcode = departmentcode;
	}
	public String getDepartmentroom() {
		return departmentroom;
	}
	public void setDepartmentroom(String departmentroom) {
		this.departmentroom = departmentroom;
	}
	public String getHomeofficenumber() {
		return homeofficenumber;
	}
	public void setHomeofficenumber(String homeofficenumber) {
		this.homeofficenumber = homeofficenumber;
	}
	public String getHomeofficename() {
		return homeofficename;
	}
	public void setHomeofficename(String homeofficename) {
		this.homeofficename = homeofficename;
	}
	public String getIsentrance() {
		return isentrance;
	}
	public void setIsentrance(String isentrance) {
		this.isentrance = isentrance;
	}
	public String getSuppliername() {
		return suppliername;
	}
	public void setSuppliername(String suppliername) {
		this.suppliername = suppliername;
	}
	public String getGeneratebusinessname() {
		return generatebusinessname;
	}
	public void setGeneratebusinessname(String generatebusinessname) {
		this.generatebusinessname = generatebusinessname;
	}
	public String getApplyoddnumbers() {
		return applyoddnumbers;
	}
	public void setApplyoddnumbers(String applyoddnumbers) {
		this.applyoddnumbers = applyoddnumbers;
	}
	public String getCompressImageUrl() {
		return compressImageUrl;
	}
	public void setCompressImageUrl(String compressImageUrl) {
		this.compressImageUrl = compressImageUrl;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getElectric() {
		return electric;
	}
	public void setElectric(String electric) {
		this.electric = electric;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Received getReceived() {
		return received;
	}
	public void setReceived(Received received) {
		this.received = received;
	}
	
	
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAssetID() {
		return assetID;
	}
	public void setAssetID(String assetID) {
		this.assetID = assetID;
	}
	public String getGeneralName() {
		return generalName;
	}
	public void setGeneralName(String generalName) {
		this.generalName = generalName;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getApplyDept() {
		return applyDept;
	}
	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}
	public String getLocDept() {
		return locDept;
	}
	public void setLocDept(String locDept) {
		this.locDept = locDept;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getOperatingRoom() {
		return operatingRoom;
	}
	public void setOperatingRoom(String operatingRoom) {
		this.operatingRoom = operatingRoom;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	
	
	public String getArchitecture() {
		return architecture;
	}
	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}
	public String getAcademy() {
		return academy;
	}
	public void setAcademy(String academy) {
		this.academy = academy;
	}
	public String getCadMapRoomName() {
		return cadMapRoomName;
	}
	public void setCadMapRoomName(String cadMapRoomName) {
		this.cadMapRoomName = cadMapRoomName;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	@Override
	public String toString() {
		return "Asset [assetID=" + assetID + ", generalName=" + generalName + ", assetName=" + assetName + ", type="
				+ type + ", brand=" + brand + ", applyDept=" + applyDept + ", locDept=" + locDept + ", location="
				+ location + ", status=" + status + ", mac=" + mac + ", operatingRoom=" + operatingRoom + ", amount="
				+ amount + ", unit=" + unit + ", buyDate=" + buyDate + ", money=" + money + ", check=" + check
				+ ", department=" + department + ", remark=" + remark + ", recorder=" + recorder + ", oneclassify="
				+ oneclassify + ", secondclassify=" + secondclassify + ", threeclassify=" + threeclassify
				+ ", fourclassify=" + fourclassify + ", specification=" + specification + ", placeoforigin="
				+ placeoforigin + ", departmentcode=" + departmentcode + ", departmentroom=" + departmentroom
				+ ", homeofficenumber=" + homeofficenumber + ", homeofficename=" + homeofficename + ", isentrance="
				+ isentrance + ", suppliername=" + suppliername + ", generatebusinessname=" + generatebusinessname
				+ ", applyoddnumbers=" + applyoddnumbers + ", received=" + received + ", imageUrl=" + imageUrl
				+ ", electric=" + electric + ", address=" + address + ", compressImageUrl=" + compressImageUrl
				+ ", leftImageUrl=" + leftImageUrl + ", allroundImageUrl=" + allroundImageUrl + ", aboveImageUrl="
				+ aboveImageUrl + ", rightImageUrl=" + rightImageUrl + ", paperlabelImageUrl=" + paperlabelImageUrl
				+ ", onecodelableImageUrl=" + onecodelableImageUrl + ", floor=" + floor + ", architecture="
				+ architecture + ", academy=" + academy + ", cadMapRoomName=" + cadMapRoomName + ", sn=" + sn
				+ ", getFloor()=" + getFloor() + ", getRecorder()=" + getRecorder() + ", getPaperlabelImageUrl()="
				+ getPaperlabelImageUrl() + ", getOnecodelableImageUrl()=" + getOnecodelableImageUrl()
				+ ", getLeftImageUrl()=" + getLeftImageUrl() + ", getAllroundImageUrl()=" + getAllroundImageUrl()
				+ ", getAboveImageUrl()=" + getAboveImageUrl() + ", getRightImageUrl()=" + getRightImageUrl()
				+ ", getOneclassify()=" + getOneclassify() + ", getSecondclassify()=" + getSecondclassify()
				+ ", getThreeclassify()=" + getThreeclassify() + ", getFourclassify()=" + getFourclassify()
				+ ", getSpecification()=" + getSpecification() + ", getPlaceoforigin()=" + getPlaceoforigin()
				+ ", getDepartmentcode()=" + getDepartmentcode() + ", getDepartmentroom()=" + getDepartmentroom()
				+ ", getHomeofficenumber()=" + getHomeofficenumber() + ", getHomeofficename()=" + getHomeofficename()
				+ ", getIsentrance()=" + getIsentrance() + ", getSuppliername()=" + getSuppliername()
				+ ", getGeneratebusinessname()=" + getGeneratebusinessname() + ", getApplyoddnumbers()="
				+ getApplyoddnumbers() + ", getCompressImageUrl()=" + getCompressImageUrl() + ", getAddress()="
				+ getAddress() + ", getElectric()=" + getElectric() + ", getImageUrl()=" + getImageUrl()
				+ ", getReceived()=" + getReceived() + ", getDepartment()=" + getDepartment() + ", getRemark()="
				+ getRemark() + ", getAssetID()=" + getAssetID() + ", getGeneralName()=" + getGeneralName()
				+ ", getAssetName()=" + getAssetName() + ", getType()=" + getType() + ", getBrand()=" + getBrand()
				+ ", getApplyDept()=" + getApplyDept() + ", getLocDept()=" + getLocDept() + ", getLocation()="
				+ getLocation() + ", getStatus()=" + getStatus() + ", getMac()=" + getMac() + ", getOperatingRoom()="
				+ getOperatingRoom() + ", getAmount()=" + getAmount() + ", getUnit()=" + getUnit() + ", getBuyDate()="
				+ getBuyDate() + ", getCheck()=" + getCheck() + ", getArchitecture()=" + getArchitecture()
				+ ", getAcademy()=" + getAcademy() + ", getCadMapRoomName()=" + getCadMapRoomName() + ", getMoney()="
				+ getMoney() + ", getSn()=" + getSn() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	
	
}