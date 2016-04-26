import java.util.ArrayList;

public class CommandProcessor {
	
	private ArrayList<String> commands = new ArrayList<String>();
	Fat fat;
	Disk disk;
	int currentCluster;
	byte[] array = null;
	BootBlock bb;
	
	boolean done;
	
	public CommandProcessor(){
		commands.add("dump");
		commands.add("dir");
		commands.add("cd");
		commands.add("cat");
		commands.add("quit");
		done = false;
		currentCluster = 0;
		
		//initialize the disk
		disk = new Disk("myFileSystemSpring2015.img");
		
		
		//initialize the Fat class
		fat = new Fat(32 * 512);
		for (int i=0;i<32;i++) {
			byte[] fatarray = disk.getSector(1+i);
			fat.addBlock(fatarray);
		}
		
		//initialize the boot block for the root directory cluster number
		array = disk.getSector(0);
		bb = new BootBlock(array);
		bb.parse();
		currentCluster = bb.getRootDirectorySectorNum();
		
	}
	
	public boolean processCommand(String _command){
        String commands[] = _command.split("\\s+");
		done = false;
		
		
		//blank input returns to prompt
		if(_command.equals("")){
			return done;
		}
		
		int command = this.commands.indexOf(commands[0]);
		String parameter = "";
		
		//if the command was not in the arrayList of valid commands
		if(command == -1){
			System.out.println("invalid command");
			return done;
		}
		
		switch(command){
		case 0:
			dump();
			break;
		case 1:
			dir(currentCluster);
			break;
		case 2: 
			if(commands.length == 2 && commands[1] != null){
				parameter = commands[1];
			}
			else{
				//back to the root directory
				parameter = ".";
			}
			cd(parameter);
			break;
		case 3:
			if(commands.length == 2 && commands[1] != null){
				parameter = commands[1];
			}
			else{
				System.out.println("no file given");
				done = false;
				break;
			}
			cat(parameter);
			break;
		case 4:
			quit();
			done = true;
			break;
		default:
			//should never reach here
			error();
		}
		
		return done;
	}
	
	public void dump(){
		System.out.println(bb);
	}
	
	public void dir(int _startingCluster){
		
		if(_startingCluster == 0){
			_startingCluster = bb.getRootDirectorySectorNum(); 
		}
		
		int lastCluster = bb.getNumRootDirectorySectors();
		Directory d = new Directory(_startingCluster, lastCluster);
		
		if(_startingCluster != bb.getRootDirectorySectorNum()){
			array = disk.getSector(bb.getFileSector(_startingCluster));
			d.addBlock(array);
		}
		else{
			for (int i=0;i<bb.getNumRootDirectorySectors();i++) {
				array = disk.getSector(bb.getRootDirectorySectorNum()+i );
				d.addBlock(array);
			}
		}
		
		d.parse();
		System.out.println(d);
	}
	
	public void cd(String _destination){
		//need to take the passed file name and shorten it
		//to the FAT format
		//I am sure there is a better way to do this but not
		//enough time
		if(_destination.length() > 8){
			_destination = _destination.substring(0,  6);
			_destination += "~1";
			
		}
		
		if(currentCluster == 0){
			currentCluster = bb.getRootDirectorySectorNum(); 
		}

		Directory d = new Directory(currentCluster, bb.getNumRootDirectorySectors());
		if(currentCluster != bb.getRootDirectorySectorNum()){
			array = disk.getSector(bb.getFileSector(currentCluster));
			d.addBlock(array);
		}
		else{
			for (int i=0;i<bb.getNumRootDirectorySectors();i++) {
				array = disk.getSector(bb.getRootDirectorySectorNum()+i );
				d.addBlock(array);
			}
		}
		
		d.parse();
		System.out.println(d);
		boolean found = false;
		for(DirectoryEntry de: d.dirEntry){
			//System.out.println(de.actualFileName);
			if(_destination.equalsIgnoreCase(de.actualFileName)){
				
				if(de.attributes == 0x10){
					currentCluster = de.startingCluster;
					found = true;
				}
				else{
					System.out.println("not a directory");
				}
			}
		}
		if(!found){
			System.out.println(_destination+" not found");
		}
		
		
	}
	
	public void cat(String _fileName){
		FileObj f = null;
		boolean found = false;
		//build the current directory
		if(currentCluster == 0){
			currentCluster = bb.getRootDirectorySectorNum(); 
		}

		Directory d = new Directory(currentCluster, bb.getNumRootDirectorySectors());
		if(currentCluster != bb.getRootDirectorySectorNum()){
			array = disk.getSector(bb.getFileSector(currentCluster));
			d.addBlock(array);
		}
		else{
			for (int i=0;i<bb.getNumRootDirectorySectors();i++) {
				array = disk.getSector(bb.getRootDirectorySectorNum()+i );
				d.addBlock(array);
			}
		}
		
		d.parse();
		
		//find the file
		for(DirectoryEntry de: d.dirEntry){
			//System.out.println(de.actualFileName);
			if(_fileName.equalsIgnoreCase(de.actualFileName)){
				found = true;
				f = new FileObj(de.startingCluster, de.fileSize);
			}
		}
		
		if(!found){
			System.out.println("file not found");
			return;
		}
		
		int sector;
		int numClusters = f.getFileSize()/(512*4);
		numClusters++;
		int clusterNum = f.getStartingCluster();
		for (int ii = 0; ii < numClusters; ii++) {
			sector = bb.getFileSector(clusterNum);
			for (int i = 0; i < 4; i++) {
				array = disk.getSector(sector+i);
				String line = new String(array);
				System.out.println(line);
			} 
			clusterNum = fat.getEntry(clusterNum);
		}
		
	}
	
	public void quit(){
		System.out.println("quitting");
	}
	
	public void error(){
		System.out.println("in error");
	}
}
