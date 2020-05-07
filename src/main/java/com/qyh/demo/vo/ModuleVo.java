package com.qyh.demo.vo;

import com.qyh.demo.entity.Module;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.xml.internal.ws.developer.Serialization;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("restriction")
@Serialization
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class ModuleVo extends Module implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private List<Module> sonModuleList;

	public List<Module> getSonModuleList() {
		return sonModuleList;
	}

	public void setSonModuleList(List<Module> sonModuleList) {
		this.sonModuleList = sonModuleList;
	}
	
}
