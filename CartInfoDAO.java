package com.internousdev.georgia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.internousdev.georgia.dto.CartInfoDTO;
import com.internousdev.georgia.util.DBConnector;

public class CartInfoDAO {

// ---------	カート一覧  CartAction ---------  //
	public ArrayList<CartInfoDTO> cartInfoList(String userId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		ArrayList<CartInfoDTO> cartInfoList = new ArrayList<CartInfoDTO>();

		String sql = "SELECT"
						+ " cart.id as id,"
						+ " cart.user_id as user_id,"
						+ " cart.product_id as product_id,"
						+ " cart.product_count as product_count,"
						+ " product.price as price,"
						+ " product.product_name as product_name,"
						+ " product.product_name_kana as product_name_kana,"
						+ " product.image_file_path as image_file_path, "
						+ " product.image_file_name as image_file_name, "
						+ " product.release_date as release_date,"
						+ " product.release_company as release_company,"
						+ " ( product.price * cart.product_count) as subtotal,"
						+ " cart.regist_date as regist_date,"
						+ " cart.update_date as update_date"
					+ " FROM cart_info as cart"
					+ " LEFT JOIN product_info as product"
					+ " ON cart.product_id = product.product_id"
					+ " WHERE cart.user_id = ?"
					+ " ORDER BY update_date desc, regist_date desc";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CartInfoDTO dto = new CartInfoDTO();
				dto.setId(rs.getInt("id"));
				dto.setUserId(rs.getString("user_id"));
				dto.setProductId(rs.getInt("product_id"));
				dto.setProductCount(rs.getInt("product_count"));
				dto.setPrice(rs.getInt("price"));
				dto.setProductName(rs.getString("product_name"));
				dto.setProductNameKana(rs.getString("product_name_kana"));
				dto.setImageFilePath(rs.getString("image_file_path"));
				dto.setImageFileName(rs.getString("image_file_name"));
				dto.setReleaseDate(rs.getDate("release_date"));
				dto.setReleaseCompany(rs.getString("release_company"));
				dto.setSubTotal(rs.getInt("subtotal"));
				cartInfoList.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
			con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return cartInfoList;
	}

// ---------	カート合計  CartAction---------  //
	public int totalPrice(String userId){
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		int totalPrice = 0;

		String sql = "SELECT "
				+ "SUM(product_count * price) as total_price "
				+ "FROM cart_info cart "
				+ "LEFT JOIN product_info product "
				+ "ON cart.product_id = product.product_id "
				+ "WHERE user_id=? group by user_id";

		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			if(rs.next()){
				totalPrice=rs.getInt("total_price");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		try{
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return totalPrice;
	}

// ---------	同じ商品がなければカートに追加する AddCartAction  ---------  //
	public int addCart(String userId, int productId, int productCount){
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		int count =0;

		String sql = "INSERT INTO cart_info(user_id,product_id,product_count,regist_date,update_date) VALUES(?,?,?,now() ,now())";

		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1,userId);
			ps.setInt(2, productId);
			ps.setInt(3,productCount);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
			con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}

// ---------	カートに同じ商品があったとき数量を更新  AddCurtAction  ---------  //
	public int updateCount(String userId, int productId, int productCount){
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		int count = 0;

		String sql = "UPDATE cart_info SET product_count=(product_count + ?), update_date = now() WHERE user_id=? AND product_id=?";

		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1,productCount);
			ps.setString(2,userId);
			ps.setInt(3,productId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
			con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}

// ---------	カートから削除  DeleteCartAction   ---------  //
	public int deleteProduct(String userId, int productId){
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		int count =0;

		String sql ="DELETE FROM cart_info WHERE user_id = ? AND product_id = ?";

		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1,userId);
			ps.setInt(2, productId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
			con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}

	// ---------	会員情報紐づけ  LoginAction   ---------  //
	public int linkToUserId(String tempUserId, String userId, int productId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		int count = 0;

		String sql = "UPDATE cart_info SET user_id=?, update_date = now() WHERE user_id=? AND product_id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, tempUserId);
			ps.setInt(3, productId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
			con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}

	// ---------	ユーザー削除時カード内も全件削除  LoginAction   ---------  //
		public int deleteCart(String userId){
			DBConnector db = new DBConnector();
			Connection con = db.getConnection();

			int count =0;

			String sql ="DELETE FROM cart_info WHERE user_id = ?";

			try{
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1,userId);
				count = ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try{
				con.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			return count;
		}

//	---------	userIdのユーザーが、productIdの商品のカートに入れた情報が存在するかどうかを判別する AddCartAction, LoginAction -----
		public boolean isExistsCartInfo (String userId, int productId) {
			DBConnector db = new DBConnector();
			Connection con = db.getConnection();

			boolean result = false;

			String sql = "SELECT count(id) as count FROM cart_info WHERE user_id = ? AND product_id=?";

			try{
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, userId);
				ps.setInt(2, productId);
				ResultSet rs = ps.executeQuery();

				if(rs.next()){
					if(rs.getInt("count") > 0) {
						result = true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try{
				con.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			return result;
		}
}
