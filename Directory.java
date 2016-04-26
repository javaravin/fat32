import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

//***************************************************
// A directory listing in FAT 16 system
// object is instantiated with a given sector number 
// and number of entries we expect
// creates a list of directoryEntries
// -------------------------------------------------- 
// Usage:
// instantiate with sector number and number of entries
// add sectors and needed
// parse
// once parsing is complete contents of directoryEntry
// can be viewed and iterated though
//***************************************************

public class Directory {
	
	ByteBuffer block;
	
	ArrayList<DirectoryEntry> dirEntry = new ArrayList<DirectoryEntry>();;
	
	int numEntries;
	int numSectors;
	
	byte array[];
	
	public Directory(int _sector, int _entries){
		numEntries = _entries;
		numSectors = _sector;
		block = ByteBuffer.allocate(_sector*512);
		block.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	public void addBlock(byte[] _block){
		block.put(_block);
	}
	
	public void parse(){
		block.rewind();
		
		for(int i=0; i< numEntries; i++){
			byte [] tempBlock = new byte[32];
			block.get(tempBlock, 0, 32);
			DirectoryEntry de = new DirectoryEntry(tempBlock);
			de.parse();
			if((de.attributes == 0x10 || de.attributes == 0x20) && de.fileName[0] > 0){
				dirEntry.add(de);
			}
		}
		
	}
	
	public String toString(){
		String retVal = "directory listings\n";
		for(DirectoryEntry de: dirEntry){
			retVal += "->"+de.viewDE()+"\n";
		}
		return retVal;
	}
	
}
