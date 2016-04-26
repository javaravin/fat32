import java.nio.ByteBuffer;
import java.nio.ByteOrder;

//****************************************
// A directory entry in the FAT16 system
// could either be a file or sub-directory
// ***************************************

public class DirectoryEntry {
	String actualFileName;
	byte[] fileName;
	byte[] extension;
	byte attributes;
	byte[] reserved;
	short time;
	short date;
	short startingCluster;
	int fileSize;
	
	ByteBuffer block;
	
	
	public DirectoryEntry(byte[] _block){
		fileName = new byte[8];
		extension = new byte[3];
		reserved = new byte[10];
		block = ByteBuffer.allocate(32);
		block.order(ByteOrder.LITTLE_ENDIAN);
		block.put(_block);
		block.rewind();
	}
	
	public void parse(){
		block.get(fileName, 0, 8);
		block.get(extension, 0, 3);
		attributes = block.get();
		block.get(reserved, 0, 10);
		time = block.getShort();
		date = block.getShort();
		startingCluster = block.getShort();
		fileSize = block.getInt();
		
		actualFileName = new String(fileName).replaceAll("\\s", "");
		
		if(fileSize != 0){
			actualFileName += "."+ new String(extension).replaceAll("\\s", "");
		}
	}
	
	public String viewDE(){
		int hours;
		int minutes; 
		int seconds;
		
		int year;
		int month;
		int day;
		
		hours = (time >>> 11) & 0x001F;
		minutes = (time >>> 5) & 0x003F;
		seconds = time & 0x001F;
		
		year = (date >>> 9) & 0x007F;
		month = (date >>> 5) & 0x000F;
		day = date & 0x001F;
		
		String retVal = "";
		retVal += new String(fileName).replaceAll("\\s", "");
		
		if((attributes & 0x10) == 0){
			retVal += "."+new String(extension).replaceAll("\\s", "");
		}
		
		retVal += " "+month+"/"+day+"/"+(year + 1980)+" ";
		retVal += hours + ":" + minutes +":"+seconds;
		
		if(fileSize != 0){
			retVal += " "+fileSize;
		}
		
		//retVal += " Starting Cluster -> "+startingCluster;
		
		return retVal;
	}
	
	

}
