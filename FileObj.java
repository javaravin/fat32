
public class FileObj {
	
	private short startingCluster;
	private int fileSize;

	FileObj(short start, int size){
		startingCluster = start;
		fileSize = size;
	}
	
	public int getFileSize(){
		return fileSize;
	}
	
	public short getStartingCluster(){
		return startingCluster;
	}
}
