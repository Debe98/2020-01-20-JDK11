package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Artist> listArtists(Map <Integer, Artist> artisti) {

		String sql = "SELECT *\r\n" + 
				"FROM artists;";
		List<Artist> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Artist exObj = new Artist(res.getInt("artist_id"), res.getString("name"));

				result.add(exObj);
				artisti.put(exObj.getId(), exObj);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public List<String> getRuoli() {
		String sql = "SELECT DISTINCT authorship.role\r\n" + 
				"FROM authorship " +
				"ORDER BY authorship.role ASC;";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("role"));
			}
			conn.close();

			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Artist> getVertex(Map<Integer, Artist> artisti, String ruolo) {
		String sql = "SELECT DISTINCT authorship.artist_id\r\n" + 
				"FROM authorship\r\n" + 
				"WHERE authorship.role = ?;";
		List<Artist> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(artisti.get(res.getInt("artist_id")));
			}
			conn.close();

			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Arco> getArchi(Map<Integer, Artist> artisti, String ruolo) {
		String sql = "SELECT a1.artist_id, a2.artist_id, COUNT(*) AS n\r\n" + 
				"FROM exhibition_objects AS e1, exhibition_objects AS e2, authorship AS a1, authorship AS a2\r\n" + 
				"WHERE e1.exhibition_id = e2.exhibition_id AND e1.object_id < e2.object_id\r\n" + 
				"	AND e1.object_id = a1.object_id AND e2.object_id = a2.object_id\r\n" + 
				"	AND a1.artist_id IN (\r\n" + 
				"			SELECT DISTINCT authorship.artist_id\r\n" + 
				"			FROM authorship\r\n" + 
				"			WHERE authorship.role = ?)\r\n" + 
				"	AND a2.artist_id IN (\r\n" + 
				"			SELECT DISTINCT authorship.artist_id\r\n" + 
				"			FROM authorship\r\n" + 
				"			WHERE authorship.role = ?)\r\n" + 
				"	AND a1.artist_id < a2.artist_id\r\n" + 
				"GROUP BY a1.artist_id, a2.artist_id;";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			st.setString(2, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Arco(artisti.get(res.getInt("a1.artist_id")), artisti.get(res.getInt("a2.artist_id")), res.getInt("n")));
			}
			conn.close();

			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
