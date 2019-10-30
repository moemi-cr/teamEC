package com.internousdev.georgia.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.georgia.dao.CartInfoDAO;
import com.internousdev.georgia.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class AddCartAction extends ActionSupport implements SessionAware {
	private ArrayList<CartInfoDTO> cartList;
	private Map<String, Object> session;
	private int productId;
	private int productCount;
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

		// カートの中に同じ商品があるか確認
		if (dao.isExistsCartInfo(userId, productId)) {
			//同じ商品があれば数量を更新
			count = dao.updateCount(userId, productId, productCount);
			//同じ商品がないならカートに追加
		} else {
			count = dao.addCart(userId, productId, productCount);

		}

		// 更新か登録された者があればリストに追加
		if (count > 0) {
			cartList = dao.cartInfoList(userId);
			setTotalPrice(dao.totalPrice(userId));
			result = SUCCESS;
		}
		return result;
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

	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
}
