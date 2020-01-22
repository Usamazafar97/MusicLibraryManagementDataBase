
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.sql.Statement;

public class TestDB2 {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "1234567";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/**
	 * The name of the database we are testing with (this default is installed with
	 * MySQL)
	 */
	private final String dbName = "MusicLibrary";

	/** The name of the table we are testing with */
	// private final String tableName = "Song";

	private Statement stmt;

	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		System.out.println("trying to get connection!! ");
		conn = DriverManager.getConnection(
				"jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName, connectionProps);
		System.out.println(" Connection achieved!! ");
		return conn;
	}

	public boolean executeUpdate(Connection conn, String command) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(command); // This will throw a SQLException if it fails
			return true;
		} finally {

			// This will run whether we throw an exception or not
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public void ChangeSongTitle(String songTitleToSet, String songTitleToChange) {
		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		String input = "UPDATE Song SET SongTitle = '" + songTitleToSet + "' WHERE SongTitle = '" + songTitleToChange
				+ "'";
		try {
			this.executeUpdate(conn1, input);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void ChangeAlbumTitle(String albumTitleToChange, String albumTitleToSet) {
		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		String input = "UPDATE Song SET AlbumTitle = '" + albumTitleToSet + "' WHERE AlbumTitle = '"
				+ albumTitleToChange + "'";
		try {
			this.executeUpdate(conn1, input);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void ChangeArtistTitle(String artistTitleToChange, String artistTitleToSet) {
		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		String input = "UPDATE Artist SET ArtistName = '" + artistTitleToSet + "' WHERE ArtistName = '"
				+ artistTitleToChange + "'";
		try {
			this.executeUpdate(conn1, input);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void RemoveSong(String SongTitle) {

		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		
		//First find the id
		int songID=0;
		try {
			stmt = conn1.createStatement();
			String sql = "SELECT SongID FROM Song WHERE SongTitle ='" + SongTitle + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				songID = rs.getInt("SongID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//remove from ArtistManager
		String input = "DELETE FROM ArtistManager WHERE SongID =" + songID;
		try {
			this.executeUpdate(conn1, input);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		input = "DELETE FROM Song WHERE SongTitle ='" + SongTitle+"'";
		try {
			this.executeUpdate(conn1, input);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void RemoveArtistFromSong(String artistName) {

		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		// First find the id of the artist
		int ArtistID = 101;
		Statement stmt = null;
		try {
			stmt = conn1.createStatement();
			String sql = "SELECT ArtistID FROM Artist WHERE ArtistName ='" + artistName + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ArtistID = rs.getInt("ArtistID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "DELETE FROM ArtistManager WHERE ArtistID =" + ArtistID;
		try {
			this.executeUpdate(conn1, input);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String input1 = "DELETE FROM Artist WHERE ArtistID =" + ArtistID;
		try {
			this.executeUpdate(conn1, input1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void AddArtistToSong(String artistName, String songTitle) {

		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		
		int artistID = 0;
		int songID = 0;
		try {
			stmt = conn1.createStatement();
			String sql = "SELECT ArtistID FROM Artist WHERE ArtistName ='" + artistName + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				artistID = rs.getInt("ArtistID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt = conn1.createStatement();
			String sql = "SELECT SongID FROM Song WHERE SongTitle ='" + songTitle + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				songID = rs.getInt("SongID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		String input = "INSERT INTO ArtistManager " + "VALUES ('" + songID  + "','" + artistID + "')";
		try {
			this.executeUpdate(conn1, input);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void AddSongInLibrary(int songID, String songTitle, String path, int songDuration, int trackNumber,
			String albumTitle) {

		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		String input = "INSERT INTO Song " + "VALUES ('" + Integer.toString(songID) + "','" + songTitle + "','" + path
				+ "','" + Integer.toString(songDuration) + "','" + Integer.toString(trackNumber) + "','" + albumTitle
				+ "')";
		try {
			this.executeUpdate(conn1, input);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void SearchSongsByArtistName(String artistName) {

		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn1.createStatement();
			String sql = "SELECT SongTitle FROM Song s JOIN ArtistManager am ON s.SongID=am.SongID JOIN Artist a ON am.ArtistID=a.ArtistID AND ArtistName ='"
					+ artistName + "'";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("Songs :");
			while (rs.next()) {
				String SongTitle = rs.getString("SongTitle");
				System.out.println(SongTitle);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void SearchSongsByAlbumTitle(String albumTitle) {

		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn1.createStatement();
			String sql = "SELECT SongTitle FROM Song WHERE AlbumTitle ='" + albumTitle + "'";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("Songs :");
			while (rs.next()) {
				String SongTitle = rs.getString("SongTitle");
				System.out.println(SongTitle);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void AllAlbums() {

		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn1.createStatement();
			String sql = "SELECT DISTINCT AlbumTitle FROM Song;";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("AlbumTitles :");
			while (rs.next()) {
				String AlbumTitle = rs.getString("AlbumTitle");
				System.out.println(AlbumTitle);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void AllArtists() {

		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn1.createStatement();
			String sql = "SELECT ArtistName FROM Artist;";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("ArtistNames :");
			while (rs.next()) {
				String ArtistName = rs.getString("ArtistName");
				System.out.println(ArtistName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void AllSongs() {

		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			// System.out.println("connection name is :: " + conn1.getClass().getName());
			// System.out.println("Connected to database");
		} catch (SQLException e) {
			// System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		Statement stmt = null;
		try {
			stmt = conn1.createStatement();
			String sql = "SELECT SongTitle,Path,SongDuration,TrackNumber,AlbumTitle,ArtistName FROM Song s JOIN ArtistManager am ON s.SongID=am.SongID JOIN Artist a ON am.ArtistID=a.ArtistID;";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("hello");
			while (rs.next()) {
				int SongDuration = rs.getInt("SongDuration");
				int TrackNumber = rs.getInt("TrackNumber");
				String SongTitle = rs.getString("SongTitle");
				String Path = rs.getString("Path");
				String AlbumTitle = rs.getString("AlbumTitle");
				String ArtistName = rs.getString("ArtistName");

				System.out.println("SongTitle: " + SongTitle);
				System.out.println("Path: " + Path);
				System.out.println("SongDuration: " + SongDuration);
				System.out.println("TrackNumber: " + TrackNumber);
				System.out.println("AlbumTitle: " + AlbumTitle);
				System.out.println("ArtistName: " + ArtistName + "\n\n");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {

		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
			System.out.println("connection name is :: " + conn.getClass().getName());
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}


		// Create a table
		try {

			// Creating all the Tables

			// Song
			String c = "CREATE TABLE  IF NOT EXISTS Song " + "( SongID INT PRIMARY KEY,"
					+ "SongTitle VARCHAR(255) NOT NULL, Path VARCHAR(300) UNIQUE, SongDuration INT NOT NULL,"
					+ " TrackNumber INT NOT NULL,AlbumTitle VARCHAR(300) NOT NULL);";

			this.executeUpdate(conn, c);

			// Now filling one by one

			// Song

			String input = "INSERT INTO Song " + "VALUES ("
					+ "'1001', 'The Girl is Mine','C:\\\\1.mp3','3','1','Thriller')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Song " + "VALUES (" + "'1002', 'Billie Jean','C:\\\\2.mp3','4','2','Thriller')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Song " + "VALUES ("
					+ "'1003', 'P.Y.T. (Pretty Young Thing)','C:\\\\3.mp3','2','3','Thriller')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Song " + "VALUES ("
					+ "'1004', 'Blue Bossa','C:\\\\4.mp3','3','1','Tom Favourite Tunes')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Song " + "VALUES ("
					+ "'1005', 'The Lion Sleeps Tonight','C:\\\\5.mp3','3','2','Tom Favourite Tunes')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Song " + "VALUES (" + "'1006', 'I Surrender','C:\\\\6.mp3','3','2','Classics')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Song " + "VALUES ("
					+ "'1007', 'Play that Funky Music','C:\\\\7.mp3','3','1','Default')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Song " + "VALUES (" + "'1008', 'Go West','C:\\\\8.mp3','3','7','Default')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Song " + "VALUES ("
					+ "'1009', 'Mystery Track of Mystery','C:\\\\9.mp3','3','10','Default')";
			this.executeUpdate(conn, input);

			// Artist
			c = "CREATE TABLE IF NOT EXISTS Artist " + "( ArtistID INT PRIMARY KEY NOT NULL,"
					+ "ArtistName VARCHAR(150) NOT NULL);";
			this.executeUpdate(conn, c);

			// Now filling one by one

			// Artist

			input = "INSERT INTO Artist " + "VALUES (" + "'101', 'Michael Jackson')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Artist " + "VALUES (" + "'102', 'Joe Henderson')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Artist " + "VALUES (" + "'103', 'The Tokens')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Artist " + "VALUES (" + "'104', 'Celine Dion')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Artist " + "VALUES (" + "'105', 'Wild Cherry')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Artist " + "VALUES (" + "'106', 'Pet Shop Boys')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO Artist " + "VALUES (" + "'107', 'Tom Hazelton')";
			this.executeUpdate(conn, input);

			// this.executeUpdate(conn, c);

			// ArtistManager
			c = "CREATE TABLE IF NOT EXISTS ArtistManager "
					+ "( SongID INT NOT NULL,FOREIGN KEY (SongID) REFERENCES Song(SongID),"
					+ "ArtistID INT NOT NULL,FOREIGN KEY (ArtistID) REFERENCES Artist(ArtistID));";

			this.executeUpdate(conn, c);

			// Now filling one by one

			// ArtistManager

			input = "INSERT INTO ArtistManager " + "VALUES (" + "'1001', '101')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO ArtistManager " + "VALUES (" + "'1002', '101')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO ArtistManager " + "VALUES (" + "'1003', '101')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO ArtistManager " + "VALUES (" + "'1004', '102')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO ArtistManager " + "VALUES (" + "'1005', '103')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO ArtistManager " + "VALUES (" + "'1005', '107')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO ArtistManager " + "VALUES (" + "'1006', '104')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO ArtistManager " + "VALUES (" + "'1007', '105')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO ArtistManager " + "VALUES (" + "'1008', '106')";
			this.executeUpdate(conn, input);

			input = "INSERT INTO ArtistManager " + "VALUES (" + "'1008', '107')";
			this.executeUpdate(conn, input);

			// System.out.println("Created a table");
			// this.SelectAll(conn);
		} catch (Exception e) {
			System.out.println("ERROR: Could not create the table");
			e.printStackTrace();
			return;
		}

	}

	public static void main(String[] args) {
		TestDB2 app = new TestDB2();
		app.run();

		int choice = 0;
		String toSend1 = "";

		while (true) {
			System.out.println("\t\t\t WelCome to Music App Library");

			System.out.println(
					"1.View a list of all the songs \r\n" + "2.View a list of all the artists in the library.\r\n"
							+ "3.View a list of all the albums in the library.\r\n"
							+ "4.Edit any information in the library.\r\n"
							+ "5.View a list of all the songs by a particular artist.\r\n"
							+ "6.View a list of all the songs on a particular album.\r\n"
							+ "7.Add songs to and remove them from the library.\r\n"
							+ "8.Add artists to and remove them from songs.\r\n"
							+ "9.Add songs to and remove them from albums.\n" + "10.Exit\n");
			System.out.println("Please Choose the following");
			Scanner cin = new Scanner(System.in);
			Scanner cin1 = new Scanner(System.in);
			Scanner cin2 = new Scanner(System.in);
			System.out.print("Waiting ...");
			choice = cin.nextInt();

			switch (choice) {
			case 1: // R1

				app.AllSongs();

				break;
			case 2: // R1.1

				app.AllArtists();

				break;
			case 3:// R1.2

				app.AllAlbums();

				break;
			case 4:// R3
				int choiceInR3 = 0;
				System.out.println("1.Change the Song Title\n" + "2.Change the Album Title.\r\n"
						+ "3.Change the Artist Name.\r\n");
				System.out.println("Please Choose the following");
				System.out.print("Waiting ...");
				choiceInR3 = cin.nextInt();

				switch (choiceInR3) {
				case 1:
					System.out.println("Song title which you want to change : ");
					String songTitleToChange = cin1.nextLine();
					System.out.println("Song title which you want to set : ");
					String songTitleToSet = cin1.nextLine();

					app.ChangeSongTitle(songTitleToSet, songTitleToChange);
					System.out.println("SongTitle Changed successfully");

					break;
				case 2:
					System.out.println("Album title which you want to change : ");
					String albumTitleToChange = cin1.nextLine();
					System.out.println("Album title which you want to set : ");
					String albumTitleToSet = cin1.nextLine();

					app.ChangeAlbumTitle(albumTitleToChange, albumTitleToSet);
					System.out.println("AlbumTitle Changed successfully");
					
					break;
				case 3:
					System.out.println("Artist Name which you want to change : ");
					String artistTitleToChange = cin1.nextLine();
					System.out.println("Artist Name which you want to set : ");
					String artistTitleToSet = cin1.nextLine();

					app.ChangeArtistTitle(artistTitleToChange, artistTitleToSet);
					System.out.println("Artist Name Changed successfully");
					
					break;
				}

				break;
			case 5:// R4

				System.out.println("Artist Name : ");
				toSend1 = cin1.nextLine();
				app.SearchSongsByArtistName(toSend1);

				break;
			case 6:// R5
				Scanner cin21 = new Scanner(System.in);
				System.out.println("Album Name : ");
				toSend1 = cin21.nextLine();
				app.SearchSongsByAlbumTitle(toSend1);

				break;
			case 7:// R6

				int choiceInR31 = 0;
				System.out.println("1.Add Song to the Lirary\n" + "2.Remove Song from the Lirary.\r\n");
				System.out.println("Please Choose the following");
				System.out.print("Waiting ...");
				choiceInR31 = cin.nextInt();

				switch (choiceInR31) {
				case 1:

					Scanner cin12 = new Scanner(System.in);

					System.out.println("Enter SongID ");
					int SongID;
					SongID = cin12.nextInt();

					System.out.println("Enter SongDuration ");
					int SongDuration;
					SongDuration = cin12.nextInt();

					System.out.println("Enter SongTitle");
					String SongTitle = null;
					// Enter data using BufferReader
					BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

					// Reading data using readLine
					try {
						SongTitle = reader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Enter TrackNumber ");
					int TrackNumber;
					TrackNumber = cin12.nextInt();

					System.out.println("Enter Path");
					String Path = null;
					// Path = cin12.nextLine();
					try {
						Path = reader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Enter AlbumTitle");
					String AlbumTitle = null;
					// AlbumTitle = cin12.nextLine();

					try {
						AlbumTitle = reader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					app.AddSongInLibrary(SongID, SongTitle, Path, SongDuration, TrackNumber, AlbumTitle);

					// app.AddStudent(0, Name, Email, CNIC, JY);
					System.out.println("Song Added successfully");
					// cin12.close();

					break;

				case 2:
					System.out.println("Enter SongTitle");
					String songTitle = cin1.nextLine();

					app.RemoveSong(songTitle);
					System.out.println("Song removed successfully");

					break;
				}
				break;
			case 8:// R7
				int choiceInR311 = 0;
				System.out.println("1.Add Artist to the Song\n" + "2.Remove Artist from the Song.\r\n");
				System.out.println("Please Choose the following");
				System.out.print("Waiting ...");
				choiceInR311 = cin.nextInt();
				Scanner cin3 = new Scanner(System.in);

				switch (choiceInR311) {
				case 1:
					System.out.println("ArtistName : ");
					String ArtistName = cin3.nextLine();

					System.out.println("SongTitle : ");
					String SongTitle = cin3.nextLine();

					app.AddArtistToSong(ArtistName, SongTitle);
					System.out.println("Artist Added successfully");

					break;
				case 2:
					System.out.println("Artist Name : ");
					String artistName = cin3.nextLine();

					app.RemoveArtistFromSong(artistName);
					System.out.println("Artist Removed successfully");

					break;
				}
				break;
			case 9:// R8
				int choiceInR3111 = 0;
				System.out.println("1.Add Songs to the Album\n" + "2.Remove Songs from the Album.\r\n");
				System.out.println("Please Choose the following");
				System.out.print("Waiting ...");
				choiceInR3111 = cin.nextInt();

				switch (choiceInR3111) {
				case 1:
					Scanner cin12 = new Scanner(System.in);

					System.out.println("Enter SongID ");
					int SongID;
					SongID = cin12.nextInt();

					System.out.println("Enter SongDuration ");
					int SongDuration;
					SongDuration = cin12.nextInt();

					System.out.println("Enter SongTitle");
					String SongTitle = null;
					// Enter data using BufferReader
					BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

					// Reading data using readLine
					try {
						SongTitle = reader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Enter TrackNumber ");
					int TrackNumber;
					TrackNumber = cin12.nextInt();

					System.out.println("Enter Path");
					String Path = null;
					// Path = cin12.nextLine();
					try {
						Path = reader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Enter AlbumTitle of the album to add");
					String AlbumTitle = null;
					// AlbumTitle = cin12.nextLine();

					try {
						AlbumTitle = reader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					app.AddSongInLibrary(SongID, SongTitle, Path, SongDuration, TrackNumber, AlbumTitle);

					// app.AddStudent(0, Name, Email, CNIC, JY);
					System.out.println("Song Added successfully");
					// cin12.close();

					break;

				case 2:

					System.out.println("Enter SongTitle");
					String songTitle = cin1.nextLine();

					app.RemoveSong(songTitle);
					System.out.println("Song removed successfully");

					break;
				}
				break;
			case 10:
				return;
			}

		}

	}
}