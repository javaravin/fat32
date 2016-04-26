import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class BootBlock {
	
	private short bytesPerSector;
	private byte sectorsPerCluster;
	private short reservedSectors;
	private byte numberOfFats;
	private short rootEntries;
	private short smallSectors;
	private byte mediaDescriptors;
	private short sectorsPerFat;
	private short sectorsPerTrack;
	private short heads;
	private int hiddenSectors;
	private int largeSectors;
	
	ByteBuffer block;
	
	public BootBlock(byte[] _block){
		block = ByteBuffer.allocate(512);
		block.order(ByteOrder.LITTLE_ENDIAN);
		block.put(_block);
		block.rewind();
	}
	
	public void parse(){
		byte [] y = new byte[11];
		block.get(y, 0, 11);

		bytesPerSector = block.getShort();
		sectorsPerCluster = block.get();
		reservedSectors = block.getShort();
		numberOfFats = block.get();
		rootEntries = block.getShort();
		smallSectors = block.getShort();
		mediaDescriptors = block.get();
		sectorsPerFat = block.getShort();
		sectorsPerTrack = block.getShort();
		heads = block.getShort();
		hiddenSectors = block.getInt();
		largeSectors = block.getInt();
	}
	
	public String toString(){
		
		String retVal = "";
		retVal += "\nBytes Per Sector -> " + bytesPerSector;
		retVal += "\nSectors Per Cluster -> " + sectorsPerCluster;
		retVal += "\nReserved Sectors -> " + reservedSectors;
		retVal += "\nNumber Of Fats -> " + numberOfFats;
		retVal += "\nRoot Entries -> " + rootEntries;
		retVal += "\nSmall Sectors -> " + smallSectors;
		retVal += "\nMediaDescriptors -> " + mediaDescriptors;
		retVal += "\nSectors Per Fat -> " + sectorsPerFat;
		retVal += "\nSectors Per Track -> " + sectorsPerTrack;
		retVal += "\nHeads -> " + heads;
		retVal += "\nHidden Sectors -> " + hiddenSectors;
		retVal += "\nLarge Sectors -> " + largeSectors;
		
		return retVal;
	}
	
	public int getDataStart(){
		//int clusterSize = bytesPerSector * sectorsPerCluster;
		int fatSectors = numberOfFats * sectorsPerFat;
		int numRootDirSectors = (rootEntries * 32) / bytesPerSector;
		int dataStart = reservedSectors + fatSectors + numRootDirSectors;
		return dataStart;
	}
	
	public int getFileSector(int clusterNumber){
		int dataStart = getDataStart();
		return dataStart + (clusterNumber-2) * sectorsPerCluster;
	}
	
	public int getRootDirectorySectorNum(){
		return (getSectorsPerFat() * getNumberOfFats()) + getReservedSectors();
	}
	
	public int getNumRootDirectorySectors(){
		return (getRootEntries()*32) / getBytesPerSector();
	}

	public short getBytesPerSector() {
		return bytesPerSector;
	}

	public byte getSectorsPerCluster() {
		return sectorsPerCluster;
	}

	public short getReservedSectors() {
		return reservedSectors;
	}

	public byte getNumberOfFats() {
		return numberOfFats;
	}

	public short getRootEntries() {
		return rootEntries;
	}

	public short getSmallSectors() {
		return smallSectors;
	}

	public byte getMediaDescriptors() {
		return mediaDescriptors;
	}

	public short getSectorsPerFat() {
		return sectorsPerFat;
	}

	public short getSectorsPerTrack() {
		return sectorsPerTrack;
	}

	public short getHeads() {
		return heads;
	}

	public int getHiddenSectors() {
		return hiddenSectors;
	}

	public int getLargeSectors() {
		return largeSectors;
	}

	public ByteBuffer getBlock() {
		return block;
	}
	
}
