package com.internousdev.georgia.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.georgia.dao.CartInfoDAO;
import com.internousdev.georgia.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class CartAction extends ActionSupport implements SessionAware {
	private ArrayList<CartInfoDTO> cartList;
	private Map<String, Object> session;
	private int totalPrice;
	private int logined;

	public String execute() {
		if (!session.containsKey("tempUserId") && !session.containsKey("userId")) {
			return "sessionTimeout";
		}

		CartInfoDAO dao = new CartInfoDAO();
		String userId = null;

		logined=Integer.parseInt(session.get("logined").toString());

		// ログインしてるならユーザーID(ログインフラグ=1)でログインしていないなら仮ユーザーID(ログインフラグ=0)を取得
		if (logined == 1) {
			userId = session.get("userId").toString();
		} else {
			userId = session.get("tempUserId").toString();
		}

		cartList = dao.cartInfoList(userId);
		totalPrice = dao.totalPrice(userId);

		return SUCCESS;
	}

	public ArrayList<CartInfoDTO> getCartList() {
		return cartList;
	}
	public void setCartList(ArrayList<CartInfoDTO> cartList) {
		this.cartList = cartList;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getLogined() {
		return logined;
	}
	public void setLogined(int logined) {
		this.logined = logined;
	}
}
