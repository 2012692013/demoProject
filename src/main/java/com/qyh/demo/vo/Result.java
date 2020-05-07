package com.qyh.demo.vo;

import lombok.Data;

@Data
public class Result {

	private String resultState;

	private String msg;

	private Object data;

	public Result(String resultState,String msg,Object data) {
		this.resultState = resultState;
		this.msg = msg;
		this.data = data;
	}


	public Result() {
		super();
		this.resultState = "0";
		this.msg = "参数未传";
	}

	public void setResltInfo(String resultState,String msg){
		setResltInfo(resultState, msg, null);
	}
	public void setResltInfo(String resultState,String msg,Object data){
		this.resultState = resultState;
		this.msg = msg;
		this.data = data;
	}

	public void successInfo(Object data){
		this.data = data;
		this.msg = "成功";
		this.resultState = "1";
	}
	public void successInfo(){
		this.data = null;
		this.msg = "成功";
		this.resultState = "1";
	}
	public void errorInfo(Object data){
		this.data = data;
		this.msg = "非法参数";
		this.resultState = "-1";
	}
	public void errorInfo(){
		this.data = null;
		this.msg = "非法参数";
		this.resultState = "-1";
	}
	
}
