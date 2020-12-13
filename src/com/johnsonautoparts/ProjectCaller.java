package com.johnsonautoparts;

import java.lang.reflect.Method;

public class ProjectCaller {
	private Project project=null;
	private Method method=null;
	//private Object paramType=null;
	private String str=null;
	private int num=-1;
	
	private Boolean isString=false;
	private Boolean isNum=false;
	
	public ProjectCaller() {
	
	}
	
	// Project
	public void setProject(Project project) {
		this.project = project;
	}
	public Project getProject() {
		return project;
	}
	
	//Method
	public void setMethod(Method method) {
		this.method = method;
	}
	public Method getMethod() {
		return method;
	}
	
	//Paramater type
	/**
	public void setParamType(Class<?> paramType) {
		this.paramType = paramType;
	}
	public Object getParamType() {
		return paramType;
	}
	**/
	
	public void setString(String str) {
		this.str = str;
		this.isString = true;
	}
	public String getString() {
		return str;
	}
	public Boolean isString() {
		return true;
	}
	
	
	public void setNum(int num) {
		this.num = num;
		this.isNum = true;
	}
	public int getNum() {
		return this.num;
	}
	public Boolean isNum() {
		return true;
	}
}
