package com.internousdev.georgia.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.georgia.dao.CartInfoDAO;
import com.internousdev.georgia.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteCartAction extends ActionSupport implements SessionAware {
	private ArrayList<CartInfoDTO> cartList;
	private Map<String, Object> session;
	private int[] checkList;
	private int totalPrice;

	public String execute() {
		if (!session.containsKey("tempUserId") && !session.containsKey("userId")) {
			return "sessionTimeout";
		}

		CartInfoDAO dao = new CartInfoDAO();
		String result = ERROR;
		String userId = null;
		int count = 0;

		// ログインしてるならユーザーID(ログインフラグ=1)でログインしていないなら仮ユーザーID(ログインフラグ=0)を取得
		if (Integer.parseInt(session.get("logined").toString()) == 1) {
			userId = session.get("userId").toString();
		} else {
			userId = session.get("tempUserId").toString();
		}

		//チェックリストにチェックが入っているものを削除
		for (int productId : checkList) {
			count += dao.deleteProduct(userId, productId);
		}
		if (count > 0) {
			cartList = dao.cartInfoList(userId);
			setTotalPrice(dao.totalPrice(userId));
			result = SUCCESS;
		}
		return result;
	}

	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int[] getCheckList() {
		return checkList;
	}
	public void setCheckList(int[] checkList) {
		this.checkList = checkList;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public ArrayList<CartInfoDTO> getCartList() {
		return cartList;
	}
	public void setCartList(ArrayList<CartInfoDTO> cartList) {
		this.cartList = cartList;
	}
}
