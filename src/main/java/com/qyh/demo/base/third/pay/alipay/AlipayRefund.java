package com.qyh.demo.base.third.pay.alipay;
/**
 * 支付宝退款对象
 * @author qiuyuehao 2017年10月27日下午8:59:22
 *
 */
public class AlipayRefund {
	
	/**
	 * 支付时的商家的订单号(必填)
	 */
	private String out_trade_no;
	/**
	 * 支付时的交易号(必填)
	 */
	private String trade_no;
	/**
	 * 退款金额(必填)
	 */
	private String refund_amount;
	/**
	 * 退款理由(必填)
	 */
	private String refund_reason;
	/**
	 * 批次号    选填(标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。)
	 */
	private String out_request_no;
	
	/**
	 * 订单退款币种信息 可选(默认人民币)  
	 * 标价币种, total_amount 对应的币种单位。
	 * 支持英镑：GBP、港币：HKD、美元：USD、新加坡元：SGD、日元：JPY、加拿大元：CAD、澳元：AUD、
	 * 欧元：EUR、新西兰元：NZD、韩元：KRW、泰铢：THB、瑞士法郎：CHF、瑞典克朗：SEK、丹麦克朗：DKK、
	 * 挪威克朗：NOK、马来西亚林吉特：MYR、印尼卢比：IDR、菲律宾比索：PHP、毛里求斯卢比：MUR、
	 * 以色列新谢克尔：ILS、斯里兰卡卢比：LKR、俄罗斯卢布：RUB、阿联酋迪拉姆：AED、
	 * 捷克克朗：CZK、南非兰特：ZAR、人民币：CNY
	 */
	private String refund_currency;
	/**
	 * 	商户的操作员编号  可选
	 */
	private String operator_id;
	/**
	 * 商户的门店编号 可选
	 */
	private String store_id;
	/**
	 * 	商户的终端编号 可选
	 */
	private String terminal_id;
	
	public AlipayRefund() {
		this.refund_reason = "正常退款";
	}
	
	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(String refund_amount) {
		this.refund_amount = refund_amount;
	}

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}

	public String getOut_request_no() {
		return out_request_no;
	}

	public void setOut_request_no(String out_request_no) {
		this.out_request_no = out_request_no;
	}



	public String getOperator_id() {
		return operator_id;
	}



	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}



	public String getStore_id() {
		return store_id;
	}



	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}



	public String getTerminal_id() {
		return terminal_id;
	}



	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}



	public String getRefund_currency() {
		return refund_currency;
	}



	public void setRefund_currency(String refund_currency) {
		this.refund_currency = refund_currency;
	}

	@Override
	public String toString() {
		return "{"
				+ "\"out_trade_no\":\"" + out_trade_no + "\", \"trade_no\":\"" + trade_no + "\", \"refund_amount\":\""
				+ refund_amount + "\", \"refund_reason\":\"" + refund_reason 
				+ "\"}";
	}
	
}
