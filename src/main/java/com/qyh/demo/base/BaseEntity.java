package com.qyh.demo.base;

import com.qyh.demo.base.util.Kit;
import com.qyh.demo.dto.PageDto;
import com.qyh.demo.entity.SysUser;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;
@Data
@MappedSuperclass
public class BaseEntity {

	/**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String createMan;
	
	private String updateMan;
	
	private Long version;
	
	private Integer delFlag;

	
	/**
	 * 保存前
	 * @author qiuyuehao
	 * 2018年6月12日
	 * @param request
	 */
	public void preInsert(HttpServletRequest request){
		if(request==null){
			this.setCreateMan(null);
		}else{
			SysUser user = Kit.getCurLoginUser(request);
			if(user==null){
				this.setCreateMan(null);
			}else{
				this.setCreateMan(user.getId());
			}
		}
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.setCreateTime(new Date());
		this.setVersion(System.currentTimeMillis());
		this.setDelFlag(0);
		if(this.id==null){
			this.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		}
	}
	
	/**
	 * 修改前
	 * @author qiuyuehao
	 * 2018年6月12日
	 * @param request
	 */
	public void preUpdate(HttpServletRequest request){
		if(request==null){
			this.setUpdateMan(null);
		}else{
			SysUser user = Kit.getCurLoginUser(request);
			if(user==null){
				this.setUpdateMan(null);
			}else{
				this.setUpdateMan(user.getId());
			}
		}
		this.setUpdateTime(new Date());
	}


	public PageDto getPage(HttpServletRequest request){
		PageDto pageDto = new PageDto();
		if (request==null){
			pageDto.setPageNum(1);pageDto.setPageSize(10);
		}
		pageDto.setPageNum(StringUtils.isBlank(request.getParameter("pageNum")) ?1: Integer.valueOf(request.getParameter("pageNum")));
		pageDto.setPageSize(StringUtils.isBlank(request.getParameter("pageSize"))?10:Integer.valueOf(request.getParameter("pageSize")));
		return pageDto;
	}
	
	
}
