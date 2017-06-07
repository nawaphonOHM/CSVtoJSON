import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CSVtoJSON {
	private String filename;
	private Scanner keyboardInput;
	private char drive;
	private String path;
	private String correctPath;
	private boolean found;
	private BufferedReader filedata;
	private String[] typeToJSON;
	private String stringFile;
	private String[] JSONString;
	private PrintWriter objectJSONFIle;
	private String stringToWrite;
	private String JSONFileNmae;

	protected void start() { //Main Method.
		recivefilename();
		if(!(isItPath(this.filename))){
			System.out.print("Searching location... ");
			while(true){
				if(!(isItMoreDrives())){
					System.out.println("File not Found!");
					System.exit(0);
				}
				else{   
					searchingPath();
					if(isFound()){
						System.out.println("Done!");
						System.out.println("Found at: " + pathFile());
						break;
					}
					else{
						nextDrive();
					}
				}
			}
		}
		else{
			itIsCorrectPathAlready();
		}
		System.out.print("Writing JSON File... ");
		readAndWriteFile();
		System.out.println("Done!");
	}
	
	private void itIsCorrectPathAlready(){
		this.correctPath = this.filename;
	}
	
	private String pathFile(){
		return this.correctPath;
	}
	
	private void nextDrive(){
		this.drive++;
		this.path = this.drive + ":\\";
	}
	
	private boolean isFound(){
		return this.found;
	}
	
	private boolean isItMoreDrives(){
		return this.drive <= 'Z';
	}
	
	private void searchingPath(){
		File files = new File(this.path);
		File[] listfiles = files.listFiles();
		if(listfiles != null){
			for(File file : listfiles){
				if(file.isDirectory()){
					this.path = file.toString() + "\\";
					searchingPath();
					if(this.found){
						break;
					}
				}
				else if(this.filename.equals(file.getName())){
					this.correctPath = file.toString();
					this.found = true;
				}
			}
		}
	}
	
	public CSVtoJSON(){
		this.drive = 'C';
		this.path = this.drive + ":\\";
		this.found = false;
		this.stringToWrite = "";
	}
	
	private void readAndWriteFile(){
		try{
			this.filedata = new BufferedReader(new FileReader(this.correctPath));
			this.typeToJSON = this.filedata.readLine().split(",,");
			this.typeToJSON = this.typeToJSON[0].split(",");
			this.correctPath = this.correctPath.substring(0, 
					this.correctPath.lastIndexOf("\\") + 1);
			this.objectJSONFIle = new PrintWriter(new BufferedWriter(
					new FileWriter(new File(this.correctPath + this.JSONFileNmae), false)));
			while((this.stringFile = this.filedata.readLine()) != null){
				this.JSONString = this.stringFile.split(",,");
				this.JSONString = this.JSONString[0].split(",");
				this.stringToWrite += "{";
				for(int i = 0; i < this.typeToJSON.length; i++){
					this.stringToWrite +=
							"'" + this.typeToJSON[i] + "' : " 
							+ "'" + this.JSONString[i] + "', ";
				}
				this.stringToWrite = this.stringToWrite.substring(0, 
						this.stringToWrite.lastIndexOf(",")) + "},\n";
			}
			this.stringToWrite = "[" + this.stringToWrite.substring(0, 
					this.stringToWrite.lastIndexOf(",")) + "]";
			this.objectJSONFIle.print(this.stringToWrite);
			this.objectJSONFIle.close();
			this.filedata.close();
		}
		catch(FileNotFoundException filenotfound){
			System.out.println("File not Found!");
		} 
		catch (IOException ioexception) {
			ioexception.getMessage();
		}
		
	}
	
	private void recivefilename(){
		System.out.print("Input CSV file: ");
		while(true){
			this.keyboardInput = new Scanner(System.in);
			this.filename = keyboardInput.nextLine();
			hasItTypeFileCSV();
			if(!(isItValidCSV())){
				System.out.println("It was not CSV file!");
				System.out.print("Input new CSV file: ");
			}
			else{
				break;
			}
		}
		System.out.print("Input JSON file name: ");
		while(true){
			this.JSONFileNmae = keyboardInput.nextLine();
			hasItTypeFileJSON();
			if(isItPath(this.JSONFileNmae)){
				System.out.println("Incorrect JSON file name!");
				System.out.print("Input new JSON file name: ");
			}
			else{
				break;
			}
		}
	}
	
	private void hasItTypeFileJSON(){
		String[] filename = this.JSONFileNmae.split("[.]");
		if(filename.length != 2){
			this.JSONFileNmae += ".json";
		}
	}
	
	private boolean isItPath(String filename){
		return filename.contains("\\");
	}
	
	private void hasItTypeFileCSV(){
		String[] filename = this.filename.split("[.]");
		if(filename.length != 2){
			this.filename += ".csv";
		}
	}
	
	private boolean isItValidCSV(){
		String[] filename = this.filename.split("[.]");
		return filename[1].contains("csv");
	}

}
