package profile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Profile {

	public String name;
	public String birth;
	public String bio;
	public String imageFile;
	public File userFile;
	public String ip;
	public String id = "0";

	final static int fileLines = 4;

	public Profile(String serialized) {
		
	}
}
