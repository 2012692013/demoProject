package com.qyh.demo.base.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * datatables实体类，用于传递参数
 * 
 * @author qiuyuehao
 *
 */
public class DataTables implements java.io.Serializable {

	/**
	 * 版本
	 */
	private static final long serialVersionUID = -4352856640468897683L;
//	private Integer start;// 起始行数
	private Integer pageSize;// 页面大小
	private Integer pageIndex;// 页码
//	private String search;// 搜索的字符串
//	private String order;// 排序方式desc or asc
//	private String column;//需要排序的列
//	private long recordsTotal;// 数据库中的结果总行数
//	private long recordsFiltered;// 搜索过滤后的行数
	private Integer draw; //datatables建议将此参数传回
	private List<?> data;// 结果集
//	private String subSQL;//手动拼装的额外参数
	private Map<String,Object> otherData;//其它要返回到页面的参数

	public DataTables() {};

	public DataTables(Integer start, String search, Integer pageSize, String order, String column, Integer pageNum,
                      Integer recordsTotal, Integer recordsFiltered, Integer draw, String subSQL) {
//		this.start = start;
		this.pageSize = pageSize;
		this.pageIndex = pageIndex;
//		this.search = search;
//		this.order = order;
		this.draw = draw;
//		this.column = column;
//		this.recordsTotal = recordsTotal;
//		this.recordsFiltered = recordsFiltered;
//		this.setSubSQL(subSQL);
	}
	
	public DataTables(HttpServletRequest request, String subSQL){
		pageSize = StringUtils.isEmpty(request.getParameter("pageSize"))?10:Integer.parseInt(request.getParameter("pageSize"));
		pageIndex = StringUtils.isEmpty(request.getParameter("pageIndex"))?1:Integer.parseInt(request.getParameter("pageIndex"));
//		start = StringUtils.isEmpty(request.getParameter("start"))?0:Integer.parseInt(request.getParameter("start"));
//		draw = StringUtils.isEmpty(request.getParameter("draw"))?0:Integer.parseInt(request.getParameter("draw"));
//		search = StringUtils.isEmpty(request.getParameter("search"))?null:request.getParameter("search");
//		order = request.getParameter("order[0][dir]");
//		column = request.getParameter("columns["+request.getParameter("order[0][column]")+"][data]");
//		this.setSubSQL(subSQL);
	}
	
	public static DataTables getInstance(HttpServletRequest request, String subSQL){
		return new DataTables(request, subSQL);
	};

//	public Integer getStart() {
//		return start;
//	}
//
//	public void setStart(Integer start) {
//		this.start = start;
//	}

//	public String getSearch() {
//		return search;
//	}
//
//	public void setSearch(String search) {
//		this.search = search;
//	}


//	public String getOrder() {
//		return order;
//	}
//
//	public void setOrder(String order) {
//		this.order = order;
//	}
	
	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

//	public long getRecordsTotal() {
//		return recordsTotal;
//	}
//
//	public void setRecordsTotal(long recordsTotal) {
//		this.recordsTotal = recordsTotal;
//	}
//
//	public long getRecordsFiltered() {
//		return recordsFiltered;
//	}
//
//	public void setRecordsFiltered(long recordsFiltered) {
//		this.recordsFiltered = recordsFiltered;
//	}

//	public String getColumn() {
//		return column;
//	}
//
//	public void setColumn(String column) {
//		this.column = column;
//	}
//
//	public String getSubSQL() {
//		return subSQL;
//	}
//
//	public void setSubSQL(String subSQL) {
//		this.subSQL = subSQL;
//	}

	public Map<String, Object> getOtherData() {
		return otherData;
	}

	public void setOtherData(Map<String, Object> otherData) {
		this.otherData = otherData;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageNum) {
		this.pageIndex = pageIndex;
	}
}
