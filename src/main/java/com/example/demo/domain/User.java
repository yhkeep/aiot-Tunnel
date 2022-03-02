package com.example.demo.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class User {
//	用户名
	private String username;
//	密码
	private String password;
//TODO  部门待定
	private String department;
	
//	电话
	private String phone;
	
//	权限
	private String role;
//	角色
	private String address;
	
	
	

//TODO	申请科室待定
	private String applyDept;
//	是否展示mac
	private String isShowMac;
//	是否展示购买日期
	private String isShowBuyDate;
//	是否展示购买金额
	private String isShowMoney;
//	是否展示电量
	private String isShowElectric;
	
	private String newpassword;
	
//	资产所在科室名称
	private String locDept;
//	部门名称
	private String departmentroom;
	
//	记忆部门
	private String memorydepart;
//	记忆所在科室
	private String memorylocDept;
//	记忆分类
	private String memoryoneclassify;
	private String memorysecondclassify;
	private String memorythreeclassify;
	private String memoryfourclassify;
	
//	用户登陆具有查看的属性
	private String authority;
	
	
//	分类是否展示
	private String	isShowSpecification;
	private String	isShowType;
	private String	isShowLocation;
	private String	isShowPlaceoforigin;
	private String	isShowBrand;
	private String	isShowDepartmentcode;
	private String	isShowDepartmentroom;
	private String	isShowHomeofficenumber;
	private String	isShowHomeofficename;
	private String	isShowIsentrance;
	private String	isShowSuppliername;
	private String	isShowGeneratebusinessname;
	private String	isShowApplyoddnumbers;
	private String	isShowLocDept;
	private String	isShowStatus;
//	用户角色权限拥有的权限路径
	private String  rolepath;
	
	
	
	public String getRolepath() {
		return rolepath;
	}

	public void setRolepath(String rolepath) {
		this.rolepath = rolepath;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getIsShowSpecification() {
		return isShowSpecification;
	}

	public void setIsShowSpecification(String isShowSpecification) {
		this.isShowSpecification = isShowSpecification;
	}

	public String getIsShowType() {
		return isShowType;
	}

	public void setIsShowType(String isShowType) {
		this.isShowType = isShowType;
	}

	public String getIsShowLocation() {
		return isShowLocation;
	}

	public void setIsShowLocation(String isShowLocation) {
		this.isShowLocation = isShowLocation;
	}

	public String getIsShowPlaceoforigin() {
		return isShowPlaceoforigin;
	}

	public void setIsShowPlaceoforigin(String isShowPlaceoforigin) {
		this.isShowPlaceoforigin = isShowPlaceoforigin;
	}

	public String getIsShowBrand() {
		return isShowBrand;
	}

	public void setIsShowBrand(String isShowBrand) {
		this.isShowBrand = isShowBrand;
	}

	public String getIsShowDepartmentcode() {
		return isShowDepartmentcode;
	}

	public void setIsShowDepartmentcode(String isShowDepartmentcode) {
		this.isShowDepartmentcode = isShowDepartmentcode;
	}

	public String getIsShowDepartmentroom() {
		return isShowDepartmentroom;
	}

	public void setIsShowDepartmentroom(String isShowDepartmentroom) {
		this.isShowDepartmentroom = isShowDepartmentroom;
	}

	public String getIsShowHomeofficenumber() {
		return isShowHomeofficenumber;
	}

	public void setIsShowHomeofficenumber(String isShowHomeofficenumber) {
		this.isShowHomeofficenumber = isShowHomeofficenumber;
	}

	public String getIsShowHomeofficename() {
		return isShowHomeofficename;
	}

	public void setIsShowHomeofficename(String isShowHomeofficename) {
		this.isShowHomeofficename = isShowHomeofficename;
	}

	public String getIsShowIsentrance() {
		return isShowIsentrance;
	}

	public void setIsShowIsentrance(String isShowIsentrance) {
		this.isShowIsentrance = isShowIsentrance;
	}

	public String getIsShowSuppliername() {
		return isShowSuppliername;
	}

	public void setIsShowSuppliername(String isShowSuppliername) {
		this.isShowSuppliername = isShowSuppliername;
	}

	public String getIsShowGeneratebusinessname() {
		return isShowGeneratebusinessname;
	}

	public void setIsShowGeneratebusinessname(String isShowGeneratebusinessname) {
		this.isShowGeneratebusinessname = isShowGeneratebusinessname;
	}

	public String getIsShowApplyoddnumbers() {
		return isShowApplyoddnumbers;
	}

	public void setIsShowApplyoddnumbers(String isShowApplyoddnumbers) {
		this.isShowApplyoddnumbers = isShowApplyoddnumbers;
	}

	public String getIsShowLocDept() {
		return isShowLocDept;
	}

	public void setIsShowLocDept(String isShowLocDept) {
		this.isShowLocDept = isShowLocDept;
	}

	public String getIsShowStatus() {
		return isShowStatus;
	}

	public void setIsShowStatus(String isShowStatus) {
		this.isShowStatus = isShowStatus;
	}

	public String getMemoryoneclassify() {
		return memoryoneclassify;
	}

	public void setMemoryoneclassify(String memoryoneclassify) {
		this.memoryoneclassify = memoryoneclassify;
	}

	public String getMemorysecondclassify() {
		return memorysecondclassify;
	}

	public void setMemorysecondclassify(String memorysecondclassify) {
		this.memorysecondclassify = memorysecondclassify;
	}

	public String getMemorythreeclassify() {
		return memorythreeclassify;
	}

	public void setMemorythreeclassify(String memorythreeclassify) {
		this.memorythreeclassify = memorythreeclassify;
	}

	public String getMemoryfourclassify() {
		return memoryfourclassify;
	}

	public void setMemoryfourclassify(String memoryfourclassify) {
		this.memoryfourclassify = memoryfourclassify;
	}

	public String getMemorydepart() {
		return memorydepart;
	}

	public void setMemorydepart(String memorydepart) {
		this.memorydepart = memorydepart;
	}

	public String getMemorylocDept() {
		return memorylocDept;
	}

	public void setMemorylocDept(String memorylocDept) {
		this.memorylocDept = memorylocDept;
	}

	public String getLocDept() {
		return locDept;
	}

	public void setLocDept(String locDept) {
		this.locDept = locDept;
	}

	public String getDepartmentroom() {
		return departmentroom;
	}

	public void setDepartmentroom(String departmentroom) {
		this.departmentroom = departmentroom;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public String getApplyDept() {
		return applyDept;
	}

	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}

	public String getIsShowMac() {
		return isShowMac;
	}

	public void setIsShowMac(String isShowMac) {
		this.isShowMac = isShowMac;
	}

	public String getIsShowBuyDate() {
		return isShowBuyDate;
	}

	public void setIsShowBuyDate(String isShowBuyDate) {
		this.isShowBuyDate = isShowBuyDate;
	}

	public String getIsShowMoney() {
		return isShowMoney;
	}

	public void setIsShowMoney(String isShowMoney) {
		this.isShowMoney = isShowMoney;
	}

	public String getIsShowElectric() {
		return isShowElectric;
	}

	public void setIsShowElectric(String isShowElectric) {
		this.isShowElectric = isShowElectric;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}



	public User() {
		super();
	}
	
	
	
//	临时测试使用
	public User(String username, String phone, String role) {
		super();
		this.username = username;
		this.phone = phone;
		this.role = role;
	}

	public User(String username, String password, String department, String phone, String role, String address,
			String applyDept, String isShowMac, String isShowBuyDate, String isShowMoney, String isShowElectric,
			String newpassword, String locDept, String departmentroom, String memorydepart, String memorylocDept,
			String memoryoneclassify, String memorysecondclassify, String memorythreeclassify,
			String memoryfourclassify, String authority, String isShowSpecification, String isShowType,
			String isShowLocation, String isShowPlaceoforigin, String isShowBrand, String isShowDepartmentcode,
			String isShowDepartmentroom, String isShowHomeofficenumber, String isShowHomeofficename,
			String isShowIsentrance, String isShowSuppliername, String isShowGeneratebusinessname,
			String isShowApplyoddnumbers, String isShowLocDept, String isShowStatus) {
		super();
		this.username = username;
		this.password = password;
		this.department = department;
		this.phone = phone;
		this.role = role;
		this.address = address;
		this.applyDept = applyDept;
		this.isShowMac = isShowMac;
		this.isShowBuyDate = isShowBuyDate;
		this.isShowMoney = isShowMoney;
		this.isShowElectric = isShowElectric;
		this.newpassword = newpassword;
		this.locDept = locDept;
		this.departmentroom = departmentroom;
		this.memorydepart = memorydepart;
		this.memorylocDept = memorylocDept;
		this.memoryoneclassify = memoryoneclassify;
		this.memorysecondclassify = memorysecondclassify;
		this.memorythreeclassify = memorythreeclassify;
		this.memoryfourclassify = memoryfourclassify;
		this.authority = authority;
		this.isShowSpecification = isShowSpecification;
		this.isShowType = isShowType;
		this.isShowLocation = isShowLocation;
		this.isShowPlaceoforigin = isShowPlaceoforigin;
		this.isShowBrand = isShowBrand;
		this.isShowDepartmentcode = isShowDepartmentcode;
		this.isShowDepartmentroom = isShowDepartmentroom;
		this.isShowHomeofficenumber = isShowHomeofficenumber;
		this.isShowHomeofficename = isShowHomeofficename;
		this.isShowIsentrance = isShowIsentrance;
		this.isShowSuppliername = isShowSuppliername;
		this.isShowGeneratebusinessname = isShowGeneratebusinessname;
		this.isShowApplyoddnumbers = isShowApplyoddnumbers;
		this.isShowLocDept = isShowLocDept;
		this.isShowStatus = isShowStatus;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", department=" + department + ", phone="
				+ phone + ", role=" + role + ", address=" + address + ", applyDept=" + applyDept + ", isShowMac="
				+ isShowMac + ", isShowBuyDate=" + isShowBuyDate + ", isShowMoney=" + isShowMoney + ", isShowElectric="
				+ isShowElectric + ", newpassword=" + newpassword + ", locDept=" + locDept + ", departmentroom="
				+ departmentroom + ", memorydepart=" + memorydepart + ", memorylocDept=" + memorylocDept
				+ ", memoryoneclassify=" + memoryoneclassify + ", memorysecondclassify=" + memorysecondclassify
				+ ", memorythreeclassify=" + memorythreeclassify + ", memoryfourclassify=" + memoryfourclassify
				+ ", authority=" + authority + ", isShowSpecification=" + isShowSpecification + ", isShowType="
				+ isShowType + ", isShowLocation=" + isShowLocation + ", isShowPlaceoforigin=" + isShowPlaceoforigin
				+ ", isShowBrand=" + isShowBrand + ", isShowDepartmentcode=" + isShowDepartmentcode
				+ ", isShowDepartmentroom=" + isShowDepartmentroom + ", isShowHomeofficenumber="
				+ isShowHomeofficenumber + ", isShowHomeofficename=" + isShowHomeofficename + ", isShowIsentrance="
				+ isShowIsentrance + ", isShowSuppliername=" + isShowSuppliername + ", isShowGeneratebusinessname="
				+ isShowGeneratebusinessname + ", isShowApplyoddnumbers=" + isShowApplyoddnumbers + ", isShowLocDept="
				+ isShowLocDept + ", isShowStatus=" + isShowStatus + "]";
	}

	/*public User(String username, String password, String department, String phone, String role, String address,
			String applyDept, String isShowMac, String isShowBuyDate, String isShowMoney, String isShowElectric,
			String newpassword, String locDept, String departmentroom, String memorydepart, String memorylocDept,
			String memoryoneclassify, String memorysecondclassify, String memorythreeclassify,
			String memoryfourclassify, String isShowSpecification, String isShowType, String isShowLocation,
			String isShowPlaceoforigin, String isShowBrand, String isShowDepartmentcode, String isShowDepartmentroom,
			String isShowHomeofficenumber, String isShowHomeofficename, String isShowIsentrance,
			String isShowSuppliername, String isShowGeneratebusinessname, String isShowApplyoddnumbers,
			String isShowLocDept, String isShowStatus) {
		super();
		this.username = username;
		this.password = password;
		this.department = department;
		this.phone = phone;
		this.role = role;
		this.address = address;
		this.applyDept = applyDept;
		this.isShowMac = isShowMac;
		this.isShowBuyDate = isShowBuyDate;
		this.isShowMoney = isShowMoney;
		this.isShowElectric = isShowElectric;
		this.newpassword = newpassword;
		this.locDept = locDept;
		this.departmentroom = departmentroom;
		this.memorydepart = memorydepart;
		this.memorylocDept = memorylocDept;
		this.memoryoneclassify = memoryoneclassify;
		this.memorysecondclassify = memorysecondclassify;
		this.memorythreeclassify = memorythreeclassify;
		this.memoryfourclassify = memoryfourclassify;
		this.isShowSpecification = isShowSpecification;
		this.isShowType = isShowType;
		this.isShowLocation = isShowLocation;
		this.isShowPlaceoforigin = isShowPlaceoforigin;
		this.isShowBrand = isShowBrand;
		this.isShowDepartmentcode = isShowDepartmentcode;
		this.isShowDepartmentroom = isShowDepartmentroom;
		this.isShowHomeofficenumber = isShowHomeofficenumber;
		this.isShowHomeofficename = isShowHomeofficename;
		this.isShowIsentrance = isShowIsentrance;
		this.isShowSuppliername = isShowSuppliername;
		this.isShowGeneratebusinessname = isShowGeneratebusinessname;
		this.isShowApplyoddnumbers = isShowApplyoddnumbers;
		this.isShowLocDept = isShowLocDept;
		this.isShowStatus = isShowStatus;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", department=" + department + ", phone="
				+ phone + ", role=" + role + ", address=" + address + ", applyDept=" + applyDept + ", isShowMac="
				+ isShowMac + ", isShowBuyDate=" + isShowBuyDate + ", isShowMoney=" + isShowMoney + ", isShowElectric="
				+ isShowElectric + ", newpassword=" + newpassword + ", locDept=" + locDept + ", departmentroom="
				+ departmentroom + ", memorydepart=" + memorydepart + ", memorylocDept=" + memorylocDept
				+ ", memoryoneclassify=" + memoryoneclassify + ", memorysecondclassify=" + memorysecondclassify
				+ ", memorythreeclassify=" + memorythreeclassify + ", memoryfourclassify=" + memoryfourclassify
				+ ", isShowSpecification=" + isShowSpecification + ", isShowType=" + isShowType + ", isShowLocation="
				+ isShowLocation + ", isShowPlaceoforigin=" + isShowPlaceoforigin + ", isShowBrand=" + isShowBrand
				+ ", isShowDepartmentcode=" + isShowDepartmentcode + ", isShowDepartmentroom=" + isShowDepartmentroom
				+ ", isShowHomeofficenumber=" + isShowHomeofficenumber + ", isShowHomeofficename="
				+ isShowHomeofficename + ", isShowIsentrance=" + isShowIsentrance + ", isShowSuppliername="
				+ isShowSuppliername + ", isShowGeneratebusinessname=" + isShowGeneratebusinessname
				+ ", isShowApplyoddnumbers=" + isShowApplyoddnumbers + ", isShowLocDept=" + isShowLocDept
				+ ", isShowStatus=" + isShowStatus + "]";
	}*/
	
	
	
	
}