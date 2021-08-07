package cs363;
/**
 * @authors Jacob Kelderman, Evelyn Khew
 * 	
 */

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.ArrayList;

//this file has 3 methods loginDialog, runQuery, and the main method

public class Group85Project {
	/**
	 * gets the username and password
	 *
	 * 	@return
	 * 	user name and password 
	 * 	
	 */
	public static String[] loginDialog() {
		String result[] = new String[2];
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.HORIZONTAL;

		JLabel lbUsername = new JLabel("Username: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lbUsername, cs);

		JTextField tfUsername = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		panel.add(tfUsername, cs);

		JLabel lbPassword = new JLabel("Password: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(lbPassword, cs);

		JPasswordField pfPassword = new JPasswordField(20);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		panel.add(pfPassword, cs);
		panel.setBorder(new LineBorder(Color.GRAY));

		String[] options = new String[] { "OK", "Cancel" };
		int ioption = JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.OK_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (ioption == 0) // pressing OK button
		{
			result[0] = tfUsername.getText();
			result[1] = new String(pfPassword.getPassword());
		}
		return result;
	}

	/**
	 * Runs query and outputs results in window
	 * @param stmt
	 * @param sqlQuery
	 * @throws SQLException
	 */
	private static void runQuery(Statement stmt, String sqlQuery) throws SQLException {
		ResultSet rs;
		ResultSetMetaData rsMetaData;
		String toShow;
		rs = stmt.executeQuery(sqlQuery);
		rsMetaData = rs.getMetaData();

		toShow = "";
		for(int i = 1; i <= rsMetaData.getColumnCount(); i++) {
			toShow += (rsMetaData.getColumnName(i) + ", ");
		}
		toShow += "\n";
		
		while (rs.next()) {
			for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
				toShow += rs.getString(i + 1) + ", ";
			}
			toShow += "\n";
		}
		JOptionPane.showMessageDialog(null, toShow);
	}
	
	/**
	 * Used for Q3.
	 * @param conn
	 * @param num
	 * @param year
	 */
	private static void topStateTags(Connection conn, int num, int year) {
		if (conn == null)
			throw new NullPointerException();
		
		try {
			conn.setAutoCommit(false);
			
			//calling the procedure
			CallableStatement cstmt = conn.prepareCall(
					"select count(distinct u.ofstate) as statenum, group_concat(distinct u.ofstate) as states, h.hastagname as name " + 
					"from users u, tweet t, hastags h " + 
					"where t.tid = h.tid and u.screen_name = t.posting_user " + 
					"and t.year_posted = ? " + 
					"and u.ofstate != 'na' " + 
					"group by hastagname " + 
					"order by statenum desc limit ? ; ");
			
			
			cstmt.setInt(1, year);
			cstmt.setInt(2, num); 
			
			ResultSet rs = cstmt.executeQuery();
			ResultSetMetaData rsMetaData;
			String toShow;
			rsMetaData = rs.getMetaData();

			toShow = "";
			for(int i = 1; i <= rsMetaData.getColumnCount(); i++) {
				toShow += (rsMetaData.getColumnName(i) + ", ");
			}
			toShow += "\n";
			
			while (rs.next()) {
				for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
					toShow += rs.getString(i + 1) + ", ";
				}
				toShow += "\n";
			}
			JOptionPane.showMessageDialog(null, toShow);
			
			cstmt.close();
			conn.commit();
		}
		catch(SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		
	}

	/**
	 * Used for Q7.
	 * @param conn
	 * @param tag
	 * @param stName
	 * @param num
	 * @param mth
	 * @param year
	 */
	private static void usersTagsUsed(Connection conn, String tag, String stName, int num, int mth, int year) {
		if (conn == null || tag == null || stName == null)
			throw new NullPointerException();
		
		try {
			conn.setAutoCommit(false);
			
			//calling the procedure
			CallableStatement cstmt = conn.prepareCall(
					"select count(distinct t.tid) as tweet_count, u.screen_name, u.category " + 
					"from users u, tweet t, hastags h " + 
					"where u.screen_name = t.posting_user and t.tid = h.tid " + 
					"and t.month_posted = ? and t.year_posted = ? " + 
					"and h.hastagname = ? and u.ofstate = ? " + 
					"group by screen_name " + 
					"order by tweet_count desc  limit  ? ; ");
			
			
			cstmt.setInt(1, mth);
			cstmt.setInt(2, year); 
			cstmt.setString(3, tag); 
			cstmt.setString(4, stName); 
			cstmt.setInt(5, num); 
			
			ResultSet rs = cstmt.executeQuery();
			ResultSetMetaData rsMetaData;
			String toShow;
			rsMetaData = rs.getMetaData();

			toShow = "";
			for(int i = 1; i <= rsMetaData.getColumnCount(); i++) {
				toShow += (rsMetaData.getColumnName(i) + ", ");
			}
			toShow += "\n";
			
			while (rs.next()) {
				for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
					toShow += rs.getString(i + 1) + ", ";
				}
				toShow += "\n";
			}
			JOptionPane.showMessageDialog(null, toShow);
			
			cstmt.close();
			conn.commit();
		}
		catch(SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		
	}
	
	/**
	 * Used for Q9.
	 * @param conn
	 * @param partyName
	 * @param num
	 */
	private static void topFollowedByParty(Connection conn, String partyName, int num) {
		if (conn == null || partyName == null)
			throw new NullPointerException();
		
		try {
			conn.setAutoCommit(false);
			
			//calling the procedure
			CallableStatement cstmt = conn.prepareCall(
					"select u.screen_name, u.sub_category, u.numFollowers " + 
					"from users u " + 
					"where u.sub_category = ? " + 
					"order by numFollowers desc limit  ? ; ");
			
			cstmt.setString(1, partyName); 
			cstmt.setInt(2, num); 
			
			ResultSet rs = cstmt.executeQuery();
			ResultSetMetaData rsMetaData;
			String toShow;
			rsMetaData = rs.getMetaData();

			toShow = "";
			for(int i = 1; i <= rsMetaData.getColumnCount(); i++) {
				toShow += (rsMetaData.getColumnName(i) + ", ");
			}
			toShow += "\n";
			
			while (rs.next()) {
				for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
					toShow += rs.getString(i + 1) + ", ";
				}
				toShow += "\n";
			}
			JOptionPane.showMessageDialog(null, toShow);
			
			cstmt.close();
			conn.commit();
		}
		catch(SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		
	}
	
	//Q16
		private static void Q16(Connection conn, int month, int yr, int k) {
			if (conn == null)
				throw new NullPointerException();
			try {
				conn.setAutoCommit(false);
				
				//calling the procedure
				CallableStatement cstmt = conn.prepareCall(
						"select u.screen_name as user_name , u.category, t.textbody as texts, t.retweet_count as retweetCt, h.url as address " + 
						"from users u, tweet t, hasurls h " + 
						"where u.screen_name = t.posting_user " + 
						"	and t.tid = h.tid " + 
						"    and t.month_posted = ? "  +
						"    and t.year_posted =  ? " +
						"order by t.retweet_count desc limit ?  ; ");
				
				
				cstmt.setInt(1, month);
				cstmt.setInt(2, yr);
				cstmt.setInt(3, k);
				
				ResultSet rs = cstmt.executeQuery();
				ResultSetMetaData rsMetaData;
				String toShow;
				rsMetaData = rs.getMetaData();

				toShow = "";
				for(int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					toShow += (rsMetaData.getColumnName(i) + ", ");
				}
				toShow += "\n";
				
				while (rs.next()) {
					for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
						toShow += rs.getString(i + 1) + ", ";
					}
					toShow += "\n";
				}
				JOptionPane.showMessageDialog(null, toShow);
				
				cstmt.close();
				conn.commit();
			}
			catch(SQLException e) {
				System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			}
			
		}
		private static void Q18(Connection conn, String category, int month, int yr, int k) {
			if (conn == null)
				throw new NullPointerException();
			try {
				conn.setAutoCommit(false);
				
				//calling the procedure
				CallableStatement cstmt = conn.prepareCall(
						"select um.screen_name as mentionedUser, um.ofstate as mentionedUserState, group_concat(distinct up.screen_name) as postingUsers " + 
								"from users um, tweet t, mentions m, users up " + 
								"where t.posting_user = up.screen_name " + 
								"    and um.screen_name = m.screen_name " + 
								"    and t.tid = m.tid " + 
								"    and up.sub_category =  ? and t.month_posted = ? and t.year_posted =  ? " + 
								"group by mentionedUser " + 
								"order by count(distinct m.tid) desc limit ? ; ");
				
				
				cstmt.setString(1, category);
				cstmt.setInt(2, month);
				cstmt.setInt(3, yr);
				cstmt.setInt(4, k);
				
				ResultSet rs = cstmt.executeQuery();
				ResultSetMetaData rsMetaData;
				String toShow;
				rsMetaData = rs.getMetaData();

				toShow = "";
				for(int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					toShow += (rsMetaData.getColumnName(i) + ", ");
				}
				toShow += "\n";
				
				while (rs.next()) {
					for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
						toShow += rs.getString(i + 1) + ", ";
					}
					toShow += "\n";
				}
				JOptionPane.showMessageDialog(null, toShow);
				
				cstmt.close();
				conn.commit();
			}
			catch(SQLException e) {
				System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			}
			
		}
		private static void Q23(Connection conn, String category, ArrayList<Integer> months, int yr, int k) {
			if (conn == null)
				throw new NullPointerException();
			try {
				conn.setAutoCommit(false);
				
				String addMonth = "";
				for(int i = 0; i < months.size(); i++) {
					if(i == 0) {
						addMonth += "t.month_posted = ? ";
					}
					else {
						addMonth += "or t.month_posted = ? ";
					}
					
				}
				
			//Q23
			String sqlQuery = "select  h.hastagname as name, count(distinct t.tid) as num_uses " + 
					"from users u, tweet t, hastags h " + 
					"where   t.tid = h.tid and u.screen_name = t.posting_user " + 
					"    and (" + addMonth + ") " + 
					"    and t.year_posted = ? "   + 
					"    and u.sub_category = ? " + 
					"group by hastagname " + 
					"order by num_uses desc limit ? ; ";
				//calling the procedure
				CallableStatement cstmt = conn.prepareCall(sqlQuery);
				
				
				
				for(int i = 0; i < months.size(); i++) {
					cstmt.setInt(i+ 1, months.get(i));
				}
				cstmt.setInt(months.size() + 1, yr);
				cstmt.setString(months.size()+ 2, category);
				cstmt.setInt(months.size() + 3, k);
				
				ResultSet rs = cstmt.executeQuery();
				ResultSetMetaData rsMetaData;
				String toShow;
				rsMetaData = rs.getMetaData();

				toShow = "";
				for(int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					toShow += (rsMetaData.getColumnName(i) + ", ");
				}
				toShow += "\n";
				
				while (rs.next()) {
					for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
						toShow += rs.getString(i + 1) + ", ";
					}
					toShow += "\n";
				}
				JOptionPane.showMessageDialog(null, toShow);
				
				cstmt.close();
				conn.commit();
			}
			catch(SQLException e) {
				System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			}
			
		}
	
	/**
	 * Used for Deletion.
	 * @param conn
	 * @param uName
	 */
	private static void toDelete(Connection conn, String uName) {
		if (conn == null || uName == null)
			throw new NullPointerException();
		
		try {
			conn.setAutoCommit(false);
			
			//calling the procedure
			CallableStatement cstmt = conn.prepareCall(
					"select u.screen_name from users u where u.screen_name = ? ; ");
			
			cstmt.setString(1, uName); 
			
			ResultSet rs = cstmt.executeQuery();
			if(rs.next()){
				cstmt = conn.prepareCall("delete from users where screen_name = ?;");
				cstmt.setString(1, uName); 
				int run = cstmt.executeUpdate();
				JOptionPane.showMessageDialog(null, "User "+ uName + " deleted successfully. ");
			}
			
			else {JOptionPane.showMessageDialog(null, "User " + uName + " does not exist.");}
			cstmt.close();
			conn.commit();
		}
		catch(SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		
	}
	
	
	public static void main(String[] args) {
		String dbServer = "jdbc:mysql://localhost:3306/group85?useSSL=false";
		// For compliance with existing applications not using SSL the verifyServerCertificate property is set to ‘false’,
		String userName = "";
		String password = "";

		String result[] = loginDialog();
		userName = result[0];
		password = result[1];

		Connection conn;
		Statement stmt;
		if (result[0]==null || result[1]==null) {
			System.out.println("Terminating: No username nor password is given");
			return;
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbServer, userName, password);
			stmt = conn.createStatement();
			String sqlQuery = "";

			String option = "";
			String instruction = "Enter 1: Q3." + "\n"
					+ "Enter 2: Q7"
					+ "\n" + "Enter 3: Q9" + "\n"
					+ "Enter 4: Q16" + "\n"
					+ "Enter 5: Q18" + "\n"
					+ "Enter 6: Q23" + "\n"
					+ "Enter 7: Insertion" + "\n"
					+ "Enter 8: Deletion" + "\n"
					+ "Enter 9: Quit Program";
			
			while (true) {
				option = JOptionPane.showInputDialog(instruction);
				if (option.equals("1")) {
						int k = Integer.parseInt(JOptionPane.showInputDialog("Enter number of results wanted:"));
						int yr = Integer.parseInt(JOptionPane.showInputDialog("Enter year:"));
					//Q3
						topStateTags(conn, k, yr);
					
				} 
				
				else if (option.equalsIgnoreCase("2")) {
						int k = Integer.parseInt(JOptionPane.showInputDialog("Enter number of results wanted:"));
						String hashTagName = JOptionPane.showInputDialog("Enter HashTag Name: ");
						String state = JOptionPane.showInputDialog("Enter State: ");
						int month = Integer.parseInt(JOptionPane.showInputDialog("Enter month:"));
						int yr = Integer.parseInt(JOptionPane.showInputDialog("Enter year:"));
						//Q7
						usersTagsUsed(conn, hashTagName, state, k, month, yr);
						
				} 
				
				else if (option.equals("3")) {
						int k = Integer.parseInt(JOptionPane.showInputDialog("Enter number of results wanted:"));
						String category = (JOptionPane.showInputDialog("Enter sub category:"));
						//Q9
						topFollowedByParty(conn, category, k);
				} 
				
				 else if (option.equals("4")) {
						int k = Integer.parseInt(JOptionPane.showInputDialog("Enter number of results wanted:"));
						int month = Integer.parseInt(JOptionPane.showInputDialog("Enter month:"));
						int yr = Integer.parseInt(JOptionPane.showInputDialog("Enter year:"));
						Q16(conn,month,yr,k);
						
					} else if (option.equals("5")) {
							
								int k = Integer.parseInt(JOptionPane.showInputDialog("Enter number of results wanted:"));
								String category = (JOptionPane.showInputDialog("Enter sub category:"));
								int month = Integer.parseInt(JOptionPane.showInputDialog("Enter month:"));
								int yr = Integer.parseInt(JOptionPane.showInputDialog("Enter year:"));
							//Q18
							Q18(conn,category,month,yr,k);
							
							
					} else if (option.equals("6")) {
							
								int k = Integer.parseInt(JOptionPane.showInputDialog("Enter number of results wanted:"));
								String category = (JOptionPane.showInputDialog("Enter sub category:"));
								int month = Integer.parseInt(JOptionPane.showInputDialog("Enter month(s) when done enter -1:"));
								ArrayList<Integer> months = new ArrayList<Integer>();
								while(month != -1) {
									months.add(month);
									month = Integer.parseInt(JOptionPane.showInputDialog("Enter month(s) when done enter -1:"));
									
								}
								int yr = Integer.parseInt(JOptionPane.showInputDialog("Enter year:"));
								
							//Q23
							Q23(conn,category,months,yr,k);
					}  
				
				else if (option.equals("7")) { //insertion
					try {
						//insert into users values('JKeld', 'JKeld', 'na', '', 'IA', 23, 41);
						String screenName = (JOptionPane.showInputDialog("Enter user screen name:"));
						String otherName = (JOptionPane.showInputDialog("Enter user other name:"));
						String subCat = (JOptionPane.showInputDialog("Enter user sub category:"));
						String cat = (JOptionPane.showInputDialog("Enter user category:"));
						String state =(JOptionPane.showInputDialog("Enter user state:"));
						int followers = Integer.parseInt(JOptionPane.showInputDialog("Enter number of followers:"));
						int following = Integer.parseInt(JOptionPane.showInputDialog("Enter number of users followed: (following users)"));
						
						if (conn == null || screenName == null || otherName == null || subCat == null || cat == null || state == null)
							throw new NullPointerException();
						
						conn.setAutoCommit(false);
						
						//calling the procedure
						CallableStatement cstmt = conn.prepareCall(
								"insert into users values(?, ?, ?, ?, ?, ?, ?)");
						
						cstmt.setString(1, screenName); 
						cstmt.setString(2, otherName);
						cstmt.setString(3, subCat);
						cstmt.setString(4, cat);
						cstmt.setString(5, state);
						cstmt.setInt(6, followers); 
						cstmt.setInt(7, following); 
						
						int run = cstmt.executeUpdate();
						JOptionPane.showMessageDialog(null, "User "+ screenName + " added successfully. ");
						cstmt.close();
						conn.commit();
					} catch (SQLException e) {
						System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
					}
					
				}
				
				else if (option.equals("8")) { //deletion
					String uName = (JOptionPane.showInputDialog("Enter user screen name:"));
					
					toDelete(conn, uName);
				}
				
				else {
					break;
				}
			}

			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("Program terminates due to errors");
			e.printStackTrace(); // for debugging
		}
	}

}
